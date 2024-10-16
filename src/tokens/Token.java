package tokens;

public interface Token {
    enum Type {
        NUMBER,
        RELATIONAL_OPERATOR,
        IDENTIFIER,
        KEYWORD
    }

    Type getType();
    String getLexeme();

    default String getStringRepresentation() {
        return String.format("%s['%s']", getType(), getLexeme());
    }
}
