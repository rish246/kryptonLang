package com.Krypton.Utilities.BinaryOperators;

import com.Krypton.EvalResult;
import com.Krypton.Syntax.Expression;
import com.Krypton.Utilities.CustomExceptions.BinaryOperators.InvalidOperationException;
import com.Krypton.Utilities.Environment;
import com.Krypton.Utilities.Printer;

import java.util.ArrayList;
import java.util.List;

public class Adder extends Operator {
    private final Expression _left;
    private final Expression _right;

    public Adder(Expression left, Expression right) {
        _left = left;
        _right = right;
    }

    @Override
    public EvalResult operate(Environment env) throws Exception {
        try {
            EvalResult leftRes = _left.evaluate(env);
            EvalResult rightRes = _right.evaluate(env);
            return addValues(leftRes, rightRes);
        } catch (Exception e) {
            addDiagnostics(_left.getDiagnostics());
            addDiagnostics(_right.getDiagnostics());
            throw e;
        }
    }

    private EvalResult addValues(EvalResult leftRes, EvalResult rightRes) throws InvalidOperationException {
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

        throw new InvalidOperationException("Invalid Binary operator '+'  For types " + _left.getType() + " and " + _right.getType());

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
