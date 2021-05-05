package Runners;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.ArrayBlockingQueue;

import javax.swing.tree.ExpandVetoException;

import java.io.File;
import java.util.Scanner;
import org.junit.Test;

public class WriterTest {

		@Test
		public void Test() throws Exception{
			ArrayBlockingQueue<String> q = new ArrayBlockingQueue<>(32);
			q.add("a");
			q.add("b");
			q.add("c");
			Writer julesVerne = new Writer(q);
			julesVerne.run();
			
			//Scanner reads EOL char
			String excpected = "aaa"+ "\r\n" + 
					"bbb" + "\r\n" +
					"ccc" + "\r\n" + "\r\n"; 
			
			//Read the csv file generated by Writer.java
			Scanner sc = new Scanner(new File("Data.csv"));
			sc.useDelimiter(",");
			String readStrings = new String();
			while (sc.hasNext()) {
				readStrings += sc.next();
			}
			
			System.out.println(readStrings);
			assertEquals(excpected, readStrings);
		}
}
