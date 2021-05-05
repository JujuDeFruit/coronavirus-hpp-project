package Utils;

import org.junit.Test;

import java.sql.Timestamp;

import static org.junit.Assert.assertEquals;

import static Utils.TimeStamp.getHoursDifference;

public class TestsTimeStamp {

    @Test
    public void test () {
        Timestamp t1 = new Timestamp((long)(1620205200.0 * 1000.0));
        Timestamp t2 = new Timestamp((long)(1620208800.0 * 1000.0));
        assertEquals(1.0, getHoursDifference(t2, t1), 0.01);
    }
}
