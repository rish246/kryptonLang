package com.Krypton.Syntax.PrimaryExpressions;

import com.Krypton.EvalResult;
import com.Krypton.ExpressionType;
import com.Krypton.Syntax.Expression;
import com.Krypton.Token;
import com.Krypton.Utilities.Environment;

import java.util.List;

public class ParensExpression extends Expression {
    public Expression _body;
    Token _openParens, _closedParens;

    public ParensExpression(Token openParens, Expression body, Token closedParens, int lineNumber) {
        super(ExpressionType.ParensExpression, lineNumber);
        _openParens = openParens;
        _closedParens = closedParens;
        _body = body;
    }


    @Override
    public EvalResult evaluate(Environment env) throws Exception {
        return _body.evaluate(env);
    }

    @Override
    public void prettyPrint(String indent) {
        _body.prettyPrint(indent);
    }

    @Override
    public List<String> getDiagnostics() {
        return _body.getDiagnostics();
    }
}

// What do we do today -->
//      fix the function calls
//      Delete function call expressions
//      Edge cases handling