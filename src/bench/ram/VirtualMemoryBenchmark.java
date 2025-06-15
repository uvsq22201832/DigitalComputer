package bench.ram;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Random;

import timing.Timer;
import bench.IBenchmark;

/**
 * Maps a large file into RAM triggering the virtual memory mechanism. Performs
 * reads and writes to the respective file.<br>
 * The access speeds depend on the file size: if the file can fit the available
 * RAM, then we are measuring RAM speeds.<br>
 * Conversely, we are measuring the access speed of virtual memory, implying a
 * mixture of RAM and HDD access speeds (i.e., lower speeds).
 */
public class VirtualMemoryBenchmark implements IBenchmark {

    private String result = "";
    private MemoryMapper core;

    @Override
    public void initialize(Object... params) {
        /* not today */
    }

    @Override
    public void warmUp() {
        /* summer is coming anyway */
    }

    @Override
    public void run() {
        throw new UnsupportedOperationException("Use run(Object[]) instead");
    }

    @Override
    public void run(Object... options) {
        // expected: {fileSize, bufferSize}
        Object[] params = (Object[]) options;
        long fileSize = Long.parseLong(params[0].toString()); // e.g. 2-16GB
        int bufferSize = Integer.parseInt(params[1].toString()); // e.g. 4+KB

        try {
            core = new MemoryMapper("D:\\000_core", fileSize); // change path as needed
            byte[] buffer = new byte[bufferSize];
            Random rand = new Random();
            DecimalFormat df = new DecimalFormat("0.00");

            Timer timer = new Timer();

            // write to VM
            timer.start();
            for (long i = 0; i < fileSize; i += bufferSize) {
                // 1. generate random content (see assignments 9,11)
                rand.nextBytes(buffer);
                // 2. write to memory mapper
                core.put(i, buffer);
            }
            timer.stop();
            // 3. fileSize/time [MB/s]
            double speed = (fileSize / 1024.0 / 1024.0) / (timer.getElapsedTime() / 1000.0);

            result = "\nWrote " + (fileSize / 1024 / 1024L)
                    + " MB to virtual memory at " + df.format(speed) + " MB/s";

            // read from VM
            timer.start();
            for (long i = 0; i < fileSize; i += bufferSize) {
                // 5. get from memory mapper
                buffer = core.get(i, bufferSize);
            }
            timer.stop();
            // 6. MB/s
            speed = (fileSize / 1024.0 / 1024.0) / (timer.getElapsedTime() / 1000.0);

            // append to previous 'result' string
            result += "\nRead " + (fileSize / 1024 / 1024L)
                    + " MB from virtual memory at " + df.format(speed) + " MB/s";

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (core != null) {
                core.purge();
            }
        }
    }

    @Override
    public void clean() {
        if (core != null) {
            core.purge();
        }
    }

    @Override
    public String getResult() {
        return result;
    }
}