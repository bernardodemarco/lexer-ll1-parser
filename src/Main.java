/*
    Bernardo De Marco Gonçalves - 22102557
*/
import lexer.Lexer;
import lexer.tokens.Token;
import parser.Parser;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        Lexer lexer = new Lexer();
        List<String> files = List.of(
                "src/resources/lsi/file-without-lexical-errors.lsi",
                "src/resources/lsi/file-with-lexical-errors.lsi"
        );
        Scanner scanner = new Scanner(System.in);

        printWelcomeMessage();
        promptOptions();

        int option = scanner.nextInt();
        while (option != 0) {
            if (option < 0 || option > 2) {
                System.out.println("Option unavailable");
                promptOptions();
                option = scanner.nextInt();
                continue;
            }

            Map.Entry<List<Token>, Map<String, Token>> lexicalResult = lexer.scan(files.get(option - 1));
            List<Token> tokens = lexicalResult.getKey();
            Map<String, Token> symbolsTable = lexicalResult.getValue();
            printLexicalResult(tokens, symbolsTable);
            new Parser().parse(tokens, symbolsTable);

            promptOptions();
            option = scanner.nextInt();
        }

        System.out.println("Exiting...");
    }

    private static void printWelcomeMessage() {
        System.out.println("Hand-written Lexer");
        System.out.println("---------------------------");
        System.out.println("This lexer is able to recognize relational operators, function and variable identifiers and integer numbers greater than zero");
        System.out.println("---------------------------");
    }

    private static void promptOptions() {
        System.out.println("Two source files are available to be scanned:");
        System.out.println("(1) - /src/resources/lsi/file-without-lexical-errors.resources.lsi (file without lexical errors)");
        System.out.println("(2) - /src/resources/lsi/file-with-lexical-errors.resources.lsi (file with lexical errors)");
        System.out.println("(0) - Enter 0 to exit");
    }

    private static void printLexicalResult(List<Token> tokens, Map<String, Token> symbolsTable) {
        System.out.println("----------TOKENS-----------");
        System.out.println(tokens);
        System.out.println();
        System.out.println("-------SYMBOLS TABLE-------");
        System.out.println(symbolsTable);
        System.out.println();
    }
}