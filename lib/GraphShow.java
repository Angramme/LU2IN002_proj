package lib;

import java.util.ArrayList;
import java.util.Date;
import java.lang.Comparable;
import java.util.Comparator;
import java.util.Calendar;

public class GraphShow{
    public static class DataPoint implements Comparable<DataPoint> {
        private long key;
        private double value;

        public long getKey(){
            return key;
        }
        public double getValue(){
            return value;
        }

        public int compareTo(DataPoint o){
            if(key == o.key) return 0;
            else return key > o.key ? 1 : -1;
        }

        public DataPoint(long key, double value){
            this.key = key;
            this.value = value;
        }
    }
    public static class DataPointComparator implements Comparator<GraphShow.DataPoint> {
        @Override
        public int compare(GraphShow.DataPoint o1, GraphShow.DataPoint o2) {
            return o1.compareTo(o2);
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

    public void drawGraph(ArrayList<DataPoint> points, String genre){
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
        double consocurrent = 0;

        Calendar c = Calendar.getInstance();

        Calendar currentDay = Calendar.getInstance();
        currentDay.setTime(new Date(points.get(0).key));

        ArrayList<Double> tabCal = new ArrayList<Double>();

        for(int a=0 ; a<points.size() ; a++){
            Date date = new Date(points.get(a).key);
            c.setTime(date);
            sb.append(ANSI_YELLOW + "║" + ANSI_CYAN + String.format("%s ", date) + ANSI_RED + ": " + ANSI_RESET);
            long nb = (long)(points.get(a).value / max * 100);
            for(int i=0 ; i<nb ; i++){
                sb.append(ANSI_GREEN + "*" + ANSI_RESET);
            }
            sb.append(String.format("%.0f",points.get(a).value));
            for(int k=0 ; k<104-nb-(int)(Math.log10(points.get(a).value)+1) ; k++) sb.append(" ");
            sb.append(ANSI_YELLOW + "║\n");
            total += points.get(a).value;

            if((c.get(Calendar.DAY_OF_MONTH) == currentDay.get(Calendar.DAY_OF_MONTH))){
                consocurrent += points.get(a).value;
                if (a == points.size()-1) tabCal.add(consocurrent);
            }
            else{
                currentDay.setTime(date);
                tabCal.add(consocurrent);
                consocurrent = points.get(a).value;
            }
        }

        sb.append(ANSI_YELLOW+"║");
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
        for(int k=0 ; k<97-((int)(Math.log10(total)+1)) ; k++) sb.append(" ");
        sb.append("║");
        sb.append("\n"+ANSI_YELLOW+"╚");
        for(int i=0 ; i<135 ; i++) sb.append("═");
        sb.append(ANSI_YELLOW+"╝");
        sb.append("\n"+ANSI_RESET);

        // recommendations

        int reco;

        if (genre.equals("homme")){
            sb.append("\nConsommation moyenne recommandée de calories journalières pour un homme : " + ANSI_YELLOW + "2600 kcal\n" + ANSI_RESET);
            reco = 2600;
        }
        else{
            sb.append("\nConsommation moyenne recommandée de calories journalières pour une femme : " + ANSI_YELLOW + "2100 kcal\n" + ANSI_RESET);
            reco = 2100;
        }

        // cas 0

        if(tabCal.get(0) < 2600){
            sb.append("\nTotal de calories consommés le premier jour : "+ANSI_GREEN + String.format("%.2f", tabCal.get(0))+" kcal"+ANSI_RESET+", vous êtes dans la recommandation !\n");
        }
        else{
            sb.append("\nTotal de calories consommés le premier jour : "+ANSI_RED + String.format("%.2f", tabCal.get(0))+" kcal"+ANSI_RESET+", vous avez trop consommé aujourd'hui\n");
        }

        // autres cas

        for(int i=1 ; i<tabCal.size() ; i++){
            if(tabCal.get(i) < reco){
                sb.append("Total de calories consommés le " + (i+1) + "ème jour " + ": "+ANSI_GREEN + String.format("%.2f", tabCal.get(i))+" kcal"+ANSI_RESET+", vous êtes dans la recommandation !\n");
            }
            else{
                sb.append("Total de calories consommés le " + (i+1) + "ème jour " + ": "+ANSI_RED + String.format("%.2f", tabCal.get(i))+" kcal"+ANSI_RESET+", vous avez trop consommé aujourd'hui\n");
            }
        }

        sb.append("\n");

        double moyennetot = total / tabCal.size();
        if(moyennetot < reco) sb.append("Moyenne totale : "+ANSI_GREEN+String.format("%.2f", moyennetot) + "kcal / jour." + ANSI_RESET + " Votre consommation au niveau des calories est OK.");
        else sb.append("Moyenne totale : "+ANSI_RED+String.format("%.2f", moyennetot) + "kcal / jour." + ANSI_RESET + " La moyenne journalière est supérieure à la consommation journalière recommandée, réduisez votre consommation.");

        sb.append("\n\nMerci d'avoir utilisé EatStats !");

        System.out.println(sb.toString());
    }
}