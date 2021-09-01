package com.Krypton.Syntax.PrimaryExpressions;

import com.Krypton.EvalResult;
import com.Krypton.ExpressionType;
import com.Krypton.Syntax.Expression;
import com.Krypton.Utilities.Environment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LengthExpression extends Expression {
    Expression _body;
    List<String> _diagnostics = new ArrayList<>();

    public LengthExpression(Expression body, int lineNumber) {
        super(ExpressionType.LengthExpression, lineNumber);
        _body = body;
    }


    @Override
    public EvalResult evaluate(Environment env) throws Exception {
        try {
            EvalResult bodyRes = _body.evaluate(env);
            return computeLength(bodyRes);
        } catch (Exception e) {
            _diagnostics.addAll(_body.getDiagnostics());
            _diagnostics.add(e.getMessage());
            throw e;
        }
    }

    private EvalResult computeLength(EvalResult bodyRes) throws Exception {
        switch(bodyRes.getType()) {
            case "list": {
                var value = (List) bodyRes.getValue();
                return new EvalResult(value.size(), "int");
            }
            case "object": {
                var value = (HashMap) bodyRes.getValue();
                return new EvalResult(value.size(), "int");
            }
            case "string": {
                String value = (String) bodyRes.getValue();
                return new EvalResult(value.length(), "int");
            }
        }

        throw new Exception("Type " + bodyRes.getType() + " does not have a property 'len'");
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

// Refactor this next