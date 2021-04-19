package com.Rishabh.Syntax.Values;

import com.Rishabh.EvalResult;
import com.Rishabh.ExpressionType;
import com.Rishabh.Syntax.Expression;
import com.Rishabh.Utilities.Environment;

public class StringExpression extends Expression {
    String _value;

    public StringExpression(String value) {
        super(ExpressionType.StringExpression);
        _value = value;
    }

    @Override
    public void prettyPrint(String indent) {
        System.out.println(indent + _value);
    }

    @Override
    public EvalResult evaluate(Environment env) {
        return new EvalResult(_value, "string");
    }

}

/*
    {} -> Declare an object
    // Parse -> {
                    while(curExpression != }) {  ThisSucksAndMaybeThisDont  }
    }
    } BindingExpression --> BindingExpresssion (0) --> { 1, 2 }
    // List of bindings



 */