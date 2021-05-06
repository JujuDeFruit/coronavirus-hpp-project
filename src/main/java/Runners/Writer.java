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
		if(!(iQueue == null || iQueue.isEmpty())) //Queue non vide
		{
			//get top1_country_origin, top1_chain_root_person_id, top1_chain_score; top2_country_origin, top2_chain_root_person_id, top2_chain_score; top3_country_origin, top3_chain_root_person_id, top3_chain_score
			//Assuming chaine1 is top1 chain
			ContaminationChain[] chains= this.iQueue.poll();
			
			//Use chain1.country_origin/chain_root_person_id/chain_score
			result = chains[0].getCountry_id() + ',' + chains[0].getFirstPersonId() + ',' + chains[0].getScore() + ";";
			result += chains[1].getCountry_id() + ',' + chains[1].getFirstPersonId() + ',' + chains[1].getScore() + ";";
			result += chains[2].getCountry_id() + ',' + chains[2].getFirstPersonId() + ',' + chains[2].getScore() + ";";
			this.builder.append(result);
			try {
				writeResult();
			}catch(IOException e)
			{
				e.printStackTrace();
			}
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
