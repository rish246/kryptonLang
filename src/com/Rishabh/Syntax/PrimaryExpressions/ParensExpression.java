package com.Rishabh.Syntax.PrimaryExpressions;

import com.Rishabh.EvalResult;
import com.Rishabh.Syntax.Expression;
import com.Rishabh.ExpressionType;
import com.Rishabh.SyntaxTree;
import com.Rishabh.Token;
import com.Rishabh.Utilities.Environment;

import java.util.List;

public class ParensExpression extends Expression {
    Expression _body;
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
