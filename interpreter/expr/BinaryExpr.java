package interpreter.expr;

import interpreter.util.Utils;
import interpreter.value.BooleanValue;
import interpreter.value.NumberValue;
import interpreter.value.StringValue;
import interpreter.value.Value;

public class BinaryExpr extends Expr {
	private Expr left;
    private BinaryOp op;
    private Expr right;

    public BinaryExpr(int line, Expr left, BinaryOp op, Expr right) {
        super(line);
        this.left = left;
        this.op = op;
        this.right = right;
    }

    @Override
        public Value<?> expr() {
            Value<?> v1 = left.expr();
            Value<?> v2 = right.expr();
            Value<?> ret = null;

            switch (op) {
                case And://conector and
                    ret = andOp(v1,v2)  ;
                    break;
                case Or://conector or 
                    ret = orOp(v1,v2)  ;
                    break;  
                case Equal: //logical =
                    ret = EqualOp(v1,v2)  ;
                    break;  
                case NotEqual://logical ~=
                    ret = NotEqualOp(v1,v2)  ;
                    break;
                case LowerThan://logical <
                    ret = LowerThanOp(v1,v2)  ;
                    break;
                case LowerEqual://logical <=
                    ret = LowerEqualOp(v1, v2)  ;
                    break;
                case GreaterThan://logical >
                    ret = GreaterThanOp(v1, v2)  ;
                    break;               
                case GreaterEqual://logical >=
                    ret = GreaterEqualOp(v1, v2)  ;
                    break;   
                case Concat: //String ..
                    ret = ConcatOp(v1, v2);
                    break;
                case Add://Numerical +
                    ret = AddOp(v1, v2)  ;
                    break;
                case Sub://Numerical -
                    ret = SubOp(v1, v2)  ;
                    break;
                case Mul://Numerical *
                    ret = MulOp(v1, v2)  ;
                    break;
                case Div://Numerical /
                    ret = DivOp(v1, v2)  ;
                    break;    
                case Mod://Numerical %
                    ret = ModOp(v1, v2)  ;
                    break;     
                default:
            }
            return ret;
        }

    public Value<?> andOp(Value<?> v1, Value<?> v2) {
        Value<?> ret = null;
        if (v1 == null){
            ret = new BooleanValue(false);
        }
        else {
            if (v1.eval() == false){
                ret = new BooleanValue(false);
            }
            else {
                if (v2 == null){
                    ret = new BooleanValue(false);
                }
                else {
                    ret = v2;
                }
            }
        }
        return ret;
    }

    public Value<?> orOp (Value<?> v1, Value<?> v2) {
        Value<?> ret = null;
        if (v1 == null || v1.eval() == false){
            if (v2 == null || v2.eval() == false){
                ret = new BooleanValue(false);
            }
            else {
                ret = v2;
            }
        }
        else {
            ret = v1;
        }
        return ret;
    }
        
    public Value<?> EqualOp(Value<?> v1,Value<?> v2) {
        Value<?> ret = null;
        //realiza o equal
        if ( ( (v1 instanceof StringValue) && (v2 instanceof StringValue) ) ) {
            ret = new BooleanValue( v1.equals(v2) );
        } else if ( ( (v1 instanceof NumberValue) && (v2 instanceof NumberValue) ) ){
            String sv1 = v1.toString();
            String sv2 = v2.toString();
            ret = new BooleanValue( sv1.equals(sv2) );
        } else if ( ( (v1 instanceof NumberValue) && (v2 instanceof StringValue) ) ){
            String sv1 = v1.toString();
            String sv2 = v2.toString();
            ret = new BooleanValue( sv1.equals(sv2) );
        } else if ( ( (v1 instanceof StringValue) && (v2 instanceof NumberValue) ) ){
            String sv1 = v1.toString();
            String sv2 = v2.toString();
            ret = new BooleanValue( sv1.equals(sv2) );
        } else {
            Utils.abort(super.getLine());
        }
        return ret;
    }

    public Value<?> NotEqualOp(Value<?> v1,Value<?> v2) {
        Value<?> ret = null;
        if ( ( (v1 instanceof StringValue) && (v2 instanceof StringValue) ) ) {
            ret = new BooleanValue( ! v1.equals(v2) );
        } else if ( ( (v1 instanceof NumberValue) && (v2 instanceof NumberValue) ) ){
            String sv1 = v1.toString();
            String sv2 = v2.toString();
            ret = new BooleanValue( ! sv1.equals(sv2) );
        } else if ( ( (v1 instanceof NumberValue) && (v2 instanceof StringValue) ) ){
            String sv1 = v1.toString();
            String sv2 = v2.toString();
            ret = new BooleanValue( ! sv1.equals(sv2) );
        } else if ( ( (v1 instanceof StringValue) && (v2 instanceof NumberValue) ) ){
            String sv1 = v1.toString();
            String sv2 = v2.toString();
            ret = new BooleanValue( ! sv1.equals(sv2) );
        } else {
            Utils.abort(super.getLine());
        }
        return ret;
    }

