package testbench;

import bench.IBenchmark;
import bench.DummyBenchmark;
import timing.ITimer;
import timing.Timer;
import logging.ILogger;
import logging.ConsoleLogger;
import logging.FileLogger;

public class TestBenchmark {
    public static void main(String[] args) {
        ITimer timer = new Timer();
        ILogger consoleLogger = new ConsoleLogger();
        ILogger fileLogger = null;

        try {
            fileLogger = new FileLogger("results/benchmark_results.txt");
        } catch (Exception e) {
            System.err.println("Cannot create file logger: " + e.getMessage());
            return;
        }

        IBenchmark benchmark = new DummyBenchmark();
        final int SIZE = 10000; // Adjust this for longer/shorter test

        // Initialize benchmark
        benchmark.initialize(SIZE);

        // Warm up JVM
        for(int i=0; i<5; i++) {
            benchmark.run();
        }

        // Actual benchmark
        timer.start();
        benchmark.run();
        long time = timer.stop();

        // Convert nanoseconds to milliseconds
        double timeMs = time / 1_000_000.0;

        // Log results
        consoleLogger.write("Benchmark completed in", time, "ns (", timeMs, "ms)");
        fileLogger.write("Benchmark with size", SIZE, "completed in", time, "ns (", timeMs, "ms)");

        // Clean up
        benchmark.clean();
        fileLogger.close();
    }
}