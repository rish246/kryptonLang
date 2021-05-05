package com.Rishabh.Syntax.PrimaryExpressions;

import com.Rishabh.EvalResult;
import com.Rishabh.ExpressionType;
import com.Rishabh.Syntax.Expression;
import com.Rishabh.Utilities.Environment;

import java.util.ArrayList;
import java.util.List;

public class IdentifierExpression extends Expression {
    public String _lexeme;
    public List<String> _diagnostics = new ArrayList<>();

    public IdentifierExpression(String lexeme, int lineNumber) {
        super(ExpressionType.IdentifierExpression, lineNumber);
        _lexeme = lexeme;
    }

    @Override
    public void prettyPrint(String indent) {
        System.out.println(_lexeme);
    }

    public List<String> getDiagnostics() {
        return _diagnostics;
    }

    @Override
    public EvalResult evaluate(Environment env) {
//        return new EvalResult(_value, _type);
        EvalResult res = env.get(_lexeme);

        if (res == null) {
            _diagnostics.add("Undefined variable : " + _lexeme + " at line number " + getLineNumber());
            return null;
        }

        return res;
    }
}

