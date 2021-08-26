package com.Krypton.Utilities.BinaryOperators.ArithematicOperators;

import com.Krypton.EvalResult;
import com.Krypton.Syntax.Expression;
import com.Krypton.Utilities.BinaryOperators.Operator;
import com.Krypton.Utilities.CustomExceptions.BinaryOperators.InvalidOperationException;
import com.Krypton.Utilities.Printer;

import java.util.ArrayList;
import java.util.List;

public class Adder extends Operator {

    public Adder(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    public EvalResult operateOnValues(EvalResult leftRes, EvalResult rightRes) throws InvalidOperationException {
        if ( isAnInt(leftRes) && isAnInt(rightRes) )
            return addInts(leftRes, rightRes);

        if (isFloatOrInt(leftRes) && isFloatOrInt(rightRes))
            return addFloats(leftRes, rightRes);


        if(isList(leftRes) && isList(rightRes))
            return concatLists(leftRes, rightRes);


        if(isList(leftRes))
            return appendItemToList(leftRes, rightRes);


        if (isString(leftRes) || isString(rightRes))
            return concatStrings(leftRes, rightRes);

        return raiseInvalidOperatorTypeException("+");
    }




    private EvalResult concatStrings(EvalResult leftRes, EvalResult rightRes) {
        String leftStr = Printer.getPrintableValue(leftRes).toString();
        String rightStr = Printer.getPrintableValue(rightRes).toString();
        return new EvalResult(leftStr + rightStr, "string");
    }

    private EvalResult appendItemToList(EvalResult leftRes, EvalResult rightRes) {
        List<EvalResult> leftListCopy = copyList((List) leftRes.getValue());
        leftListCopy.add(rightRes);
        return new EvalResult(leftListCopy, "list");
    }

    private EvalResult addInts(EvalResult leftRes, EvalResult rightRes) {
        return new EvalResult((int) leftRes.getValue() + (int) rightRes.getValue(), "int");
    }

    private EvalResult addFloats(EvalResult leftRes, EvalResult rightRes) {
        return new EvalResult(parseFloat(leftRes) + parseFloat(rightRes), "float");
    }

    private EvalResult concatLists(EvalResult leftRes, EvalResult rightRes) {
        List<EvalResult> leftListCopy = copyList((List) leftRes.getValue());
        List<EvalResult> rightListCopy = copyList((List) rightRes.getValue());

        List<EvalResult> concatenatedList = new ArrayList<>();
        concatenatedList.addAll(leftListCopy);
        concatenatedList.addAll(rightListCopy);
        return new EvalResult(concatenatedList, "list");
    }
}