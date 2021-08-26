package com.Krypton.Utilities.BinaryOperators.ArithematicOperators;

import com.Krypton.EvalResult;
import com.Krypton.Syntax.Expression;
import com.Krypton.Utilities.BinaryOperators.Operator;
import com.Krypton.Utilities.CustomExceptions.BinaryOperators.InvalidOperationException;

public class Subtractor extends Operator {

    public Subtractor(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    public EvalResult operateOnValues(EvalResult leftRes, EvalResult rightRes) throws InvalidOperationException {
        if ( isAnInt(leftRes) && isAnInt(rightRes) )
            return subtractInts(leftRes, rightRes);

        if (isFloatOrInt(leftRes) && isFloatOrInt(rightRes))
            return subtractFloats(leftRes, rightRes);

        return raiseInvalidOperatorTypeException("+");
    }

    private EvalResult subtractFloats(EvalResult left, EvalResult right) {
        return new EvalResult(parseFloat(left) - parseFloat(right), "float");
    }

    private EvalResult subtractInts(EvalResult left, EvalResult right) {
        return new EvalResult((int) left.getValue() - (int) right.getValue(), "int");
    }
}


