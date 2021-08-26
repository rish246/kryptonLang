package com.Krypton.Utilities.BinaryOperators.ArithematicOperators;

import com.Krypton.EvalResult;
import com.Krypton.Syntax.Expression;
import com.Krypton.Utilities.BinaryOperators.Operator;
import com.Krypton.Utilities.CustomExceptions.BinaryOperators.InvalidOperationException;

public class ModuloOperator extends Operator {

    public ModuloOperator(Expression left, Expression right) {
       super(left, right);
    }


    @Override
    public EvalResult operateOnValues(EvalResult left, EvalResult right) throws InvalidOperationException {
        if ( isAnInt(left) && isAnInt(right) )
            return moduloInts(left, right);

        return raiseInvalidOperatorTypeException("%");
    }

    private EvalResult moduloInts(EvalResult left, EvalResult right) throws InvalidOperationException {
        if (right.getValue().equals(0)) {
            addNewDiagnostic("ZeroDivisionError at line number " + getLineNumber());
            throw new InvalidOperationException("ZeroDivisionError at line number " + getLineNumber());
        }

        return new EvalResult((int) left.getValue() % (int) right.getValue(), "int");
    }
}
