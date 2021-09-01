package com.Krypton.Utilities.BinaryOperators.ComparisonOperators;

import com.Krypton.EvalResult;
import com.Krypton.Syntax.PrimaryExpressions.BinaryExpression;
import com.Krypton.TokenType;
import com.Krypton.Utilities.BinaryOperators.BinaryOperator;
import com.Krypton.Utilities.BinaryOperators.ComparisonOperators.Utilities.ComparisonFunc;
import com.Krypton.Utilities.BinaryOperators.ComparisonOperators.Utilities.ComparsionOperationEvaluator;
import com.Krypton.Utilities.CustomExceptions.BinaryOperators.InvalidOperationException;
import com.Krypton.Utilities.Environment;

import java.util.ArrayList;
import java.util.List;

public class ComparisonOperatorFacade implements BinaryOperator {
    private final List<String> _diagnostics = new ArrayList<>();
    private final TokenType _operatorToken;
    ComparsionOperationEvaluator _evaluator;

    public ComparisonOperatorFacade(TokenType operatorToken) {
        _operatorToken = operatorToken;
    }

    @Override
    public EvalResult operateOn(BinaryExpression binExp, Environment env) throws Exception {
        ComparisonFunc comparisonFunc = getRightComparisonFunc();
        _evaluator = new ComparsionOperationEvaluator(binExp);
        try {
            return _evaluator.evaluate(comparisonFunc, env);
        } catch (Exception e) {
            _diagnostics.addAll(_evaluator.getDiagnostics());
            throw e;
        }
    }

    private ComparisonFunc getRightComparisonFunc() throws InvalidOperationException {
        if (_operatorToken == TokenType.LessThanToken)
            return LessThanOperator.getComparisonFunc();

        if (_operatorToken  == TokenType.LessThanEqualToken)
            return LessThanEqualOperator.getComparisonFunc();

        if (_operatorToken  == TokenType.GreaterThanToken)
            return MoreThanOperator.getComparisonFunc();

        if (_operatorToken  == TokenType.GreaterThanEqualToken)
            return GreaterThanEqualOperator.getComparisonFunc();

        if (_operatorToken  == TokenType.EqualityToken)
            return EqualityOperator.getComparisonFunc();

        if (_operatorToken  == TokenType.NotEqualsToken)
            return NonEqualityOperator.getComparisonFunc();

        throw new InvalidOperationException("Invalid comparison operator " + _operatorToken);
    }


    @Override
    public List<String> getDiagnostics() {
        return _diagnostics;
    }
}

// Refactor using We can implement other things as well -> I can add other things as well
