package syntatic;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
 
import interpreter.command.AssignCommand;
import interpreter.command.BlocksCommand;
import interpreter.command.Command;
import interpreter.command.PrintCommand;
import interpreter.command.WhileCommand;
import interpreter.command.RepeatCommand;
import interpreter.command.IfCommand;
import interpreter.command.GenericForCommand;
import interpreter.command.NumericForCommand;
import interpreter.expr.AccessExpr;
import interpreter.expr.BinaryExpr;
import interpreter.expr.BinaryOp;
import interpreter.expr.ConstExpr;
import interpreter.expr.Expr;
import interpreter.expr.SetExpr;
import interpreter.expr.TableEntry;
import interpreter.expr.TableExpr;
import interpreter.expr.UnaryOp;
import interpreter.expr.UnaryExpr;
import interpreter.expr.Variable;
import interpreter.value.BooleanValue;
import interpreter.value.NumberValue;
import interpreter.value.StringValue;
import interpreter.value.Value;
import lexical.Lexeme;
import lexical.LexicalAnalysis;
import lexical.TokenType;

public class SyntaticAnalysis {

    private LexicalAnalysis lex;
    private Lexeme current;

    public SyntaticAnalysis(LexicalAnalysis lex) {
        this.lex = lex;
        this.current = lex.nextToken();
    }

    public Command start() {
        BlocksCommand bc = procCode();
        eat(TokenType.END_OF_FILE);
        return bc;
    }

    private void advance() {
        //System.out.println("Advanced (\"" + current.token + "\", " + current.type + ")"); //Comentar dps
        current = lex.nextToken();
    }

    private void eat(TokenType type) {
        //System.out.println("Expected (..., " + type + "), found (\"" + current.token + "\", " + current.type + ")"); //Comentar dps
        if (type == current.type) {
            current = lex.nextToken();
        } else {
            showError();
        }
    }

    private void showError() {
        //System.out.printf("%02d: ", lex.getLine());
        switch (current.type) {
            case INVALID_TOKEN:
                System.out.printf("Lexema inválido [%s]\n", current.token);
                break;
            case UNEXPECTED_EOF:
            case END_OF_FILE:
                System.out.printf("Fim de arquivo inesperado\n");
                break;
            default:
                System.out.printf("Lexema não esperado [%s]\n", current.token);
                break;
        }
        System.exit(1);
    }

    // <code> ::= { <cmd> }
    private BlocksCommand procCode() {
        int line = lex.getLine();
        List<Command> cmds = new ArrayList<Command>();
        while (current.type == TokenType.IF || current.type == TokenType.WHILE || current.type == TokenType.REPEAT ||
               current.type == TokenType.FOR || current.type == TokenType.PRINT || current.type == TokenType.ID) {
            Command cmd = procCmd();
            cmds.add(cmd);
        }
        BlocksCommand bc = new BlocksCommand(line, cmds);
        return bc;
    }

    // <cmd> ::= (<if> | <while> | <repeat> | <for> | <print> | <assign>) [';']
    private Command procCmd() {
        Command cmd = null;
        if (current.type == TokenType.IF) {
            cmd  = procIf();
        } else if (current.type == TokenType.WHILE) {
            cmd = procWhile();
        } else if (current.type == TokenType.REPEAT) {
            cmd = procRepeat();
        } else if (current.type == TokenType.FOR) {
            cmd  = procFor();
        } else if (current.type == TokenType.PRINT) {
            cmd = procPrint();
        } else if (current.type == TokenType.ID) {
            cmd = procAssign();
        } else {
            showError();
        }

        if (current.type == TokenType.SEMI_COLON)
            advance();

        return cmd;
    }

    // <if> ::= if <expr> then <code> { elseif <expr> then <code> } [ else <code> ] end
    private IfCommand procIf() {
        eat(TokenType.IF);
        int line = lex.getLine();
        Expr expr = procExpr();
        eat(TokenType.THEN);
        Command thenCmds = procCode();
        IfCommand ic =  new IfCommand(line, expr, thenCmds);
        ArrayList <IfCommand>  ics = new ArrayList<>();
        ics.add(ic);

        int i = 0;
        while (current.type == TokenType.ELSEIF){
            advance();
            line = lex.getLine();
            Expr expr2 = procExpr();
            eat(TokenType.THEN);
            thenCmds = procCmd();
            IfCommand eic = new IfCommand(line, expr2, thenCmds);
            ics.get(i).setElseCmds(eic);
            ics.add(eic);
            i++;
        }

        if (current.type == TokenType.ELSE) {
            advance();
            Command elseCmds = procCode();
            ics.get(i).setElseCmds(elseCmds);

        }
        eat(TokenType.END);
        return ic;
    }

