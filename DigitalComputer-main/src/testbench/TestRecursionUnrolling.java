package testbench;

import bench.IBenchmark;
import bench.cpu.CPURecursionLoopUnrolling;
import timing.ITimer;
import timing.Timer;

public class TestRecursionUnrolling {
    public static void main(String[] args) {
        IBenchmark bench = new CPURecursionLoopUnrolling();
        ITimer timer = new Timer();
        long size = 2_000_000; // workload size

        bench.initialize(size);
        bench.warmUp();

        // Test different unroll levels
        testUnrollLevel(bench, false, 0); // No unrolling
        testUnrollLevel(bench, true, 1);  // Unroll level 1
        testUnrollLevel(bench, true, 5);  // Unroll level 5
        testUnrollLevel(bench, true, 15); // Unroll level 15

        bench.clean();
    }

    private static void testUnrollLevel(IBenchmark bench, boolean unrolled, int level) {
        System.out.printf("%nTesting %sunrolled (level %d)%n", unrolled ? "" : "not ", level);

        ITimer timer = new Timer();
        timer.start();
        if (unrolled) {
            bench.run(true, level);
        } else {
            bench.run(false);
        }
        long time = timer.stop();

        // Here you would add the run to your composite benchmark
        // compositeBench.addRun(time, size, operations, level);
    }
}