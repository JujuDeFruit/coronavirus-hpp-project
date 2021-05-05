package Runners;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.ArrayBlockingQueue;

public class Writer implements Runnable {
	
	private ArrayBlockingQueue<String> iQueue;
	private String result;

	private StringBuilder builder;
	private PrintWriter pw;
	
	Writer(ArrayBlockingQueue<String> q){
		this.iQueue = q;
		this.pw = null;
		try {
            pw = new PrintWriter(new File("Data.csv"));
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
			String chain1= this.iQueue.poll();
			String chain2= this.iQueue.poll();
			String chain3= this.iQueue.poll();
			
			//Use chain1.country_origin/chain_root_person_id/chain_score
			result = chain1 + ',' + chain1 + ',' + chain1 + "\r\n";
			result += chain2 + ',' + chain2 + ',' + chain2 + "\r\n";
			result += chain3 + ',' + chain3 + ',' + chain3 + "\r\n";
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
		//On �crit les r�sultats
		pw.println(this.builder.toString());
		pw.flush();
		
	}
}
