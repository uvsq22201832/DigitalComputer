package bench;

public class DummyBenchmark implements IBenchmark {
    private int size;
    private volatile boolean running = false;

    @Override
    public void initialize(Object... params) {
        this.size = (int)params[0]; // Example: array size
    }

    @Override
    public void run() {
        running = true;
        // Bubble sort on random array
        int[] array = new int[size];
        java.util.Random rand = new java.util.Random();
        for(int i=0; i<size; i++) {
            array[i] = rand.nextInt();
        }

        for(int i=0; i<size && running; i++) {
            for(int j=1; j<size-i && running; j++) {
                if(array[j-1] > array[j]) {
                    int temp = array[j-1];
                    array[j-1] = array[j];
                    array[j] = temp;
                }
            }
        }
    }

    @Override
    public void clean() {
        // Cleanup if needed
    }

    @Override
    public void cancel() {
        running = false;
    }

    @Override
    public void run(Object... params) {
        run(); // Default implementation
    }
}