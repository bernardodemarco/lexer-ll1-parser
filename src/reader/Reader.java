package reader;

import java.util.List;

public class Reader {
    private List<String> content = List.of(
            "= 100<> 2356>g> 2004=f25<?<=! 10 bernardo be2004 bmg def int if else return inteiro returned",
            "(10 bernardo be2004 bmg def <while"
    );
    private int line = 0;
    private int column = 0;

    public boolean hasContentToConsume() {
        boolean currentLineIsPreviousToLastLine = line < content.size() - 1;
        boolean isLastLine = line == content.size() - 1;
        return currentLineIsPreviousToLastLine
                || (isLastLine && column < content.get(line).length());
    }

    public boolean isEndOfLine() {
        return column == content.get(line).length() - 1;
    }

    public char getCurrentChar() {
        return content.get(line).charAt(column);
    }

    public void goToNextChar() {
        if (!isEndOfLine()) {
            column++;
            return;
        }

        column = 0;
        line++;
    }

    public void goToPreviousChar() {
        if (column > 0) {
            column--;
            return;
        }

        line--;
        column = content.get(line).length() - 1;
    }
}
