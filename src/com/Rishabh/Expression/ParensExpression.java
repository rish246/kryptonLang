package com.Rishabh.Expression;

import com.Rishabh.EvalResult;
import com.Rishabh.ExpressionType;
import com.Rishabh.Token;

import java.util.List;

public class ParensExpression extends Expression {
    Expression _body;
    Token _openParens, _closedParens;

    public ParensExpression(Token openParens, Expression body, Token closedParens) {
        super(ExpressionType.ParensExpression);
        _openParens = openParens;
        _closedParens = closedParens;
        _body = body;
    }


    @Override
    public EvalResult evaluate() throws Exception {
        return _body.evaluate();
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
