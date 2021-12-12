package lib;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class EatHistoryChunk {
    public class DataPoint{
        public long timestamp;
        public SimpleFood food;
    }

    public interface DataPointEach{
        public void run(DataPoint point);
    }

    public static final int CHUNK_SIZE = 200; // in number of repas

    private HashMap<Long, SimpleFood> datapoints;
    private File sync_file;
    private boolean modified_since_open;
    private long min_timestamp;
    private long max_timestamp;

    public EatHistoryChunk(File file) throws Exception {
        sync_file = file;
        datapoints = new HashMap<Long, SimpleFood>();

        min_timestamp = Long.MAX_VALUE;
        max_timestamp = Long.MIN_VALUE;

        if(file.exists()){
            try{
                parse(file);

                Pattern pt = Pattern.compile("([0-9]+)\\.chunk");
                Matcher mc = pt.matcher(file.getName());

                if(!mc.matches() || Long.parseLong(mc.group(1)) != getBeginTimestamp()){
                    throw new Exception("non matching timestamps! file : " + file.getAbsolutePath());
                }
            }catch(FileNotFoundException err){
                throw new Exception("WTF");
            }
            modified_since_open = false;
        }else{
            file.createNewFile();
            modified_since_open = false;
        }
    }

    private void parse(File file) throws Exception {
        Scanner scan = new Scanner(file);

        while(scan.hasNextLine()){
            long timestamp = scan.nextLong();
            double calories = scan.nextDouble();
            scan.skip("\\s*");
            String food_signature = scan.next("([a-zA-Z])+");
            scan.skip("\\s*\\n");
            SimpleFood nfood = new SimpleFood(food_signature, calories, 0);

            if(!addDataPoint(timestamp, nfood)){
                scan.close();
                throw new Exception("bad sized chunk!");
            }
        }

        scan.close();
    }

    public void sync() throws Exception {
        if(!modified_since_open) return; 
        try{
            FileWriter wrtr = new FileWriter(sync_file, false); // open with truncate

            for(HashMap.Entry<Long, SimpleFood> entry : datapoints.entrySet()){
                wrtr.write(String.format("%d %f %s\n", entry.getKey(), entry.getValue().getCalories(), entry.getValue().getName()));
            }

            wrtr.close();

            System.out.println(" * synced chunk "+getBeginTimestamp());
            modified_since_open = false;
        }catch(IOException err){
            throw new Exception("cannot sync chunk! error: "+err.getMessage());
        }
    }

    public long getBeginTimestamp(){
        return min_timestamp;
    }
    public long getEndTimestamp(){
        return max_timestamp;
    }

    public DataPoint popLast() throws Exception {
        modified_since_open = true;

        DataPoint ret = new DataPoint();
        ret.timestamp = getEndTimestamp();
        ret.food = datapoints.get(getEndTimestamp());
        if(ret.food == null) throw new Exception("popLast internal error!");

        datapoints.remove(getEndTimestamp());
        max_timestamp = 0;
        for(Long ts : datapoints.keySet()){
            max_timestamp = Math.max(max_timestamp, ts);
        }

        return ret;
    }

    public boolean addDataPoint(long timestamp, SimpleFood food){
        if(datapoints.size() >= CHUNK_SIZE) return false;
        datapoints.put(timestamp, food);
        min_timestamp = Math.min(timestamp, min_timestamp);
        max_timestamp = Math.max(timestamp, max_timestamp);

        modified_since_open = true;
        return true;
    }

    public void eachDataPoint(DataPointEach func){
        for(HashMap.Entry<Long, SimpleFood> entry : datapoints.entrySet()){
            DataPoint dt = new DataPoint();
            dt.timestamp = entry.getKey();
            dt.food = entry.getValue();
            func.run(dt);
        }
    }
    public void eachDataPoint(long start, long end, DataPointEach func){
        for(HashMap.Entry<Long, SimpleFood> entry : datapoints.entrySet()){
            if(entry.getKey() >= start && entry.getKey() <= end){
                DataPoint dt = new DataPoint();
                dt.timestamp = entry.getKey();
                dt.food = entry.getValue();
                func.run(dt);
            }
        }
    }
}
