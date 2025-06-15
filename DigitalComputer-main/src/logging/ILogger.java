package logging;

public interface ILogger {
    void write(long value);
    void write(String text);
    void write(Object... values);
    void close();
}