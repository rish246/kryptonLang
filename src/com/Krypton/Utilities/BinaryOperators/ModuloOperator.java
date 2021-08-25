package com.Krypton.Utilities.BinaryOperators;

import com.Krypton.EvalResult;
import com.Krypton.Syntax.Expression;
import com.Krypton.Utilities.CustomExceptions.BinaryOperators.InvalidOperationException;
import com.Krypton.Utilities.Environment;

public class ModuloOperator extends Operator {
    private Expression _left;
    private Expression _right;

    public ModuloOperator(Expression left, Expression right) {
        _left = left;
        _right = right;
    }

    @Override
    public EvalResult operate(Environment env) throws Exception {
        try {
            EvalResult leftRes = _left.evaluate(env);
            EvalResult rightRes = _right.evaluate(env);
            return moduloValues(leftRes, rightRes);
        } catch (Exception e) {
            addDiagnostics(_left.getDiagnostics());
            addDiagnostics(_right.getDiagnostics());
            throw e;
        }
    }

    private EvalResult moduloValues(EvalResult left, EvalResult right) throws InvalidOperationException {
        if ( isAnInt(left) && isAnInt(right) )
            return moduloInts(left, right);

//        if (isFloatOrInt(leftRes) && isFloatOrInt(rightRes))
//            return addFloats(leftRes, rightRes);
//
//
//        if(isList(leftRes) && isList(rightRes))
//            return concatLists(leftRes, rightRes);
//
//
//        if(isList(leftRes))
//            return appendItemToList(leftRes, rightRes);
//
//
//        if (isString(leftRes) || isString(rightRes))
//            return concatStrings(leftRes, rightRes);

        throw new InvalidOperationException("Invalid Binary operator '%'  For types " + _left.getType() + " and " + _right.getType());
    }

    private EvalResult moduloInts(EvalResult left, EvalResult right) throws InvalidOperationException {
        if (right.getValue().equals(0))
            throw new InvalidOperationException("ZeroDivisionError at line number ");

        return new EvalResult((int) left.getValue() % (int) right.getValue(), "int");

    }
}
