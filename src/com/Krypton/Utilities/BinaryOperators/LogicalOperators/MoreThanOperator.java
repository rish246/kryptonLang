package com.Krypton.Utilities.BinaryOperators.LogicalOperators;

import com.Krypton.EvalResult;
import com.Krypton.Syntax.Expression;
import com.Krypton.Utilities.BinaryOperators.Operator;
import com.Krypton.Utilities.CustomExceptions.BinaryOperators.InvalidOperationException;

public class MoreThanOperator extends Operator {
    public MoreThanOperator(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    public EvalResult operateOnValues(EvalResult left, EvalResult right) throws Exception {
        if (isAnInt(left) && isAnInt(right))
            return compareInts(left, right);

        if (isFloat(left) && isFloat(right))
            return compareFloats(left, right);

        throw new InvalidOperationException("Invalid operator '>' for types " + _left.getType() + " and " + _right.getType());
    }

    private EvalResult compareInts(EvalResult left, EvalResult right) {
        return new EvalResult((int) left.getValue() > (int) right.getValue(), "boolean");
    }

    private EvalResult compareFloats(EvalResult left, EvalResult right) {
        return new EvalResult(parseFloat(left) > parseFloat(right), "boolean");
    }
}
