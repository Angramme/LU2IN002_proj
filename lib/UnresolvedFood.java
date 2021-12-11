package lib;

public class UnresolvedFood extends BaseFood {
    public UnresolvedFood(String name){
        super(name);
    }

    public String toString(){
        return "(UNRESOLVED food "+getName()+")";
    }
}
