package com.Rishabh.Expression;

import com.Rishabh.EvalResult;
import com.Rishabh.ExpressionType;
import com.Rishabh.Utilities.Environment;

public class StringExpression extends Expression {
    String _value;

    public StringExpression(String value) {
        super(ExpressionType.IntExpression);
        _value = value;
    }

    @Override
    public void prettyPrint(String indent) {
        System.out.println(_value);
    }

    @Override
    public EvalResult evaluate(Environment env) {
        return new EvalResult(_value, "string");
    }

}

// @TODO -->
// make a new enum DataType