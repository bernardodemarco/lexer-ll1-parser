package lexer.tokens;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public enum SpecialSymbol implements Token {
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

    private static final Map<String, Token> MAP;

    static {
        MAP = new HashMap<>();
        Arrays.stream(SpecialSymbol.values()).forEach((specialSymbol) -> {
            MAP.put(specialSymbol.getLexeme(), specialSymbol);
        });
    }

    private final String lexeme;

    SpecialSymbol(String lexeme) {
        this.lexeme = lexeme;
    }

    public static Token get(String lexeme) {
        return MAP.getOrDefault(lexeme, null);
    }

    public static Map<String, Token> getMap() {
        return MAP;
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
