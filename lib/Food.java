package lib;

import java.util.ArrayList;
import java.util.Scanner;
import java.lang.StringBuilder;

interface NameToFood{
    SimpleFood run(String name);
}

public class Food extends SimpleFood {
    private ArrayList<BaseFood> components;
    private ArrayList<Double> quantities;
    // in kcal per 100g
    private int unresolved;

    public Food(String name){
        super(name, 0);
        this.components = new ArrayList<BaseFood>();
        this.quantities = new ArrayList<Double>();
    }
    public Food(String name, ArrayList<BaseFood> comps, ArrayList<Double> quants){
        super(name, 0);
        components = comps;
        quantities = quants;
    }
    static public SimpleFood parse(Scanner scan) {
        scan.skip("\\s*!\\{\\s*\\n\\s*");
        String name = scan.next("[A-Za-z]+");
        scan.skip("\\s*\\n");
        if(scan.hasNext("\\s*calories:\\s*")){
            scan.skip("\\s*calories:\\s*");
            double calories = scan.nextDouble();
            scan.skip("\\s*kcal\\s*\\n");
            scan.skip("\\s*}\\s*");
            return new SimpleFood(name, calories);
        }else{
            Food ret = new Food(name);

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

                ret.addComponent(new UnresolvedFood(foodname), quantity);
            }
            scan.skip("\\s*}\\s*");
            return ret;
        }
    }

    public boolean isResolved(){
        return unresolved == 0;
    }
    public void resolveComponents(NameToFood getFood){
        if(isResolved()) return;
        for(int i=0; i<components.size(); i++){
            BaseFood f = components.get(i);
            if(f instanceof UnresolvedFood){
                f = getFood.run(f.getName());
                if(((Food)f).isResolved()){
                    addCalories(((Food)f).getCalories(), quantities.get(i), components.size()-unresolved);
                    unresolved--;
                }
            }
        }
    }

    private void addCalories(double kcal, double Q, int total){
        calories = (calories * total + (kcal * Q / 100)) / (total + 1);
    }
    public void addComponent(Food food, double quantity){
        components.add(food);
        quantities.add(quantity);
        addCalories(food.getCalories(), quantity, components.size()-1);
    }
    public void addComponent(UnresolvedFood food, double quantity){
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

    @Override
    public Double getCalories(){
        if(!isResolved()) return null;
        return calories;
    }
}
