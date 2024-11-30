package lexer.tokens;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public enum Symbol implements Token {
    PLUS("+"),
    MINUS("-"),
    MULTIPLY("*"),
    DIVIDE("/"),
    ASSIGN(":="),
    LPAREN("("),
    RPAREN(")"),
    LBRACE("{"),
    RBRACE("}"),
    COMMA(","),
    SEMICOLON(";"),
    END_OF_INPUT("$");

    private static final Map<String, Symbol> MAP;

    static {
        MAP = new HashMap<>();
        Arrays.stream(Symbol.values()).forEach((symbol) -> {
            MAP.put(symbol.getLexeme(), symbol);
        });
    }

    private final String lexeme;

    Symbol(String lexeme) {
        this.lexeme = lexeme;
    }

    public static Symbol get(String lexeme) {
        return MAP.getOrDefault(lexeme, null);
    }

    @Override
    public Type getType() {
        return Token.Type.SYMBOL;
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
