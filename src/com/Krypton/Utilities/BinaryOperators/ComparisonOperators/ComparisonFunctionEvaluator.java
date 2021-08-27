package com.Krypton.Utilities.BinaryOperators.ComparisonOperators;

import com.Krypton.EvalResult;
import com.Krypton.Syntax.Expression;
import com.Krypton.Syntax.PrimaryExpressions.BinaryExpression;
import com.Krypton.TokenType;
import com.Krypton.Utilities.CustomExceptions.BinaryOperators.InvalidOperationException;
import com.Krypton.Utilities.Environment;
import com.Krypton.Utilities.Typing;

import java.util.ArrayList;
import java.util.List;

public class ComparisonFunctionEvaluator {
    private final Expression _left;
    private final Expression _right;
    private final List<String> _diagnostics = new ArrayList<>();
    private final Comparator _comparator;
    private final int _lineNumber;
    private final TokenType _operator;


    ComparisonFunctionEvaluator(BinaryExpression expression, ComparisonFunc fn) {
        _left = expression.getLeft();
        _right = expression.getRight();
        _comparator = new Comparator(fn);
        _operator = expression.getOperatorToken();
        _lineNumber = expression.getLineNumber();
    }

    public EvalResult evaluate(Environment env) throws Exception {
        try {
            EvalResult leftRes = _left.evaluate(env);
            EvalResult rightRes = _right.evaluate(env);

            if ( Typing.isFloatOrInt(leftRes) && Typing.isFloatOrInt(rightRes))
                return _comparator.compareFloats(leftRes, rightRes);

            throw new InvalidOperationException("Invalid operator '" + _operator + "' for type " + leftRes.getType() + " and " + rightRes.getType() + " at line number " + _lineNumber);
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
