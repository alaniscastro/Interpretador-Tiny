package interpreter.expr;

public class TableEntry {
    public Expr key;
    public Expr value;
        
    public TableEntry(){
    }    

    public TableEntry(Expr key, Expr value){
        this.value = value;
        this.key = key;
    }    
}
