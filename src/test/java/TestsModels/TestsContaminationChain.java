package TestsModels;

import org.junit.Test;

import Models.DataType;
import Models.ContaminationChain;

import java.sql.Timestamp;

import static org.junit.Assert.*;

public class TestsContaminationChain {
    /**
     * Test constructor of the ContaminationChain Object.
     */
    @Test
    public void testConstructor() {
        String[] firstStr = { "4", "\"Daniel\"", "\"ROBINSON\"", "1995-08-21 00:00:00", "1582161158.5235808", "unknown", "\"course à pieds avec la grand-mère au marché\"" };
        DataType first = new DataType( firstStr, (short)0);
        ContaminationChain contaminationChain = new ContaminationChain(first);

        assertEquals(10, contaminationChain.getScore());
        assertEquals(0, contaminationChain.getCountry_id());

        assertEquals(1, contaminationChain.getContaminationId().size());
        assertEquals(4, (int)contaminationChain.getContaminationId().get(0));

        assertEquals(1, contaminationChain.getContaminationTs().size());
        assertEquals(new Timestamp((long)(1582161158.5235808 * 1000.0)), contaminationChain.getContaminationTs().get(0));

    }

    /**
     * Test Push method.
     */
    @Test
    public void testPush() {
        // DataTypes declaration
        String[] firstStr = { "4", "\"Daniel\"", "\"ROBINSON\"", "1995-08-21 00:00:00", "1582161158.5235808", "unknown", "\"course à pieds avec la grand-mère au marché\"" };
        DataType first = new DataType( firstStr, (short)0);
        String[] secondStr = { "5", "\"Jessica\"", "\"WATSON\"", "1995-08-21 00:00:00", "1583091884.9200459", "4", "\"course à pieds avec la grand-mère au marché\"" };
        DataType second = new DataType( secondStr, (short)0);
        String[] thirdStr = { "9", "\"Stephanie\"", "\"MITCHELL\"", "1995-08-21 00:00:00", "1585699579.2617905", "2", "\"course à pieds avec la grand-mère au marché\"" };
        DataType third = new DataType( thirdStr, (short)0);

        // Push all
        ContaminationChain contaminationChain = new ContaminationChain(first);

        assertTrue(contaminationChain.push(second));
        assertFalse(contaminationChain.push(third));

        // Third should not be push on the ground, third was not contaminated by one person of this chain.
        assertEquals(2, contaminationChain.getContaminationTs().size());
        assertEquals(2, contaminationChain.getContaminationId().size());

        assertEquals(5, (int)contaminationChain.getContaminationId().get(1));
        assertEquals(new Timestamp((long)(1583091884.9200459 * 1000.)), contaminationChain.getContaminationTs().get(1));

        thirdStr[5] = "5";
        third = new DataType( thirdStr, (short)0);
        assertTrue(contaminationChain.push(third));

        assertEquals(3, contaminationChain.getContaminationTs().size());
        assertEquals(3, contaminationChain.getContaminationId().size());

        assertEquals(9, (int)contaminationChain.getContaminationId().get(2));
        assertEquals(new Timestamp((long)(1585699579.2617905 * 1000.0)), contaminationChain.getContaminationTs().get(2));
    }

    /**
     * Test CalculateScore method.
     */
    @Test
    public void testCalculateScore() {
        // DataTypes declaration
        String[] firstStr = { "4", "\"Daniel\"", "\"ROBINSON\"", "1995-08-21 00:00:00", "1582161158.5235808", "unknown", "\"course à pieds avec la grand-mère au marché\"" };
        DataType first = new DataType( firstStr, (short)0);
        String[] secondStr = { "5", "\"Jessica\"", "\"WATSON\"", "1995-08-21 00:00:00", "1583091884.9200459", "4", "\"course à pieds avec la grand-mère au marché\"" };
        DataType second = new DataType( secondStr, (short)0);
        String[] thirdStr = { "9", "\"Stephanie\"", "\"MITCHELL\"", "1995-08-21 00:00:00", "1585699579.2617905", "4", "\"course à pieds avec la grand-mère au marché\"" };
        DataType third = new DataType( thirdStr, (short)0);

        ContaminationChain contaminationChain = new ContaminationChain(first);
        contaminationChain.push(second);

        assertFalse(contaminationChain.calculateScore(contaminationChain.getContaminationTs().get(1)));
        assertEquals(14, contaminationChain.getScore());

        contaminationChain.push(third);

        assertFalse(contaminationChain.calculateScore(contaminationChain.getContaminationTs().get(2)));
        assertEquals(10, contaminationChain.getScore());
    }
}
