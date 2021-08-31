package com.Krypton.Syntax.Values;

import com.Krypton.EvalResult;
import com.Krypton.ExpressionType;
import com.Krypton.Syntax.Expression;
import com.Krypton.Utilities.Environment;

public class StringExpression extends Expression {
    public String _value;

    public String getValue() {
        return _value;
    }

    public StringExpression(String value, int lineNumber) {
        super(ExpressionType.StringExpression, lineNumber);
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
