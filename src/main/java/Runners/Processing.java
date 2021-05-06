package Runners;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;
import java.util.Vector;
import java.util.concurrent.BlockingQueue;

import javax.print.attribute.Size2DSyntax;

import Models.ContaminationChain;
import Models.DataType;


public class Processing implements Runnable {

	private final BlockingQueue<DataType> inQueue_;
	private final BlockingQueue<ContaminationChain[]> outQueue_;
	private Vector<ContaminationChain> VectorOfContaminationChain_=null;	
	
	private boolean ending=false; 
	private Timestamp currentTimestamp;
	private final String[] poisonPill = { "-1", "", "", "", "1582161158", "unknown", "" };
	
<<<<<<< HEAD
	Processing(BlockingQueue<DataType> inQueue, BlockingQueue<Vector<ContaminationChain>> outQueue, Vector<ContaminationChain> VectorOfContaminationChain){
		inQueue_ = inQueue;
		outQueue_ = outQueue;
=======
	Processing(BlockingQueue<DataType> inQueue, BlockingQueue<ContaminationChain[]> outQueue, Vector<ContaminationChain> VectorOfContaminationChain){
		inQueue_=inQueue;
		outQueue_=outQueue;
>>>>>>> ee49a29 (v2 processing)
		VectorOfContaminationChain_=VectorOfContaminationChain;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			DataType onePerson = inQueue_.take();
			while(onePerson.getPerson_id()!=-1) {
				processId(onePerson);
				outQueue_.add(new Vector<ContaminationChain>(VectorOfContaminationChain_));
				onePerson = inQueue_.take();
			}
			// poison-pill            
            inQueue_.add(new DataType(poisonPill, (short) -1));
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
		processSort();	
		int size = VectorOfContaminationChain_.size();
		ContaminationChain[] top3 = {VectorOfContaminationChain_.get(size-1), VectorOfContaminationChain_.get(size-2), VectorOfContaminationChain_.get(size-3)};
		outQueue_.add(top3);
	}
	
	private void processSort() {
		Collections.sort(VectorOfContaminationChain_, Comparator.comparing(ContaminationChain::getScore));
	}
}
