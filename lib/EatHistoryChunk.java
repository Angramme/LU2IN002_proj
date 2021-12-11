package lib;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;

public class EatHistoryChunk {
    public static final int CHUNK_SIZE = 365; // in days

    private HashMap<Integer, SimpleFood> datapoints;
    private File sync_file;
    private boolean modified_since_open;
    private int min_timestamp;

    public EatHistoryChunk(File file) throws FileNotFoundException {
        Scanner scan = new Scanner(file);
        sync_file = file;

        while(scan.hasNextLine()){
            int timestamp = scan.nextInt();
            min_timestamp = Math.min(timestamp, min_timestamp);
            double calories = scan.nextDouble();
            scan.skip("\\s*");
            String food_signature = scan.next("([a-zA-Z]\\s+)+");
            scan.skip("\\s*\\n");
            SimpleFood nfood = new SimpleFood(food_signature, calories);

            datapoints.put(timestamp, nfood);
        }

        scan.close();

        modified_since_open = false;
    }

    public void sync() throws Exception {
        if(!modified_since_open) return; 
        try{
            FileWriter wrtr = new FileWriter(sync_file, false); // open with truncate

            for(HashMap.Entry<Integer, SimpleFood> entry : datapoints.entrySet()){
                wrtr.write(String.format("%d %f %s\n", entry.getKey(), entry.getValue().getCalories(), entry.getValue().getName()));
            }

            wrtr.close();
        }catch(IOException err){
            throw new Exception("cannot sync chunk! error: "+err.getMessage());
        }
    }

    public int getBeginTimestamp(){
        return min_timestamp;
    }
}
