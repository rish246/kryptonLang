package com.Krypton.Utilities.BinaryOperators.ComparisonOperators;

import com.Krypton.EvalResult;
import com.Krypton.Syntax.PrimaryExpressions.BinaryExpression;
import com.Krypton.Utilities.Environment;

import java.util.ArrayList;
import java.util.List;

public class LessThanOperator implements ComparisonOperator {

    List<String> _diagnostics = new ArrayList<>();
    ComparisonFunctionEvaluator _evaluator;

    @Override
    public EvalResult operateOn(BinaryExpression binExp, Environment env) throws Exception {
        _evaluator = new ComparisonFunctionEvaluator(binExp,  (left, right) -> left < right);

        try {
            return _evaluator.evaluate(env);
        } catch (Exception e) {
            _diagnostics.addAll(_evaluator.getDiagnostics());
            throw e;
        }
    }

    @Override
    public List<String> getDiagnostics() {
        return _diagnostics;
    }
}

