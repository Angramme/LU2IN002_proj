import java.io.FileNotFoundException;
import java.util.InputMismatchException;

import lib.Foodpedia;
import lib.SimpleFood;

public class Calories {
    public static void main(String[] args) throws Exception {
        try{
            Foodpedia fp = Foodpedia.openAndSync("./data/");
            
            for(int i=0; i<args.length; i++){
                String a = args[i];
                SimpleFood food = fp.FindExactMatch(a);
                if(food == null){
                    System.out.println("hmmm, seems like "+a+" doesn't exist in the database. SKIPPING...");
                }else{
                    System.out.println("LE "+food.getName());
                    System.out.println("--- calories : "+food.getCaloriesPortion() + " kcal");
                }
            }

        }catch(FileNotFoundException ex){
            System.out.println("fatal error: cannot find foodpedia database!");
        }
        catch(InputMismatchException ex){
            System.out.println("the foodpedia file has errors!");
        }
    }
}
