package lib;

public class GraphShow{
    public class DataPoint{
        int key;
        double value;

        public DataPoint(int key, double value){
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

    public void drawGraph(DataPoint[] points){
        StringBuilder sb = new StringBuilder();
        
        sb.append(ANSI_YELLOW + "╔");
        for(int i=0 ; i<120 ; i++) sb.append("═"); sb.append("╗\n║");
        sb.append(ANSI_CYAN + " key" + ANSI_YELLOW + " ║ " + ANSI_GREEN + "value (1 * = 1 kcal, 1 line = 100 kcal)" + ANSI_YELLOW);
        for(int i=0 ; i<74 ; i++) sb.append(" ");
        sb.append("║\n");
        sb.append("║");
        for(int i=0 ; i<120 ; i++) sb.append("═");
        sb.append(ANSI_YELLOW+"║");
        sb.append("\n");
        for(int a=0 ; a<points.length-1 ; a++){
            sb.append(ANSI_YELLOW + "║" + ANSI_CYAN + String.format("%4d ", points[a].key)+ ANSI_RED + ": " + ANSI_RESET);
            int nb = (int)points[a].value;
            int j=0;
            for(int i=0 ; i<nb ; i++){
                sb.append(ANSI_GREEN + "*" + ANSI_RESET);
                j++;
                if (j == 100){
                    for(int k = 0 ; k<13 ; k++) sb.append(" ");
                    sb.append(ANSI_YELLOW + "║\n" + ANSI_YELLOW + "║" + ANSI_GREEN + "       ");
                    j = 0;
                }
            }
            sb.append("\n"+ANSI_YELLOW+"║");
            for(int i=0 ; i<120 ; i++) sb.append("═");
            sb.append("║\n");
        }
        sb.append(ANSI_YELLOW + "║" + ANSI_CYAN + String.format("%4d ", points[points.length - 1].key)+ ANSI_RED + ": " + ANSI_RESET);
        int nb = (int)points[points.length - 1].value;
        int j=0;
        for(int i=0 ; i<nb ; i++){
            sb.append(ANSI_GREEN + "*" + ANSI_RESET);
            j++;
            if (j == 100){
                sb.append("\n" + ANSI_YELLOW + "║" + ANSI_GREEN + "       ");
                j = 0;
            }
        }
        sb.append("\n"+ANSI_YELLOW+"╚");
        for(int i=0 ; i<120 ; i++) sb.append("═");
        sb.append("\n");

        System.out.println(sb.toString());
    }
}