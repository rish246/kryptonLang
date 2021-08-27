package com.Krypton.Utilities.BinaryOperators.ComparisonOperators;

import com.Krypton.EvalResult;
import com.Krypton.Syntax.PrimaryExpressions.BinaryExpression;
import com.Krypton.TokenType;
import com.Krypton.Utilities.CustomExceptions.BinaryOperators.InvalidOperationException;
import com.Krypton.Utilities.Environment;

import java.util.ArrayList;
import java.util.List;

public class ComparisonOperatorImpl implements ComparisonOperator {
    private final List<String> _diagnostics = new ArrayList<>();
    private final TokenType _operatorToken;

    public ComparisonOperatorImpl(TokenType operatorToken) {
        _operatorToken = operatorToken;
    }

    @Override
    public EvalResult operateOn(BinaryExpression binExp, Environment env) throws Exception {
        ComparisonOperator comparisonOperator = getRightComparisonOperator();
        try {
            return comparisonOperator.operateOn(binExp, env);
        } catch (InvalidOperationException e) {
            _diagnostics.addAll(comparisonOperator.getDiagnostics());
            throw e;
        }
    }

    private ComparisonOperator getRightComparisonOperator() throws InvalidOperationException {
        if ( _operatorToken == TokenType.LessThanToken )
            return new LessThanOperator();

        if (_operatorToken  == TokenType.LessThanEqualToken)
            return new LessThanEqualOperator();

        if (_operatorToken  == TokenType.GreaterThanToken)
            return new MoreThanOperator();

        if (_operatorToken  == TokenType.GreaterThanEqualToken)
            return new GreaterThanEqualOperator();

        if (_operatorToken  == TokenType.EqualityToken)
            return new EqualityOperator();


        if (_operatorToken  == TokenType.NotEqualsToken)
            return new NonEqualityOperator();

        throw new InvalidOperationException("Invalid comparison operator " + _operatorToken);
    }


    @Override
    public List<String> getDiagnostics() {
        return _diagnostics;
    }
}

// Refactor using We can implement other things as well -> I can add other things as well
