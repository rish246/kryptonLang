package com.Krypton.Syntax.Statements;

import com.Krypton.EvalResult;
import com.Krypton.ExpressionType;
import com.Krypton.Syntax.Expression;
import com.Krypton.Syntax.Statement;
import com.Krypton.Utilities.Environment;

import java.util.ArrayList;
import java.util.List;
public class ReturnStatement extends Statement {
    public Expression _body;
    //    ExpressionType _type;
    List<String> _diagnostics = new ArrayList<>();

    public ReturnStatement(Expression body, int lineNumber) {
        super(ExpressionType.ReturnExpression, lineNumber);
        _body = body;
    }

    @Override
    public void prettyPrint(String indent) {

        System.out.println("Return expression");
        System.out.println(indent + "|");
        System.out.print(indent + "├──");
        _body.prettyPrint(indent + "    ");
    }

    @Override
    public List<String> getDiagnostics() {
        return _diagnostics;
    }


    public EvalResult evaluate(Environment env) throws Exception {
        try {
            return _body.evaluate(env);
        } catch (Exception e) {
            _diagnostics.addAll(_body.getDiagnostics());
            throw e;
        }
    }
}
/*
def even_or_odd(x) {
		if(x % 2 == 0) { return null; }
		return "odd";
	}
 */