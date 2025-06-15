package bench;

import java.util.ArrayList;
import java.util.List;

public class CompositeBenchmark {
    private List<BenchmarkRun> runs = new ArrayList<>();

    public void addRun(double time, long size, long operations, int unrollLevel) {
        runs.add(new BenchmarkRun(time, size, operations, unrollLevel));
    }

    public double calculateCompositeScore() {
        if (runs.isEmpty()) return 0;

        double totalScore = 0;
        for (BenchmarkRun run : runs) {
            // Normalize values using logarithms to bring them to similar scales
            double normalizedTime = Math.log1p(run.time); // log(1 + time)
            double normalizedSize = Math.log1p(run.size);
            double normalizedOps = Math.log1p(run.operations);

            // Higher unroll levels should contribute more to the score
            double unrollWeight = 1 + (run.unrollLevel / 10.0);

            // Composite score formula
            double score = (normalizedSize * normalizedOps) / (normalizedTime * unrollWeight);
            totalScore += score;
        }

        return totalScore / runs.size();
    }

    private static class BenchmarkRun {
        double time; // in nanoseconds
        long size;
        long operations;
        int unrollLevel;

        BenchmarkRun(double time, long size, long operations, int unrollLevel) {
            this.time = time;
            this.size = size;
            this.operations = operations;
            this.unrollLevel = unrollLevel;
        }
    }
}