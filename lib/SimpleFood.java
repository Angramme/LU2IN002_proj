package lib;

public class SimpleFood extends BaseFood {
    protected double calories;

    protected SimpleFood(){
        this.calories = 0;
    }
    public SimpleFood(String name, double calories){
        super(name);
        this.calories = calories;
    }

    public String serialize(){
        StringBuilder sb = new StringBuilder();

        sb.append("!{\n");
        sb.append("\t"+getName()+"\n");
        sb.append("\tcalories: "+ Double.toString(calories) + " kcal\n");
        sb.append("}\n");
        
        return sb.toString();
    }

    public Double getCalories(){
        return calories;
    }
    static public Double toCalories(Food food){
        return food.getCalories();
    }
}
