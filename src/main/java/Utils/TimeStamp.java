package Utils;

import java.sql.Timestamp;

public class TimeStamp {

    public static float getHoursDifference(Timestamp t1, Timestamp t2) {
        return (float) ((t1.getTime() - t2.getTime()) / 3600000.0);
    }
}
