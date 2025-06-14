package logging;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Logger implementation that writes to a file
 */
public class FileLogger implements ILogger {
    private BufferedWriter writer;

    /**
     * Creates a FileLogger that writes to specified file
     * @param filename Path to output file
     * @throws IOException If file cannot be opened for writing
     */
    public FileLogger(String filename) throws IOException {
        // Create parent directories if they don't exist
        Files.createDirectories(Paths.get(filename).getParent());
        writer = new BufferedWriter(new FileWriter(filename));
    }

    @Override
    public void write(long value) {
        try {
            writer.write(String.valueOf(value));
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    @Override
    public void write(String text) {
        try {
            writer.write(text);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    @Override
    public void write(Object... values) {
        try {
            StringBuilder sb = new StringBuilder();
            for (Object o : values) {
                sb.append(o.toString()).append(" ");
            }
            writer.write(sb.toString().trim());
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    @Override
    public void close() {
        try {
            if (writer != null) {
                writer.close();
            }
        } catch (IOException e) {
            System.err.println("Error closing file: " + e.getMessage());
        }
    }
}