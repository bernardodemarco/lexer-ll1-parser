package tokens;

public interface Token {
    enum Type {
        INT, RELOP, ID, KEYWORD
    }

    Type getType();
}
