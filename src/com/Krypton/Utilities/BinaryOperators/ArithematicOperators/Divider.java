package com.Krypton.Utilities.BinaryOperators.ArithematicOperators;

import com.Krypton.EvalResult;
import com.Krypton.Syntax.Expression;
import com.Krypton.Utilities.BinaryOperators.Operator;
import com.Krypton.Utilities.CustomExceptions.BinaryOperators.InvalidOperationException;

public class Divider extends Operator {

    public Divider(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    public EvalResult operateOnValues(EvalResult left, EvalResult right) throws InvalidOperationException {
        if ( isFloatOrInt(left) && isFloatOrInt(right) )
            return divideInts(left, right);

        return raiseInvalidOperatorTypeException("/");
    }

    private EvalResult divideInts(EvalResult left, EvalResult right) throws InvalidOperationException {
        if (right.getValue().equals(0)) {
            addNewDiagnostic("ZeroDivisionError at line number " + getLineNumber());
            throw new InvalidOperationException("ZeroDivisionError at line number " + getLineNumber());
        }

        return new EvalResult(parseFloat(left) / parseFloat(right), "float");
    }
}
