package interpreter.command;

import interpreter.expr.Expr;
import interpreter.value.NumberValue;
import interpreter.value.Value;
import interpreter.expr.Variable;

public class NumericForCommand extends Command {

    private Variable var;
    private Expr expr1;
    private Expr expr2;
    private Expr expr3;
    private Command cmds;

    public NumericForCommand (int line, Variable var, Expr expr1, Expr expr2, Expr expr3, Command cmds) {
        super(line);
        this.var = var;
        this.expr1 = expr1;
        this.expr2 = expr2;
        this.expr3 = expr3;
        this.cmds = cmds;
    }

    public NumericForCommand (int line, Variable var, Expr expr1, Expr expr2, Command cmds) {
        super(line);
        this.var = var;
        this.expr1 = expr1;
        this.expr2 = expr2;
        this.cmds = cmds;
    }
    
    // for <name> '=' <expr1> ',' <expr> [',' <expr>]  do <code> end
    @Override
    public void execute() {
        Value<?> v1 = expr1.expr();
        var.setValue(v1);

        Variable cont = var;

        while (Double.valueOf( (double) cont.expr().value()) <= (double) expr2.expr().value()){ 
            cmds.execute();
            Double v;
            if (expr3 != null)
                v = Double.valueOf( (double) cont.expr().value() + (double) expr3.expr().value() );
            else
                v = Double.valueOf( (double) cont.expr().value() + 1 );
            NumberValue vv = new NumberValue(v);
            cont.setValue(vv);
        }                
    }   
}