package com.Krypton.Utilities.BinaryOperators.ComparisonOperators;

import com.Krypton.EvalResult;
import com.Krypton.Syntax.Expression;

public class LessThanOperator extends ComparisonOperator {

    public LessThanOperator(Expression left, Expression right) {
        super(left, right, "<");
    }

    @Override
    public EvalResult compareInts(EvalResult left, EvalResult right) {
        return new EvalResult((int) left.getValue() < (int) right.getValue(), "boolean");
    }

    @Override
    public EvalResult compareFloats(EvalResult left, EvalResult right) {
        return new EvalResult(parseFloat(left) < parseFloat(right), "boolean");
    }
}
