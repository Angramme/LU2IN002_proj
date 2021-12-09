import lib.Foodpedia;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.InputMismatchException;

public class IveEaten{
    public static void main(String[] args) {
        Foodpedia fp;
        try{
            fp = new Foodpedia(new File("./data/foodpedia.txt"));
            System.out.println(fp);
            // System.out.println();

            fp.serialize(new File("./data/foodpedia.lock"));
        }catch(FileNotFoundException ex){
            System.out.println("fatal error: cannot find foodpedia database!");
        }catch(IOException ex){
            System.out.println("fatal error: io error!");
        }
        catch(InputMismatchException ex){
            System.out.println("the foodpedia file has errors!");
        }
    }
}