package TestsRunners;

import java.sql.Timestamp;
import java.util.Comparator;
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
	private final BlockingQueue<int[]> outQueue_;
	private Vector<ContaminationChain> VectorOfContaminationChain_=null;

	private Timestamp currentTimestamp;
	private final String[] poisonPill = { "-1", "", "", "", "1582161158", "unknown", "" };

	public Processing(BlockingQueue<DataType> inQueue, BlockingQueue<int[]> outQueue, Vector<ContaminationChain> VectorOfContaminationChain){
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
			while(onePerson.getPerson_id() != -1) {
				processId(onePerson);
				//we take another person
				onePerson = inQueue_.take();
			}
			// poison-pill
            inQueue_.add(new DataType(poisonPill, (short) -1));
			int[] poisonChain = { -1 };
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
	private void processId(DataType myPerson) throws InterruptedException {
		currentTimestamp = myPerson.getDiagnosed_ts();

		// Recalculate all scores and remove the ContaminationChain if its score is 0
		/*for (int i = 0; i < VectorOfContaminationChain_.size(); i++) {
			if(VectorOfContaminationChain_.get(i).calculateScore(currentTimestamp)) VectorOfContaminationChain_.remove(i);
		}*/

		// Create new ContaminationChain when the contaminated id is equal to -1 meaning contaminated by "unknown"
		if(myPerson.getContaminated_by() == -1) {
			VectorOfContaminationChain_.add(new ContaminationChain(myPerson));
		} else {
			// Else we search the chain that contains the contaminated id
			boolean ending = false;
			int i=0;
			while(i < VectorOfContaminationChain_.size() && !ending) {
				ending = VectorOfContaminationChain_.get(i).push(myPerson);
				if (ending && VectorOfContaminationChain_.get(i).calculateScore(currentTimestamp)) {
					VectorOfContaminationChain_.remove(i);
				}
				i++;
			}

			// If ending is still false that means the person was contaminated by a chain with a score of 0 so it has been destroyed
			if(!ending) {
				VectorOfContaminationChain_.add(new ContaminationChain(myPerson));
			}
		}

		VectorOfContaminationChain_.sort(Comparator.comparing(ContaminationChain::getScore));

		int size = VectorOfContaminationChain_.size();
		if (size >= 3) {

			ContaminationChain first = VectorOfContaminationChain_.get(size - 1);
			ContaminationChain second = VectorOfContaminationChain_.get(size - 2);
			ContaminationChain third = VectorOfContaminationChain_.get(size - 3);

			int[] top3 = {
					first.getCountry_id(), first.getFirstPersonId(), first.getScore(),
					second.getCountry_id(), second.getFirstPersonId(), second.getScore(),
					third.getCountry_id(), third.getFirstPersonId(), third.getScore()
			};

			outQueue_.add(top3);
		}
	}
}