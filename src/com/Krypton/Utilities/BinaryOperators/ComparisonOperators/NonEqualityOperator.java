package com.Krypton.Utilities.BinaryOperators.ComparisonOperators;

import com.Krypton.EvalResult;
import com.Krypton.Syntax.Expression;
import com.Krypton.Utilities.BinaryOperators.Operator;
import com.Krypton.Utilities.CustomExceptions.BinaryOperators.InvalidOperationException;

public class NonEqualityOperator extends Operator {

    public NonEqualityOperator(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    public EvalResult operateOnValues(EvalResult left, EvalResult right) throws Exception {
        if (left.getValue() == null)
            return new EvalResult(right.getValue() != null, "boolean");

        // If not of same type, raise an InvalidOperationExcetion
        if(!left.getType().equals(right.getType()))
            throw new InvalidOperationException("Invalid operator '!=' for type " + _left.getType() + " and " + _right.getType());

        return new EvalResult(!left.equals(right), "boolean");
    }
}
