package testbench;

import bench.IBenchmark;
import bench.cpu.CPUThreadedHashing;

public class TestCPUThreadedHashing {

    public static void main(String[] args) {
        // Create timer and logger instances (adjust based on your framework)
        // Timer timer = new Timer();
        // Logger log = new Logger();

        IBenchmark bench = new CPUThreadedHashing();

        // Test parameters
        int maxLength = 10;
        int nThreads = 8; // Adjust based on your CPU cores

        // Test all three hash codes
        int[] hashCodes = {524381996, 52703576, 605107138, 1018655712, 317266982};

        for (int hashCode : hashCodes) {
            System.out.println("Breaking hash code: " + hashCode);

            // Reset benchmark
            bench = new CPUThreadedHashing();

            long startTime = System.currentTimeMillis();
            bench.run(maxLength, nThreads, hashCode);
            long endTime = System.currentTimeMillis();

            long executionTime = endTime - startTime;

            System.out.println("Finished in: " + executionTime + " ms");
            System.out.println("Result is: " + bench.getResult());
            System.out.println("--------------------");
        }
    }
}