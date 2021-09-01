package com.Krypton.Utilities.BinaryOperators.ComparisonOperators.Utilities;

import com.Krypton.EvalResult;
import com.Krypton.Syntax.Expression;
import com.Krypton.Syntax.PrimaryExpressions.BinaryExpression;
import com.Krypton.TokenType;
import com.Krypton.Utilities.CustomExceptions.BinaryOperators.InvalidOperationException;
import com.Krypton.Utilities.Environment;
import com.Krypton.Utilities.Printer;

import java.util.ArrayList;
import java.util.List;

public class ComparsionOperationEvaluator {
    private final Expression _left;
    private final Expression _right;
    private final List<String> _diagnostics = new ArrayList<>();
    private Comparator _comparator;
    private final int _lineNumber;
    private final TokenType _operator;


    public ComparsionOperationEvaluator(BinaryExpression expression) {
        _left = expression.getLeft();
        _right = expression.getRight();
        _operator = expression.getOperatorToken();
        _lineNumber = expression.getLineNumber();
    }

    // If (leftRes -> rightRes)
    public EvalResult evaluate(ComparisonFunc fn, Environment env) throws Exception {
        try {
            EvalResult leftRes = _left.evaluate(env);
            EvalResult rightRes = _right.evaluate(env);
            return new EvalResult(fn.compare(leftRes, rightRes), "boolean");
        }
        catch (InvalidOperationException e) {
            _diagnostics.add(e.getMessage());
            throw e;
        }
        catch (Exception e) {
            _diagnostics.addAll(_left.getDiagnostics());
            _diagnostics.addAll(_right.getDiagnostics());
            throw e;
        }
    }

    public List<String> getDiagnostics() {
        return _diagnostics;
    }
}

// Depending on dataTypes()
// TheComparsionNeedsToBeIndividualToFunctionAndNotEvaluator
