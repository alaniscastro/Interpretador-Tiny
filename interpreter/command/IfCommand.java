package interpreter.command;

import interpreter.expr.Expr;
import interpreter.value.Value;

public class IfCommand extends Command {

    private Expr expr;
    private Command thenCmds;
    private Command elseCmds;

    public IfCommand(int line, Expr expr, Command thenCmds) {
        super(line);
        this.expr = expr;
        this.thenCmds = thenCmds;
    }

    public void setElseCmds(Command elseCmds) {
        this.elseCmds = elseCmds;
    }

    public void setExpr(Expr expr) {
        this.expr = expr;
    }

    public void thenCmds (Command thenCmds) {
        this.thenCmds = thenCmds;
    }

    @Override
    public void execute() {
        Value<?> v;
        if (((v = expr.expr()) != null) && (v.eval()))
            thenCmds.execute();
        else if (elseCmds != null)
            elseCmds.execute();
    }
}