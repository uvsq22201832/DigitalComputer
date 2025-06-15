package timing;

public class Timer implements ITimer {
    private long startTime;
    private long pausedTime;
    private long totalPausedDuration;
    private boolean isRunning;
    private boolean isPaused;

    @Override
    public void start() {
        if (!isRunning && !isPaused) {
            startTime = System.nanoTime();
            isRunning = true;
            totalPausedDuration = 0;
        } else if (isPaused) {
            totalPausedDuration += System.nanoTime() - pausedTime;
            isPaused = false;
        }
    }

    @Override
    public long pause() {
        if (isRunning && !isPaused) {
            pausedTime = System.nanoTime();
            isPaused = true;
            return pausedTime - startTime - totalPausedDuration;
        }
        return 0;
    }

    @Override
    public long stop() {
        if (isRunning) {
            long elapsed = getElapsedTime();
            if (isPaused) {
                totalPausedDuration += System.nanoTime() - pausedTime;
                isPaused = false;
            }
            isRunning = false;
            return elapsed;
        }
        return 0;
    }

    @Override
    public void resume() {
        if (isPaused) {
            totalPausedDuration += System.nanoTime() - pausedTime;
            isPaused = false;
        }
    }

    private long getElapsedTime() {
        if (!isRunning) return 0;
        long currentTime = System.nanoTime();
        if (isPaused) {
            return pausedTime - startTime - totalPausedDuration;
        }
        return currentTime - startTime - totalPausedDuration;
    }
}