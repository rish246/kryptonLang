package com.Krypton.Utilities.BinaryOperators.LogicalOperators;

import com.Krypton.EvalResult;
import com.Krypton.Syntax.PrimaryExpressions.BinaryExpression;
import com.Krypton.TokenType;
import com.Krypton.Utilities.CustomExceptions.BinaryOperators.InvalidOperationException;
import com.Krypton.Utilities.Environment;

import java.util.ArrayList;
import java.util.List;

public class LogicalBinaryOperatorImpl implements LogicalBinaryOperator {
    private final List<String> _diagnostics = new ArrayList<>();
    private final TokenType _operatorToken;
    private int _lineNumber;
    public LogicalBinaryOperatorImpl(TokenType operatorToken) {
        _operatorToken = operatorToken;
    }

    @Override
    public EvalResult operateOn(BinaryExpression binaryExpression, Environment env) throws Exception {
        LogicalBinaryOperator logicalOperator = getRightOperator();
        _lineNumber = binaryExpression.getLineNumber();
        try {
            return logicalOperator.operateOn(binaryExpression, env);
        } catch (Exception e) {
            _diagnostics.addAll(logicalOperator.getDiagnostics());
            throw e;
        }
    }

    private LogicalBinaryOperator getRightOperator() throws InvalidOperationException {
        if ( _operatorToken == TokenType.LogicalAndToken )
            return new LogicalAndOperator(_lineNumber);

        if ( _operatorToken  == TokenType.LogicalOrToken )
            return new LogicalOrOperator(_lineNumber);

        throw new InvalidOperationException("Invalid logical operator " + _operatorToken);
    }


    @Override
    public List<String> getDiagnostics() {
        return _diagnostics;
    }
}
