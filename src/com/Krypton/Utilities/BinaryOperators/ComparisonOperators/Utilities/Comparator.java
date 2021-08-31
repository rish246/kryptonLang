package com.Krypton.Utilities.BinaryOperators.ComparisonOperators.Utilities;

import com.Krypton.EvalResult;
import com.Krypton.Utilities.CustomExceptions.BinaryOperators.InvalidOperationException;
import com.Krypton.Utilities.Typing;

public class Comparator {

    public Comparator() {
    }

    public EvalResult compareFloats(ComparisonFunc fn, EvalResult leftFloat, EvalResult rightFloat) throws InvalidOperationException {
         return new EvalResult(fn.compare(Typing.parseFloat(leftFloat), Typing.parseFloat(rightFloat)), "boolean");
    }

    public EvalResult compareObjects(ComparisonFunc fn, EvalResult leftObject, EvalResult rightObject) throws InvalidOperationException {
        return new EvalResult(fn.compare(leftObject, rightObject), "boolean");
    }
}
