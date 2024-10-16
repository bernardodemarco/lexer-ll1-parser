package tokens;

public class UnrecognizedCharacterException extends RuntimeException {
    public UnrecognizedCharacterException(char character) {
        super(String.format("Unrecognized character \"%s\"", character));
    }
}
