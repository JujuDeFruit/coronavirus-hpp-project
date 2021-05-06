package Runners;

import static org.junit.Assert.*;

import Models.DataType;
import Utils.ThreadUtils;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class ReaderTest {
    /**
     * We call {@link Reader#Reader(BlockingQueue, String)}
     * with a wrong path to the files, therefore we expect an
     * {@link IOException}, which is unchecked.
     */
    @Test(expected = IOException.class)
    public void testOpenFile() throws IOException {
        BlockingQueue<DataType> queue = new LinkedBlockingQueue<DataType>();
        Reader reader = new Reader(queue, "wrongPath");
        reader.openFile();
    }
    /**
     * We call {@link Reader#Reader(BlockingQueue, String)}
     * with a correct path to the files, then we close it.
     * This test open and close at the same time.
     */
    @Test
    public void testCloseFile() throws IOException {
        BlockingQueue<DataType> queue = new LinkedBlockingQueue<DataType>();
        Reader reader = new Reader(queue, "data\\20");
        reader.openFile();
        reader.closeFile();
    }
    /**
     * Test to check if the Reader read forwards the information
     * in the correct ante-chronological order.
     */
    @Test
    public void testRun() {
        BlockingQueue<DataType> queue = new LinkedBlockingQueue<DataType>();
        Reader reader = new Reader(queue, "data\\20");
        ExecutorService service = Executors.newFixedThreadPool(1);
        // Start reader
        service.execute(reader);
        // Wait for end of thread
        ThreadUtils.shutdownAndAwaitTermination(service);
        // Verify order
        for (int i = 0; i < 20; i++) {
            try {
                int currentId = queue.take().getPerson_id();
                assertEquals(i, currentId);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // Verify poison-pill
        try {
            int currentId = queue.take().getPerson_id();
            assertEquals(-1, currentId);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}