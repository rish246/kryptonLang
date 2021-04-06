package com.Rishabh.Expression;

import com.Rishabh.EvalResult;
import com.Rishabh.ExpressionType;

public class BoolExperssion extends Expression {
    boolean  _value;
//    ExpressionType _type;

    public BoolExperssion(boolean value) {
        super(ExpressionType.BoolExpression);
        _value = value;
//        super(ExpressionType.BinaryExpression);
    }

    @Override
    public void prettyPrint(String indent) {
//        System.out.print("|");
        System.out.println(_value);
    }

    @Override
    public EvalResult evaluate() {
        return new EvalResult(_value, "boolean");
    }



}