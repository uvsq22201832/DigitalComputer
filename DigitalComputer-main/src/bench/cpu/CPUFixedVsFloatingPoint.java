package bench.cpu;

import bench.IBenchmark;

public class CPUFixedVsFloatingPoint implements IBenchmark {
    public enum NumberRepresentation {
        FIXED, FLOATING
    }

    private int size;
    private double fpResult;
    private int intResult;

    @Override
    public void initialize(Object... params) {
        this.size = (Integer) params[0];
    }

    @Override
    public void warmUp() {
        runFixed(10000);
        runFloating(10000);
    }

    @Override
    public void run() {
        throw new UnsupportedOperationException("Use run(Object) instead");
    }

    @Override
    public void run(Object... options) {
        NumberRepresentation type = (NumberRepresentation) options[0];
        switch (type) {
            case FIXED -> runFixed(size);
            case FLOATING -> runFloating(size);
        }
    }

    private void runFixed(int iterations) {
        intResult = 0;
        for (int i = 0; i < iterations; i++) {
            // Optimized fixed-point division by 256 using bit shift
            intResult += i >> 8;  // Equivalent to i/256 but faster
        }
    }

    private void runFloating(int iterations) {
        fpResult = 0.0;
        for (int i = 0; i < iterations; i++) {
            fpResult += i / 256.0;  // Floating-point division
        }
    }

    @Override
    public void clean() {
        // Nothing to clean
    }

    @Override
    public void cancel() {
        // Not implemented
    }

    @Override
    public String getResult() {
        return "Fixed: " + intResult + ", Floating: " + fpResult;
    }
}