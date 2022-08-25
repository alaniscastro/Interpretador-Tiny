package interpreter.expr;

import interpreter.util.Utils;
import interpreter.value.BooleanValue;
import interpreter.value.NumberValue;
import interpreter.value.StringValue;
import interpreter.value.TableValue;
import interpreter.value.Value;
import java.util.Scanner;

public class UnaryExpr extends Expr {
    
    private Expr expr;
    private UnaryOp op;

    public UnaryExpr(int line, Expr expr, UnaryOp op) {
        super(line);
        this.expr = expr;
        this.op = op;
    }

    @Override
    public Value<?> expr() {
        Value<?> ret = null;
        Value<?> v = null;

        if (expr != null)
            v = expr.expr();

        switch (op) {
            case Neg:
                ret = negOp(v);
                break;
            case Size:
                ret = sizeOp(v);
                break;
            case Not:
                ret = notOp(v);
                break;
            case Read:
                ret = readOp(v);
                break;
            case ToNumber:
                ret = toNumberOp(v);
                break;
            case ToString:
                ret = toStringOp(v);
                break;
            default:
                Utils.abort(super.getLine());
        }
        return ret;
    }

    public Value<?> negOp(Value<?> v) {
        Value<?> ret = null;
        if (v instanceof NumberValue) {
            NumberValue nv = (NumberValue) v;
            Double d = -nv.value();
            ret = new NumberValue(d);

        } else if (v instanceof StringValue) {
            StringValue sv = (StringValue) v;
            String s = sv.value();

            try {
                Double d = -Double.valueOf(s);
                ret = new NumberValue(d);
            } catch (Exception e) {
                Utils.abort(super.getLine());
            }
        } else {
            Utils.abort(super.getLine());
        }
        return ret;
    }

    //arrumei 1 vez
    public Value<?> sizeOp(Value<?> v) {
        Value<?> ret = null;
        if (v instanceof TableValue) {
            TableValue tv = (TableValue) v;
            int aux = tv.value().size();
            NumberValue d = new NumberValue(Double.valueOf(aux));            
            ret = d;

        } else {
            Utils.abort(super.getLine());
        }
        return ret;
    }

    public Value<?> notOp(Value<?> v) {
        Value<?> ret = null;
        if (v instanceof BooleanValue) {
            BooleanValue bv = (BooleanValue) v;
            Boolean b = !(bv.value());
            ret = new BooleanValue(b);

        } else {
            Utils.abort(super.getLine());
        }
        return ret;
    }

    //string e number
    public Value<?> readOp(Value<?> v) {
        Value<?> ret = null;
        if ((v instanceof StringValue)){
            System.out.print(v);
        }
        Scanner scan = new Scanner(System.in);
        try {
            Object obj = scan.nextLine();  
            try {
                Double d = Double.parseDouble(obj.toString());           
                ret = new NumberValue(d);
            } catch (NumberFormatException nfe) {
                ret = new StringValue(obj.toString());
            }
        } catch (Exception e) {
            Utils.abort(super.getLine());
        }
        return ret;
    }

    public Value<?> toNumberOp(Value<?> v) { 
        Value<?> ret = null;
        if (v instanceof NumberValue) {
            ret = v;

        } else if (v instanceof StringValue) {
            try {
                StringValue sv = (StringValue) v;
                String s = sv.value();
                Double d = Double.valueOf(s);            
                ret = new NumberValue(d);
            } catch (NumberFormatException nfe) {
                ret = null;
            }
        } else {
            ret = null;
        }
        return ret;
    }

    public Value<?> toStringOp(Value<?> v) { 
       Value<?> ret = null;
        if (v instanceof StringValue) {
            StringValue sv = (StringValue) v;
            String s = sv.toString();         
            ret = new StringValue(s);

        } else if (v instanceof NumberValue) {
            NumberValue nv = (NumberValue) v;
            String s = nv.toString();      
            ret = new StringValue(s);

        } else {
            ret = null;
        }
        return ret;
    }
}