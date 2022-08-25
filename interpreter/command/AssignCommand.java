package interpreter.command;

import java.util.Vector;

import interpreter.expr.Expr;
import interpreter.expr.SetExpr;
import interpreter.value.Value;

public class AssignCommand extends Command {

    private Vector<SetExpr> lhs;
    private Vector<Expr> rhs;

    public AssignCommand(int line, Vector<SetExpr> lhs, Vector<Expr> rhs) {
        super(line);
        this.lhs = lhs;
        this.rhs = rhs;
    }

    @Override
    public void execute() {
        if ( lhs.size() >= rhs.size() ){
            for (int i = 0; i < rhs.size(); i++) {
                Expr right = rhs.get(i);
                Value<?> v = right.expr();
            
                SetExpr left = lhs.get(i);
                left.setValue(v);
            }
            for (int j = rhs.size(); j < lhs.size(); j++) {
                Value<?> v = null;
                SetExpr left = lhs.get(j);
                left.setValue(v);
            }
        } else {
            for (int i = 0; i < lhs.size(); i++) {
                Expr right = rhs.get(i);
                Value<?> v = right.expr();
            
                SetExpr left = lhs.get(i);
                left.setValue(v);
            }
        }
    }
}
