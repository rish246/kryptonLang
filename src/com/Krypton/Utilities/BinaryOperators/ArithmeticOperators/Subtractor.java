package com.Krypton.Utilities.BinaryOperators.ArithmeticOperators;

import com.Krypton.EvalResult;
import com.Krypton.Syntax.Expression;
import com.Krypton.Syntax.PrimaryExpressions.BinaryExpression;
import com.Krypton.Utilities.CustomExceptions.BinaryOperators.InvalidOperationException;
import com.Krypton.Utilities.Environment;
import com.Krypton.Utilities.Typing;

import java.util.ArrayList;
import java.util.List;

public class Subtractor implements ArithmeticOperator {
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

    public EvalResult operateOnValues(EvalResult leftRes, EvalResult rightRes) throws InvalidOperationException {
        if ( Typing.isAnInt(leftRes) && Typing.isAnInt(rightRes) )
            return subtractInts(leftRes, rightRes);

        if (Typing.isFloatOrInt(leftRes) && Typing.isFloatOrInt(rightRes))
            return subtractFloats(leftRes, rightRes);

        throw new InvalidOperationException("Invalid operator '*' for types " + leftRes.getType() + " and " + rightRes.getType() + " at line number " + _lineNumber);
    }

    private EvalResult subtractFloats(EvalResult left, EvalResult right) {
        return new EvalResult(Typing.parseFloat(left) - Typing.parseFloat(right), "float");
    }

    private EvalResult subtractInts(EvalResult left, EvalResult right) {
        return new EvalResult((int) left.getValue() - (int) right.getValue(), "int");
    }
//
//    @Override
//    public EvalResult operateOn(Expression left, Expression right, Environment env) throws Exception {
//        return null;
//    }
//
//    @Override
//    public List<String> getDiagnostics() {
//        return null;
//    }
}