    // <while> ::= while <expr> do <code> end
    private WhileCommand procWhile() {
        eat(TokenType.WHILE);
        int line = lex.getLine();
        Expr expr = procExpr();

        eat(TokenType.DO);
        Command cmd = procCode();

        eat(TokenType.END);

        WhileCommand wc = new WhileCommand(line, expr, cmd);
        return wc;
    }

    // <repeat> ::= repeat <code> until <expr>
    private RepeatCommand procRepeat() {
        eat(TokenType.REPEAT);
        int line = lex.getLine();
        Command cmd = null;
        while (current.type != TokenType.UNTIL)
            cmd = procCode();
        eat(TokenType.UNTIL);
        Expr expr = procExpr();

        RepeatCommand rc = new RepeatCommand(line, expr, cmd);
        return rc;
    }

    // <for> ::= for <name> (('=' <expr> ',' <expr> [',' <expr>]) | ([',' <name>] in <expr>)) do <code> end
    private Command procFor() {
        eat(TokenType.FOR);
      	int line = lex.getLine();
      	Variable var1 = new Variable(line, current.token);
        advance();
        Variable var2 = null;
        Expr expr1 = null;
        Expr expr2 = null;
        Expr expr3 = null;
        Command fc = null;

        if (current.type == TokenType.ASSIGN) {
            advance();
            expr1 = procExpr();
            eat(TokenType.COLON);
            expr2 = procExpr();
            if (current.type == TokenType.COLON) {
                advance();
                expr3 = procExpr();
            }
            eat(TokenType.DO);
            Command cmds = procCode();
            fc = new NumericForCommand(line, var1, expr1, expr2, expr3, cmds);

        } else if (current.type == TokenType.IN || current.type == TokenType.COLON) {
            if(current.type == TokenType.COLON){
                advance();
                line = lex.getLine();
                var2 = new Variable(line, current.token);
                procName();
            }
            advance();
            expr1 = procExpr();
            eat(TokenType.DO);
            Command cmds = procCode();
            fc = new GenericForCommand(line, var1, var2, expr1, cmds);
        }
        eat(TokenType.END);
      	return fc;
    }

    // <print> ::= print '(' [ <expr> ] ')'
    private PrintCommand procPrint() {
        eat(TokenType.PRINT);
        eat(TokenType.OPEN_PAR);
        Expr expr = null;
        if (current.type == TokenType.OPEN_PAR || current.type == TokenType.SUB || current.type == TokenType.SIZE ||
            current.type == TokenType.NOT || current.type == TokenType.NUMBER || current.type == TokenType.STRING ||
            current.type == TokenType.FALSE || current.type == TokenType.TRUE || current.type == TokenType.NIL ||
            current.type == TokenType.READ || current.type == TokenType.TONUMBER || current.type == TokenType.TOSTRING ||
            current.type == TokenType.OPEN_CUR || current.type == TokenType.ID) {
            expr = procExpr();
        }
        int line = lex.getLine();
        eat(TokenType.CLOSE_PAR);
        PrintCommand pc = new PrintCommand(line, expr);
        return pc;
    }

    // <assign> ::= <lvalue> { ',' <lvalue> } '=' <expr> { ',' <expr> }
    private AssignCommand procAssign() {
        Vector<SetExpr> lhs = new Vector<SetExpr>();
        Vector<Expr> rhs = new Vector<Expr>();

        lhs.add(procLValue());
        while (current.type == TokenType.COLON) {
            advance();
            lhs.add(procLValue());
        }
        eat(TokenType.ASSIGN);
        int line = lex.getLine();

        rhs.add(procExpr());
        while (current.type == TokenType.COLON) {
            advance();
            rhs.add(procExpr());
        }

        AssignCommand ac = new AssignCommand(line, lhs, rhs);
        return ac;
    }

    // <expr> ::= <rel> { (and | or) <rel> } 
    private Expr procExpr() {
        Expr expr1 = procRel();
        Expr expr2 = null;
        BinaryOp op = null;
        int line = lex.getLine();
        while (current.type == TokenType.AND || current.type == TokenType.OR) {
            if (current.type == TokenType.AND)
                op = BinaryOp.And;
            else 
                op = BinaryOp.Or;

            advance();
            expr2 = procRel();
            Expr expr = new BinaryExpr(line, expr1, op, expr2);
            expr1 = expr;
        }
        return expr1; 
    }

