package tokens;

public enum Keyword implements Token {
    INT("int"),
    IF("if"),
    ELSE("else"),
    DEF("def"),
    PRINT("print"),
    RETURN("return");

    private final String lexema;

    Keyword(String lexema) {
        this.lexema = lexema;
    }

    public String getLexema() {
        return lexema;
    }

    @Override
    public Type getType() {
        return Type.KEYWORD;
    }
}
