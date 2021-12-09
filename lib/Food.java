package lib;

import java.util.ArrayList;
import java.util.Scanner;
import java.lang.StringBuilder;

interface NameToFood{
    Food run(String name);
}

public class Food extends UFood {
    private ArrayList<UFood> components;
    private ArrayList<Double> quantities;
    private double calories;

    public Food(String name, double calories){
        super(name);
        this.calories = calories;
        this.components = new ArrayList<UFood>();
    }
    public Food(Scanner scan) {
        components = new ArrayList<UFood>();
        quantities = new ArrayList<Double>();

        scan.skip("\\s*!\\{\\s*\\n\\s*");
        name = scan.next("[A-Za-z]+");
        scan.skip("\\s*\\n");
        if(scan.hasNext("\\s*calories:\\s*")){
            scan.skip("\\s*calories:\\s*");
            calories = scan.nextDouble();
            scan.skip("\\s*\\n");
        }else{
            while(scan.hasNext("\\s*-")){
                scan.skip("\\s*-\\s*");
                String foodname = scan.next("[a-zA-Z]+");
                scan.skip("\\s*");
                double quantity = scan.nextDouble();
                String unit = scan.next("((mg)|(kg)|(g))");
                switch(unit){
                    case "g": break;
                    case "kg": quantity *= 1000; break;
                    case "mg": quantity *= 0.001; break;
                }
                scan.skip("\\s*\\n");

                addComponent(new UFood(foodname), quantity);
            }
        }
        scan.skip("\\s*}\\s*");
    }

    public void resolveComponents(NameToFood getFood){
        for(UFood f : components){
            if(!(f instanceof Food)){
                f = getFood.run(f.getName());
            }
        }
    }
    public void addComponent(Food food, double quantity){
        components.add(food);
        quantities.add(quantity);
        calories += food.getCalories();
    }
    public void addComponent(UFood food, double quantity){
        components.add(food);
        quantities.add(quantity);
    }

    public String serialize(){
        StringBuilder sb = new StringBuilder();

        sb.append("!{\n");
        sb.append("\t"+getName()+"\n");
        if(components.size() == 0){
            sb.append("\tcalories: "+ Double.toString(calories) + "\n");
        }else{
            for(UFood f : components){
                sb.append("\t- " + f.getName()+"\n");
            }
        }
        sb.append("}\n");
        
        return sb.toString();
    }
    public String toString(){
        return "(food "+getName()+")";
    }
    
    double getCalories(){
        return calories;
    }
}
