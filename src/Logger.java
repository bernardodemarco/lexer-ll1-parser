public class Logger {
    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    public static final String GREEN_BOLD = "\033[1;92m";
    private static final String RESET = "\u001B[0m";

    public static void debug(String message) {
        message = "DEBUG - " + message;
        System.out.println(message);
    }

    public static void info(String message) {
        message = "INFO - " + message;
        System.out.println(GREEN + message + RESET);
    }

    public static void error(String message) {
        message = "ERROR - " + message;
        System.out.println(RED + message + RESET);
    }

    public static void prompt(String message) {
        System.out.println(GREEN_BOLD + message + RESET);
    }
}
