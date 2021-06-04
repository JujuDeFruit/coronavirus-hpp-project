package Runners;

import java.sql.Timestamp;
import java.util.Collections;
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
	private Vector<ContaminationChain> VectorOfContaminationChain_;


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
		try {
			DataType currentPerson = inQueue_.take();
			// While we don't reach the poison pill "-1" do:
			while(currentPerson.getPerson_id() != -1) {
				// Not converted to local variable in order to optimize memory allocation
				Timestamp currentTimestamp = currentPerson.getDiagnosed_ts(); // timestamp when this person was contaminated

				// 1. Find where the current person should be added
					// Create new ContaminationChain when the contaminated id is equal to -1 meaning contaminated by "unknown"
				if(currentPerson.getContaminated_by() == -1) {
					VectorOfContaminationChain_.add(new ContaminationChain(currentPerson));
				} else {
					// Else we search the chain that contains the contaminated id
					short pushState = 2; // we initialise at the value of not found
					int i=0;
					while(i < VectorOfContaminationChain_.size() && pushState == 2) {
						pushState = VectorOfContaminationChain_.get(i).push(currentPerson);
						i++;
					}
					// If pushState equal is different than 0 then we create a new chain
					if(pushState != 0) {
						VectorOfContaminationChain_.add(new ContaminationChain(currentPerson));
						if (pushState == 1) {
							// Being here means that the last chain (index i-1) we browse contained the contaminated id
							// we were searching but that the chain is now "dead" (score ==0) so we delete it)
							VectorOfContaminationChain_.remove(i-1);
						}
					}
				}
				// 2. Recalculate score of the top 3 and reorder it if there is 3 or more chains
					// if one of the score is null then we suppress it and don't reorder
				if (VectorOfContaminationChain_.size() >= 3) {
					if (VectorOfContaminationChain_.get(0).calculateScore(currentTimestamp)) {
						VectorOfContaminationChain_.remove(0);
					} else {
						if (VectorOfContaminationChain_.get(1).calculateScore(currentTimestamp)) {
							VectorOfContaminationChain_.remove(1);
						} else {
							if (VectorOfContaminationChain_.get(2).calculateScore(currentTimestamp)) {
								VectorOfContaminationChain_.remove(2);
							} else {
								// Reorder top3 (needed for the estimation to work)
									// score
								int oldFirstScore = VectorOfContaminationChain_.get(0).getScore();
								int oldSecondScore = VectorOfContaminationChain_.get(1).getScore();
								int oldThirdScore = VectorOfContaminationChain_.get(2).getScore();
									// new order
								ContaminationChain first, second, third;
								if( oldFirstScore > oldSecondScore ){
									if( oldFirstScore > oldThirdScore ){
										first = VectorOfContaminationChain_.get(0);
										if( oldSecondScore > oldThirdScore ){
											second = VectorOfContaminationChain_.get(1);
											third = VectorOfContaminationChain_.get(2);
										}else{
											second = VectorOfContaminationChain_.get(2);
											third = VectorOfContaminationChain_.get(1);
										}
									}else{
										second = VectorOfContaminationChain_.get(0);
										first = VectorOfContaminationChain_.get(2);
										third = VectorOfContaminationChain_.get(1);
									}
								}else{
									if( oldSecondScore > oldThirdScore ){
										first = VectorOfContaminationChain_.get(1);
										if( oldFirstScore > oldThirdScore ){
											second = VectorOfContaminationChain_.get(0);
											third = VectorOfContaminationChain_.get(2);
										}else{
											second = VectorOfContaminationChain_.get(2);
											third = VectorOfContaminationChain_.get(0);
										}
									}else{
										second = VectorOfContaminationChain_.get(1);
										first = VectorOfContaminationChain_.get(2);
										third = VectorOfContaminationChain_.get(0);
									}
								}
								VectorOfContaminationChain_.set(0, first);
								VectorOfContaminationChain_.set(1, second);
								VectorOfContaminationChain_.set(2, third);
							}
						}
					}
				}
				// 3. Browse all chains (expect the first 3 which are the top 3) and estimate if there score can go
				// higher than one of the top 3
				if (VectorOfContaminationChain_.size() >= 3){ // no point doing things if there are not 3 or more chains
					int thirdScore = VectorOfContaminationChain_.get(2).getScore(); // we get it in memory to avoid some unnecessary memory access
					// We browse the vector in descending order in order not to skip elements when removing "dead" chain
					for (int i = VectorOfContaminationChain_.size()-1; i > 2; i--) {
						ContaminationChain currentChain = VectorOfContaminationChain_.get(i);
						if((currentChain.getChainSize() - currentChain.getIdToCalculateScore())*10 > thirdScore) {
							// We have estimated that this chain can potentially enter the top 3 (good estimation)
							if (currentChain.calculateScore(currentTimestamp)){
								// Precise calculation of the score and suppression of the chain if it is 0
								VectorOfContaminationChain_.remove(i);
							} else {
								int chainScore = currentChain.getScore();
								if ( chainScore > thirdScore) {
									// This chain can enter the top 3, we need to determine its place and then insert it
									if (chainScore > VectorOfContaminationChain_.get(0).getScore()) {
										// the current chain is first, we shift this way :
										// current chain -> first
										// first -> second
										// second -> third
										// third -> current chain
										Collections.swap(VectorOfContaminationChain_, 0, i);
										Collections.swap(VectorOfContaminationChain_, i, 1);
										Collections.swap(VectorOfContaminationChain_, i, 2);
										thirdScore = VectorOfContaminationChain_.get(2).getScore();
									} else {
										if (chainScore > VectorOfContaminationChain_.get(1).getScore()) {
											// the current chain is second, we shift this way :
											// current chain -> second
											// second -> third
											// third -> current chain
											Collections.swap(VectorOfContaminationChain_, 1, i);
											Collections.swap(VectorOfContaminationChain_, i, 2);
											thirdScore = VectorOfContaminationChain_.get(2).getScore();
										} else {
											// the current chain is third, we shift this way :
											// current chain -> third
											// third -> current chain
											Collections.swap(VectorOfContaminationChain_, 2, i);
											thirdScore = chainScore;
										}
									}
								}
							}
						}
					}
					// Forward the top3 to the outQueue
					ContaminationChain first = VectorOfContaminationChain_.get(0);
					ContaminationChain second = VectorOfContaminationChain_.get(1);
					ContaminationChain third = VectorOfContaminationChain_.get(2);
					outQueue_.add(new int[] {
							first.getCountry_id(), first.getFirstPersonId(), first.getScore(),
							second.getCountry_id(), second.getFirstPersonId(), second.getScore(),
							third.getCountry_id(), third.getFirstPersonId(), third.getScore()
					});
				}
				// We take another person
				currentPerson = inQueue_.take();
			}
			// poison-pill
            inQueue_.add(new DataType());
			outQueue_.add(new int[] { -1 });
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}