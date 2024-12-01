package parser;

import lexer.tokens.Symbol;
import lexer.tokens.Token;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Stack;

// add logger
// handle exceptions better
// manual tests
// refactor lexer

public class Parser {
    private Map<String, Token> symbolsTable;
    private Map<String, Map<String, String>> transitionTable = new Grammar().getTable();
    private List<Token> tokens;
    private Integer iterator = 0;
    private Stack<String> stack = getInitialSymbolsStack();

    public void parse(List<Token> tokens, Map<String, Token> symbolsTable) {
        this.tokens = getParserTokens(tokens);
        this.symbolsTable = symbolsTable;
        Token input = getCurrentToken();

        while (!input.getLexeme().equals(Symbol.END_OF_INPUT.getLexeme())) {
            boolean isTerminal = isTerminalSymbol(stack.peek());

            System.out.println("STACK PEEK = " + stack.peek());
            System.out.println("CURRENTTOKEN = " + input);
            System.out.println("IS IT A TERMINAL? " + isTerminal);

            if (isTerminal) {
                if (stack.peek().equals(Grammar.EPSILON)) {
                    stack.pop();
                    continue;
                }

                if (!symbolMatchesWithToken(stack.peek(), input)) {
                    throw new RuntimeException("PARSER ERROR!!!");
                }

                stack.pop();
                input = getNextToken();
            } else {
                String production = getProduction(stack.peek(), input);

                if (production == null) {
                    throw new RuntimeException("PARSER ERROR!!!");
                }

                System.out.println(production);
                stack.pop();
                getReverseProductionBody(production).forEach(stack::push);
            }
        }

        reset();
    }

    private void reset() {
        iterator = 0;
        stack = getInitialSymbolsStack();
    }

    private String getProduction(String nonTerminal, Token input) {
        Map<String, String> nonTerminalMap = transitionTable.get(nonTerminal);
        if (input.getType() == Token.Type.NUMBER) {
            return nonTerminalMap.get(Grammar.NUM);
        }

        if (input.getType() != Token.Type.IDENTIFIER) {
            return nonTerminalMap.get(input.getLexeme());
        }

        if (!stack.peek().equals(Grammar.ATTRIBUTION_PRIME_SYMBOL)) {
            return nonTerminalMap.get(Grammar.ID);
        }

        return handleAttributionProductionConflict(nonTerminalMap);
    }

    private String handleAttributionProductionConflict(Map<String, String> nonTerminalMap) {
        if (peekNextToken().getLexeme().equals(Symbol.LPAREN.getLexeme())) {
            return nonTerminalMap.get(Grammar.FUNCTION_CALL_ATTRIBUTION);
        }

        return nonTerminalMap.get(Grammar.EXPRESSION_ATTRIBUTION);
    }

    private boolean isTerminalSymbol(String symbol) {
        List<String> grammarSpecialSymbols = List.of(Grammar.ID, Grammar.NUM, Grammar.EPSILON);
        return grammarSpecialSymbols.contains(symbol) || symbolsTable.containsKey(symbol);
    }

    private boolean symbolMatchesWithToken(String symbol, Token token) {
        boolean matchesIdentifier = symbol.equals(Grammar.ID) && token.getType() == Token.Type.IDENTIFIER;
        boolean matchesNumber = symbol.equals(Grammar.NUM) && token.getType() == Token.Type.NUMBER;
        boolean matchesOtherTokenTypes = symbol.equals(token.getLexeme());

        return matchesIdentifier || matchesNumber || matchesOtherTokenTypes;
    }

    private List<String> getReverseProductionBody(String production) {
        String body = production.split(Grammar.PRODUCTION)[1].trim();
        List<String> nonTerminals = Arrays.asList(body.split("\\s+"));
        Collections.reverse(nonTerminals);
        return nonTerminals;
    }

    private List<Token> getParserTokens(List<Token> tokens) {
        tokens.add(Symbol.END_OF_INPUT);
        return tokens;
    }

    private Stack<String> getInitialSymbolsStack() {
        Stack<String> stack = new Stack<>();
        stack.push(Symbol.END_OF_INPUT.getLexeme());
        stack.push(Grammar.START_SYMBOL);
        return stack;
    }

    private Token getCurrentToken() {
        return tokens.get(iterator);
    }

    private Token getNextToken() {
        iterator++;
        return getCurrentToken();
    }

    private Token peekNextToken() {
        return tokens.get(iterator + 1);
    }
}
