package Runners;

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
    private BufferedReader franceReader = null;
    private BufferedReader italyReader = null;
    private BufferedReader spainReader = null;

    public Reader(BlockingQueue<DataType> queue, String pathToData) {
        this.queue = queue;
        this.pathToData = pathToData;
    }

    @Override
    public void run() {

    }

    private void openFile() throws IOException{
        franceReader = new BufferedReader(new FileReader(pathToData + "/France.csv"));
        italyReader = new BufferedReader(new FileReader(pathToData + "/Italy.csv"));
        spainReader = new BufferedReader(new FileReader(pathToData + "/Spain.csv"));
    }
}
