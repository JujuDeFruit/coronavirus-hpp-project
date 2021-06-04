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
    private int idToCalculateScore;
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

        idToCalculateScore = 0;
    }

    /**
     * Push a person into the contamination chain.
     * @param person person to add to the chain.
     * @return  0: the person was inserted in the chain
     *          1: the contaminated id was found but the chain's score is now 0
     *          2: the contaminated id wasn't found in the chain
     */
    public short push (DataType person) {
        // dichotomous search of the id
        int searched = person.getPerson_id();
        boolean found = false;
        int end = chainSize - 1, begin = 0, mid;
        while (!found && end >= begin) {
            mid = (end + begin)/2;
            if (contaminationId.get(mid) == searched) {
                found = true;
            } else {
                if (searched > contaminationId.get(mid)) {
                    begin = mid + 1;
                } else {
                    end = mid - 1;
                }
            }
        }
        if (found) {
            // look what is the the first id that corresponds to a person that gives a score > 0 to this chain.
            majID(person.getDiagnosed_ts());
            if (idToCalculateScore == chainSize) {
                // then the score of the chain is 0
                return 1;
            } else {
                // We add the person to this alive chain
                contaminationId.add(person.getPerson_id());
                contaminationTs.add(person.getDiagnosed_ts());
                chainSize += 1;
                return 0;
            }
        }
        return 2;
    }

    /**
     * Find the first index corresponding to a person that gives a score > 0 to this chain.
     * If idToCalculateScore == chainSize that means this chain is "dead".
     * @param ts timestamp of the instant
     */
    public void majID(Timestamp ts) {
        int i = contaminationTs.size() - 1;
        while (i >= 0 && TimeStamp.getHoursDifference(ts, contaminationTs.get(i)) < 336.0) i--;
        idToCalculateScore = i + 1;
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
        for (int i = idToCalculateScore; i < chainSize; i++) {
            // Compare date of the contaminated person with the first one of the chain.
            compare = TimeStamp.getHoursDifference(time, contaminationTs.get(i));
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
     * @return the id where people in the chain starts having a score > 0.
     */
    public int getIdToCalculateScore() { return idToCalculateScore; }

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
