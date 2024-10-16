package reader;

public class Reader {
    private String content = "= 100<> 2356>g> 2004=f25<?<=! 10 bernardo be2004 bmg def int if else return inteiro returned";
    private int column = 0;

    public boolean isEndOfContent() {
        return column == content.length() - 1;
    }

    public boolean hasContentToConsume() {
        return column < content.length();
    }

    public char getCurrentChar() {
        return content.charAt(column);
    }

    public void goToNextChar() {
        column++;
    }

    public void goToPreviousChar() {
        column--;
    }
}
