import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import Models.*;
import Runners.*;
import Utils.*;

public class Main {
	
	public void main(String args[]) {
		this.process();
	}
	
	public void process() {
		//Instanciate every components
		BlockingQueue<DataType> q = new LinkedBlockingQueue<DataType>();
		Reader reader = new Reader(q, "src\\main\\resources\\Test"); //Add Test to read TestFrance.csv
		//ArrayBlockingQueue<DataType> q = new ArrayBlockingQueue<DataType>();
		//Writer writer = new Writer(q);
		ExecutorService service = Executors.newFixedThreadPool(5); //5 is the limit

		//Start timer
		long startTime = System.nanoTime();
		service.execute(reader);
		//service.execute(writer);
		
		//Wait for the threads
		ThreadUtils.shutdownAndAwaitTermination(service);
		//Calculate execution time
		long endTime = System.nanoTime(); //100-300ns without traversing any array

	}
}
