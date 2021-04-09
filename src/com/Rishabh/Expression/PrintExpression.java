package com.Rishabh.Expression;

import com.Rishabh.EvalResult;
import com.Rishabh.ExpressionType;
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

        if(bodyOp == null) {
            return null;
        }

        System.out.println(TEXT_GREEN + bodyOp._value + TEXT_RESET);

//        System.out.println(bodyOp);
        return new EvalResult(null, null);
    }

    public void prettyPrint(String indent) {
        System.out.println(indent + "PrintExpression");
        System.out.print("|-");
        _body.prettyPrint(indent + "    ");
    }

    public List<String> getDiagnostics() {
        return _diagnostics;
    }

}
