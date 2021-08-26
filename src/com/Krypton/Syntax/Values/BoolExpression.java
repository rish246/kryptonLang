package com.Krypton.Syntax.Values;

import com.Krypton.EvalResult;
import com.Krypton.ExpressionType;
import com.Krypton.Syntax.Expression;
import com.Krypton.Utilities.Environment;

public class BoolExpression extends Expression {
    boolean  _value;

    public BoolExpression(boolean value, int lineNumber) {
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
