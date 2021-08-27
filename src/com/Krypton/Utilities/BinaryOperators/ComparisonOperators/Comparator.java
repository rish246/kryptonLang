package com.Krypton.Utilities.BinaryOperators.ComparisonOperators;

import com.Krypton.EvalResult;
import com.Krypton.Utilities.CustomExceptions.BinaryOperators.InvalidOperationException;
import com.Krypton.Utilities.Typing;

public class Comparator {
    ComparisonFunc _fn;

    public Comparator(ComparisonFunc fn) {
        _fn = fn;
    }

    public EvalResult compareFloats(EvalResult leftFloat, EvalResult rightFloat) throws InvalidOperationException {
         return new EvalResult(_fn.compare(Typing.parseFloat(leftFloat), Typing.parseFloat(rightFloat)), "boolean");
    }

    public EvalResult compareObjects(EvalResult leftObject, EvalResult rightObject) throws InvalidOperationException {
        return new EvalResult(_fn.compare(leftObject, rightObject), "boolean");
    }
}
