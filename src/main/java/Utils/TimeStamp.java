package Utils;

import java.sql.Timestamp;

/**
 * Manipulate timestamps
 */
public class TimeStamp {

    /**
     * Get the numbers of hours between 2 different timestamps.
     * @param t1 first timestamp
     * @param t2 second timestamp
     * @return number of hours
     */
    public static double getHoursDifference(Timestamp t1, Timestamp t2) {
        return ((t1.getTime() - t2.getTime()) / 3_600_000.0);
    }
}
