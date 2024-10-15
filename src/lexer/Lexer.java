package lexer;

import tokens.*;

import java.util.HashMap;
import java.util.Map;

// TODO:
// - Review Character.isDigit() isLetter() and isAlphabetic() differences
// - Review behaviour with keywords
// - Add multiple lines
// - Handle when non-recognized characters are inserted
// - Refactor code (Classes Diagram + logic)

public class Lexer {
    private final Map<String, Token> reservedWords = new HashMap<>();
    private String content = "= 100<> 2356>g> 2004=f25<?<=! 10 bernardo be2004 bmg";
//    private String content = "for each <= >= = while ( )";
    private int column = 0;

    // refactor this
    private void reserve(Token token) {
        if (token instanceof Word word) {
            reservedWords.put(word.getLexema(), word);
        } else if (token instanceof Keyword keyword) {
            reservedWords.put(keyword.getLexema(), keyword);
        }
    }

    private void reserveKeywords() {
        for (Keyword keyword : Keyword.values()) {
            reserve(keyword);
        }
    }

    public Lexer() {
        reserveKeywords();
    }

    public void run()  {
        while (column < content.length()) {
            char character = content.charAt(column);

            if (Character.isWhitespace(character)) {
                column++;
                continue;
            }

            if (character == '<' || character == '>' || character == '=') {
                Token relopToken = handleRelop(character);
                System.out.println(relopToken);
            } else if (Character.isDigit(character)) {
                Token digitToken = handleDigit(character);
                System.out.println(digitToken);
            } else if (Character.isLetter(character)) {
                Token wordToken = handleIdentifier(character);
                System.out.println(wordToken);
            } else {
                System.out.println("did not identify the char");
            }
            column++;
        }
    }

    public Token handleIdentifier(char character) {
        StringBuilder identifierBuilder = new StringBuilder();
        while (true) {
            identifierBuilder.append(character);

            if (column == content.length() - 1) {
                break;
            }

            column++;
            character = content.charAt(column);
            if (!Character.isLetterOrDigit(character)) {
                column--;
                break;
            }
        }

        if (reservedWords.containsKey(identifierBuilder.toString())) {
            return reservedWords.get(identifierBuilder.toString());
        }

        Token token = new Word(identifierBuilder.toString());
        reservedWords.put(identifierBuilder.toString(), token);
        return token;
    }

    // 100  1000 1
    public Token handleDigit(char character) {
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

        } while (Character.isDigit(character));
        // reaches out here either because is endoffile (do not need
        // to come back) or because is not a digit, then needs to comeback
        if (!isDigitAndIsEndOfFile) {
            column--;
        }

        return new IntegerNumber(value);
    }

    public Token handleRelop(char character) {
        Map<Integer, RelationalOperator> acceptStates = Map.of(
                2, new RelationalOperator(RelationalOperator.Operator.LE),
                3, new RelationalOperator(RelationalOperator.Operator.NE),
                4, new RelationalOperator(RelationalOperator.Operator.LT),
                5, new RelationalOperator(RelationalOperator.Operator.EQ),
                7, new RelationalOperator(RelationalOperator.Operator.GE),
                8, new RelationalOperator(RelationalOperator.Operator.GT)
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

                if (character != '=' && character != '<' && character != '>') {
                    character = '*';
                    column--;
                }
            }

            state = transitionTable.get(state).get(character);
        }

        return acceptStates.get(state);
    }
}
