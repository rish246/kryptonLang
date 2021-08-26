package com.Krypton.Utilities.BinaryOperators.ComparisonOperators;

import com.Krypton.EvalResult;
import com.Krypton.Syntax.Expression;
import com.Krypton.Utilities.BinaryOperators.Operator;

public class NonEqualityOperator extends Operator {

    public NonEqualityOperator(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    public EvalResult operateOnValues(EvalResult left, EvalResult right) throws Exception {
        if (left.getValue() == null)
            return new EvalResult(right.getValue() != null, "boolean");

        if (right.getValue() == null)
            return new EvalResult(left.getValue() != null, "boolean");

        if(!left.getType().equals(right.getType()))
            return raiseInvalidOperatorTypeException("!=");

        return new EvalResult(!left.equals(right), "boolean");
    }
}
