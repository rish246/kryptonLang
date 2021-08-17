package com.Krypton.Syntax.PrimaryExpressions;

import com.Krypton.EvalResult;
import com.Krypton.ExpressionType;
import com.Krypton.Syntax.Expression;
import com.Krypton.Utilities.Environment;

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

