package Runners;

import Models.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;

/**
 * This class represent the thread that will read the data
 * from the csv files. It will forward it to a queue in an
 * ante-chronological order.
 */
public class Reader implements Runnable{
    private final BlockingQueue<DataType> queue;
    private final String pathToData;
    // FileReaders
    private FileReader franceFR = null;
    private FileReader italyFR = null;
    private FileReader spainFR = null;
    // BufferedReaders
    private BufferedReader franceReader = null;
    private BufferedReader italyReader = null;
    private BufferedReader spainReader = null;

    public Reader(BlockingQueue<DataType> queue, String pathToData) {
        this.queue = queue;
        this.pathToData = pathToData;
    }

    @Override
    public void run() {
        try {
            openFile();

            // int to know which file contains the data to forward to the queue between france(0), italy(1) and spain(2)
            short countryMem = -1;
            // String array storing the current split line per file
            String[] franceSplit, italySplit, spainSplit;
            // int storing the current id per file
            int franceId = 0, italyId = 0, spainId = 0;

            String franceLine = franceReader.readLine();
            String italyLine = italyReader.readLine();
            String spainLine = spainReader.readLine();
            while (franceLine != null && italyLine != null && spainLine != null) {
                // Split line to get id if the previous data was not already split
                // (e.g. either when the data was forwarded to queue or when it is the first time we enter the loop)
                switch (countryMem) {
                    case 0: // only france needs to be split
                        franceSplit = franceLine.replaceAll(" ", "").split(",");
                        franceId = Integer.parseInt(franceSplit[0]);
                        break;
                    case 1: // only italy needs to be split
                        italySplit = italyLine.replaceAll(" ", "").split(",");
                        italyId = Integer.parseInt(italySplit[0]);
                        break;
                    case 2: // only spain needs to be split
                        spainSplit = spainLine.replaceAll(" ", "").split(",");
                        spainId = Integer.parseInt(spainSplit[0]);
                        break;
                    default: // all lines needs to be split
                        franceSplit = franceLine.replaceAll(" ", "").split(",");
                        franceId = Integer.parseInt(franceSplit[0]);
                        italySplit = italyLine.replaceAll(" ", "").split(",");
                        italyId = Integer.parseInt(italySplit[0]);
                        spainSplit = spainLine.replaceAll(" ", "").split(",");
                        spainId = Integer.parseInt(spainSplit[0]);
                }

                // Research of the minimum id between the three
                int temp;
                if (franceId < italyId) {
                    countryMem = 0;
                    temp = franceId;
                }
                else {
                    countryMem = 1;
                    temp = italyId;
                }
                if (spainId < temp){
                    countryMem = 2;
                }
                // Forward the minimum id data to the queue and go to the next line for this country
                switch (countryMem){
                    case 0:
                        queue.add(new DataType(franceLine));
                        franceLine = franceReader.readLine();
                        break;
                    case 1:
                        queue.add(new DataType(italyLine));
                        italyLine = italyReader.readLine();
                        break;
                    case 2:
                        queue.add(new DataType(spainLine));
                        spainLine = spainReader.readLine();
                        break;
                    default:
                        System.out.println("Couldn't find a min ?");
                }
            }
            // poison-pill
            queue.add(new DataType("-1, \"\", \"\", 0000-00-00 00:00:00, 0, unknown, \"\""));

            for (int i=0; i<queue.size(); i++){
                try{
                    System.out.println(queue.take().getPerson_id());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                closeFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void openFile() throws IOException {
        // France
        franceFR = new FileReader(pathToData + "/France.csv");
        franceReader = new BufferedReader(franceFR);
        // Italy
        italyFR = new FileReader(pathToData + "/Italy.csv");
        italyReader = new BufferedReader(italyFR);
        // Spain
        spainFR = new FileReader(pathToData + "/Spain.csv");
        spainReader = new BufferedReader(spainFR);
    }

    public void closeFile() throws IOException {
        // France
        if (franceReader != null)
            franceReader.close();
        if (franceFR != null)
            franceFR.close();
        // Italy
        if (italyReader != null)
            italyReader.close();
        if (italyFR != null)
            italyFR.close();
        // Spain
        if (spainReader != null)
            spainReader.close();
        if (spainFR != null)
            spainFR.close();
    }
}
