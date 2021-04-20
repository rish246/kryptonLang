package com.Rishabh.Syntax.Statements;

import com.Rishabh.EvalResult;
import com.Rishabh.ExpressionType;
import com.Rishabh.Syntax.Expression;
import com.Rishabh.Syntax.Statement;
import com.Rishabh.Utilities.Environment;

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

        if(bodyOp._type == "list") {
            List<Object> printValues = new ArrayList<>();
            List<EvalResult> list = (List) bodyOp._value;
            for(EvalResult nextElement : list) {
                printValues.add(nextElement._value);
            }
            System.out.println(TEXT_GREEN + printValues + TEXT_RESET);

        }
        else if(bodyOp._type == "object") {
            Map<Object, Object> printValues = new HashMap<>();
            Map<Object, EvalResult> object = (HashMap) bodyOp._value;
            for(Map.Entry<Object, EvalResult> binding : object.entrySet()) {
                printValues.put(binding.getKey(), binding.getValue()._value);
            }

            System.out.println(TEXT_GREEN + printValues + TEXT_RESET);



        }
        else if(bodyOp._type == "null") {
            System.out.println(TEXT_GREEN + "null" + TEXT_RESET);

        }
        else {
            System.out.println(TEXT_GREEN + bodyOp._value + TEXT_RESET);

        }

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

    public boolean isExpressionPrimary() {
        return false;
    }

}
