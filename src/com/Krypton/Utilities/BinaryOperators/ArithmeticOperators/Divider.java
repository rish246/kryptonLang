package com.Krypton.Utilities.BinaryOperators.ArithmeticOperators;

import com.Krypton.EvalResult;
import com.Krypton.Syntax.Expression;
import com.Krypton.Syntax.PrimaryExpressions.BinaryExpression;
import com.Krypton.Utilities.CustomExceptions.BinaryOperators.InvalidOperationException;
import com.Krypton.Utilities.Environment;
import com.Krypton.Utilities.Typing;

import java.util.ArrayList;
import java.util.List;

public class Divider implements ArithmeticOperator {
    private Expression _left;
    private Expression _right;
    private int _lineNumber;
    private List<String> _diagnostics = new ArrayList<>();

    @Override
    public EvalResult operateOn(BinaryExpression exp, Environment env) throws Exception {
        _left = exp.getLeft();
        _right = exp.getRight();
        _lineNumber = exp.getLineNumber();

        try {
            EvalResult leftRes = _left.evaluate(env);
            EvalResult rightRes = _right.evaluate(env);
            return operateOnValues(leftRes, rightRes);
        } catch (Exception e) {
            _diagnostics.addAll(_left.getDiagnostics());
            _diagnostics.addAll(_right.getDiagnostics());
            _diagnostics.add(e.getMessage());
            throw e;
        }
    }

    @Override
    public List<String> getDiagnostics() {
        return _diagnostics;
    }

    public EvalResult operateOnValues(EvalResult left, EvalResult right) throws InvalidOperationException {
        if ( Typing.isFloatOrInt(left) && Typing.isFloatOrInt(right) )
            return divideInts(left, right);

        throw new InvalidOperationException("Invalid operator '/' for types " + left.getType() + " and " + right.getType() + " at line number " + _lineNumber);
    }

    private EvalResult divideInts(EvalResult left, EvalResult right) throws InvalidOperationException {
        if (right.getValue().equals(0))
            throw new InvalidOperationException("ZeroDivisionError at line number " + _lineNumber);

        return new EvalResult(Typing.parseFloat(left) / Typing.parseFloat(right), "float");
    }

}
