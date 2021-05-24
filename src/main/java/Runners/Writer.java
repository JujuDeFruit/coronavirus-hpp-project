package Runners;

import Models.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.BlockingQueue;

public class Writer implements Runnable {

	private final BlockingQueue<ContaminationChain []> iQueue;
	private final StringBuilder builder;

	private String result;
	private PrintWriter pw;

	public Writer(BlockingQueue<ContaminationChain []> q){
		iQueue = q;
		pw = null;
		try {
			pw = new PrintWriter(new File("src\\main\\resources\\Data.csv"));
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
		ContaminationChain[] top3;
		try {
			top3 = iQueue.take();
			while(top3[0].getCountry_id() != -1)
			{
				builder.setLength(0);

				System.out.println("in writer");
				System.out.println(top3[0].getScore());
				System.out.println(top3[1].getScore());
				System.out.println(top3[2].getScore());
				System.out.println("\n");

				//Parsing each chain
				result = top3[0].getCountry_id() + "," + top3[0].getFirstPersonId() + "," + top3[0].getScore() + ";";
				result += top3[1].getCountry_id() + "," + top3[1].getFirstPersonId() + "," + top3[1].getScore() + ";";
				result += top3[2].getCountry_id() + "," + top3[2].getFirstPersonId() + "," + top3[2].getScore() + ";";
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
