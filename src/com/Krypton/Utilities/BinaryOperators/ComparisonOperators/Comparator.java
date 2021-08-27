package com.Krypton.Utilities.BinaryOperators.ComparisonOperators;

import com.Krypton.EvalResult;
import com.Krypton.Utilities.Typing;

// Send in a lambda
// Which takes two values and compare where one is less than other
public class Comparator {
    // compareFunction
    ComparisonFunc _fn;
    public Comparator(ComparisonFunc fn) {
        // setUpCompareFunction -> LessThanFunc
        _fn = fn;
    }

    public EvalResult compareInts(EvalResult leftInt, EvalResult rightInt) {
        return compareFloats(leftInt, rightInt);
    }

    public EvalResult compareFloats(EvalResult leftFloat, EvalResult rightFloat) {
         return new EvalResult(_fn.compare(Typing.parseFloat(leftFloat), Typing.parseFloat(rightFloat)), "boolean");
    }
}
