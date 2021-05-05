package TestsModels;

import Models.ContaminationChain;
import Models.DataType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestsContaminationChain {

    @Test
    public void test() {
        DataType first = new DataType("4", "Daniel", "ROBINSON", "1582161158.5235808", "unknown", (short)0);
        ContaminationChain contaminationChain = new ContaminationChain(first);

        assertEquals(10, contaminationChain.getScore());

        DataType second = new DataType("5", "Jessica", "WATSON", "1583091884.9200459", "4", (short)0);
        contaminationChain.push(second);

        assertEquals(14, contaminationChain.getScore());

        DataType third = new DataType("9", "Stephanie", "MITCHELL", "1585699579.2617905", "2", (short)0);
        contaminationChain.push(third);

        assertEquals(14, contaminationChain.getScore());
    }
}
