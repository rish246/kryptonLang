package com.Krypton.Utilities.BinaryOperators;

import com.Krypton.EvalResult;
import com.Krypton.Syntax.Expression;
import com.Krypton.Utilities.CustomExceptions.BinaryOperators.InvalidOperationException;

public class Divider extends Operator {

    public Divider(Expression left, Expression right) {
        super(left, right);
    }


    @Override
    public EvalResult operateOnValues(EvalResult left, EvalResult right) throws InvalidOperationException {
        if ( isFloatOrInt(left) && isFloatOrInt(right) )
            return divideInts(left, right);

        throw new InvalidOperationException("Invalid Binary operator '/'  For types " + _left.getType() + " and " + _right.getType());
    }

    private EvalResult divideInts(EvalResult left, EvalResult right) throws InvalidOperationException {
        if (right.getValue().equals(0))
            throw new InvalidOperationException("Cannot divide by 0");

        return new EvalResult(parseFloat(left) / parseFloat(right), "float");
    }
}
