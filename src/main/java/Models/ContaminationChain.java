package Models;

import Utils.TimeStamp;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

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

    private short country_id;
    private int score;
    private List<Integer> contaminationId;
    private List<Timestamp> contaminationTs;

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

    public void push (DataType person) {

        // Leave function if person to add was not contaminated by last person of the chain.
        if (person.getContaminated_by() != contaminationId.get(contaminationId.size() - 1)) return;

        // Compare date of the contaminated person with the first one of the chain.
        final double compare = TimeStamp.getHoursDifference(person.getDiagnosed_ts(), contaminationTs.get(0));

        if (compare > 0.0) {
            if (compare <= 168.0) {
                score += 10;
            }
            else if (compare <= 336.0) {
                score += 4;
            }

            contaminationId.add(person.getPerson_id());
            contaminationTs.add(person.getDiagnosed_ts());
        }
        /*else if (compare < 0) {
            contaminationId.set(0, person.getPerson_id());
            contaminationTs.set(0, person.getDiagnosed_ts());
        }*/
    }

    /* Getters */

    /**
     *
     * @return score of the chain.
     */
    public int getScore() { return score; }
}
