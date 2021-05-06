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
    }

    /**
     * Construct a ContaminationChain having the roll of a poison pill to stop the
     * process of the Writer.
     */
    public ContaminationChain() {
        country_id = -1;
        contaminationId = null;
        contaminationTs = null;
        score = 0;
    }

    /**
     * Push a person into the contamination chain.
     * @param person person to add to the chain.
     * @return true if person was contaminated by a person of the chain.
     */
    public boolean push (DataType person) {
        // Browse IDs store in list, to check if person to add to the chain was contaminated by one of the chain.
        for(int id : contaminationId) {
            if (person.getContaminated_by() == id) {
                contaminationId.add(person.getPerson_id());
                contaminationTs.add(person.getDiagnosed_ts());
                return true;
            }
        }
        return false;
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
