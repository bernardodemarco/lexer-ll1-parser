/*
    Bernardo De Marco Gonçalves - 22102557
*/
package lexer.tokens;

public interface Token {
    enum Type {
        NUMBER,
        RELATIONAL_OPERATOR,
        IDENTIFIER,
        KEYWORD,
        SYMBOL
    }

    Type getType();
    String getLexeme();

    default String getStringRepresentation() {
        return String.format("%s['%s']", getType(), getLexeme());
    }
}
