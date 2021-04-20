package com.Rishabh.Syntax.Values;

import com.Rishabh.EvalResult;
import com.Rishabh.ExpressionType;
import com.Rishabh.Syntax.Expression;
import com.Rishabh.Utilities.Environment;

public class BoolExperssion extends Expression {
    boolean  _value;

    public BoolExperssion(boolean value, int lineNumber) {
        super(ExpressionType.BoolExpression, lineNumber);
        _value = value;
    }

    @Override
    public void prettyPrint(String indent) {
        System.out.println(indent + _value);
    }

    @Override
    public EvalResult evaluate(Environment env) {
        return new EvalResult(_value, "boolean");
    }
}

// Two expressions
// null
// len
