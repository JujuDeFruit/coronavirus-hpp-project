package Models;

import java.sql.Timestamp;

/**
 * Class to store data from CSV upon a format.
 *
 * <ul>
 * <li> SEPARATOR : CSV separator used to separate values.</li>
 * <li> person_id : ID of the person.</li>
 * <li> person : Name of the person.</li>
 * <li> diagnosed_ts : Timestamp when person was contaminated.</li>
 * <li> contaminated_by : ID of the person who contaminated this one.</li>
 * <li> contaminated_by : ID of the country.</li>
 * </ul>
 */
public class DataType {

    private final int person_id;
    private final String person;
    private final Timestamp diagnosed_ts;
    private final int contaminated_by;
    private final short country_id;

    /**
     * Constructor from line in CSV.
     *
     * @param splitLine : result of the splitting of one line from a file
     *                  <ul>
     *                  <li>0 : person ID in string format</li>
     *                  <li>1 : person name in string format</li>
     *                  <li>2 : person surname in string format</li>
     *                  <li>4 : Timestamp when person was diagnosed as sick in string format</li>
     *                  <li>5 : ID of the person who contaminated this one in string format</li>
     *                  </ul>
     * @param country_id_ : ID of the country of the person
     *                   <ul>
     *                   <li>0 : France</li>
     *                   <li>1 : Italy</li>
     *                   <li>2 : Spain</li>
     *                   </ul>
     */
    public DataType(String[] splitLine, short country_id_) {

        person_id = Integer.parseInt(splitLine[0]);
        // Remove quotes from names
        person = splitLine[1].replace("\"", "") + " " + splitLine[2].replace("\"", "");
        diagnosed_ts = new Timestamp((long)Double.parseDouble(splitLine[4]));
        contaminated_by = splitLine[5].equals("unknown") ? -1 : Integer.parseInt(splitLine[5]);
        country_id = country_id_;
    }

    /* Getters */
    /**
     *
     * @return ID of the person
     */
    public int getPerson_id() {
        return person_id;
    }

    /**
     *
     * @return Name of the person
     */
    public String getPerson() {
        return person;
    }

    /**
     *
     * @return Timestamp when person was contaminated
     */
    public Timestamp getDiagnosed_ts() {
        return diagnosed_ts;
    }

    /**
     *
     * @return ID of the person who contaminated this one
     */
    public int getContaminated_by() {
        return contaminated_by;
    }

    /**
     *
     * @return ID of the country
     */
    public short getCountry_id() { return country_id; }
}
