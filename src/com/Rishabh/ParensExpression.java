package com.Rishabh;

class ParensExpression extends Expression {
    Expression _body;
    Token _openParens, _closedParens;

    ParensExpression(Token openParens, Expression body, Token closedParens) {
        super(ExpressionType.ParensExpression);
        _openParens = openParens;
        _closedParens = closedParens;
        _body = body;
    }


    @Override
    public Object evaluate() throws Exception {
        return _body.evaluate();
    }

    @Override
    public void prettyPrint(String indent) {
        _body.prettyPrint(indent);
    }
}
