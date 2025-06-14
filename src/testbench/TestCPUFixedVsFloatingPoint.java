package testbench;

import bench.IBenchmark;
import bench.cpu.CPUFixedVsFloatingPoint;
import bench.cpu.CPUFixedVsFloatingPoint.NumberRepresentation;
import logging.ConsoleLogger;
import logging.ILogger;
import timing.ITimer;
import timing.Timer;

public class TestCPUFixedVsFloatingPoint {
    public static void main(String[] args) {
        ITimer timer = new Timer();
        ILogger log = new ConsoleLogger();
        IBenchmark bench = new CPUFixedVsFloatingPoint();

        final int workload = 10000000;

        bench.initialize(workload);
        bench.warmUp();

        log.write("Comparing fixed vs floating point performance");
        log.write("Workload size: " + workload);
        log.write("----------------------------------");

        // Test fixed point
        testRepresentation(bench, timer, log, NumberRepresentation.FIXED);

        // Test floating point
        testRepresentation(bench, timer, log, NumberRepresentation.FLOATING);

        bench.clean();
    }

    private static void testRepresentation(IBenchmark bench, ITimer timer,
                                           ILogger log, NumberRepresentation type) {
        log.write("\nTesting: " + type);
        timer.start();
        bench.run(type);
        long time = timer.stop();
        log.write("Time: " + time + " ns");
        log.write("Result: " + bench.getResult());
        log.write("----------------------------------");
    }
}