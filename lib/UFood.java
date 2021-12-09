package lib;

public class UFood {
    protected String name;
    
    protected UFood(){
        name = "$unknown$";
    }
    public UFood(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }
}
