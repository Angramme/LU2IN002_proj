package lib;

public class SimpleFood extends BaseFood {
    protected double calories;
    protected double portion;

    protected SimpleFood(){
        this.calories = 0;
    }
    public SimpleFood(String name, double calories, double portion){
        super(name);
        this.calories = calories;
        this.portion = portion;
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
    public Double getCaloriesPortion(){
        return calories * portion / 100;
    }
    static public Double toCaloriesPortion(Food food){
        return food.getCaloriesPortion();
    }
}
