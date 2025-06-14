package testbench;

import bench.IBenchmark;
import bench.DummyBenchmark;
import timing.ITimer;
import timing.Timer;
import logging.ILogger;
import logging.ConsoleLogger;
import logging.TimeUnitConverter;

public class TestBenchmark {
    public static void main(String[] args) {
        ITimer timer = new Timer();
        ILogger log = new ConsoleLogger();
        IBenchmark bench = new DummyBenchmark();

        // Test with different time units
        testWithTimeUnit(timer, log, bench, TimeUnitConverter.TimeUnit.NANOSECONDS);
        testWithTimeUnit(timer, log, bench, TimeUnitConverter.TimeUnit.MICROSECONDS);
        testWithTimeUnit(timer, log, bench, TimeUnitConverter.TimeUnit.MILLISECONDS);
        testWithTimeUnit(timer, log, bench, TimeUnitConverter.TimeUnit.SECONDS);

        // Test pause-resume sequence
        testPauseResumeSequence(timer, log);
    }

    private static void testWithTimeUnit(ITimer timer, ILogger log, IBenchmark bench,
                                         TimeUnitConverter.TimeUnit unit) {
        ((ConsoleLogger)log).setTimeUnit(unit);
        log.write("Testing with time unit: " + unit.name());

        timer.start();
        bench.run();
        long time = timer.stop();

        log.write("Elapsed time: ", time);
        log.write("----------------------");
    }

    private static void testPauseResumeSequence(ITimer timer, ILogger log) {
        ((ConsoleLogger)log).setTimeUnit(TimeUnitConverter.TimeUnit.MILLISECONDS);
        log.write("Testing pause-resume sequence");

        timer.start();
        try {
            Thread.sleep(100);
            long pausedTime = timer.pause();
            log.write("Paused after: ", pausedTime);

            Thread.sleep(200); // This shouldn't count
            timer.resume();
            Thread.sleep(100);

            long totalTime = timer.stop();
            log.write("Total time: ", totalTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}