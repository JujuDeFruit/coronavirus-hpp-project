package Models;

import org.junit.Test;
import java.sql.Timestamp;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;


public class TestsDataType {
    /**
     * We call {@link DataType#DataType(String[], short)} and check if the created object contains what we expect
     */
    @Test
    public void test (){
        String[] unknownContaminator = { "4", "\"Daniel\"", "\"ROBINSON\"", "1995-08-21 00:00:00", "1582161158.5235808", "unknown", "\"course à pieds avec la grand-mère au marché\"" };
        DataType data = new DataType( unknownContaminator, (short)0);

        assertEquals(4, data.getPerson_id());
        assertEquals("Daniel ROBINSON", data.getPerson());
        assertEquals(new Timestamp((long)(1582161158.5235808*1000.0)), data.getDiagnosed_ts());
        assertEquals(-1, data.getContaminated_by());
        assertEquals(0, data.getCountry_id());

        String[] knownContaminator = { "9", "\"Stephanie\"", "\"MITCHELL\"", "1924-03-17 00:00:00", "1585699579.2617905", "4", "\"promenade avec mon fils à la campagne\""};
        DataType data2 = new DataType(knownContaminator, (short)1);

        assertEquals(9, data2.getPerson_id());
        assertEquals("Stephanie MITCHELL", data2.getPerson());
        assertEquals(new Timestamp((long)(1585699579.2617905*1000.0)), data2.getDiagnosed_ts());
        assertEquals(4, data2.getContaminated_by());
        assertEquals(1, data2.getCountry_id());
    }
    /**
     * We call {@link DataType#DataType(String[], short)} and check if the created object contains what we expect
     */
    @Test
    public void testPoisonPill (){
        DataType data = new DataType();

        assertEquals(-1, data.getPerson_id());
        assertNull(data.getPerson());
        assertNull(data.getDiagnosed_ts());
        assertEquals(-1, data.getContaminated_by());
        assertEquals(-1, data.getCountry_id());
    }
}
