package lib;

import java.util.HashMap;
import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;

public class Foodpedia {
    HashMap<String, SimpleFood> foods;

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

        foods = new HashMap<String, SimpleFood>();

        while(scan.hasNextLine()){
            SimpleFood food = Food.parse(scan);
            addFood(food);
            scan.skip("\\s*\\n*\\s*");
        }

        scan.close();

        resolveDependencies();
    }

    public void addFood(SimpleFood food){
        foods.put(food.getName(), food);
    }

    public SimpleFood FindExactMatch(String name){
        return foods.get(name);
    }
    // finds more or less matching results
    public SimpleFood[] FindAll(String name){
        return new SimpleFood[]{FindExactMatch(name)};
    } 

    public void resolveDependencies() throws Exception {
        boolean hasResolvedAll = false;
        int it = 0;
        while(!hasResolvedAll){
            hasResolvedAll = true;
            for(SimpleFood x : foods.values()){
                resolveFood(x); 
                hasResolvedAll = hasResolvedAll && isResolved(x);
            }
            it++;
            if(it > 100){
                throw new Exception("cannot resolve all dependencies!!! probably a circular dependency.");
            }
        }
    }

    private boolean isResolved(SimpleFood food){
        return !(food instanceof Food) || ((Food)food).isResolved();
    }
    public void resolveFood(SimpleFood food){
        if(food instanceof Food)
            ((Food)food).resolveComponents(this::FindExactMatch);
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();

        sb.append("Foodpedia [\n");
        for(SimpleFood f : foods.values()){
            sb.append("  "+ f.toString() + "\n");
        }
        sb.append("]\n");

        return sb.toString();
    }

    public void serialize(File file) throws IOException {
        FileWriter wrtr = new FileWriter(file);

        for(SimpleFood f : foods.values()){
            wrtr.write(f.serialize());
        }

        wrtr.close();
    }
}
