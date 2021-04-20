package com.Rishabh.Syntax.PrimaryExpressions;

import com.Rishabh.EvalResult;
import com.Rishabh.ExpressionType;
import com.Rishabh.Syntax.Expression;
import com.Rishabh.Utilities.Environment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LengthExpression extends Expression {
    Expression _body;
    List<String> _diagnostics = new ArrayList<>();

    public LengthExpression(Expression body, int lineNumber) {
        super(ExpressionType.LengthExpression, lineNumber);
        _body = body;
    }


    @Override
    public EvalResult evaluate(Environment env) throws Exception {
        EvalResult bodyRes = _body.evaluate(env);
        _diagnostics.addAll(_body.getDiagnostics());

        if(_diagnostics.size() > 0) {
            return null;
        }


        if(bodyRes._type != "list" && bodyRes._type != "object" && bodyRes._type != "string") {
            _diagnostics.add("Expressions of Type " + bodyRes._type + " has no property len"+ " at line number " + getLineNumber());
            return null;
        }

        switch(bodyRes._type) {
            case "list": {
                // Get the value from
                List value = (List) bodyRes._value;
                return new EvalResult(value.size(), "int");
            }

            case "object": {
                Map value = (HashMap) bodyRes._value;
                return new EvalResult(value.size(), "int");
            }

            case "string": {
                String value = (String) bodyRes._value;
                return new EvalResult(value.length(), "int");
            }
        }

        _diagnostics.add("Invalid value passed in length expression"+ " at line number " + getLineNumber());
        return null;

    }

    @Override
    public void prettyPrint(String indent) {
        _body.prettyPrint(indent);
    }

    @Override
    public List<String> getDiagnostics() {
        return _diagnostics;
    }
}
