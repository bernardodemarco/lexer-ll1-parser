package lexer.tokens;

public class Symbol implements Token {
    private String lexeme;
    private SymbolType symbolType;

    public enum SymbolType {
        PLUS, MINUS, MULTIPLY,
        DIVIDE, ASSIGN, LPAREN,
        RPAREN, LBRACE, RBRACE,
        COMMA, SEMICOLON, END_OF_INPUT
    }

    public Symbol(String lexeme, SymbolType symbolType) {
        this.lexeme = lexeme;
        this.symbolType = symbolType;
    }

    @Override
    public Type getType() {
        return Token.Type.SYMBOL;
    }

    public SymbolType getSymbolType() {
        return symbolType;
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
