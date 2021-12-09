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
    // in kcal per 100g
    private double calories;
    private int unresolved;

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
            scan.skip("\\s*kcal\\s*\\n");
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

    public boolean isResolved(){
        return unresolved == 0;
    }
    public void resolveComponents(NameToFood getFood){
        for(int i=0; i<components.size(); i++){
            UFood f = components.get(i);
            if(!(f instanceof Food)){
                f = getFood.run(f.getName());
                if(((Food)f).isResolved()){
                    calories += ((Food)f).getCalories() * quantities.get(i) / 100;
                    unresolved--;
                }
            }
        }
    }
    public void addComponent(Food food, double quantity){
        components.add(food);
        quantities.add(quantity);
        calories += food.getCalories() * quantity / 100;
    }
    public void addComponent(UFood food, double quantity){
        components.add(food);
        quantities.add(quantity);
        unresolved++;
    }

    public String serialize(){
        StringBuilder sb = new StringBuilder();

        sb.append("!{\n");
        sb.append("\t"+getName()+"\n");
        if(components.size() == 0){
            sb.append("\tcalories: "+ Double.toString(calories) + " kcal\n");
        }else{
            for(int i=0; i<components.size(); i++){
                sb.append("\t- " + components.get(i).getName()+" "+quantities.get(i)+" g \n");
            }
        }
        sb.append("}\n");
        
        return sb.toString();
    }
    public String toString(){
        return "(food "+getName()+")";
    }
    
    public Double getCalories(){
        if(!isResolved()) return null;
        return calories;
    }
}
