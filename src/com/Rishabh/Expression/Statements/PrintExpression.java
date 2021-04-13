package com.Rishabh.Expression.Statements;

import com.Rishabh.EvalResult;
import com.Rishabh.ExpressionType;
import com.Rishabh.Expression.Expression;
import com.Rishabh.Utilities.Environment;

import java.util.ArrayList;
import java.util.List;

public class PrintExpression extends Expression {

    public static final String TEXT_RED = "\u001B[31m";
    public static final String TEXT_RESET = "\u001B[0m";
    public static final String TEXT_GREEN = "\u001B[32m";


    public Expression _body;
    List<String> _diagnostics = new ArrayList<>();

    public PrintExpression(Expression body) {
        super(ExpressionType.printExpression);
        _body = body;
    }

    public ExpressionType getType() {
        return _type;
    }

    public EvalResult evaluate(Environment env) throws Exception {
        EvalResult bodyOp = _body.evaluate(env);
        _diagnostics.addAll(_body.getDiagnostics());

        if(bodyOp == null || _diagnostics.size() > 0) {
            return null;
        }

        System.out.println(TEXT_GREEN + bodyOp._value + TEXT_RESET);

        return new EvalResult(null, null);
    }

    public void prettyPrint(String indent) {
        System.out.println("PrintExpression");
        System.out.print(indent + "|-");
        _body.prettyPrint(indent + "    ");
    }

    public List<String> getDiagnostics() {
        return _diagnostics;
    }

}
