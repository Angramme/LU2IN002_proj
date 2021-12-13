package lib;

import java.util.ArrayList;
import java.util.Date;

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
        double max = points.get(0).value;
        for(DataPoint dp : points){
            if (dp.value > max) max = dp.value;
        }

        StringBuilder sb = new StringBuilder();

        sb.append(ANSI_RED + "veuillez mettre votre terminal en plein écran pour bénéficier du meilleur affichage possible. Si vous avez des soucis d'affichage, relancez le programme avec le terminal en plein écran.\n");
        
        sb.append(ANSI_YELLOW + "╔");
        for(int i=0 ; i<135 ; i++) sb.append("═"); sb.append("╗\n║");
        sb.append(ANSI_CYAN + "            Date             " + ANSI_YELLOW + "║ " + ANSI_GREEN + "     nombre de calories (* = 1% du repas où vous aviez été le plus gourmand) " + ANSI_YELLOW);
        for(int i=0 ; i<27 ; i++) sb.append(" ");
        sb.append("║\n");
        sb.append("║");
        for(int i=0 ; i<135 ; i++) sb.append("═");
        sb.append(ANSI_YELLOW+"║");
        sb.append("\n");
        double total=0;
        for(int a=0 ; a<points.size() ; a++){
            sb.append(ANSI_YELLOW + "║" + ANSI_CYAN + String.format("%s ", new Date(points.get(a).key))+ ANSI_RED + ": " + ANSI_RESET);
            long nb = (long)(points.get(a).value / max * 100);
            for(int i=0 ; i<nb ; i++){
                sb.append(ANSI_GREEN + "*" + ANSI_RESET);
            }
            for(int k=0 ; k<104-nb ; k++) sb.append(" ");
            sb.append(ANSI_YELLOW + "║\n");
            total += points.get(a).value;
        }
        // sb.append(ANSI_YELLOW + "║" + ANSI_CYAN + String.format("%4s ", new Date(points.get(points.size() - 1).key))+ ANSI_RED + ": " + ANSI_RESET);
        // long nb = (long)(points.get(points.size() - 1).value / max * 100);
        // int j=0;
        // for(int i=0 ; i<nb ; i++){
        //     sb.append(ANSI_GREEN + "*" + ANSI_RESET);
        //     j++;
        //     if (j == 100){
        //         for(int k = 0 ; k<4 ; k++) sb.append(" ");
        //         sb.append(ANSI_YELLOW + "║\n" + ANSI_YELLOW + "║" + ANSI_GREEN + "                ");
        //         j = 0;
        //     }
        // }
        // for(int k=0 ; k<104-j ; k++) sb.append(" ");
        sb.append(ANSI_YELLOW+"║");
        // sb.append("\n║");
        for(int k=0 ; k<30 ; k++) sb.append(" ");
        sb.append("0");
        int value;
        for(int pourc = 1 ; pourc <= 100 ; pourc++){
            if (pourc % 10 == 0){
                value = (int)(max * ((double)pourc / 100));
                sb.delete(sb.length()-(int)(Math.log10(value)+2), sb.length()-1);
                sb.append(String.format("%d", value));
            }
            sb.append(" ");
        }
        sb.append("\n║");
        sb.append("Total de calories consommés : "+String.format("%.2f", total)+" kcal");
        sb.append("\n"+ANSI_YELLOW+"╚");
        for(int i=0 ; i<135 ; i++) sb.append("═");
        sb.append(ANSI_YELLOW+"╝");

        System.out.println(sb.toString());
    }
}