    // <rel> ::= <concat> [ ('<' | '>' | '<=' | '>=' | '~=' | '==') <concat> ]
    private Expr procRel() {
        Expr expr1 = procConcat();
        BinaryOp op = null;
        int line = lex.getLine();

        if (current.type == TokenType.EQUAL || current.type == TokenType.NOT_EQUAL || current.type == TokenType.LOWER_THAN || 
        current.type == TokenType.LOWER_EQUAL || current.type == TokenType.GREATER_THAN || current.type == TokenType.GREATER_EQUAL){
            if (current.type == TokenType.EQUAL){
                op = BinaryOp.Equal;
            } else if (current.type == TokenType.NOT_EQUAL){
                op = BinaryOp.NotEqual;
            } else if (current.type == TokenType.LOWER_THAN){
                op = BinaryOp.LowerThan;
            } else if (current.type == TokenType.LOWER_EQUAL){
                op = BinaryOp.LowerEqual;
            } else if (current.type == TokenType.GREATER_THAN){
                op = BinaryOp.GreaterThan;
            } else {
                op = BinaryOp.GreaterEqual;
            }
            advance();
            Expr expr2 = procConcat();
            Expr expr = new BinaryExpr(line, expr1, op, expr2);
            return expr;
        }
        return expr1;
    }

    // <concat> ::= <arith> { '..' <arith> }
    private Expr procConcat() {

        Expr expr1 = procArith();
        BinaryOp op = null;
        int line = lex.getLine();
        while (current.type == TokenType.CONCAT){
            advance();
            op = BinaryOp.Concat;
            Expr expr2 = procArith();
            Expr expr = new BinaryExpr(line,expr1, op, expr2);
            if (current.type != TokenType.CONCAT)
                return expr;
            expr1 = expr;   //atualiza a expressao 1 com o resultado da anterior
        }
        return expr1;
    }

    // <arith> ::= <term> { ('+' | '-') <term> }
    private Expr procArith() {
        Expr expr1 = procTerm();
        BinaryOp op = null;
        int line = lex.getLine();

        while (current.type == TokenType.ADD || current.type == TokenType.SUB){
            if (current.type == TokenType.ADD){
                advance();
                op = BinaryOp.Add;
            } else {
                advance();
                op = BinaryOp.Sub;
            }
            Expr expr2 = procTerm();
            Expr expr = new BinaryExpr(line, expr1, op, expr2);
            if (current.type != TokenType.ADD && current.type != TokenType.SUB)
                return expr;
            expr1 = expr;
        }
        return expr1;
    }

    // <term> ::= <factor> { ('*' | '/' | '%') <factor> }
    private Expr procTerm() {
        Expr expr1 = procFactor();
        BinaryOp op = null;
        int line = lex.getLine();

        while (current.type == TokenType.MUL || current.type == TokenType.DIV || current.type == TokenType.MOD){
            if (current.type == TokenType.MUL) {
                op = BinaryOp.Mul;
            } else if (current.type == TokenType.DIV) {
                op = BinaryOp.Div;
            } else {
                op = BinaryOp.Mod;
            }
            advance();
            Expr expr2 = procFactor();
            Expr expr = new BinaryExpr(line, expr1, op, expr2);
            if (current.type != TokenType.MUL && current.type != TokenType.DIV && current.type != TokenType.MOD)
                return expr;
            expr1 = expr;
        }
        return expr1;
    }

    // <factor> ::= '(' <expr> ')' | [ '-' | '#' | not ] <rvalue>
    private Expr procFactor() {
        Expr expr = null;
        if (current.type == TokenType.OPEN_PAR) {
            advance();
            expr = procExpr();
            eat(TokenType.CLOSE_PAR);
        } else {
            UnaryOp op = null;
            if (current.type == TokenType.SUB) {
                advance();
                op = UnaryOp.Neg;
            } else if (current.type == TokenType.SIZE) {
                advance();
                op = UnaryOp.Size;
            } else if (current.type == TokenType.NOT) {
                advance();
                op = UnaryOp.Not;
            }
            int line = lex.getLine();
            expr = procRValue();

            if (op != null)
                expr = new UnaryExpr(line, expr, op);
        }

        return expr;
    }

    // <lvalue> ::= <name> { '.' <name> | '[' <expr> ']' }
    private SetExpr procLValue() {
        int line = lex.getLine();
        SetExpr expr = procName();
        while (current.type == TokenType.DOT || current.type == TokenType.OPEN_BRA) {
            if (current.type == TokenType.DOT) {
                advance();
                Variable index = procName();
                index.setValue(new StringValue(index.getName()));
                AccessExpr a = new AccessExpr(line, expr, index);
                expr = (AccessExpr) a;

            } else {
                eat(TokenType.OPEN_BRA);
                AccessExpr a = new AccessExpr(line, expr, procExpr());
                expr = (AccessExpr) a;
                eat(TokenType.CLOSE_BRA);
            }
        }
        return expr;
    }

