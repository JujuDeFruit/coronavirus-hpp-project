package Runners;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;
import java.util.Vector;
import java.util.concurrent.BlockingQueue;


import Models.ContaminationChain;
import Models.DataType;

/**
 * Process who creates chain of contamination, sort them, and add them in a Queue 
 *
 * <ul>
 *     <li>inQueue_ : BlockingQueue of Datatype, Datatypre represent a person</li>
 *     <li>outQueue_: BlockingQueue of ContaminationChain[], represent the 3 contaminatedChain with the highest score</li>
 *     <li>VectorOfContaminationChain_ : Vector of ContaminatedChain</li>
 *     <li>ending : boolean</li>
 *     <li>currentTimestamp : </li>
 *     <li>poisonPill : poison-pill for inQueue_</li>
 * </ul>
 */
public class Processing implements Runnable {

	private final BlockingQueue<DataType> inQueue_;
	private final BlockingQueue<ContaminationChain[]> outQueue_;
	private Vector<ContaminationChain> VectorOfContaminationChain_=null;

	private Timestamp currentTimestamp;
	private final String[] poisonPill = { "-1", "", "", "", "1582161158", "unknown", "" };

	public Processing(BlockingQueue<DataType> inQueue, BlockingQueue<ContaminationChain[]> outQueue, Vector<ContaminationChain> VectorOfContaminationChain){
		inQueue_=inQueue;
		outQueue_=outQueue;
		VectorOfContaminationChain_=VectorOfContaminationChain;
	}
	
   /**
    * Run the thread  
    */
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			DataType onePerson = inQueue_.take();
			//while we don't reach the poison pill "-1" do:
			while(onePerson.getPerson_id()!=-1) {
				processId(onePerson);
				//we take another person
				onePerson = inQueue_.take();
			}
			// poison-pill
            inQueue_.add(new DataType(poisonPill, (short) -1));
			ContaminationChain[] poisonChain = { new ContaminationChain() };
			outQueue_.add(poisonChain);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
		
	/**
	 * Process who ..
	 * @param myPerson : the person taken in charge in the inQueue_
	 */
	private void processId(DataType myPerson){
		currentTimestamp = myPerson.getDiagnosed_ts();
		for (int i = 0; i < VectorOfContaminationChain_.size(); i++) {
			if(VectorOfContaminationChain_.get(i).calculateScore(currentTimestamp)) VectorOfContaminationChain_.remove(VectorOfContaminationChain_.get(i));
		}
//		VectorOfContaminationChain_.forEach((myContaminationChain)->{
//			if(myContaminationChain.calculateScore(currentTimestamp)) VectorOfContaminationChain_.remove(myContaminationChain);
//		});

		if(myPerson.getContaminated_by() == -1) {
			VectorOfContaminationChain_.add(new ContaminationChain(myPerson));
		} else {
			// etc ...
			boolean ending = false;
			int i=0;
			while(i<VectorOfContaminationChain_.size() && !ending) {
				ending =VectorOfContaminationChain_.get(i).push(myPerson);
				i++;
			}
			//if ending=false mean that the person was contaminated by a chain with a score of 0 so she has been destroyed
			if(!ending) {
				VectorOfContaminationChain_.add(new ContaminationChain(myPerson));
			}
		}
		//processSort();
		VectorOfContaminationChain_.sort(Comparator.comparing(ContaminationChain::getScore));
		int size = VectorOfContaminationChain_.size();
		if (size >= 3) {
			ContaminationChain[] top3 = {VectorOfContaminationChain_.get(size - 1), VectorOfContaminationChain_.get(size - 2), VectorOfContaminationChain_.get(size - 3)};
			outQueue_.add(top3);
		}
	}

	/**
	 * Process who sort the Vector of contaminationChain by their score
	 */
	private void processSort() {
		Collections.sort(VectorOfContaminationChain_, Comparator.comparing(ContaminationChain::getScore));
	}
}



//List<Integer> listOfIdContamination = myContaminationChain.getContaminationId();
//ListIterator<Integer> iterator = listOfIdContamination.listIterator(listOfIdContamination.size());
//int contaminatedBy = myPerson.getContaminated_by();
//while(iterator.hasPrevious() && ending==false) {
//	ending=iterator.equals(contaminatedBy);
//	iterator.previous();
//}
/*VectorOfContaminationChain_.forEach((myContaminationChain)->{
				
				if(ending==true) {
					myContaminationChain.push(myPerson);
				}	
			});*/