package interpreter.expr;

import interpreter.value.TableValue;
import interpreter.value.Value;

//acessa a tabela
public class AccessExpr extends SetExpr {
	private Expr base;
	private Expr index;

	public AccessExpr (int line, Expr base, Expr index) {
        super(line);
        this.base = base;
        this.index = index;
	}

	@Override
    public Value<?> expr() {
        TableValue table = (TableValue) base.expr();
        Value<?> ret = table.value().get(index.expr());
        return ret;        
    }

    /*A classe AccessExpr é para acessar um índice da tabela. Você não vai precisar acessar a memória, 
    já que ele já contém as expressões para a tabela (base) e o índice (index). A base tem que ser um 
    TableValue e o index tem que ser qualquer tipo diferente de null (não pode ser nil de lua). Aí é só acessar esse índice (index) na tabela (base)*/
    @Override
    public void setValue(Value<?> value) {        
        TableValue table = (TableValue)base.expr();
        if (value!=null){
            table.value().put(index.expr(),value);    
        }else{
            table.value().remove(index.expr());    
        }
    }
}
