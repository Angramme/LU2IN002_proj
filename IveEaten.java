import lib.EatHistory;
import lib.Foodpedia;
import lib.SimpleFood;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.InputMismatchException;

import java.util.Date;
public class IveEaten{
    public static void main(String[] args) throws Exception {
        Foodpedia fp;
        try{
            fp = Foodpedia.openAndSync("./data/");

            SimpleFood repas = fp.repas(args);
            if(repas == null){
                System.out.println(String.join(" ", args)+" n'existe pas dans la base des donnes!");
                return;
            }

            EatHistory eh = new EatHistory(new File("./data/history_chunks/"));
            eh.addDataPoint((new Date()).getTime(), repas);
        }
        catch(FileNotFoundException ex){
            System.out.println("fatal error: cannot find foodpedia database!" + ex);
            ex.printStackTrace();
        }
        catch(InputMismatchException ex){
            System.out.println("the .lock file is broken!" + ex);
            ex.printStackTrace();
        }
    }
}