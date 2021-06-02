package Utils;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import java.sql.Timestamp;

import static Utils.TimeStamp.getHoursDifference;


public class TestsTimeStamp {
    /**
     * We call {@link TimeStamp#getHoursDifference(Timestamp, Timestamp)} and check if the result correspond 
     * to the hours difference between the two timestamp.
     */
    @Test
    public void test () {
        // Check on chosen Timestamps with a difference of exactly 26 hours
        Timestamp t1_26 = new Timestamp((long)(1619740800 * 1000.0));
        Timestamp t2_26 = new Timestamp((long)(1619834400 * 1000.0));
        assertEquals(26, getHoursDifference(t2_26, t1_26), 0.01);
        // Check on chosen Timestamps with a difference of exactly 26.75 hours
        Timestamp t1_26_75 = new Timestamp((long)(1619740800 * 1000.0));
        Timestamp t2_26_75 = new Timestamp((long)(1619837100 * 1000.0));
        assertEquals(26.75, getHoursDifference(t2_26_75, t1_26_75), 0.01);
        // Check on Timestamps found in the data.
        Timestamp t1_data = new Timestamp((long)(1583091884.9200459 * 1000.0));
        Timestamp t2_data = new Timestamp((long)(1585699579.2617905 * 1000.0));
        assertEquals(724, getHoursDifference(t2_data, t1_data), 0.5);
    }
}