    // <rvalue> ::= <const> | <function> | <table> | <lvalue>
    private Expr procRValue() {
        Expr expr = null;
        if (current.type == TokenType.NUMBER || current.type == TokenType.STRING || current.type == TokenType.FALSE ||
            current.type == TokenType.TRUE || current.type == TokenType.NIL) {
            Value<?> v = procConst();
            int line = lex.getLine();
            expr = new ConstExpr(line, v);
        } else if (current.type == TokenType.READ || current.type == TokenType.TONUMBER || current.type == TokenType.TOSTRING) {
            expr = procFunction();
        } else if (current.type == TokenType.OPEN_CUR) {
            expr = procTable();
        } else {
            expr = procLValue();
        }

        return expr;
    }

    // <const> ::= <number> | <string> | false | true | nil
    private Value<?> procConst() {
        Value<?> v = null;
        if (current.type == TokenType.NUMBER) {
            v = procNumber();
        } else if (current.type == TokenType.STRING) {
            v = procString();
        } else if (current.type == TokenType.FALSE) {
            v = new BooleanValue(false);
            advance();
        } else if (current.type == TokenType.TRUE) {
            v = new BooleanValue(true);
            advance();
        } else if (current.type == TokenType.NIL) {
            v = null;
            advance();
        } else {
            showError();
        }
        return v;
    }

    // <function> ::= (read | tonumber | tostring) '(' [ <expr> ] ')'
    private Expr procFunction() {
        UnaryOp op = null;
        if (current.type == TokenType.READ || current.type == TokenType.TONUMBER || current.type == TokenType.TOSTRING) {            
            switch (current.type) {
                case READ:
                    op = UnaryOp.Read;     
                    break;
                case TONUMBER:
                    op = UnaryOp.ToNumber;     
                    break;
                case TOSTRING:
                    op = UnaryOp.ToString;
                    break;        
                default:
                    break;
            }
            advance();
        }

        eat(TokenType.OPEN_PAR);
        Expr expr = null;
        int line = lex.getLine();
        if (current.type == TokenType.AND || current.type == TokenType.OR || current.type == TokenType.OPEN_PAR ||
        current.type == TokenType.SUB || current.type == TokenType.SIZE|| current.type == TokenType.NOT ||
        current.type == TokenType.NUMBER || current.type == TokenType.STRING || current.type == TokenType.FALSE ||
        current.type == TokenType.TRUE || current.type == TokenType.NIL || current.type == TokenType.READ ||
        current.type == TokenType.TONUMBER || current.type == TokenType.TOSTRING || current.type == TokenType.OPEN_CUR ||
        current.type == TokenType.ID) {
            expr = procExpr();
        }
        if (op!=null){
            expr = new UnaryExpr(line, expr, op);
        }
        eat(TokenType.CLOSE_PAR);
        return expr;
    }

    // <table> ::= '{' [ <elem> { ',' <elem> } ] '}'
    private TableExpr procTable() {
        TableEntry elements = new TableEntry();
        eat(TokenType.OPEN_CUR);
      	int line = lex.getLine();
        TableExpr expr = new TableExpr(line);
        if (current.type == TokenType.OPEN_BRA || current.type == TokenType.AND || current.type == TokenType.OR || 
        current.type == TokenType.OPEN_PAR || current.type == TokenType.SUB || current.type == TokenType.SIZE || 
        current.type == TokenType.NOT || current.type == TokenType.NUMBER || current.type == TokenType.STRING || 
        current.type == TokenType.FALSE || current.type == TokenType.NIL || current.type == TokenType.TRUE || 
        current.type == TokenType.READ || current.type == TokenType.TONUMBER || current.type == TokenType.TOSTRING || 
        current.type == TokenType.OPEN_CUR || current.type == TokenType.ID) {      	
            elements = procElem();
            expr.addEntry(elements.key, elements.value);
            while (current.type == TokenType.COLON) {
                advance();
                elements = procElem();
                expr.addEntry(elements.key, elements.value);
            }
        }
        eat(TokenType.CLOSE_CUR);
        return expr;
    }

    // <elem> ::= [ '[' <expr> ']' '=' ] <expr>
    private TableEntry procElem() {
        TableEntry element = new TableEntry();
        element.key = null;
        if (current.type == TokenType.OPEN_BRA) {
            advance();
            element.key = procExpr();
            eat(TokenType.CLOSE_BRA);
            eat(TokenType.ASSIGN);
        }
        element.value = procExpr();
        return element;
    }

    private Variable procName() {
        String name = current.token;
        eat(TokenType.ID);
        int line = lex.getLine();

        Variable var = new Variable(line, name);
        return var;
    }

    private NumberValue procNumber() {
        String tmp = current.token;
        eat(TokenType.NUMBER);

        Double v = Double.valueOf(tmp);
        NumberValue nv = new NumberValue(v);
        return nv;
    }

    private StringValue procString() {
        String name = current.token;
        eat(TokenType.STRING);

        StringValue sv = new StringValue(name);
        return sv;
    }
}
