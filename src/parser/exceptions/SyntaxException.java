package parser.exceptions;

import utils.Logger;

import java.util.Set;
import java.util.stream.Collectors;

public class SyntaxException extends RuntimeException {
    public SyntaxException(String stackTop, String input, Set<String> expectedInputs) {
        String expectedInputsReadableRepresentation = expectedInputs.stream()
                .map(expectedInput -> String.format("\"%s\"", expectedInput))
                .collect(Collectors.joining(", "));
        Logger.error(String.format("When handling [%s], one of the following input options [%s] were expected, but [%s] was received.", stackTop, expectedInputsReadableRepresentation, input));
    }
}
