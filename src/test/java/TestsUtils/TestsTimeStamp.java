package TestsUtils;

import org.junit.Test;

import java.sql.Timestamp;

import static org.junit.Assert.assertEquals;

import static Utils.TimeStamp.getHoursDifference;

public class TestsTimeStamp {

    @Test
    public void test () {
        Timestamp t1 = new Timestamp((long)(1583091884.9200459 * 1000.0));
        Timestamp t2 = new Timestamp((long)(1585699579.2617905 * 1000.0));
        assertEquals(724, getHoursDifference(t2, t1), 0.5);
    }
}
