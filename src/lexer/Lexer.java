package lexer;

import tokens.*;
import tokens.Number;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// TODO:
// - Review Character.isDigit() isLetter() and isAlphabetic() differences
// - Review behaviour with keywords
// - Add multiple lines
// - Handle when non-recognized characters are inserted
// - Refactor code (Classes Diagram + logic)
// - Take a look at maximal crunch

public class Lexer {
    private final Map<String, Token> reservedWords = new HashMap<>();
    private String content = "= 100<> 2356>g> 2004=f25<?<=! 10 bernardo be2004 bmg";
    private int column = 0;

    public Lexer() {
        reserveKeywords();
    }

    public void scan()  {
        while (column < content.length()) {
            char character = content.charAt(column);

            if (isWhitespace(character)) {
                column++;
                continue;
            }

            Token token = tokenize(character);
            if (token != null) {
                System.out.println(token);
            }

            column++;
        }
    }

    private void reserve(Token token) {
        reservedWords.put(token.getLexeme(), token);
    }

    private void reserveKeywords() {
        Arrays.stream(Keyword.values()).forEach(this::reserve);
    }

    private Token tokenize(char character) {
        if (isRelOpCharacter(character)) {
            return tokenizeRelOp(character);
        }

        if (isDigit(character)) {
            return tokenizeNumber(character);
        }

        if (isIdentifierStart(character)) {
            return tokenizeIdentifier(character);
        }

        System.out.println("did not identify the char");
        return null;
    }

    private Token tokenizeIdentifier(char character) {
        StringBuilder identifierBuilder = new StringBuilder();
        while (true) {
            identifierBuilder.append(character);

            if (column == content.length() - 1) {
                break;
            }

            column++;
            character = content.charAt(column);
            if (!isIdentifierPart(character)) {
                column--;
                break;
            }
        }

        if (reservedWords.containsKey(identifierBuilder.toString())) {
            return reservedWords.get(identifierBuilder.toString());
        }

        Token token = new Identifier(identifierBuilder.toString());
        reservedWords.put(identifierBuilder.toString(), token);
        return token;
    }

    // 100  1000 1
    private Token tokenizeNumber(char character) {
        int value = 0;
        boolean isDigitAndIsEndOfFile = false;
        do {
            int digit = Character.getNumericValue(character);
            value = value * 10 + digit;

            if (column < content.length() - 1) {
                column++;
                character = content.charAt(column);
            } else {
                // se ta na ultima coluna, vaza
                isDigitAndIsEndOfFile = true;
                break;
            }

        } while (isDigit(character));
        // reaches out here either because is endoffile (do not need
        // to come back) or because is not a digit, then needs to comeback
        if (!isDigitAndIsEndOfFile) {
            column--;
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
            if (column == content.length() - 1) {
                character = '*';
            } else {
                column++;
                character = content.charAt(column);

                if (!isRelOpCharacter(character)) {
                    character = '*';
                    column--;
                }
            }

            state = transitionTable.get(state).get(character);
        }

        return acceptStates.get(state);
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
