import java.io.FileNotFoundException;
import java.util.InputMismatchException;

import lib.Foodpedia;
import lib.Food;

public class Calories {
    public static void main(String[] args) throws Exception {
        try{
            Foodpedia fp = Foodpedia.openAndSync("./data/");
            
            for(String a : args){
                Food food = fp.FindExactMatch(a);
                if(food == null){
                    System.out.println("hmmm, seems like "+a+" doesn't exist in the database. SKIPPING...");
                }else{
                    System.out.println(">>>"+food.getName());
                    System.out.println("> calories = "+food.getCalories() + " kcal / 100g");
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
