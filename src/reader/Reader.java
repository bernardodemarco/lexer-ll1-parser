package reader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

public class Reader {
    private List<String> content;
    private int line = 0;
    private int column = 0;

    public Map<String, Integer> getLocation() {
        return Map.of(
                "line", line,
                "column", column
        );
    }

    public void loadSourceFile(String filePath) throws IOException {
        content = Files.readAllLines(Paths.get(filePath));
        line = 0;
        column = 0;
    }

    public boolean hasContentToConsume() {
        boolean currentLineIsPreviousToLastLine = line < content.size() - 1;
        boolean isLastLine = line == content.size() - 1;
        return currentLineIsPreviousToLastLine
                || (isLastLine && column < content.get(line).length());
    }

    public boolean isEndOfLine() {
        return column == content.get(line).length() - 1;
    }

    public boolean isLineEmpty() {
        return content.get(line).isEmpty();
    }

    public char getCurrentChar() {
        return content.get(line).charAt(column);
    }

    public void goToNextChar() {
        if (!isEndOfLine()) {
            column++;
            return;
        }

        column = 0;
        line++;
    }

    public void goToPreviousChar() {
        if (column > 0) {
            column--;
            return;
        }

        line--;
        column = content.get(line).length() - 1;
    }
}
