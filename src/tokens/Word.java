package tokens;

public class Word implements Token {
    private final String lexema;

    public Word(String lexema) {
        this.lexema = lexema;
    }

    @Override
    public Type getType() {
        return Type.ID;
    }

    public String getLexema() {
        return lexema;
    }

    @Override
    public String toString() {
        return "Word{" +
                "lexema='" + lexema + '\'' +
                '}';
    }
}
