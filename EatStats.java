import java.io.File;
import java.util.ArrayList;
import java.util.Date;

import lib.EatHistory;
import lib.EatHistoryChunk;
import lib.GraphShow;

public class EatStats {
    public static void main(String[] args) throws Exception {
        Long start = null;
        Long end = null;
        if(args.length != 2){
            end = (new Date()).getTime();
            start = end - 14*24*60*60*1000;
        }else{
            start = Long.parseLong(args[0]);
            end = Long.parseLong(args[1]);
        }

        EatHistory eh = new EatHistory(new File("./data/history_chunks"));
        ArrayList<GraphShow.DataPoint> points = new ArrayList<GraphShow.DataPoint>();

        EatHistoryChunk.DataPointEach F = (EatHistoryChunk.DataPoint d) -> {
            points.add(new GraphShow.DataPoint(d.timestamp, d.food.getCalories()));
        };
        eh.eachDataPoint(start, end, F);

        for(GraphShow.DataPoint dp : points){
            System.out.println(dp.getKey());
            System.out.println(dp.getValue());
        }

        GraphShow.get().drawGraph(points);
    }
}
