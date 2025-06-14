package timing;

public class Timer implements ITimer {
    private long startTime;
    private long totalTime = 0;
    private boolean isRunning = false;

    @Override
    public void start() {
        startTime = System.nanoTime();
        totalTime = 0;
        isRunning = true;
    }

    @Override
    public long stop() {
        if (isRunning) {
            totalTime += System.nanoTime() - startTime;
            isRunning = false;
        }
        return totalTime;
    }

    @Override
    public void resume() {
        if (!isRunning) {
            startTime = System.nanoTime();
            isRunning = true;
        }
    }

    @Override
    public long pause() {
        if (isRunning) {
            long elapsed = System.nanoTime() - startTime;
            totalTime += elapsed;
            isRunning = false;
            return elapsed;
        }
        return 0;
    }
}