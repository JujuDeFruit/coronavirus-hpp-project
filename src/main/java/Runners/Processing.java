package Runners;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Vector;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import Models.ContaminationChain;
import Models.DataType;
import Utils.TimeStamp;

public class Processing implements Runnable {

	private final BlockingQueue<DataType> inQueue_;
	private final BlockingQueue<Vector<ContaminationChain>> outQueue_;
	private Vector<ContaminationChain> VectorOfContaminationChain_=null;	
	
	private boolean ending=false; 
	private Timestamp currentTimestamp;
	
	Processing(BlockingQueue<DataType> inQueue, BlockingQueue<Vector<ContaminationChain>> outQueue, Vector<ContaminationChain> VectorOfContaminationChain){
		inQueue_ = inQueue;
		outQueue_ = outQueue;
		VectorOfContaminationChain_=VectorOfContaminationChain;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			DataType onePerson = inQueue_.take();
			while(!onePerson.getPerson().equals("Poison PILL")) {
				processId(onePerson);
				outQueue_.add(new Vector<ContaminationChain>(VectorOfContaminationChain_));
				onePerson = inQueue_.take();
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
		
	private void processId(DataType myPerson){
		currentTimestamp = myPerson.getDiagnosed_ts();
		VectorOfContaminationChain_.forEach((myContaminationChain)->{
			if(myContaminationChain.calculateScore(currentTimestamp)) VectorOfContaminationChain_.remove(myContaminationChain);
		});
		
		if(myPerson.getContaminated_by()==-1) {
			VectorOfContaminationChain_.add(new ContaminationChain(myPerson));			
		}else {
			// etc ...
			ending=false;
			VectorOfContaminationChain_.forEach((myContaminationChain)->{
				List<Integer> listOfIdContamination = myContaminationChain.getContaminationId();
				ListIterator<Integer> iterator = listOfIdContamination.listIterator(listOfIdContamination.size());
				int contaminatedBy = myPerson.getContaminated_by();
				while(iterator.hasPrevious() && ending==false) {
					ending=iterator.equals(contaminatedBy);
					iterator.previous();
				}
				if(ending==true) {
					myContaminationChain.push(myPerson);
				}	
			});
			//if ending=false mean that the person was contaminated by a chain with a score of 0 so she has been destroyed 
			if(ending==false) {
				VectorOfContaminationChain_.add(new ContaminationChain(myPerson));					
			}
		}
	}
}
