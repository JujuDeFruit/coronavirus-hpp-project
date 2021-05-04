package Runners;

import static org.junit.Assert.*;

import org.junit.Test;

import java.io.IOException;

public class ReaderTest {
    /**
     * We call {@link Reader#Reader()} with unknown file,
     * therefore we expect an {@link IOException}, which is
     * unchecked.
     */
    @Test(expected = IOException.class)
    public void testReadError(){

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