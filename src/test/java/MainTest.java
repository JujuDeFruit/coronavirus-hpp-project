import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import static org.junit.Assert.*;

public class MainTest {

    @Test
    public void process() throws IOException {
        Main.process("data\\5000");
        // Open files
            // DataGenerated by process
        FileReader generatedDataReader = new FileReader("src/main/resources/data.csv"); // TODO replace data.cvs by the correct name
        BufferedReader generatedDataBuffer = new BufferedReader(generatedDataReader);
            // Waited data
        FileReader testDataReader = new FileReader("src/main/resources/testData.csv");
        BufferedReader testDataBuffer = new BufferedReader(testDataReader);

        // Test if the files are identical
        String generatedDataLine = generatedDataBuffer.readLine();
        String testDataLine = testDataBuffer.readLine();
        while (generatedDataLine != null || testDataLine != null) {
            assertEquals(generatedDataLine, testDataLine);
            generatedDataLine = generatedDataBuffer.readLine();
            testDataLine = testDataBuffer.readLine();
        }

        // Close files
        generatedDataBuffer.close();
        generatedDataReader.close();
        testDataBuffer.close();
        testDataReader.close();
    }
}