package lexer;/*
    Bernardo De Marco Gonçalves - 22102557
*/

import lexer.exceptions.UnrecognizedCharacterException;
import lexer.tokens.Identifier;
import lexer.tokens.Keyword;
import lexer.tokens.RelOp;
import lexer.tokens.Symbol;
import lexer.tokens.Token;
import reader.Reader;
import lexer.tokens.Number;

import java.io.IOException;
import java.util.*;

public class Lexer {
    private final Map<String, Token> symbolsTable = new HashMap<>();
    private final Reader reader;

    public Lexer() {
        reader = new Reader();
        reserveKeywords();
        reserveSymbols();
        reserveRelOps();
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
        if (character == Symbol.ASSIGN.getLexeme().charAt(0)) {
            return tokenizeAssignSymbol();
        }

        return Symbol.get(String.valueOf(character));
    }

    private Token tokenizeAssignSymbol() {
        reader.goToNextChar();
        char character = reader.getCurrentChar();
        if (character == '=') {
            return Symbol.ASSIGN;
        }

        reader.goToPreviousChar();
        return null;
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

        Token number = new Number(String.valueOf(value), value);
        reserve(number);
        return number;
    }

    private Token tokenizeRelOp(char character) {
        Map<Integer, RelOp> acceptStates = Map.of(
                2, RelOp.LE,
                3, RelOp.NE,
                4, RelOp.LT,
                6, RelOp.EQ,
                8, RelOp.GE,
                9, RelOp.GT
        );

        Map<Integer, Map<Character, Integer>> transitionTable = new HashMap<>();
        transitionTable.put(0, Map.of(
                '<', 1,
                '=', 5,
                '>', 7
        ));
        transitionTable.put(1, Map.of(
                '=', 2,
                '>', 3,
                '*', 4
        ));
        transitionTable.put(5, Map.of(
                '=', 6
        ));
        transitionTable.put(7, Map.of(
                '=', 8,
                '*', 9
        ));

        Integer state = transitionTable.get(0).get(character);
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

            Integer possibleState = transitionTable.get(state).get(character);
            if (isRelOpCharacter(character) && possibleState == null) {
                character = '*';
                reader.goToPreviousChar();
                possibleState = transitionTable.get(state).get(character);
            }
            state = possibleState;
        }

        return acceptStates.get(state);
    }

    private void reserve(Token token) {
        symbolsTable.put(token.getLexeme(), token);
    }

    private void reserveAll(Map<String, Token> entries) {
        symbolsTable.putAll(entries);
    }

    private void reserveKeywords() {
        Arrays.stream(Keyword.values()).forEach(this::reserve);
    }

    private void reserveSymbols() {
        reserveAll(Symbol.getMap());
    }

    private void reserveRelOps() {
        Arrays.stream(RelOp.values()).forEach(this::reserve);
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
