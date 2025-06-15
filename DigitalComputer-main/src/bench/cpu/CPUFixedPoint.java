package bench.cpu;

import bench.IBenchmark;
import timing.ITimer;
import timing.Timer;
import logging.ILogger;
import logging.ConsoleLogger;

public class CPUFixedPoint implements IBenchmark {
    private int size;
    private int[] num;
    private int[] res;
    private int j, k, l;
    private long operationCount;
    private ITimer timer;
    private ILogger log;

    @Override
    public void initialize(Object... params) {
        this.size = (Integer) params[0];
        this.num = new int[]{0, 1, 2, 3};
        this.res = new int[size];
        this.j = 1;
        this.k = 2;
        this.l = 3;
        this.timer = new Timer();
        this.log = new ConsoleLogger();
    }

    @Override
    public void warmUp() {
        arithmeticTest(10000);
        branchingTest(10000);
        arrayTest(10000);
    }

    @Override
    public void run() {
        run(1); // Run all tests by default
    }

    @Override
    public void run(Object... options) {
        operationCount = 0;
        timer.start();

        int testType = (Integer) options[0];
        switch (testType) {
            case 0 -> arithmeticTest(size);
            case 1 -> branchingTest(size);
            case 2 -> arrayTest(size);
            case 3 -> {
                arithmeticTest(size);
                branchingTest(size);
                arrayTest(size);
            }
            default -> throw new IllegalArgumentException("Invalid test type");
        }

        long time = timer.stop();
        calculateMOPS(time);
    }

    private void arithmeticTest(int iterations) {
        // Counts as 29 operations per iteration (see PDF)
        for (int i = 0; i < iterations; i++) {
            j = num[1] * (k - j) * (l - k);  // 9 ops
            k = num[3] * k - (l - j) * k;     // 9 ops
            l = (l - k) * (num[2] + j);       // 7 ops
            res[l % res.length] = j + k + l;   // 4 ops (mod counts as 3)
            operationCount += 29;
        }
    }

    private void branchingTest(int iterations) {
        // Counts as 15 operations per iteration
        for (int i = 0; i < iterations; i++) {
            if (j == 1) {          // 2 ops (j, ==)
                j = num[2];        // 2 ops
            } else {
                j = num[3];        // 2 ops
            }
            if (j > 2) {           // 2 ops
                j = num[0];        // 2 ops
            } else {
                j = num[1];        // 2 ops
            }
            if (j < 1) {           // 2 ops
                j = num[1];        // 2 ops
            } else {
                j = num[0];        // 2 ops
            }
            operationCount += 15;
        }
    }

    private void arrayTest(int iterations) {
        // Counts as 7 operations per iteration
        int[] a = new int[iterations];
        int[] b = new int[iterations];

        for (int i = 0; i < iterations; i++) {
            a[i] = i % 4;                   // 4 ops
            b[i] = (i + 1) % 4;             // 4 ops
            res[i % res.length] = a[b[i]];   // 7 ops
            operationCount += 15;
        }
    }

    private void calculateMOPS(long timeInNs) {
        double timeInSeconds = timeInNs / 1e9;
        double mops = (operationCount / timeInSeconds) / 1e6;
        log.write("MOPS: " + String.format("%.2f", mops));
        log.write("Total operations: " + operationCount);
        log.write("Time: " + timeInNs + " ns");
    }

    @Override
    public void clean() {
        num = null;
        res = null;
    }

    @Override
    public void cancel() {
        // Not implemented
    }

    @Override
    public String getResult() {
        return "MOPS calculation completed";
    }
}