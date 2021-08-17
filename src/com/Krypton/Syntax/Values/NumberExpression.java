package com.Krypton.Syntax.Values;

import com.Krypton.EvalResult;
import com.Krypton.ExpressionType;
import com.Krypton.Syntax.Expression;
import com.Krypton.Utilities.Environment;

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
