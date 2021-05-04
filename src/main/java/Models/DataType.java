package Models;

import java.sql.Timestamp;

/*
 * Class to store data from CSV upon a format.
 */
public class DataType {

    private final String SEPARATOR = ",";

    private int person_id;
    private String person;
    private Timestamp diagnosed_ts;
    private int contaminated_by;

    /*
     * Constructor from line in CSV.
     */
    DataType(String line) {
        // Separate each data
        final String[] separatedLine = line.split(SEPARATOR);

        person_id = Integer.parseInt(separatedLine[0]);
        person = separatedLine[1] + " " + separatedLine[2];
        diagnosed_ts = new Timestamp(Long.parseLong(separatedLine[5]));
        contaminated_by = Integer.parseInt(separatedLine[6]);
    }

    /*
     *  Getters
     */
    public int getPerson_id() {
        return person_id;
    }

    public String getPerson() {
        return person;
    }

    public Timestamp getDiagnosed_ts() {
        return diagnosed_ts;
    }

    public int getContaminated_by() {
        return contaminated_by;
    }
}
