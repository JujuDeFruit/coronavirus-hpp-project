package TestsModels;

import org.junit.Test;

import Models.DataType;
import Models.ContaminationChain;

import java.sql.Timestamp;

import static org.junit.Assert.assertEquals;

public class TestsContaminationChain {

    @Test
    public void test() {
        testConstructor();
        testPush();
        testCalculateScore();
    }

    /**
     * Test constructor of the ContaminationChain Object.
     */
    public void testConstructor() {
        DataType first = new DataType("4", "Daniel", "ROBINSON", "1582161158.5235808", "unknown", (short)0);
        ContaminationChain contaminationChain = new ContaminationChain(first);

        assertEquals(10, contaminationChain.getScore());
        assertEquals(0, contaminationChain.getCountry_id());

        assertEquals(1, contaminationChain.getContaminationId().size());
        assertEquals(4, (int)contaminationChain.getContaminationId().get(0));

        assertEquals(1, contaminationChain.getContaminationTs().size());
        assertEquals(new Timestamp((long)(1582161158.5235808 * 1000.0)), (Timestamp)contaminationChain.getContaminationTs().get(0));

    }

    /**
     * Test Push method.
     */
    public void testPush() {
        DataType first = new DataType("4", "Daniel", "ROBINSON", "1582161158.5235808", "unknown", (short)0);
        DataType second = new DataType("5", "Jessica", "WATSON", "1583091884.9200459", "4", (short)0);
        DataType third = new DataType("9", "Stephanie", "MITCHELL", "1585699579.2617905", "2", (short)0);

        ContaminationChain contaminationChain = new ContaminationChain(first);
        contaminationChain.push(second);
        contaminationChain.push(third);

        assertEquals(3, contaminationChain.getContaminationTs().size());
        assertEquals(3, contaminationChain.getContaminationId().size());

        assertEquals(9, (int)contaminationChain.getContaminationId().get(2));
        assertEquals(new Timestamp((long)(1585699579.2617905 * 1000.0)), (Timestamp)contaminationChain.getContaminationTs().get(2));
    }

    /**
     * Test CalculateScore method.
     */
    public void testCalculateScore() {
        DataType first = new DataType("4", "Daniel", "ROBINSON", "1582161158.5235808", "unknown", (short)0);
        DataType second = new DataType("5", "Jessica", "WATSON", "1582391884.9200459", "4", (short)0);
        DataType third = new DataType("9", "Stephanie", "MITCHELL", "1582399579.2617905", "2", (short)0);

        ContaminationChain contaminationChain = new ContaminationChain(first);
        contaminationChain.push(second);
        contaminationChain.push(third);

        assertEquals(false, contaminationChain.calculateScore(new Timestamp((long)(1582151158.5235808 * 1000.0))));
        assertEquals(30, contaminationChain.getScore());
    }
}
