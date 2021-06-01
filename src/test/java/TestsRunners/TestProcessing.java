package TestsRunners;

import Models.ContaminationChain;
import Models.DataType;
import Utils.ThreadUtils;
import org.junit.Test;

import java.util.Vector;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class TestProcessing {

	@Test
	public void testRun() {
		// Shared data
		BlockingQueue<DataType> inQueue = new LinkedBlockingQueue<DataType>();
		BlockingQueue<int[]> outQueue = new LinkedBlockingQueue<int[]>();
		Vector<ContaminationChain> vectorOfContaminationChain = new Vector<ContaminationChain>();
		// Create Reader
		Reader reader = new Reader(inQueue, "data\\20");
		// Create Processing
		Processing processing = new Processing(inQueue, outQueue, vectorOfContaminationChain);
		// Launch Threads
		ExecutorService service = Executors.newFixedThreadPool(2);
		service.execute(reader);
		service.execute(processing);
		ThreadUtils.shutdownAndAwaitTermination(service);
		// Check data

	}
}
