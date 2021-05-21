import java.util.Vector;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import Models.*;
import Runners.*;
import Utils.*;

public class Main {
	
	public static void main(String args[]) {
		process("data\\20");
	}
	
	public static void process(String path) {
		//Instantiate every components
		BlockingQueue<DataType> inQueue = new LinkedBlockingQueue<>();
		BlockingQueue<ContaminationChain[]> outQueue = new LinkedBlockingQueue<ContaminationChain[]>();
		Vector<ContaminationChain> vectorOfContaminationChain = new Vector<ContaminationChain>();

		Reader reader = new Reader(inQueue, path);
		Processing processing = new Processing(inQueue, outQueue, vectorOfContaminationChain);
		Writer writer = new Writer(outQueue);

		ExecutorService service = Executors.newFixedThreadPool(5); //5 threads is the limit

		//Start timer
		long startTime = System.nanoTime();
		service.execute(reader);
		service.execute(processing);
		service.execute(writer);
		
		//Wait for the threads to end
		ThreadUtils.shutdownAndAwaitTermination(service);

		//Print execution time
		System.out.println(System.nanoTime()-startTime);
	}
}
