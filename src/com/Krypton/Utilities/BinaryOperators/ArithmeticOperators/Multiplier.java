package com.Krypton.Utilities.BinaryOperators.ArithmeticOperators;

import com.Krypton.EvalResult;
import com.Krypton.Syntax.Expression;
import com.Krypton.Syntax.PrimaryExpressions.BinaryExpression;
import com.Krypton.Utilities.CustomExceptions.BinaryOperators.InvalidOperationException;
import com.Krypton.Utilities.Environment;
import com.Krypton.Utilities.Typing;

import java.util.ArrayList;
import java.util.List;


public class Multiplier implements ArithmeticOperator {
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

//    public Multiplier(Expression left, Expression right) {
//        super(left, right);
//    }
//
//    @Override
    public EvalResult operateOnValues(EvalResult left, EvalResult right) throws InvalidOperationException {
        if ( Typing.isAnInt(left) && Typing.isAnInt(right) )
            return multiplyInts(left, right);

        if (Typing.isFloatOrInt(left) && Typing.isFloatOrInt(right))
            return multiplyFloats(left, right);

        if (Typing.isList(left) && Typing.isAnInt(right))
            return duplicateList(left, right);

        throw new InvalidOperationException("Invalid operator '*' for types " + left.getType() + " and " + right.getType() + " at line number " + _lineNumber);
    }
//
    private EvalResult duplicateList(EvalResult left, EvalResult right) throws InvalidOperationException {
        int nTimes = (int) right.getValue();
        if(nTimes < 0) throw new InvalidOperationException("Expected a positive integer, got " + nTimes);

        List<EvalResult> leftList = (List) left.getValue();
        return duplicateListNTimes(leftList, nTimes);
    }

    private EvalResult duplicateListNTimes(List leftList, int nTimes) {
        List<EvalResult> newList = new ArrayList<>();
        for(int i = 0; i< nTimes; i++) {
            List<EvalResult> copiedList = ArithematicOperatorFacade.copyList(leftList);
            newList.addAll(copiedList);
        }
        return new EvalResult(newList, "list");
    }

    private EvalResult multiplyFloats(EvalResult left, EvalResult right) {
        return new EvalResult(Typing.parseFloat(left) * Typing.parseFloat(right), "float");
    }

    private EvalResult multiplyInts(EvalResult left, EvalResult right) {
        return new EvalResult((int) left.getValue() * (int) right.getValue(), "int");
    }

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
