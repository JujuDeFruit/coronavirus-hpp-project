import Models.DataType;
import Runners.Reader;
import Utils.ThreadUtils;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class MainReader {
    public static void main(String[] args) {
        BlockingQueue<DataType> queue = new LinkedBlockingQueue<DataType>();
        Reader reader = new Reader(queue, "data\\1000000");
        ExecutorService service = Executors.newFixedThreadPool(1);
        // Start reader
        long time = System.nanoTime();
        service.execute(reader);
        // Wait for end of thread
        ThreadUtils.shutdownAndAwaitTermination(service);
        time = System.nanoTime() - time;
        // Display time
        System.out.println(time/1_000_000 + " ms");
    }
}
