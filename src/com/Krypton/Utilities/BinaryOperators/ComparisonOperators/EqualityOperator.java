package com.Krypton.Utilities.BinaryOperators.ComparisonOperators;

import com.Krypton.EvalResult;
import com.Krypton.Syntax.PrimaryExpressions.BinaryExpression;
import com.Krypton.Utilities.CustomExceptions.BinaryOperators.InvalidOperationException;
import com.Krypton.Utilities.Environment;

import java.util.ArrayList;
import java.util.List;

public class EqualityOperator implements ComparisonOperator {
//
List<String> _diagnostics = new ArrayList<>();
    ComparisonFunctionEvaluator _evaluator;

    @Override
    public EvalResult operateOn(BinaryExpression binExp, Environment env) throws Exception {
        _evaluator = new ComparisonFunctionEvaluator(binExp, this::compareObjects);

        try {
            return _evaluator.evaluate(env);
        } catch (Exception e) {
            _diagnostics.addAll(_evaluator.getDiagnostics());
            throw e;
        }
    }

    public boolean compareObjects(Object left, Object right) throws InvalidOperationException {
        EvalResult _left = (EvalResult) left;
        EvalResult _right = (EvalResult) right;

        if (_left.getValue() == null)
            return _right.getValue() == null;

        if (_right.getValue() == null)
            return _left.getValue() == null;

        if(!_left.getType().equals(_right.getType()))
            throw new InvalidOperationException("Invalid operator 'EqualEqualOperator' for type " + _left.getType() + " and " + _right.getType() + " at line number ");

        return _left.equals(_right);
    }

    @Override
    public List<String> getDiagnostics() {
        return _diagnostics;
    }
}
