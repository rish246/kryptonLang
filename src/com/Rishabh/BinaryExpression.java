package com.Rishabh;

class BinaryExpression extends Expression {
    Expression _left, _right;
    TokenType _operatorToken;
//    ExpressionType _type;

    BinaryExpression(Expression left, TokenType operatorToken, Expression right) {
        super(ExpressionType.BinaryExpression);
        _left = left;
        _operatorToken = operatorToken;
        _right = right;
    }

    @Override
    void prettyPrint(String indent) {

        System.out.println(_operatorToken);
        System.out.println(indent + "|");
        System.out.print(indent + "├──");
        _left.prettyPrint(indent + "    ");
        // Add some long lines here
        System.out.println(indent + "|");

        System.out.print(indent + "└──");
        _right.prettyPrint(indent + "    ");

    }

    @Override
    Object evaluate() throws Exception {
        if (_operatorToken == TokenType.AddToken)
            return ((int) _left.evaluate() + (int) _right.evaluate());

        else if (_operatorToken == TokenType.SubToken) {
            return ((int) _left.evaluate() - (int) _right.evaluate());
        } else if (_operatorToken == TokenType.MultToken) {
            return ((int) _left.evaluate() * (int) _right.evaluate());
        } else if (_operatorToken == TokenType.DivToken) {
            return ((int) _left.evaluate() / (int) _right.evaluate());
        } else {
            throw new Exception("Unknown binary operator" + _operatorToken);
        }
    }

}
