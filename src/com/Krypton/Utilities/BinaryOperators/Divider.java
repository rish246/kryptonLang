package com.Krypton.Utilities.BinaryOperators;

import com.Krypton.EvalResult;
import com.Krypton.Syntax.Expression;
import com.Krypton.Utilities.CustomExceptions.BinaryOperators.InvalidOperationException;
import com.Krypton.Utilities.Environment;

public class Divider extends Operator {
    private Expression _left;
    private Expression _right;
    public Divider(Expression left, Expression right) {
        _left = left;
        _right = right;
    }


    @Override
    public EvalResult operate(Environment env) throws Exception {
        try {
            EvalResult leftRes = _left.evaluate(env);
            EvalResult rightRes = _right.evaluate(env);
            return divideValues(leftRes, rightRes);
        } catch (Exception e) {
            addDiagnostics(_left.getDiagnostics());
            addDiagnostics(_right.getDiagnostics());
            throw e;
        }
    }

    private EvalResult divideValues(EvalResult left, EvalResult right) throws InvalidOperationException {
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
