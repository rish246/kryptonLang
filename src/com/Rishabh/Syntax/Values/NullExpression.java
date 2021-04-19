package com.Rishabh.Syntax.Values;

import com.Rishabh.EvalResult;
import com.Rishabh.ExpressionType;
import com.Rishabh.Syntax.Expression;
import com.Rishabh.Utilities.Environment;

public class NullExpression extends Expression {
//    ExpressionType _type;

    public NullExpression() {
        super(ExpressionType.NullExpression);
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
