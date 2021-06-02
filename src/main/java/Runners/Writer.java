package Runners;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.concurrent.BlockingQueue;

public class Writer implements Runnable {

	private final BlockingQueue<int []> iQueue;
	private final StringBuilder builder;

	private String result;
	private PrintWriter pw;

	public Writer(BlockingQueue<int []> q){
		iQueue = q;
		pw = null;
		try {
			pw = new PrintWriter(new File("src\\main\\resources\\data.csv"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		builder = new StringBuilder();
		result = "";
	}

	public void run() {
		//Read from input Queue
		//Parse to csv
		//get top1_country_origin, top1_chain_root_person_id, top1_chain_score; top2_country_origin, top2_chain_root_person_id, top2_chain_score; top3_country_origin, top3_chain_root_person_id, top3_chain_score
		//Getting the top3, take()
		int[] top3;
		try {
			top3 = iQueue.take();
			while(top3[0] != -1)
			{
				builder.setLength(0);

				//Parsing each chain
				result = top3[0] + "," + top3[1] + "," + top3[2] + ";";
				result += top3[3] + "," + top3[4] + "," + top3[5] + ";";
				result += top3[6] + "," + top3[7] + "," + top3[8] + ";";
				builder.append(result);

				//Write in file
				writeResult();
				top3= iQueue.take();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}	

	public void writeResult() {
		builder.delete(0, builder.length());
		builder.append(result);
		//On efface le fichier
		pw.print("");
		pw.flush();
		//On écrit les résultats
		pw.println(builder.toString());
		pw.flush();

	}
}
