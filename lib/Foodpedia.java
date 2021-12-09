package lib;

import java.util.HashMap;
import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;

public class Foodpedia {
    HashMap<String, Food> foods;

    public Foodpedia(File file) throws FileNotFoundException {
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

    Food FindBestMatch(String name){
        return foods.get(name);
    }
    // finds more or less matching results
    Food[] FindAll(String name){
        return new Food[]{FindBestMatch(name)};
    } 

    public void resolveDependencies(){
        for(Food x : foods.values()){
            x.resolveComponents(this::FindBestMatch);
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
