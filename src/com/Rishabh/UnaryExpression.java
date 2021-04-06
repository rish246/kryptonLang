package com.Rishabh;

class UnaryExpression extends Expression {
    Expression _body;
    TokenType _operatorToken;
//    ExpressionType _type;

    UnaryExpression(TokenType operatorToken, Expression body) {
        super(ExpressionType.UnaryExpression);
        _operatorToken = operatorToken;
        _body = body;
    }

    @Override
    void prettyPrint(String indent) {

        System.out.println(_operatorToken);
        System.out.println(indent + "|");
        System.out.print(indent + "├──");
        _body.prettyPrint(indent + "    ");

    }

    @Override
    Object evaluate() throws Exception {
        switch (_operatorToken) {
            case AddToken:
                return _body.evaluate();
            case SubToken:
                return -(int)(_body.evaluate());
            default:
                throw new Exception("Unknown binary operator" + _operatorToken);
        }
    }

}
