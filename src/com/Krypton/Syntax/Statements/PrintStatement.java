package com.Krypton.Syntax.Statements;

import com.Krypton.EvalResult;
import com.Krypton.ExpressionType;
import com.Krypton.Syntax.Expression;
import com.Krypton.Syntax.Statement;
import com.Krypton.Utilities.Environment;
import com.Krypton.Utilities.Printer;

import java.util.ArrayList;
import java.util.List;

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
        try {
            EvalResult bodyOp = _body.evaluate(env);
            Printer.print(bodyOp);
            return new EvalResult(null, "null");
        } catch (Exception e) {
            _diagnostics.addAll(_body.getDiagnostics());
            throw e;
        }
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
