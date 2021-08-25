package com.Krypton.Utilities.BinaryOperators;

import com.Krypton.EvalResult;
import com.Krypton.Syntax.Expression;
import com.Krypton.Utilities.CustomExceptions.BinaryOperators.InvalidOperationException;
import com.Krypton.Utilities.Environment;

import java.util.ArrayList;
import java.util.List;

public class Multiplier extends Operator {
    private final Expression _left;
    private final Expression _right;

    public Multiplier(Expression left, Expression right) {
        _left = left;
        _right = right;
    }

    @Override
    public EvalResult operate(Environment env) throws Exception {
        try {
            EvalResult leftRes = _left.evaluate(env);
            EvalResult rightRes = _right.evaluate(env);
            return multiplyValues(leftRes, rightRes);
        } catch (Exception e) {
            addDiagnostics(_left.getDiagnostics());
            addDiagnostics(_right.getDiagnostics());
            throw e;
        }
    }

    private EvalResult multiplyValues(EvalResult left, EvalResult right) throws InvalidOperationException {
        if ( isAnInt(left) && isAnInt(right) )
            return multiplyInts(left, right);

        if (isFloatOrInt(left) && isFloatOrInt(right))
            return multiplyFloats(left, right);

        if (isList(left) && isAnInt(right))
            return duplicateList(left, right);

        throw new InvalidOperationException("Invalid Binary operator '+'  For types " + _left.getType() + " and " + _right.getType());
    }

    /*
        [1] * 3 --> [1, 1, 1]
     */
    private EvalResult duplicateList(EvalResult left, EvalResult right) throws InvalidOperationException {
        int nTimes = (int) right.getValue();
        if(nTimes < 0) throw new InvalidOperationException("Expected a positive integer, got " + nTimes);

        List<EvalResult> leftList = (List) left.getValue();
        return duplicateListNTimes(leftList, nTimes);
    }

    private EvalResult duplicateListNTimes(List leftList, int nTimes) {
        List<EvalResult> newList = new ArrayList<>();
        for(int i = 0; i< nTimes; i++) {
            List<EvalResult> copiedList = copyList(leftList);
            newList.addAll(copiedList);
        }
        return new EvalResult(newList, "list");
    }

    private EvalResult multiplyFloats(EvalResult left, EvalResult right) {
        return new EvalResult(parseFloat(left) * parseFloat(right), "float");
    }

    private EvalResult multiplyInts(EvalResult left, EvalResult right) {
        return new EvalResult((int) left.getValue() * (int) right.getValue(), "int");
    }

}
