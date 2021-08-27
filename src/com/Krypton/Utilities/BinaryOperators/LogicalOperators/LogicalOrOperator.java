package com.Krypton.Utilities.BinaryOperators.LogicalOperators;

import com.Krypton.EvalResult;
import com.Krypton.Syntax.Expression;
import com.Krypton.Syntax.PrimaryExpressions.BinaryExpression;
import com.Krypton.Utilities.CustomExceptions.BinaryOperators.InvalidOperationException;
import com.Krypton.Utilities.Environment;
import com.Krypton.Utilities.Typing;

import java.util.ArrayList;
import java.util.List;

public class LogicalOrOperator implements LogicalBinaryOperator {
    private List<String> _diagnostics = new ArrayList<>();
    private Expression _left;
    private Expression _right;
    int _lineNumber;
    public LogicalOrOperator(int lineNumber) {
        _lineNumber = lineNumber;
    }

    @Override
    public EvalResult operateOn(BinaryExpression binExp, Environment env) throws Exception {
        _left = binExp.getLeft();
        _right = binExp.getRight();
        try {
            return evaluateLogicalOperatorUnder(env);
        } catch (InvalidOperationException e) {
            _diagnostics.addAll(_left.getDiagnostics());
            _diagnostics.addAll(_right.getDiagnostics());
            throw e;
        }
    }

    public EvalResult evaluateLogicalOperatorUnder(Environment env) throws Exception {
        EvalResult leftRes = evaluateExpression(_left, env);

        if (Typing.isBoolAndTrue(leftRes))
            return new EvalResult(leftRes.getValue(), leftRes.getType());

        return evaluateExpression(_right, env);
    }

    private EvalResult evaluateExpression(Expression expression, Environment env) throws Exception {
        try {
            EvalResult result = expression.evaluate(env);
            if (!Typing.isBool(result))
                throw new InvalidOperationException("Invalid operator '||' for type " + result.getType() + " at line number " + _lineNumber);
            return result;
        }
        catch (InvalidOperationException e) {
            _diagnostics.add(e.getMessage());
            throw e;
        }
    }

    @Override
    public List<String> getDiagnostics() {
        return _diagnostics;
    }
}
