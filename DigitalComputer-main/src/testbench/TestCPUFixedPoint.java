package testbench;

import bench.IBenchmark;
import bench.cpu.CPUFixedPoint;
import logging.ConsoleLogger;
import logging.ILogger;
import timing.ITimer;
import timing.Timer;

public class TestCPUFixedPoint {
    public static void main(String[] args) {
        ITimer timer = new Timer();
        ILogger log = new ConsoleLogger();
        IBenchmark bench = new CPUFixedPoint();

        // Test with 1 million operations
        final int workload = 1000000;

        bench.initialize(workload);
        bench.warmUp();

        log.write("Testing fixed point arithmetic performance");
        log.write("Workload size: " + workload);
        log.write("----------------------------------");

        // Test individual components
        testComponent(bench, timer, log, 0, "Arithmetic test");
        testComponent(bench, timer, log, 1, "Branching test");
        testComponent(bench, timer, log, 2, "Array test");

        // Test all components together
        testComponent(bench, timer, log, 3, "Combined tests");

        bench.clean();
    }

    private static void testComponent(IBenchmark bench, ITimer timer, ILogger log,
                                      int option, String testName) {
        log.write("\n" + testName);
        timer.resume();
        bench.run(option);
        timer.pause();
        log.write("----------------------------------");
    }
}