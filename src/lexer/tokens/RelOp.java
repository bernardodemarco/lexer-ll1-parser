/*
    Bernardo De Marco Gonçalves - 22102557
*/
package lexer.tokens;

public enum RelOp implements Token {
    LT("<"),
    LE("<="),
    GT(">"),
    GE(">="),
    EQ("="),
    NE("<>");

    private final String lexeme;

    RelOp(String lexeme) {
        this.lexeme = lexeme;
    }

    @Override
    public Type getType() {
        return Type.RELATIONAL_OPERATOR;
    }

    @Override
    public String getLexeme() {
        return lexeme;
    }

    @Override
    public String toString() {
        return getStringRepresentation();
    }
}
