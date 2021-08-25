package com.Krypton.Utilities.BinaryOperators;

import com.Krypton.EvalResult;
import com.Krypton.Syntax.Expression;
import com.Krypton.Utilities.CustomExceptions.BinaryOperators.InvalidOperationException;
import com.Krypton.Utilities.Environment;

public class Subtractor extends Operator {
    private final Expression _left;
    private final Expression _right;

    public Subtractor(Expression left, Expression right) {
        _left = left;
        _right = right;
    }

    @Override
    public EvalResult operate(Environment env) throws Exception {
        try {
            EvalResult leftRes = _left.evaluate(env);
            EvalResult rightRes = _right.evaluate(env);
            return subtractValues(leftRes, rightRes);
        } catch (Exception e) {
            addDiagnostics(_left.getDiagnostics());
            addDiagnostics(_right.getDiagnostics());
            throw e;
        }
    }

    private EvalResult subtractValues(EvalResult leftRes, EvalResult rightRes) throws InvalidOperationException {
        if ( isAnInt(leftRes) && isAnInt(rightRes) )
            return subtractInts(leftRes, rightRes);

        if (isFloatOrInt(leftRes) && isFloatOrInt(rightRes))
            return subtractFloats(leftRes, rightRes);

        throw new InvalidOperationException("Invalid Binary operator '+'  For types " + _left.getType() + " and " + _right.getType());

    }

    private EvalResult subtractFloats(EvalResult left, EvalResult right) {
        return new EvalResult(parseFloat(left) - parseFloat(right), "float");
    }

    private EvalResult subtractInts(EvalResult left, EvalResult right) {
        return new EvalResult((int) left.getValue() - (int) right.getValue(), "int");
    }



}
