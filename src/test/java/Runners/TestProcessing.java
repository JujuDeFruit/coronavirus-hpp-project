package Runners;

import static org.junit.Assert.*;

import java.util.Vector;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.junit.Test;

import Models.ContaminationChain;
import Models.DataType;

public class TestProcessing {

	@Test
	public void testRun() {
		
		fail("Not yet implemented");
	}

	@Test
	public void testProccesId() {
		String[] knownContaminator = { "9", "\"Stephanie\"", "\"MITCHELL\"", "1924-03-17 00:00:00", "1585699579.2617905", "4", "\"promenade avec mon fils à la campagne\""};
        DataType data2 = new DataType(knownContaminator, (short)1);
        
        BlockingQueue<DataType> inQueue = new LinkedBlockingQueue<DataType>();
        BlockingQueue<Vector<ContaminationChain>> outQueue = new LinkedBlockingQueue<>()
        Processing currentProcess = new Processing(Queue, BlockingQueue<Vector<ContaminationChain>> outQueue, Vector<ContaminationChain> VectorOfContaminationChain){
        	
        assertEquals(expected, actual);
		
	}
}
