package com.Rishabh.Syntax.Values;

import com.Rishabh.EvalResult;
import com.Rishabh.ExpressionType;
import com.Rishabh.Syntax.Expression;
import com.Rishabh.Utilities.Environment;

public class NumberExpression extends Expression {
    public int _value;
//    ExpressionType _type;

    public NumberExpression(int value, int lineNumber) {
        super(ExpressionType.IntExpression, lineNumber);
        _value = value;
    }

    @Override
    public void prettyPrint(String indent) {
//        System.out.print("|");
        System.out.println(indent + _value);
    }

    @Override
    public EvalResult evaluate(Environment env) {
        return new EvalResult(_value, "int");
    }

}
