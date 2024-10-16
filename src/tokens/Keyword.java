package tokens;

public enum Keyword implements Token {
    INT("int"),
    IF("if"),
    ELSE("else"),
    DEF("def"),
    PRINT("print"),
    RETURN("return");

    private final String lexeme;

    Keyword(String lexeme) {
        this.lexeme = lexeme;
    }

    @Override
    public String getLexeme() {
        return lexeme;
    }

    @Override
    public Type getType() {
        return Type.KEYWORD;
    }

    @Override
    public String toString() {
        return getStringRepresentation();
    }
}
