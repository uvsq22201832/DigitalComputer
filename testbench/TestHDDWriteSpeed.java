package testbench;

import bench.IBenchmark;
import bench.hdd.HDDWriteSpeed;

public class TestHDDWriteSpeed {

    public static void main(String[] args) {
        IBenchmark bench = new HDDWriteSpeed();

        System.out.println("=== HDD Write Speed Benchmark ===");
        System.out.println();

        // Test 1: Fixed Size benchmark (fs)
        System.out.println("--- Test 1: Fixed File Size (512MB) with Variable Buffer Sizes ---");
        bench = new HDDWriteSpeed();
        bench.run("fs", true); // fixed size, clean files after
        System.out.println("Result: " + bench.getResult());
        System.out.println();

        // Wait a bit between tests
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Test 2: Fixed Buffer benchmark (fb)
        System.out.println("--- Test 2: Fixed Buffer Size (2KB) with Variable File Sizes ---");
        bench = new HDDWriteSpeed();
        bench.run("fb", true); // fixed buffer, clean files after
        System.out.println("Result: " + bench.getResult());
        System.out.println();

        System.out.println("=== Benchmark Complete ===");

        // Optional: Test without cleaning files to inspect results
        /*
        System.out.println("--- Test 3: Fixed Size without cleaning (for inspection) ---");
        bench = new HDDWriteSpeed();
        bench.run("fs", false); // don't clean files
        System.out.println("Result: " + bench.getResult());
        */
    }
}