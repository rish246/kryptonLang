package com.Krypton.Syntax.Values;

import com.Krypton.EvalResult;
import com.Krypton.ExpressionType;
import com.Krypton.Syntax.Expression;
import com.Krypton.Utilities.Environment;

public class NullExpression extends Expression {
//    ExpressionType _type;

    public NullExpression( int lineNumber) {
        super(ExpressionType.NullExpression, lineNumber);
    }

    @Override
    public void prettyPrint(String indent) {
        System.out.println(indent + "null");
    }

    @Override
    public EvalResult evaluate(Environment env) {
        return new EvalResult(null, "null");
    }

}
