package interpreter.command;
import interpreter.expr.Expr;
import interpreter.value.TableValue;
import interpreter.value.Value;
import interpreter.expr.Variable;
import java.util.Map;

public class GenericForCommand extends Command {

    private Variable var1;
    private Variable var2;
    private Expr expr;
    private Command cmds;

    public GenericForCommand (int line, Variable var1, Variable var2, Expr expr, Command cmds) {
        super(line);
        this.var1 = var1;
        this.var2 = var2;
        this.expr = expr;
        this.cmds = cmds;
    }

    public GenericForCommand (int line, Variable var1, Expr expr, Command cmds) {
        super(line);
        this.var1 = var1;
        this.expr = expr;
        this.cmds = cmds;
    }

    // for <name> [',' <name>] in <expr> do <code> end
    @Override
    public void execute() {
        TableValue tv = (TableValue) expr.expr();
        
        for (Map.Entry<Value<?>, Value<?>> e : tv.value().entrySet()) {
            Value<?> k = e.getKey();
            var1.setValue(k);
            if (var2!=null)
                var2.setValue(e.getValue());
            cmds.execute();
        }
    }   
}