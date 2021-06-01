package TestsScore;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;

public class TestsScore {

    @Test
    public void test() {
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            BufferedReader bufferedReader = new BufferedReader(new FileReader(classLoader.getResource("data.csv").getFile()));
            String line;
            int i = 1;
            while ((line = bufferedReader.readLine()) != null) {
                String[] values = line.split(";");
                for(String val : values) {
                    if (Integer.parseInt(val.split(",")[2]) == 0) throw new Exception("Error score is 0 at line " + i);
                }
                i++;
            }
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }

}