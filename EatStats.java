import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

import lib.EatHistory;
import lib.EatHistoryChunk;
import lib.GraphShow;

public class EatStats {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";
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

        points.sort(new GraphShow.DataPointComparator());

        System.out.println(ANSI_PURPLE + "Bienvenue sur le programme EatStats ! Ce programme va afficher sous forme de graphique les calories que vous avez consommé récemment.");
        
        System.out.print(ANSI_CYAN + "Etes-vous un homme ou une femme ? (tapez votre réponse) : "+ANSI_RESET);

        Scanner genre = new Scanner(System.in);
        String tap = genre.nextLine();

        genre.close();

        if(!(tap.equals("homme")) && !(tap.equals("femme"))){
            throw new Exception("saisie incorrecte");
        }



        GraphShow.get().drawGraph(points, tap);
    }
}
