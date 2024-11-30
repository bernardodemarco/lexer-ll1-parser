/*
    Bernardo De Marco Gonçalves - 22102557
*/
package lexer.tokens;

public class Identifier implements Token {
    private final String lexeme;

    public Identifier(String lexeme) {
        this.lexeme = lexeme;
    }

    @Override
    public Type getType() {
        return Type.IDENTIFIER;
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
