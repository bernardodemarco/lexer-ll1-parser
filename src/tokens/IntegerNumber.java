package tokens;

public class IntegerNumber implements Token {
    private final int value;

    public IntegerNumber(int value) {
        this.value = value;
    }

    @Override
    public Type getType() {
        return Type.INT;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "IntegerNumber{" +
                "value=" + value +
                '}';
    }
}
