package logging;

public class TimeUnitConverter {
    public enum TimeUnit {
        NANOSECONDS("ns", 1),
        MICROSECONDS("µs", 1000),
        MILLISECONDS("ms", 1000000),
        SECONDS("s", 1000000000);

        private final String symbol;
        private final long divisor;

        TimeUnit(String symbol, long divisor) {
            this.symbol = symbol;
            this.divisor = divisor;
        }

        public String format(long nanoseconds) {
            double value = (double)nanoseconds / divisor;
            return String.format("%.3f %s", value, symbol);
        }
    }

    public static String convert(long nanoseconds, TimeUnit targetUnit) {
        return targetUnit.format(nanoseconds);
    }
}