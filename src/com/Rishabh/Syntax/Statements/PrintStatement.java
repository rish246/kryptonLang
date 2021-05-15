package com.Rishabh.Syntax.Statements;

import com.Rishabh.EvalResult;
import com.Rishabh.ExpressionType;
import com.Rishabh.Syntax.Expression;
import com.Rishabh.Syntax.Statement;
import com.Rishabh.Utilities.Environment;
import com.Rishabh.Utilities.Printer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PrintStatement extends Statement {

    public static final String TEXT_RED = "\u001B[31m";
    public static final String TEXT_RESET = "\u001B[0m";
    public static final String TEXT_GREEN = "\u001B[32m";


    public Expression _body;
    List<String> _diagnostics = new ArrayList<>();

    public PrintStatement(Expression body, int lineNumber) {
        super(ExpressionType.printExpression, lineNumber);
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

        Printer.print(bodyOp);
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
