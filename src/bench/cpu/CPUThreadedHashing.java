package bench.cpu;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import bench.IBenchmark;

public class CPUThreadedHashing implements IBenchmark {

    private String result;
    volatile boolean running = true;

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

        // maximum text length
        int maxTextLength = (Integer)options[0];
        // thread pool size
        int nThreads = (Integer)options[1];
        // hash code
        int hashCode = (Integer)options[2];

        // try to break these hash codes (in ascending order of difficulty):
        // 524381996
        // 52703576
        // 605107138

        int length = 2;

        ExecutorService executor = Executors.newFixedThreadPool(nThreads);
        HashManager hasher = new HashManager();
        String text = "aa";

        while (running) {
            HashBreakerTask worker = new HashBreakerTask(hasher, text, hashCode);
            // assign new runnable to executor
            executor.execute(worker);
            // get next string (new task) OR NULL if final combination "zzz..z" reached
            text = hasher.getNextString(text);

            // stop search condition#1
            if (length > maxTextLength) {
                running = false;
            }

            // reset string to "aaa...a" with length+1
            if (text == null) {
                length++;
                text = "aa";
                for (int i = 2; i < length; ++i)
                    text += "a";
            }
        }

        // stop executor
        executor.shutdown();
        while (!executor.isTerminated()) {
        }

    }

    @Override
    public void clean() {
    }

    @Override
    public String getResult() {
        return String.valueOf(result);
    }

    class HashBreakerTask implements Runnable {

        // used to compute hashes from strings
        private final HashManager hasher;
        // the string to be hashed
        private final String text;
        // the expected hash output
        private final int expectedHash;

        public HashBreakerTask(HashManager hasher, String text, int expectedHash) {
            this.hasher = hasher;
            this.text = text;
            // 'text' is hashed and compared to 'expected hash'
            this.expectedHash = expectedHash;
        }

        @Override
        public void run() {
            // if we found the hash
            if (expectedHash == hasher.hash(text)) {
                // stop condition#2
                running = false;
                //save password text as result to be printed on screen
                result = text;
            }
        }
    }

    /**
     * Used to compute hashes from strings
     */
    class HashManager {

        // do not change alphabet
        private final String charSet = "abcdefghijklmnopqrstuvwxyz";
        private final Random rand = new Random();

        // do not change function
        public int hash(String text) {
            int a = 0;
            int b = 0;
            for (char c : text.toCharArray()) {
                int index = charSet.indexOf(c);
                if (index == -1)
                    index = charSet.length() + 1;
                for (int i = 0; i < 17; i++) {
                    a = a * -6 + b + 0x74FA - index;
                    b = b / 3 + a + 0x81BE - index;
                }
            }

            return (a ^ b) % Integer.MAX_VALUE;
        }

        /**
         * Compute the next alphabetical string that follows naturally after the one
         * given as a parameter, with the same length. If no further combination is
         * possible (e.g., after "zzz") then null should be returned.
         *
         * @param text
         *         - A string of any length
         * @return - Next alphabetic string after given one, e.g., "aaa"+1 => "aab";
         *         "abz"+1 => "aca" <br/>
         *         - Null: if there is no further combination after given text, e.g.,
         *         "zz...zzz"
         */
        public String getNextString(String text) {
            int[] index = new int[text.length()];
            int end = charSet.length() - 1;

            // convert string to table of indices
            // abz = [0,1,25]
            for (int i = 0; i < text.length(); i++) {
                index[i] = charSet.indexOf(text.charAt(i));
            }

            // increment last character and manage possible carry
            // [0,1,25] + 1 = [0,1,26!] => [0,2,0]
            int carry = 1;
            for (int i = index.length - 1; i >= 0 && carry > 0; i--) {
                index[i] += carry;
                if (index[i] > end) {
                    index[i] = 0;
                    carry = 1;
                } else {
                    carry = 0;
                }
            }

            // if complete overflow ("zzz..zz") return null
            if (carry > 0) {
                return null;
            }

            // convert back to string
            // [0,2,0] = aca
            StringBuilder result = new StringBuilder();
            for (int i = 0; i < index.length; i++) {
                result.append(charSet.charAt(index[i]));
            }

            return result.toString();
        }

        // can be used as an alternative to getNextString, but it will be infinitely slower to break longer hashes
        public String getRandomString(int length) {
            String text = "";

            for (int i = 0; i < length; i++) {
                char c = charSet.charAt(rand.nextInt(charSet.length()));
                text += c;
            }

            return text;
        }
    }

}