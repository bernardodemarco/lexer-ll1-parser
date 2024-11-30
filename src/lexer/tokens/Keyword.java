/*
    Bernardo De Marco Gonçalves - 22102557
*/
package lexer.tokens;

public enum Keyword implements Token {
    IF("if"),
    ELSE("else"),
    DEF("def"),
    RETURN("return"),
    PRINT("print"),
    INT("int");

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
