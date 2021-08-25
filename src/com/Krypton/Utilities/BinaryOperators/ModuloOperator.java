package com.Krypton.Utilities.BinaryOperators;

import com.Krypton.EvalResult;
import com.Krypton.Syntax.Expression;
import com.Krypton.Utilities.CustomExceptions.BinaryOperators.InvalidOperationException;

public class ModuloOperator extends Operator {

    public ModuloOperator(Expression left, Expression right) {
       super(left, right);
    }


    @Override
    public EvalResult operateOnValues(EvalResult left, EvalResult right) throws InvalidOperationException {
        if ( isAnInt(left) && isAnInt(right) )
            return moduloInts(left, right);
        throw new InvalidOperationException("Invalid Binary operator '%'  For types " + _left.getType() + " and " + _right.getType());
    }

    private EvalResult moduloInts(EvalResult left, EvalResult right) throws InvalidOperationException {
        if (right.getValue().equals(0))
            throw new InvalidOperationException("ZeroDivisionError at line number ");

        return new EvalResult((int) left.getValue() % (int) right.getValue(), "int");
    }
}
