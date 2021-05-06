package Runners;

import Models.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.BlockingQueue;

public class Writer implements Runnable {

	private BlockingQueue<ContaminationChain []> iQueue;
	private String result;

	private StringBuilder builder;
	private PrintWriter pw;

	Writer(BlockingQueue<ContaminationChain []> q){
		this.iQueue = q;
		this.pw = null;
		try {
			pw = new PrintWriter(new File("src\\main\\resources\\Data.csv"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		this.builder = new StringBuilder();
		this.result = "";
	}

	public void run() {
		//Read from input Queue
		//Parse to csv
		//get top1_country_origin, top1_chain_root_person_id, top1_chain_score; top2_country_origin, top2_chain_root_person_id, top2_chain_score; top3_country_origin, top3_chain_root_person_id, top3_chain_score
		//Getting the chains, take() 
		ContaminationChain[] chains= this.iQueue.take();

		while(chains[0].getCountry_id() != -1)
		{
			this.builder.setLength(0);
			result = "";

			//Parsing each chain
			for(ContaminationChain chain : chains) {
				result += chain.getCountry_id() + ',' + chain.getFirstPersonId() + ',' + chain.getScore() + ";";
			}
			this.builder.append(result);

			//Write in file
			try {
				writeResult();
			}catch(IOException e)
			{
				e.printStackTrace();
			}
			chains= this.iQueue.take();
		}	
		
	}	

	public void writeResult() throws IOException {
		this.builder.delete(0, this.builder.length());
		this.builder.append(result);
		//On efface le fichier
		pw.print("");
		pw.flush();
		//On écrit les résultats
		pw.println(this.builder.toString());
		pw.flush();

	}
}
