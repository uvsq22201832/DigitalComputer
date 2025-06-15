package bench;

public interface IBenchmark {
    void initialize(Object... params);
    void warmUp();
    void run();
    void run(Object... options);
    void clean();
    void cancel();
    String getResult();
}