    public Value<?> LowerThanOp(Value<?> v1,Value<?> v2) {
        Value<?> ret = null;
        if ( ( (v1 instanceof StringValue) && (v2 instanceof StringValue) ) ) {            
            Double dv1 = Double.valueOf(v1.toString());
            Double dv2 = Double.valueOf(v2.toString());
            ret = new BooleanValue(Double.compare(dv1, dv2)==-1); 
        } else if ( ( (v1 instanceof NumberValue) && (v2 instanceof NumberValue) ) ){
            Double dv1 = Double.valueOf(v1.toString());
            Double dv2 = Double.valueOf(v2.toString());
            ret = new BooleanValue(Double.compare(dv1, dv2)==-1); 
        } else if ( ( (v1 instanceof NumberValue) && (v2 instanceof StringValue) ) ){
            Double dv1 = Double.valueOf(v1.toString());
            Double dv2 = Double.valueOf(v2.toString());
            ret = new BooleanValue(Double.compare(dv1, dv2)==-1); 
        } else if ( ( (v1 instanceof StringValue) && (v2 instanceof NumberValue) ) ){
            Double dv1 = Double.valueOf(v1.toString());
            Double dv2 = Double.valueOf(v2.toString());
            ret = new BooleanValue(Double.compare(dv1, dv2)==-1); 
        } else {
            Utils.abort(super.getLine());
        }
        return ret;
    }

    public Value<?> LowerEqualOp(Value<?> v1,Value<?> v2) {
        Value<?> ret = null;
        if ( ( (v1 instanceof StringValue) && (v2 instanceof StringValue) ) ) {            
            Double dv1 = Double.valueOf(v1.toString());
            Double dv2 = Double.valueOf(v2.toString());
            ret = new BooleanValue(dv1 <= dv2);
        } else if ( ( (v1 instanceof NumberValue) && (v2 instanceof NumberValue) ) ){
            Double dv1 = Double.valueOf(v1.toString());
            Double dv2 = Double.valueOf(v2.toString());
            ret = new BooleanValue(dv1 <= dv2); 
        } else if ( ( (v1 instanceof NumberValue) && (v2 instanceof StringValue) ) ){
            Double dv1 = Double.valueOf(v1.toString());
            Double dv2 = Double.valueOf(v2.toString());
            ret = new BooleanValue(dv1 <= dv2);
        } else if ( ( (v1 instanceof StringValue) && (v2 instanceof NumberValue) ) ){
            Double dv1 = Double.valueOf(v1.toString());
            Double dv2 = Double.valueOf(v2.toString());
            ret = new BooleanValue(dv1 <= dv2);
        } else {
            Utils.abort(super.getLine());
        }
        return ret;
    }

    public Value<?> GreaterThanOp(Value<?> v1,Value<?> v2) {
        Value<?> ret = null;
        if ( ( (v1 instanceof StringValue) && (v2 instanceof StringValue) ) ) {            
            Double dv1 = Double.valueOf(v1.toString());
            Double dv2 = Double.valueOf(v2.toString());
            ret = new BooleanValue(dv1 > dv2);
        } else if ( ( (v1 instanceof NumberValue) && (v2 instanceof NumberValue) ) ){
            Double dv1 = Double.valueOf(v1.toString());
            Double dv2 = Double.valueOf(v2.toString());
            ret = new BooleanValue(dv1 > dv2);
        } else if ( ( (v1 instanceof NumberValue) && (v2 instanceof StringValue) ) ){
            Double dv1 = Double.valueOf(v1.toString());
            Double dv2 = Double.valueOf(v2.toString());
            ret = new BooleanValue(dv1 > dv2);
        } else if ( ( (v1 instanceof StringValue) && (v2 instanceof NumberValue) ) ){
            Double dv1 = Double.valueOf(v1.toString());
            Double dv2 = Double.valueOf(v2.toString());
            ret = new BooleanValue(dv1 > dv2);
        } else {
            Utils.abort(super.getLine());
        }
        return ret;
    }

