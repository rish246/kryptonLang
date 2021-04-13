package com.Rishabh.Expression.Values;

import com.Rishabh.EvalResult;
import com.Rishabh.ExpressionType;
import com.Rishabh.Expression.Expression;
import com.Rishabh.Utilities.Environment;

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
    public EvalResult evaluate(Environment env) {
        return new EvalResult(_value, "boolean");
    }



}