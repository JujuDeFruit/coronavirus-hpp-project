package Runners;

import static org.junit.Assert.*;

import Models.DataType;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ReaderTest {
    /**
     * We call {@link Reader#Reader(BlockingQueue, String)}
     * with a wrong path to the files, therefore we expect an
     * {@link IOException}, which is unchecked.
     */
    @Test(expected = IOException.class)
    public void testReadError() throws IOException {
        BlockingQueue<DataType> queue = new LinkedBlockingQueue<DataType>();
        Reader reader = new Reader(queue, "wrongPath");
        reader.openFile();
    }
    /**
     * We call {@link Reader#Reader(BlockingQueue, String)}
     * with a correct path to the files.
     */
    @Test
    public void testRead() throws IOException {
        BlockingQueue<DataType> queue = new LinkedBlockingQueue<DataType>();
        Reader reader = new Reader(queue, "data\\20");
        reader.openFile();
    }
    /**
     * Test to check if the Reader read forwards the information
     * in the correct ante-chronological order.
     */
    @Test
    public void testForwardOrder(){
        // Start reader
        // Wait for end of thread
        // Verify order
    }
}