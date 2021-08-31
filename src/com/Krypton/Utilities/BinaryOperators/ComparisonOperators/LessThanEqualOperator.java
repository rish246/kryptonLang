package com.Krypton.Utilities.BinaryOperators.ComparisonOperators;

import com.Krypton.EvalResult;
import com.Krypton.Utilities.BinaryOperators.ComparisonOperators.Utilities.ComparisonFunc;
import com.Krypton.Utilities.CustomExceptions.BinaryOperators.InvalidOperationException;
import com.Krypton.Utilities.Typing;

import java.util.ArrayList;
import java.util.List;

public class LessThanEqualOperator implements ComparisonOperator {
    List<String> _diagnostics = new ArrayList<>();

    // We don't need to implement ComparisonOperator then
    public static ComparisonFunc getComparisonFunc() {
        return LessThanEqualOperator::compareValues;
    }

    public static boolean compareValues(Object left, Object right) throws InvalidOperationException {
        EvalResult _left = (EvalResult) left;
        EvalResult _right = (EvalResult) right;

        if (Typing.isFloatOrInt(_left) && Typing.isFloatOrInt(_right))
            return Typing.parseFloat(_left) <= Typing.parseFloat(_right);

        throw new InvalidOperationException("Invalid operator '<=' for types " + _left.getType() + " and " + _right.getType());
    }

    public List<String> getDiagnostics() {
        return _diagnostics;
    }
}
