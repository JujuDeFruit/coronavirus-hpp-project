package Runners;

import static org.junit.Assert.*;
import org.junit.Test;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import Models.ContaminationChain;
import Models.DataType;
import Utils.ThreadUtils;


public class TestProcessing {
	/**
	 * We test that what the process returns is what will be displayed in the the data.csv using a testData.csv
	 * generated using the brute method that we hope works. Note that we use Reader to fill the inQueue so this test is
	 * valid if the the unit test of this class works.
	 */
	@Test
	public void testRun() throws IOException {
		// Shared data
		BlockingQueue<DataType> inQueue = new LinkedBlockingQueue<DataType>();
		BlockingQueue<int[]> outQueue = new LinkedBlockingQueue<int[]>();
		Vector<ContaminationChain> vectorOfContaminationChain = new Vector<ContaminationChain>();
		// Create Reader
		Reader reader = new Reader(inQueue, "data\\5000");
		// Create Processing
		Processing processing = new Processing(inQueue, outQueue, vectorOfContaminationChain);
		// Launch Threads
		ExecutorService service = Executors.newFixedThreadPool(2);
		service.execute(reader);
		service.execute(processing);
		ThreadUtils.shutdownAndAwaitTermination(service);

		// Check data
			// test data
		FileReader testDataReader = new FileReader("src/main/resources/testDataOpt.csv");
		BufferedReader testDataBuffer = new BufferedReader(testDataReader);

		try {
			int[] top3 = outQueue.take();
			String[] test_top3;
			while (top3[0] != -1) {
				// read line and split it
				test_top3 = testDataBuffer.readLine().split("[,;]");

				// check equality
				assertEquals(top3[0], Integer.parseInt(test_top3[0]));
				assertEquals(top3[1], Integer.parseInt(test_top3[1]));
				assertEquals(top3[2], Integer.parseInt(test_top3[2]));
				assertEquals(top3[3], Integer.parseInt(test_top3[3]));
				assertEquals(top3[4], Integer.parseInt(test_top3[4]));
				assertEquals(top3[5], Integer.parseInt(test_top3[5]));
				assertEquals(top3[6], Integer.parseInt(test_top3[6]));
				assertEquals(top3[7], Integer.parseInt(test_top3[7]));
				assertEquals(top3[8], Integer.parseInt(test_top3[8]));

				// retake in the queue
				top3 = outQueue.take();
			}

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
