package main;

import bench.CompositeBenchmark;
import bench.cpu.CPURecursionLoopUnrolling;
import timing.Timer;

public class Main {
    public static void main(String[] args) {
        CompositeBenchmark composite = new CompositeBenchmark();
        CPURecursionLoopUnrolling bench = new CPURecursionLoopUnrolling();
        Timer timer = new Timer();

        long size = 2_000_000;
        int[] unrollLevels = {0, 1, 5, 15};
        int runsPerLevel = 3;

        bench.initialize(size);
        bench.warmUp();

        for (int level : unrollLevels) {
            for (int i = 0; i < runsPerLevel; i++) {
                timer.start();
                if (level == 0) {
                    bench.run(false);
                } else {
                    bench.run(true, level);
                }
                long time = timer.stop();

                // Estimate operations: prime checks + recursive calls
                long operations = size + (size / (level > 0 ? level : 1));
                composite.addRun(time, size, operations, level);
            }
        }

        double score = composite.calculateCompositeScore();
        System.out.println("Composite Benchmark Score: " + score);
    }
}