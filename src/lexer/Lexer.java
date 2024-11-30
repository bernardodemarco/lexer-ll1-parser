/*
    Bernardo De Marco Gonçalves - 22102557
*/
package lexer;

import exceptions.UnrecognizedCharacterException;
import reader.Reader;
import tokens.*;
import tokens.Number;

import java.io.IOException;
import java.util.*;

public class Lexer {
    private final Map<String, Token> symbolsTable = new HashMap<>();
    private final Reader reader;

    public Lexer() {
        reader = new Reader();
        reserveKeywords();
    }

    public Map.Entry<List<Token>, Map<String, Token>> scan(String filePath) throws IOException {
        reader.loadSourceFile(filePath);
        List<Token> tokens = new ArrayList<>();

        while (reader.hasContentToConsume()) {
            char character = reader.getCurrentChar();

            if (isWhitespace(character)) {
                reader.goToNextChar();
                continue;
            }

            tokens.add(tokenize(character));
            reader.goToNextChar();
        }

        return new AbstractMap.SimpleImmutableEntry<>(tokens, symbolsTable);
    }

    private Token tokenize(char character) {
        Token symbol = tokenizeSymbol(character);
        if (symbol != null) {
            return symbol;
        }

        if (isRelOpCharacter(character)) {
            return tokenizeRelOp(character);
        }

        if (isDigit(character)) {
            return tokenizeNumber(character);
        }

        if (isIdentifierStart(character)) {
            return tokenizeIdentifier(character);
        }

        throw new UnrecognizedCharacterException(character, reader.getHumanReadablePosition());
    }

    private Token tokenizeSymbol(char character) {
        return switch (character) {
            case '+' -> new Symbol("+", Symbol.SymbolType.PLUS);
            case '-' -> new Symbol("-", Symbol.SymbolType.MINUS);
            case '*' -> new Symbol("*", Symbol.SymbolType.MULTIPLY);
            case '/' -> new Symbol("/", Symbol.SymbolType.DIVIDE);
            case '(' -> new Symbol("(", Symbol.SymbolType.LPAREN);
            case ')' -> new Symbol(")", Symbol.SymbolType.RPAREN);
            case '{' -> new Symbol("{", Symbol.SymbolType.LBRACE);
            case '}' -> new Symbol("}", Symbol.SymbolType.RBRACE);
            case ',' -> new Symbol(",", Symbol.SymbolType.COMMA);
            case ';' -> new Symbol(";", Symbol.SymbolType.SEMICOLON);
            default -> null;
        };
    }

    private Token tokenizeIdentifier(char character) {
        StringBuilder identifierBuilder = new StringBuilder();
        while (true) {
            identifierBuilder.append(character);

            if (reader.isEndOfLine()) {
                break;
            }

            reader.goToNextChar();
            character = reader.getCurrentChar();
            if (!isIdentifierPart(character)) {
                reader.goToPreviousChar();
                break;
            }
        }

        String identifierLexeme = identifierBuilder.toString();
        if (symbolsTable.containsKey(identifierLexeme)) {
            return symbolsTable.get(identifierLexeme);
        }

        Token token = new Identifier(identifierLexeme);
        reserve(token);
        return token;
    }

    private Token tokenizeNumber(char character) {
        int value = 0;
        boolean isEndOfLine = false;
        do {
            int digit = Character.getNumericValue(character);
            value = value * 10 + digit;

            if (reader.isEndOfLine()) {
                isEndOfLine = true;
                break;
            }

            reader.goToNextChar();
            character = reader.getCurrentChar();
        } while (isDigit(character));

        if (!isEndOfLine) {
            reader.goToPreviousChar();
        }

        return new Number(String.valueOf(value), value);
    }

    private Token tokenizeRelOp(char character) {
        Map<Integer, RelOp> acceptStates = Map.of(
                2, RelOp.LE,
                3, RelOp.NE,
                4, RelOp.LT,
                5, RelOp.EQ,
                7, RelOp.GE,
                8, RelOp.GT
        );

        Map<Integer, Map<Character, Integer>> transitionTable = new HashMap<>();
        transitionTable.put(0, Map.of(
                '<', 1,
                '=', 5,
                '>', 6
        ));
        transitionTable.put(1, Map.of(
                '=', 2,
                '>', 3,
                '*', 4
        ));
        transitionTable.put(6, Map.of(
                '=', 7,
                '*', 8
        ));

        int state = transitionTable.get(0).get(character);
        while (!acceptStates.containsKey(state)) {
            if (reader.isEndOfLine()) {
                character = '*';
                state = transitionTable.get(state).get(character);
                break;
            }

            reader.goToNextChar();
            character = reader.getCurrentChar();

            if (!isRelOpCharacter(character)) {
                character = '*';
                reader.goToPreviousChar();
            }

            state = transitionTable.get(state).get(character);
        }

        return acceptStates.get(state);
    }

    private void reserve(Token token) {
        symbolsTable.put(token.getLexeme(), token);
    }

    private void reserveKeywords() {
        Arrays.stream(Keyword.values()).forEach(this::reserve);
    }

    private boolean isWhitespace(char character) {
        return Character.isWhitespace(character);
    }

    private boolean isRelOpCharacter(char character) {
        List<Character> relOpCharacters = List.of('<', '>', '=');
        return relOpCharacters.contains(character);
    }

    private boolean isIdentifierStart(char character) {
        return Character.isJavaIdentifierStart(character);
    }

    private boolean isIdentifierPart(char character) {
        return Character.isJavaIdentifierPart(character);
    }

    private boolean isDigit(char character) {
        return Character.isDigit(character);
    }
}
