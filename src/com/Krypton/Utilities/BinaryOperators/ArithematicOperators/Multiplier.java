package com.Krypton.Utilities.BinaryOperators.ArithematicOperators;

import com.Krypton.EvalResult;
import com.Krypton.Syntax.Expression;
import com.Krypton.Utilities.BinaryOperators.Operator;
import com.Krypton.Utilities.CustomExceptions.BinaryOperators.InvalidOperationException;

import java.util.ArrayList;
import java.util.List;

public class Multiplier extends Operator {

    public Multiplier(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    public EvalResult operateOnValues(EvalResult left, EvalResult right) throws InvalidOperationException {
        if ( isAnInt(left) && isAnInt(right) )
            return multiplyInts(left, right);

        if (isFloatOrInt(left) && isFloatOrInt(right))
            return multiplyFloats(left, right);

        if (isList(left) && isAnInt(right))
            return duplicateList(left, right);

        return raiseInvalidOperatorTypeException("+");
    }

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
