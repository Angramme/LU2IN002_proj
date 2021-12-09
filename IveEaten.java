import lib.Foodpedia;

import java.io.FileNotFoundException;
import java.util.InputMismatchException;

public class IveEaten{
    public static void main(String[] args) throws Exception {
        Foodpedia fp;
        try{
            fp = Foodpedia.openAndSync("./data/");
            System.out.println(fp);
        }catch(FileNotFoundException ex){
            System.out.println("fatal error: cannot find foodpedia database!");
        }
        catch(InputMismatchException ex){
            System.out.println("the foodpedia file has errors!");
        }
    }
}