package Runners;

import Models.ContaminationChain;
import Models.DataType;
import Utils.ThreadUtils;
import org.junit.Test;

import javax.swing.plaf.IconUIResource;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import static org.junit.Assert.*;

public class TestProcessing {

	@Test
	public void testRun() {
		// Shared data
		BlockingQueue<DataType> inQueue = new LinkedBlockingQueue<DataType>();
		BlockingQueue<ContaminationChain[]> outQueue = new LinkedBlockingQueue<ContaminationChain[]>();
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
		try {
			ContaminationChain[] currentTop3 = outQueue.take();
			while (currentTop3[0].getCountry_id() != -1) {
				List<Integer> top1 = currentTop3[0].getContaminationId();
				top1.forEach(System.out::println);
				System.out.println("\n");
				// refresh data
				currentTop3 = outQueue.take();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
