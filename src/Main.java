/*
    Bernardo De Marco Gonçalves - 22102557
*/
import lexer.Lexer;
import lexer.tokens.Token;
import parser.Parser;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static List<String> files = List.of(
            "src/resources/lsi/fibonacci.lsi",
            "src/resources/lsi/fibonacci-with-syntax-errors.lsi",
            "src/resources/lsi/recursive-factorial.lsi",
            "src/resources/lsi/recursive-factorial-with-syntax-errors.lsi"
    );

    public static final Lexer lexer = new Lexer();
    public static final Parser parser = new Parser();

    public static void main(String[] args) {
        Logger.prompt("Lexer & LL(1) Parser Implementation");
        Logger.prompt("-----------------------------------");

        int option = getInputOption();
        while (option != 0) {
            if (option < 0 || option > files.size()) {
                Logger.error(String.format("Invalid input. Please, enter an integer number between %s and %s", 0, files.size()));
                option = getInputOption();
                continue;
            }

            String filename = files.get(option - 1);
            try {
                runCompiler(filename);
            } catch (IOException e) {
                Logger.error(String.format("Unable to read %s file at the moment. Please, try again later.", filename));
            }

            option = getInputOption();
        }
    }

    private static void runCompiler(String sourceFilename) throws IOException {
        Map.Entry<List<Token>, Map<String, Token>> lexicalResult = lexer.scan(sourceFilename);
        List<Token> tokens = lexicalResult.getKey();
        Map<String, Token> symbolsTable = lexicalResult.getValue();
        printLexicalResult(tokens, symbolsTable);
        parser.parse(tokens, symbolsTable);
    }

    private static void printLexicalResult(List<Token> tokens, Map<String, Token> symbolsTable) {
        Logger.prompt("TOKENS");
        System.out.println(tokens);
        System.out.println();
        Logger.prompt("SYMBOLS");
        System.out.println(symbolsTable);
        System.out.println();
    }

    private static int getInputOption() {
        Scanner scanner = new Scanner(System.in);
        int option;
        promptOptions();

        try {
            option = scanner.nextInt();
        } catch (InputMismatchException e) {
            option = -1;
        }

        return option;
    }

    private static void promptOptions() {
        Logger.prompt(String.format("%s LSI source files are available:", files.size()));
        files.forEach((file) -> {
            Logger.prompt(String.format("(%s) - %s", files.indexOf(file) + 1, file));
        });
        Logger.prompt("(0) - Enter 0 to exit");
    }
}