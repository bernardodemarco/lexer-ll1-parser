package exceptions;

import java.util.Map;

public class UnrecognizedCharacterException extends RuntimeException {
    public UnrecognizedCharacterException(char character, Map<String, Integer> position) {
        super(String.format("Unrecognized character ['%s'] at [line: %s, column: %s]", character, position.get("line"), position.get("column")));
    }
}
