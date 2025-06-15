package bench.cpu;

public class TestThreadedRoots {

    public static void main(String[] args) {
        // Configuration du benchmark
        int workload = 100000; // Ajustez selon vos besoins pour avoir au moins 1 seconde
        int[] threadCounts = {1, 2, 4, 8, 16, 32, 64};
        int iterations = 3; // Nombre d'itérations pour calculer la moyenne

        System.out.println("=== Multi-threaded Square Root Benchmark ===");
        System.out.println("Workload: " + workload);
        System.out.println("CPU Cores: " + Runtime.getRuntime().availableProcessors());
        System.out.println();

        // En-tête du tableau
        System.out.printf("%-10s %-15s %-15s %-15s%n",
                "Threads", "Avg Time (ms)", "Speedup", "Score");
        System.out.println("-------------------------------------------------------");

        double baselineTime = 0;

        for (int nThreads : threadCounts) {
            CPUThreadedRoots benchmark = new CPUThreadedRoots();
            benchmark.initialize(workload);

            // Warmup
            benchmark.warmUp();

            double totalTime = 0;

            // Exécuter plusieurs fois pour calculer la moyenne
            for (int i = 0; i < iterations; i++) {
                benchmark = new CPUThreadedRoots(); // Nouvelle instance pour chaque test
                benchmark.initialize(workload);

                long startTime = System.nanoTime();
                benchmark.run(nThreads);
                long endTime = System.nanoTime();

                double timeMs = (endTime - startTime) / 1_000_000.0;
                totalTime += timeMs;
            }

            double avgTime = totalTime / iterations;

            // Calculer le speedup par rapport au cas à 1 thread
            if (nThreads == 1) {
                baselineTime = avgTime;
            }

            double speedup = baselineTime / avgTime;
            double score = (workload * nThreads) / (avgTime / 1000.0); // Score basé sur workload/(time*threads)

            System.out.printf("%-10d %-15.2f %-15.2f %-15.0f%n",
                    nThreads, avgTime, speedup, score);
        }

        System.out.println();
        System.out.println("=== Analysis Notes ===");
        System.out.println("- Speedup = BaselineTime(1 thread) / CurrentTime");
        System.out.println("- Score = workload * threads / time(seconds)");
        System.out.println("- Look for diminishing returns as threads increase beyond CPU cores");
        System.out.println("- Runtime should decrease initially, then level off or increase");
    }
}