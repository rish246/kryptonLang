package com.Krypton.Utilities.BinaryOperators.ComparisonOperators;

import com.Krypton.EvalResult;
import com.Krypton.Syntax.Expression;
import com.Krypton.Utilities.BinaryOperators.Operator;

public abstract class ComparisonOperator extends Operator {
    String _operatorToken;
    public ComparisonOperator(Expression left, Expression right, String operatorToken) {
        super(left, right);
        _operatorToken = operatorToken;
    }

    public EvalResult operateOnValues(EvalResult left, EvalResult right) throws Exception {
        if (isAnInt(left) && isAnInt(right))
            return compareInts(left, right);

        if (isFloat(left) && isFloat(right))
            return compareFloats(left, right);

        return raiseInvalidOperatorTypeException(_operatorToken);
    }


    public abstract EvalResult compareInts(EvalResult left, EvalResult right) throws Exception;

    public abstract EvalResult compareFloats(EvalResult left, EvalResult right) throws Exception;
}

// Refactor using We can implement other things as well -> I can add other things as well
