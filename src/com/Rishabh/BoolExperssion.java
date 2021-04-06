package com.Rishabh;

class BoolExpression extends Expression {
    boolean  _value;
//    ExpressionType _type;

    BoolExpression(boolean value) {
        super(ExpressionType.BoolExpression);
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