package com.Rishabh;

class Expression {
    ExpressionType _type;

    Expression(ExpressionType type) {
        _type = type;
    }

    ExpressionType getType() {
        return _type;
    }

    Object evaluate() throws Exception {
        return 0;
    }

    void prettyPrint(String indent) {
    }

}
