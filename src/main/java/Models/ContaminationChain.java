package Models;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import Utils.TimeStamp;

/**
 * Contamination chain containing all IDs of contaminated persons from the same root.
 *
 * <ul>
 *     <li>country_id : country ID of the first infected person.</li>
 *     <li>score : score of the chain.</li>
 *     <li>contaminationId : List of ID of all contaminated persons.</li>
 *     <li>contaminationTs : List of Timestamp of all contaminated persons.</li>
 * </ul>
 */
public class ContaminationChain {

    private final short country_id;
    private int score;
    private final List<Integer> contaminationId;
    private final List<Timestamp> contaminationTs;
    private int chainSize;

    /**
     * Constructor of the contamination chain.
     * @param firstPerson first person that created contamination chain.
     */
    public ContaminationChain(DataType firstPerson) {
        contaminationId = new ArrayList<Integer>();
        contaminationTs = new ArrayList<Timestamp>();

        // Set initial score to 10
        score = 10;
        country_id = firstPerson.getCountry_id();

        // Push first contaminated person into lists.
        contaminationId.add(firstPerson.getPerson_id());
        contaminationTs.add(firstPerson.getDiagnosed_ts());

        // Set size to 1
        chainSize = 1;
    }

    /**
     * Push a person into the contamination chain.
     * @param person person to add to the chain.
     * @return  0: the person was inserted in the chain
     *          1: the contaminated id was found but the chain's score is now 0
     *          2: the contaminated id wasn't found in the chain
     */
    public short push (DataType person) {
        // Browse IDs store in list, to check if person to add to the chain was contaminated by one of the member of this chain.
        for(int id : contaminationId) {
            if (person.getContaminated_by() == id) {
                if (calculateScore(person.getDiagnosed_ts())) {
                    return 1;
                } else {
                    contaminationId.add(person.getPerson_id());
                    contaminationTs.add(person.getDiagnosed_ts());
                    score += 10;
                    chainSize += 1;
                    return 0;
                }
            }
        }
        return 2;
    }

    /**
     * Calculate score from a reference timestamp.
     * @param time reference timestamp.
     * @return boolean to inform if score is 0 or not.
     */
    public boolean calculateScore(Timestamp time) {
        score = 0;
        // Browse contamination time
        double compare;
        for (Timestamp ts : contaminationTs) {
            // Compare date of the contaminated person with the first one of the chain.
            compare = TimeStamp.getHoursDifference(time, ts);

            if (compare <= 168.0) {
                score += 10;
            } else if (compare < 336.0) {
                score += 4;
            }
        }
        return score == 0;
    }

    // Getters
    /**
     *
     * @return the number of person "in" the chain.
     */
    public int getChainSize() { return chainSize; }

    /**
     *
     * @return first person ID.
     */
    public Integer getFirstPersonId() { return contaminationId.get(0); }

    /**
     *
     * @return IDs in the contaminated persons.
     */
    public List<Integer> getContaminationId() { return contaminationId; }

    /**
     *
     * @return Timestamps of contaminated persons.
     */
    public List<Timestamp> getContaminationTs() { return contaminationTs; }

    /**
     *
     * @return ID of the country of the first person.
     */
    public short getCountry_id() { return country_id; }

    /**
     *
     * @return score of the chain.
     */
    public int getScore() { return score; }
}
