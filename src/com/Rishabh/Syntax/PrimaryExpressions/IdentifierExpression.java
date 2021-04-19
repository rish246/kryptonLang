package com.Rishabh.Syntax.PrimaryExpressions;

import com.Rishabh.EvalResult;
import com.Rishabh.ExpressionType;
import com.Rishabh.Syntax.Expression;
import com.Rishabh.Utilities.Environment;
import com.Rishabh.Utilities.Symbol;

import java.util.ArrayList;
import java.util.List;

public class IdentifierExpression extends Expression {
    public String _lexeme;
    public List<String> _diagnostics = new ArrayList<>();

    public IdentifierExpression(String lexeme) {
        super(ExpressionType.IdentifierExpression);
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
        Symbol res = env.get(_lexeme);

        if(res == null) {
            _diagnostics.add("Undefined variable : " + _lexeme);
            return null;
        }

        return new EvalResult(res._value, res._type);


    }

}
