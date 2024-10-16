package tokens;

public enum Keyword implements Token {
    IF("if"),
    ELSE("else"),
    WHILE("while"),
    FOR("for"),
    PUBLIC("public"),
    PROTECTED("protected"),
    PRIVATE("private"),
    DEF("def"),
    RETURN("return"),
    TRUE("true"),
    FALSE("false"),
    PRINT("print");

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