    public Value<?> GreaterEqualOp(Value<?> v1,Value<?> v2) {
        Value<?> ret = null;
        if ( ( (v1 instanceof StringValue) && (v2 instanceof StringValue) ) ) {            
            Double dv1 = Double.valueOf(v1.toString());
            Double dv2 = Double.valueOf(v2.toString());
            ret = new BooleanValue(dv1 >= dv2);
        } else if ( ( (v1 instanceof NumberValue) && (v2 instanceof NumberValue) ) ){
            Double dv1 = Double.valueOf(v1.toString());
            Double dv2 = Double.valueOf(v2.toString());
            ret = new BooleanValue(dv1 >= dv2);
        } else if ( ( (v1 instanceof NumberValue) && (v2 instanceof StringValue) ) ){
            Double dv1 = Double.valueOf(v1.toString());
            Double dv2 = Double.valueOf(v2.toString());
            ret = new BooleanValue(dv1 >= dv2);
        } else if ( ( (v1 instanceof StringValue) && (v2 instanceof NumberValue) ) ){
            Double dv1 = Double.valueOf(v1.toString());
            Double dv2 = Double.valueOf(v2.toString());
            ret = new BooleanValue(dv1 >= dv2);
        } else {
            Utils.abort(super.getLine());
        }
        return ret;
    }

    public Value<?> ConcatOp(Value<?> v1,Value<?> v2) {
        Value<?> ret = null;
        if ( ( (v1 instanceof StringValue) && (v2 instanceof StringValue) ) ) {
           String sv1 = String.valueOf(v1.toString());
           String sv2 = String.valueOf(v2.toString());
           ret = new StringValue(sv1+sv2);
                
        } else if ( ( (v1 instanceof NumberValue) && (v2 instanceof NumberValue) ) ) {
            String sv1 = String.valueOf(v1.toString());
            String sv2 = String.valueOf(v2.toString());
            ret = new StringValue(sv1+sv2);
                 
        } else if ( ( (v1 instanceof NumberValue) && (v2 instanceof StringValue) ) ) {
            String sv1 = String.valueOf(v1.toString());
            String sv2 = String.valueOf(v2.toString());
            ret = new StringValue(sv1+sv2);
                 
        } else if ( ( (v1 instanceof StringValue) && (v2 instanceof NumberValue) ) ) {
            String sv1 = String.valueOf(v1.toString());
            String sv2 = String.valueOf(v2.toString());
            ret = new StringValue(sv1+sv2);
                 
        } else {
            Utils.abort(super.getLine());
        }
        return ret;
    }

    public Value<?> AddOp(Value<?> v1,Value<?> v2) {
        Value<?> ret = null;

        if (v1 instanceof NumberValue && v2 instanceof NumberValue) {
            Double d1 = Double.parseDouble(v1.toString());
            Double d2 = Double.parseDouble(v2.toString());
            ret = new NumberValue(d1 + d2);
        } else {
            Utils.abort(super.getLine());
        }
        return ret;
    }

    public Value<?> SubOp(Value<?> v1,Value<?> v2) {
        Value<?> ret = null;

        if (v1 instanceof NumberValue && v2 instanceof NumberValue) {
            Double d1 = Double.parseDouble(v1.toString());
            Double d2 = Double.parseDouble(v2.toString());
            ret = new NumberValue(d1 - d2);
        } else {
            Utils.abort(super.getLine());
        }
        return ret;
    }

    public Value<?> MulOp(Value<?> v1,Value<?> v2) {
        Value<?> ret = null;
        if (v1 instanceof NumberValue && v2 instanceof NumberValue) {
            Double d1 = Double.parseDouble(v1.toString());
            Double d2 = Double.parseDouble(v2.toString());
            ret = new NumberValue(d1 * d2);
        } else {
            Utils.abort(super.getLine());
        }
        return ret;
    }

    public Value<?> DivOp(Value<?> v1,Value<?> v2) {
        Value<?> ret = null;

        if (v1 instanceof NumberValue && v2 instanceof NumberValue) {
            Double d1 = Double.parseDouble(v1.toString());
            Double d2 = Double.parseDouble(v2.toString());
            ret = new NumberValue(d1 / d2);
        } else {
            Utils.abort(super.getLine());
        }
        return ret;
    }

    public Value<?> ModOp(Value<?> v1,Value<?> v2) {
        Value<?> ret = null;
        if (v1 instanceof NumberValue && v2 instanceof NumberValue) {
            Double d1 = Double.parseDouble(v1.toString());
            Double d2 = Double.parseDouble(v2.toString());
            ret = new NumberValue(d1 % d2);
        } else {
            Utils.abort(super.getLine());
        }
        return ret;
    }
}
