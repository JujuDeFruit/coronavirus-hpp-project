package Runners;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import Models.ContaminationChain;
import Models.DataType;

public class Processing implements Runnable {
	private BlockingQueue<DataType> queue_ = null;
	private ArrayList<ContaminationChain> ArrayOfContaminationChain_=null;
	
	Processing(BlockingQueue<DataType> Queue, ArrayList<ContaminationChain> ArrayOfContaminationChain){
		queue_=Queue;
		ArrayOfContaminationChain_=ArrayOfContaminationChain;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			DataType onePerson = queue_.take();
			while(!onePerson.getPerson().equals("Poison PILL")) {
				processId(onePerson);
				onePerson = queue_.take();
			}
			queue_.add(new DataType("9, \"Poison\", \"PILL\", 1924-03-17 00:00:00, 1585699579.2617905, 4, \"Une fin est toujours un début\""));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
		
	private void processId(DataType myPerson){
		if(myPerson.getContaminated_by()==-1) {
			ArrayOfContaminationChain_.add(new ContaminationChain());
			//return;
		}else {
			// etc ... 
		}
	}
}
