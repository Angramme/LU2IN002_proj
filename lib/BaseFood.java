package lib;

// Unknown, named food
public abstract class BaseFood {
    protected String name;
    
    protected BaseFood(){
        name = "$unknown$";
    }
    public BaseFood(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public String toString(){
        return "(food "+getName()+")";
    }
}
