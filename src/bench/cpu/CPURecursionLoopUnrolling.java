package bench.cpu;

import bench.IBenchmark;
import timing.ITimer;
import timing.Timer;

public class CPURecursionLoopUnrolling implements IBenchmark {
    private long size;
    private boolean unrolled;
    private int unrollLevel;
    private ITimer timer;
    private long lastPrime;
    private int callCount;

    @Override
    public void initialize(Object... params) {
        this.size = (Long) params[0];
    }

    @Override
    public void warmUp() {
        // Warm up with a small prime calculation
        recursive(2, 1000, 0);
        recursiveUnrolled(2, 1, 1000, 0);
    }

    @Override
    public void run() {
        throw new UnsupportedOperationException("Use run(Object...) instead");
    }

    @Override
    public void run(Object... options) {
        unrolled = (Boolean) options[0];
        if (unrolled) {
            unrollLevel = (Integer) options[1];
        }

        timer = new Timer();
        callCount = 0;
        lastPrime = 0;

        timer.start();
        if (unrolled) {
            recursiveUnrolled(2, unrollLevel, size, 0);
        } else {
            recursive(2, size, 0);
        }
        long time = timer.stop();

        System.out.printf("Last prime reached: %d%n", lastPrime);
        System.out.printf("Total recursive calls: %d%n", callCount);
        System.out.printf("Execution time: %.4f ms%n", time / 1_000_000.0);
    }

    private long recursive(long start, long size, int counter) {
        callCount = counter;
        try {
            if (start > size) {
                return start;
            }

            if (isPrime(start)) {
                lastPrime = start;
            }

            return recursive(start + 1, size, counter + 1);
        } catch (StackOverflowError e) {
            System.out.printf("Stack overflow at prime %d after %d calls%n", lastPrime, callCount);
            return 0;
        }
    }

    private long recursiveUnrolled(long start, int unrollLevel, long size, int counter) {
        callCount = counter;
        try {
            if (start > size) {
                return start;
            }

            // Find 'unrollLevel' primes in this call
            int found = 0;
            long current = start;
            while (found < unrollLevel && current <= size) {
                if (isPrime(current)) {
                    lastPrime = current;
                    found++;
                }
                current++;
            }

            return recursiveUnrolled(current, unrollLevel, size, counter + 1);
        } catch (StackOverflowError e) {
            System.out.printf("Stack overflow at prime %d after %d calls (unroll level %d)%n",
                    lastPrime, callCount, unrollLevel);
            return 0;
        }
    }

    private boolean isPrime(long x) {
        if (x <= 2) {
            return x == 2;
        }
        if (x % 2 == 0) {
            return false;
        }
        for (long i = 3; i * i <= x; i += 2) {
            if (x % i == 0) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void clean() {
        // No resources to clean
    }

    @Override
    public void cancel() {
        // Not implemented
    }

    @Override
    public String getResult() {
        return String.format("Last prime: %d, Calls: %d", lastPrime, callCount);
    }
}