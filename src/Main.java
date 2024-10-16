import lexer.Lexer;
import tokens.Token;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        Lexer lexer = new Lexer();
        List<String> files = List.of(
                "src/lsi/file-without-lexical-errors.lsi",
                "src/lsi/file-with-lexical-errors.lsi"
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

            printOutput(lexer.scan(files.get(option - 1)));
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
        System.out.println("(1) - /src/lsi/file-without-lexical-errors.lsi (file without lexical errors)");
        System.out.println("(2) - /src/lsi/file-with-lexical-errors.lsi (file with lexical errors)");
        System.out.println("(0) - Enter 0 to exit");
    }

    private static void printOutput(Map.Entry<List<Token>, Map<String, Token>> output) {
        List<Token> tokens = output.getKey();
        Map<String, Token> symbolsTable = output.getValue();

        System.out.println("----------TOKENS-----------");
        System.out.println(tokens);
        System.out.println();
        System.out.println("-------SYMBOLS TABLE-------");
        System.out.println(symbolsTable);
        System.out.println();
    }
}