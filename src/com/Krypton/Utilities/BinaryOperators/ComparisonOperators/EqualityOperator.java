package com.Krypton.Utilities.BinaryOperators.ComparisonOperators;

import com.Krypton.EvalResult;
import com.Krypton.Utilities.BinaryOperators.ComparisonOperators.Utilities.ComparisonFunc;
import com.Krypton.Utilities.CustomExceptions.BinaryOperators.InvalidOperationException;

import java.util.ArrayList;
import java.util.List;

public class EqualityOperator implements ComparisonOperator {
    List<String> _diagnostics = new ArrayList<>();

    public static ComparisonFunc getComparisonFunc() {
        return EqualityOperator::compareValues;
    }


    public static boolean compareValues(Object left, Object right) throws InvalidOperationException {
        EvalResult _left = (EvalResult) left;
        EvalResult _right = (EvalResult) right;

        if (_left.getValue() == null || _right.getValue() == null)
            return _left.getValue() == _right.getValue();

        if(!_left.getType().equals(_right.getType()))
            throw new InvalidOperationException("Invalid operator 'EqualEqualOperator' for type " + _left.getType() + " and " + _right.getType() + " at line number ");

        return _left.equals(_right);
    }

    public List<String> getDiagnostics() {
        return _diagnostics;
    }
}
