package lib;

import java.util.HashMap;
import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;

public class Foodpedia {
    HashMap<String, Food> foods;

    static public Foodpedia openAndSync(String path) throws FileNotFoundException, Exception {
        File text = new File(path + "/foodpedia.txt");
        File lock = new File(path + "/foodpedia.lock");

        try{
            Foodpedia fp = new Foodpedia(text);
            fp.serialize(lock);
            return fp;
        }catch(Exception ex){
            System.out.println("couldn't open foodpedia.txt, using .lock fallback ! : " + ex);
            return new Foodpedia(lock);
        }
    }

    public Foodpedia(File file) throws FileNotFoundException, Exception {
        Scanner scan = new Scanner(file);

        foods = new HashMap<String, Food>();

        while(scan.hasNextLine()){
            Food food = new Food(scan);
            foods.put(food.getName(), food);
            scan.skip("\\s*\\n*\\s*");
        }

        scan.close();

        resolveDependencies();
    }

    public Food FindExactMatch(String name){
        return foods.get(name);
    }
    // finds more or less matching results
    public Food[] FindAll(String name){
        return new Food[]{FindExactMatch(name)};
    } 

    public void resolveDependencies() throws Exception {
        boolean hasResolvedAll = false;
        int it = 0;
        while(!hasResolvedAll){
            hasResolvedAll = true;
            for(Food x : foods.values()){
                x.resolveComponents(this::FindExactMatch);
                hasResolvedAll = hasResolvedAll && x.isResolved();
            }
            it++;
            if(it > 100){
                throw new Exception("cannot resolve all dependencies!!! probably a circular dependency.");
            }
        }
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();

        sb.append("Foodpedia [\n");
        for(Food f : foods.values()){
            sb.append("  "+ f.toString() + "\n");
        }
        sb.append("]\n");

        return sb.toString();
    }

    public void serialize(File file) throws IOException {
        FileWriter wrtr = new FileWriter(file);

        for(Food f : foods.values()){
            wrtr.write(f.serialize());
        }

        wrtr.close();
    }

    // - addFood(Food food)
    // - String serialize() // serializes to string
    // - void serialize(String filepath) // serializes to file
}
