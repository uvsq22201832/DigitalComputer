package testbench;

import bench.IBenchmark;
import bench.cpu.CPUDigitsOfPi;
import logging.ConsoleLogger;
import logging.ILogger;
import timing.ITimer;
import timing.Timer;

public class TestBenchmark {
    public static void main(String[] args) {
        ITimer timer = new Timer();
        ILogger log = new ConsoleLogger();
        IBenchmark bench = new CPUDigitsOfPi();

        // Test different digit counts
        int[] digitCounts = {50, 100, 500, 1000, 5000, 10000, 50000, 100000};

        log.write("Starting Pi benchmark tests");
        log.write("-------------------------");

        for (int digits : digitCounts) {
            // Initialize benchmark with digit count
            bench.initialize(digits);

            // Warm up
            bench.warmUp();

            // Run benchmark
            timer.start();
            bench.run(0); // Using Gauss-Legendre algorithm
            long time = timer.stop();

            // Output results
            log.write("Digits:", digits, "Time:", time, "ns");
            log.write("-------------------------");
        }

        bench.clean();
    }
}