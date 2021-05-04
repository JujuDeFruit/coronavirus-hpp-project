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
 * </ul>
 */
public class DataType {

    private final String SEPARATOR = ",";

    private int person_id;
    private String person;
    private Timestamp diagnosed_ts;
    private int contaminated_by;

    /**
     * Constructor from line in CSV.
     *
     * @param line : CSV line.
     */
    public DataType(String line) {
        // Separate each data
//        line = line.replaceAll(" ", "");
        final String[] separatedLine = line.split(SEPARATOR);

        for(int i = 0; i < separatedLine.length; i++) separatedLine[i] = separatedLine[i].replace(" ", "");

        person_id = Integer.parseInt(separatedLine[0]);
        // Remove quotes from names
        person = separatedLine[1].replace("\"", "") + " " + separatedLine[2].replace("\"", "");
        diagnosed_ts = new Timestamp((long)Double.parseDouble(separatedLine[4]));
        contaminated_by = separatedLine[5].equals("unknown") ? -1 : Integer.parseInt(separatedLine[5]);

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
}
