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
        switch (_operatorToken) {
            case AddToken:
                return ((int) _left.evaluate() + (int) _right.evaluate());
            case SubToken:
                return ((int) _left.evaluate() - (int) _right.evaluate());
            case MultToken:
                return ((int) _left.evaluate() * (int) _right.evaluate());
            case DivToken:
                return ((int) _left.evaluate() / (int) _right.evaluate());
            default:
                throw new Exception("Unknown binary operator" + _operatorToken);
        }
    }

}
