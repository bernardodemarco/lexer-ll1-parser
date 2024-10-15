package tokens;

public class RelationalOperator implements Token {
    private final Operator operator;

    public RelationalOperator(Operator operator) {
        this.operator = operator;
    }

    public enum Operator {
        LT, LE, GT, GE, EQ, NE, WILDCARD
    }

    @Override
    public Type getType() {
        return Type.RELOP;
    }

    public Operator getOperator() {
        return operator;
    }

    @Override
    public String toString() {
        return "RelationalOperator{" +
                "operator=" + operator +
                '}';
    }
}
