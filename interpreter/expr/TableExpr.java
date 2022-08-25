package interpreter.expr;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import interpreter.value.TableValue;
import interpreter.value.Value;
import interpreter.value.NumberValue;

public class TableExpr extends Expr {

    List <TableEntry> table = new ArrayList<TableEntry>(); //table = lista de table entries

    public TableExpr(int line) {
        super(line);  
    }
    public void addEntry (Expr key, Expr value) {
    //adicionar TableEntry na List
        table.add(new TableEntry(key,value));
    }
    @Override
    public Value<?> expr() {
        //criar LinkedHashMap e retornar como se fosse table value
        Map<Value<?>,Value<?>> map = new LinkedHashMap<Value<?>,Value<?>>();
        
        int seq=1;
        for(TableEntry te:table){
            Expr key = te.key;
            Expr value = te.value;
            if(te.key == null){
                map.put(new NumberValue(Double.valueOf(seq)),value.expr());
                seq++;
            }else{
                     
                map.put(key.expr(),value.expr());
            }
        }
        
        TableValue tv = new TableValue(map);

       return tv;
    }   
}