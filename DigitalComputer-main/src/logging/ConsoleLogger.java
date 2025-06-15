package logging;

public class ConsoleLogger implements ILogger {
    private TimeUnitConverter.TimeUnit timeUnit = TimeUnitConverter.TimeUnit.MILLISECONDS;

    @Override
    public void write(String message) {
        System.out.println(message);
    }

    @Override
    public void write(long value) {
        System.out.println(TimeUnitConverter.convert(value, timeUnit));
    }

    @Override
    public void write(Object... values) {
        StringBuilder sb = new StringBuilder();
        for (Object value : values) {
            if (value instanceof Long) {
                sb.append(TimeUnitConverter.convert((Long)value, timeUnit));
            } else {
                sb.append(value.toString());
            }
            sb.append(" ");
        }
        System.out.println(sb.toString().trim());
    }

    public void setTimeUnit(TimeUnitConverter.TimeUnit unit) {
        this.timeUnit = unit;
    }

    @Override
    public void close() {
        // Nothing to close for console logger
    }
}