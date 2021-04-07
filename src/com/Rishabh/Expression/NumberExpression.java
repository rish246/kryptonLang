package com.Rishabh.Expression;

import com.Rishabh.EvalResult;
import com.Rishabh.ExpressionType;
import com.Rishabh.Utilities.Environment;

public class NumberExpression extends Expression {
    int _value;
//    ExpressionType _type;

    public NumberExpression(int value) {
        super(ExpressionType.IntExpression);
        _value = value;
//        super(ExpressionType.BinaryExpression);
    }

    @Override
    public void prettyPrint(String indent) {
//        System.out.print("|");
        System.out.println(_value);
    }

    @Override
    public EvalResult evaluate(Environment env) {
        return new EvalResult(_value, "int");
    }

}
