package lib;

public class FoodpediaParseException extends Exception {
    private String line;
    private Long line_n;

    public FoodpediaParseException(String msg, String line, long line_n){
        super(msg);
        this.line = line;
        this.line_n = line_n;
    }
    public FoodpediaParseException(String msg){
        super(msg);
        this.line = null;
        this.line_n = null;
    }

    public void printToUser(){
        System.out.println(getMessage());
        System.out.println("on line ("+(line_n!=null?line_n:"?")+"): \n\t" + (line!=null?line:"?"));
    }
}
