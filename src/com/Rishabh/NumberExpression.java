package com.Rishabh;

class NumberExpression extends Expression {
    int _value;
//    ExpressionType _type;

    NumberExpression(int value) {
        super(ExpressionType.BinaryExpression);
        _value = value;
//        super(ExpressionType.BinaryExpression);
    }

    @Override
    void prettyPrint(String indent) {
//        System.out.print("|");
        System.out.println(_value);
    }

    @Override
    Object evaluate() {
        return _value;
    }

}
