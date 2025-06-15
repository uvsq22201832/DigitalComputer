package bench.hdd;

import java.io.IOException;

import bench.IBenchmark;

public class HDDWriteSpeed implements IBenchmark {

    private String result;

    @Override
    public void initialize(Object... params) {
    }

    @Override
    public void warmUp() {
    }

    @Override
    public void run() {
        throw new UnsupportedOperationException(
                "Method not implemented. Use run(Object) instead");
    }

    @Override
    public void run(Object... options) {
        FileWriter writer = new FileWriter();
        // either "fs" - fixed size, or "fb" - fixed buffer
        String option = (String) options[0];
        // true/false whether the written files should be deleted at the end
        Boolean clean = (Boolean) options[1];

        // Adjust this path based on your system
        // For Windows lab computers: "D:\\000-bench\\write-"
        // For your personal computer, change to a path where you have write permissions
        String prefix = "D:\\000-bench\\write-";
        String suffix = ".dat";
        int minIndex = 0;
        int maxIndex = 8;
        long fileSize = 512 * 1024 * 1024L; // 512 MB
        int bufferSize = 4 * 1024; // 4 KB

        try {
            if (option.equals("fs")) {
                writer.streamWriteFixedFileSize(prefix, suffix, minIndex,
                        maxIndex, fileSize, clean);
                result = "Fixed file size benchmark completed";
            } else if (option.equals("fb")) {
                writer.streamWriteFixedBufferSize(prefix, suffix, minIndex,
                        maxIndex, bufferSize, clean);
                result = "Fixed buffer size benchmark completed";
            } else {
                throw new IllegalArgumentException("Argument "
                        + options[0].toString() + " is undefined");
            }
        } catch (IOException e) {
            e.printStackTrace();
            result = "Benchmark failed: " + e.getMessage();
        }
    }

    @Override
    public void clean() {
        // Additional cleanup if needed
    }

    @Override
    public String getResult() {
        return result;
    }
}