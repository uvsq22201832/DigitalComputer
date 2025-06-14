package bench.cpu;

import bench.IBenchmark;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import static java.math.BigDecimal.TWO;

public class CPUDigitsOfPi implements IBenchmark {
    private int digits;
    private BigDecimal pi;
    private boolean running = true;

    @Override
    public void initialize(Object... params) {
        this.digits = (Integer) params[0];
    }

    @Override
    public void warmUp() {
        // Compute a smaller number of digits for warm-up
        System.out.println("Warming up...");
        computePi(1000); // Warm up with 1000 digits
        System.out.println("Warmup completed");
    }

    @Override
    public void run() {
        run(0); // Default algorithm
    }

    @Override
    public void run(Object... options) {
        if (!running) return;

        int algorithm = (Integer) options[0];
        switch (algorithm) {
            case 0:
                computePi(digits);
                break;
            case 1:
                computePiBBP(digits);
                break;
            default:
                throw new IllegalArgumentException("Invalid algorithm option");
        }
    }

    @Override
    public void clean() {
        // Nothing to clean
    }

    @Override
    public void cancel() {
        running = false;
    }

    @Override
    public String getResult() {
        return pi.toString();
    }

    // Gauss-Legendre algorithm implementation
    private void computePi(int digits) {
        MathContext mc = new MathContext(digits + 2, RoundingMode.HALF_UP);
        final BigDecimal TWO = BigDecimal.valueOf(2);
        final BigDecimal FOUR = BigDecimal.valueOf(4);

        BigDecimal a = BigDecimal.ONE;
        BigDecimal b = BigDecimal.ONE.divide(sqrt(TWO, mc), mc);
        BigDecimal t = BigDecimal.valueOf(0.25);
        BigDecimal x = BigDecimal.ONE;
        BigDecimal y;

        while (!a.equals(b)) {
            y = a;
            a = a.add(b).divide(TWO, mc);
            b = sqrt(b.multiply(y), mc);
            t = t.subtract(x.multiply(y.subtract(a).multiply(y.subtract(a)), mc));
            x = x.multiply(TWO);
        }

        pi = a.add(b).multiply(a.add(b)).divide(FOUR.multiply(t), mc);
        pi = pi.setScale(digits, RoundingMode.DOWN);
    }

    // Bailey-Borwein-Plouffe algorithm implementation
    private void computePiBBP(int digits) {
        MathContext mc = new MathContext(digits + 2, RoundingMode.HALF_UP);
        BigDecimal pi = BigDecimal.ZERO;
        final BigDecimal SIXTEEN = BigDecimal.valueOf(16);

        for (int k = 0; k < digits; k++) {
            BigDecimal term = BigDecimal.ONE.divide(
                            BigDecimal.valueOf(16).pow(k, mc), mc)
                    .multiply(
                            BigDecimal.valueOf(4).divide(
                                            BigDecimal.valueOf(8 * k + 1), mc)
                                    .subtract(
                                            BigDecimal.valueOf(2).divide(
                                                            BigDecimal.valueOf(8 * k + 4), mc)
                                                    .subtract(
                                                            BigDecimal.valueOf(1).divide(
                                                                            BigDecimal.valueOf(8 * k + 5), mc)
                                                                    .subtract(
                                                                            BigDecimal.valueOf(1).divide(
                                                                                    BigDecimal.valueOf(8 * k + 6), mc)
                                                                    ))));

            pi = pi.add(term, mc);

            if (!running) return;
        }

        this.pi = pi.setScale(digits, RoundingMode.DOWN);
    }

    // Helper method to compute square root using Newton's method
    private BigDecimal sqrt(BigDecimal A, MathContext mc) {
        BigDecimal x0 = BigDecimal.ZERO;
        BigDecimal x1 = new BigDecimal(Math.sqrt(A.doubleValue()));

        while (!x0.equals(x1)) {
            x0 = x1;
            x1 = A.divide(x0, mc);
            x1 = x1.add(x0);
            x1 = x1.divide(TWO, mc);
        }

        return x1;
    }
}