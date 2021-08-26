package com.Krypton.Utilities.BinaryOperators.ComparisonOperators;

import com.Krypton.EvalResult;
import com.Krypton.Syntax.Expression;

public class MoreThanOperator extends ComparisonOperator {
    public MoreThanOperator(Expression left, Expression right) {
        super(left, right, ">");
    }

    public EvalResult compareInts(EvalResult left, EvalResult right) {
        return new EvalResult((int) left.getValue() > (int) right.getValue(), "boolean");
    }

    public EvalResult compareFloats(EvalResult left, EvalResult right) {
        return new EvalResult(parseFloat(left) > parseFloat(right), "boolean");
    }
}
