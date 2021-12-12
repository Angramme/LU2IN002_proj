package lib;

import java.util.ArrayList;
public class GraphShow{
    static public class DataPoint{
        private long key;
        private double value;

        public long getKey(){
            return key;
        }
        public double getValue(){
            return value;
        }

        public DataPoint(long key, double value){
            this.key = key;
            this.value = value;
        }
    }
    
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public static final GraphShow instance = new GraphShow();

    private GraphShow(){}

    public static GraphShow get(){
        return instance;
    }

    public void drawGraph(ArrayList<DataPoint> points){
        StringBuilder sb = new StringBuilder();

        sb.append(ANSI_RED + "veuillez mettre votre terminal en plein écran pour bénéficier du meilleur affichage possible.\n");
        
        sb.append(ANSI_YELLOW + "╔");
        for(int i=0 ; i<120 ; i++) sb.append("═"); sb.append("╗\n║");
        sb.append(ANSI_CYAN + "     key     " + ANSI_YELLOW + " ║ " + ANSI_GREEN + "value (1 * = 1 kcal, 1 line = 100 kcal)" + ANSI_YELLOW);
        for(int i=0 ; i<65 ; i++) sb.append(" ");
        sb.append("║\n");
        sb.append("║");
        for(int i=0 ; i<120 ; i++) sb.append("═");
        sb.append(ANSI_YELLOW+"║");
        sb.append("\n");
        for(int a=0 ; a<points.size()-1 ; a++){
            sb.append(ANSI_YELLOW + "║" + ANSI_CYAN + String.format("%4d ", points.get(a).key)+ ANSI_RED + ": " + ANSI_RESET);
            long nb = (long)points.get(a).value;
            int j=0;
            for(int i=0 ; i<nb ; i++){
                sb.append(ANSI_GREEN + "*" + ANSI_RESET);
                j++;
                if (j == 100){
                    for(int k = 0 ; k<4 ; k++) sb.append(" ");
                    sb.append(ANSI_YELLOW + "║\n" + ANSI_YELLOW + "║" + ANSI_GREEN + "                ");
                    j = 0;
                }
            }
            for(int k=0 ; k<104-j ; k++) sb.append(" ");
            sb.append(ANSI_YELLOW+"║");
            sb.append("\n"+ANSI_YELLOW+"║");
            for(int i=0 ; i<120 ; i++) sb.append("═");
            sb.append("║\n");
        }
        sb.append(ANSI_YELLOW + "║" + ANSI_CYAN + String.format("%4d ", points.get(points.size() - 1).key)+ ANSI_RED + ": " + ANSI_RESET);
        long nb = (int)points.get(points.size() - 1).value;
        int j=0;
        for(int i=0 ; i<nb ; i++){
            sb.append(ANSI_GREEN + "*" + ANSI_RESET);
            j++;
            if (j == 100){
                sb.append("\n" + ANSI_YELLOW + "║" + ANSI_GREEN + "       ");
                j = 0;
            }
        }
        for(int k=0 ; k<104-j ; k++) sb.append(" ");
        sb.append(ANSI_YELLOW+"║");
        sb.append("\n"+ANSI_YELLOW+"╚");
        for(int i=0 ; i<120 ; i++) sb.append("═");
        sb.append(ANSI_YELLOW+"╝");

        System.out.println(sb.toString());
    }
}