package lib;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.lang.StringBuilder;

public class Food extends SimpleFood {
    public static interface NameToFood{
        public SimpleFood run(String name);
    }
    private ArrayList<BaseFood> components;
    private ArrayList<Double> quantities;
    // in kcal per 100g
    private int unresolved;

    public Food(String name){
        super(name, 0, 0);
        this.components = new ArrayList<BaseFood>();
        this.quantities = new ArrayList<Double>();
    }
    public Food(String name, ArrayList<BaseFood> comps, ArrayList<Double> quants){
        super(name, 0, 0);
        components = comps;
        quantities = quants;
    }
    public static SimpleFood parse(Scanner scan) throws FoodpediaParseException {
        String name = null;
        try{
            scan.skip("\\s*!\\{\\s*\\n+\\s*");
            name = scan.next("[a-zA-Z\u00C0-\u017F]+");
            scan.skip("\\s*\\n+");
            Double portion = null;
            if(scan.hasNext("\\s*portion")){
                scan.skip("\\s*portion:\\s*");
                portion = scanWUnit(scan);
                scan.skip("\\s*\\n+");
            }
            if(scan.hasNext("\\s*calories:\\s*")){
                scan.skip("\\s*calories:\\s*");
                double calories = scan.nextDouble();
                scan.skip("\\s*kcal\\s*\\n+");
                scan.skip("\\s*}\\s*");
                return new SimpleFood(name, calories, portion==null ? 100 : portion);
            }else{
                Food ret = new Food(name);
                ret.portion = 0;
    
                while(scan.hasNext("\\s*-")){
                    scan.skip("\\s*-\\s*");
                    String foodname = scan.next("[a-zA-Z\u00C0-\u017F]+");
                    scan.skip("\\s*");
                    double quantity = scanWUnit(scan);
                    scan.skip("\\s*\\n+");

                    if(portion == null) ret.portion += quantity;
    
                    ret.addComponent(new UnresolvedFood(foodname), quantity);
                }
                scan.skip("\\s*}\\s*");

                ret.portion = portion==null ? ret.portion : portion;
                return ret;
            }
        }catch(InputMismatchException ex){
            ex.fillInStackTrace();
            throw new FoodpediaParseException("errors while parsing food"+(name!=null?("("+name+")"):"")+"! message:"+ex);
        }
    }

    private static double scanWUnit(Scanner scan){
        double quantity = scan.nextDouble();
        String unit = scan.next("((mg)|(kg)|(g))");
        switch(unit){
            case "g": break;
            case "kg": quantity *= 1000; break;
            case "mg": quantity *= 0.001; break;
        }
        return quantity;
    }

    public boolean isResolved(){
        return unresolved == 0;
    }
    public void resolveComponents(NameToFood getFood) throws FoodpediaParseException {
        if(isResolved()) return;
        for(int i=0; i<components.size(); i++){
            BaseFood f = components.get(i);
            if(f instanceof UnresolvedFood){
                String name = f.getName();
                f = getFood.run(name);
                if(f == null) throw new FoodpediaParseException(String.format("cannot resolve %s! does not exist!", name));
                if(!(f instanceof Food) || ((Food)f).isResolved()){
                    addCalories(((SimpleFood)f).getCalories(), quantities.get(i), components.size()-unresolved);
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
        sb.append("\tportion: "+Double.toString(getPortion())+" g\n");
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
