package parser;

import lexer.tokens.SpecialSymbol;
import lexer.tokens.Token;
import parser.exceptions.SyntaxException;
import utils.Logger;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

// take a look at the behavior of if statements
// refactor lexer

public class Parser {
    private Map<String, Token> symbolsTable;
    private final Map<String, Map<String, String>> transitionTable = new Grammar().getTable();
    private List<Token> tokens;
    private Integer iterator = 0;
    private Stack<String> stack = getInitialSymbolsStack();

    public void parse(List<Token> tokens, Map<String, Token> symbolsTable) {
        this.tokens = getParserTokens(tokens);
        this.symbolsTable = symbolsTable;
        Token input = getCurrentToken();

        while (!input.getLexeme().equals(SpecialSymbol.END_OF_INPUT.getLexeme())) {
            boolean isTerminal = isTerminalSymbol(stack.peek());

            Logger.debug(String.format("Symbol at the top of the stack: [%s], input token: [%s]", stack.peek(), input));
            if (isTerminal) {
                if (stack.peek().equals(Grammar.SpecialSymbols.EPSILON.getRepresentation())) {
                    stack.pop();
                    continue;
                }

                if (!symbolMatchesWithToken(stack.peek(), input)) {
                    throw new SyntaxException(stack.peek(), input.getLexeme(), Set.of(stack.peek()));
                }

                stack.pop();
                input = getNextToken();
            } else {
                String production = getProduction(stack.peek(), input);

                if (production == null) {
                    throw new SyntaxException(stack.peek(), input.getLexeme(), transitionTable.get(stack.peek()).keySet());
                }

                Logger.info(production);
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
            return nonTerminalMap.get(Grammar.SpecialSymbols.NUM.getRepresentation());
        }

        if (input.getType() != Token.Type.IDENTIFIER) {
            return nonTerminalMap.get(input.getLexeme());
        }

        if (!stack.peek().equals(Grammar.SpecialSymbols.ATTRIBUTION_PRIME_SYMBOL.getRepresentation())) {
            return nonTerminalMap.get(Grammar.SpecialSymbols.ID.getRepresentation());
        }

        return handleAttributionProductionConflict(nonTerminalMap);
    }

    private String handleAttributionProductionConflict(Map<String, String> nonTerminalMap) {
        if (peekNextToken().getLexeme().equals(SpecialSymbol.LPAREN.getLexeme())) {
            return nonTerminalMap.get(Grammar.SpecialSymbols.FUNCTION_CALL_ATTRIBUTION.getRepresentation());
        }

        return nonTerminalMap.get(Grammar.SpecialSymbols.EXPRESSION_ATTRIBUTION.getRepresentation());
    }

    private boolean isTerminalSymbol(String symbol) {
        List<String> grammarSpecialSymbols = List.of(
                Grammar.SpecialSymbols.ID.getRepresentation(),
                Grammar.SpecialSymbols.NUM.getRepresentation(),
                Grammar.SpecialSymbols.EPSILON.getRepresentation()
        );
        return grammarSpecialSymbols.contains(symbol) || symbolsTable.containsKey(symbol);
    }

    private boolean symbolMatchesWithToken(String symbol, Token token) {
        boolean matchesIdentifier = symbol.equals(Grammar.SpecialSymbols.ID.getRepresentation()) && token.getType() == Token.Type.IDENTIFIER;
        boolean matchesNumber = symbol.equals(Grammar.SpecialSymbols.NUM.getRepresentation()) && token.getType() == Token.Type.NUMBER;
        boolean matchesOtherTokenTypes = symbol.equals(token.getLexeme());

        return matchesIdentifier || matchesNumber || matchesOtherTokenTypes;
    }

    private List<String> getReverseProductionBody(String production) {
        String body = production.split(Grammar.SpecialSymbols.PRODUCTION.getRepresentation())[1].trim();
        List<String> nonTerminals = Arrays.asList(body.split("\\s+"));
        Collections.reverse(nonTerminals);
        return nonTerminals;
    }

    private List<Token> getParserTokens(List<Token> tokens) {
        tokens.add(SpecialSymbol.END_OF_INPUT);
        return tokens;
    }

    private Stack<String> getInitialSymbolsStack() {
        Stack<String> stack = new Stack<>();
        stack.push(SpecialSymbol.END_OF_INPUT.getLexeme());
        stack.push(Grammar.SpecialSymbols.START_SYMBOL.getRepresentation());
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
