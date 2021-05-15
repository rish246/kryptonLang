package com.Rishabh.Syntax.Values;

import com.Rishabh.EvalResult;
import com.Rishabh.ExpressionType;
import com.Rishabh.Syntax.Expression;
import com.Rishabh.Utilities.Environment;

public class FloatExpression extends Expression {
    public float _value;
//    ExpressionType _type;

    public FloatExpression(float value, int lineNumber) {
        super(ExpressionType.FloatExpression, lineNumber);
        _value = value;
    }

    @Override
    public void prettyPrint(String indent) {
//        System.out.print("|");
        System.out.println(indent + _value);
    }

    @Override
    public EvalResult evaluate(Environment env) {
        return new EvalResult(_value, "float");
    }

}
