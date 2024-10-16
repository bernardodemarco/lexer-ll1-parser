package tokens;

public class Number implements Token {
    private final String lexeme;
    private final int value;

    public Number(String lexeme, int value) {
        this.lexeme = lexeme;
        this.value = value;
    }

    @Override
    public Type getType() {
        return Type.NUMBER;
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
