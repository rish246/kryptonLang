package com.Krypton.Utilities.BinaryOperators.LogicalOperators;

import com.Krypton.EvalResult;
import com.Krypton.Syntax.Expression;
import com.Krypton.Utilities.Environment;

public class LogicalAndOperator extends LogicalOperator {
    public LogicalAndOperator(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    public EvalResult evaluateLogicalOperatorUnder(Environment env) throws Exception {
        EvalResult leftRes = _left.evaluate(env);
        assertBoolean(leftRes, "&&");

        if (isBoolAndFalse(leftRes))
            return new EvalResult(leftRes.getValue(), leftRes.getType());

        EvalResult rightRes = _right.evaluate(env);
        assertBoolean(rightRes, "&&");
        return new EvalResult(rightRes.getValue(), rightRes.getType());
    }
}

