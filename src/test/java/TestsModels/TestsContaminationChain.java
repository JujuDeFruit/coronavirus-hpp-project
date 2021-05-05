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

        DataType second = new DataType("5", "Jessica", "WATSON", "1583091884.9200459", "4", (short)0);
        contaminationChain.push(second);

        assertEquals(20, contaminationChain.getScore());
    }
}
