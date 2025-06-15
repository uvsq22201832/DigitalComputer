package testbench;

import bench.ram.VirtualMemoryBenchmark;

public class TestVirtualMemory {

    public static void main(String[] args) {
        VirtualMemoryBenchmark vmBench = new VirtualMemoryBenchmark();

        // Scenario 1: File size smaller than available RAM (e.g., 2GB)
        System.out.println("=== Scenario 1: RAM-only access (2GB file) ===");
        long fileSize1 = 2L * 1024 * 1024 * 1024; // 2GB
        int bufferSize1 = 4 * 1024; // 4KB

        vmBench.run(fileSize1, bufferSize1);
        System.out.println(vmBench.getResult());

        // Wait a bit between tests
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Scenario 2: File size larger than available RAM (e.g., 8GB)
        // WARNING: Adjust this value based on your system's RAM!
        // If your system has 8GB RAM, use 12GB or 16GB for this test
        System.out.println("\n=== Scenario 2: Virtual memory access (8GB file) ===");
        System.out.println("WARNING: This test may take significantly longer and stress your system!");

        long fileSize2 = 8L * 1024 * 1024 * 1024; // 8GB
        int bufferSize2 = 64 * 1024; // 64KB

        VirtualMemoryBenchmark vmBench2 = new VirtualMemoryBenchmark();
        vmBench2.run(fileSize2, bufferSize2);
        System.out.println(vmBench2.getResult());

        // Additional tests with different buffer sizes
        System.out.println("\n=== Additional tests with different buffer sizes ===");

        // Test with 1KB buffer
        VirtualMemoryBenchmark vmBench3 = new VirtualMemoryBenchmark();
        vmBench3.run(1L * 1024 * 1024 * 1024, 1024); // 1GB file, 1KB buffer
        System.out.println("1KB buffer: " + vmBench3.getResult());

        // Test with 1MB buffer
        VirtualMemoryBenchmark vmBench4 = new VirtualMemoryBenchmark();
        vmBench4.run(1L * 1024 * 1024 * 1024, 1024 * 1024); // 1GB file, 1MB buffer
        System.out.println("1MB buffer: " + vmBench4.getResult());
    }
}