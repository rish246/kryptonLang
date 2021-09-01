package com.Krypton.Utilities.BinaryOperators.ArithmeticOperators;

import com.Krypton.EvalResult;
import com.Krypton.Syntax.Expression;
import com.Krypton.Syntax.PrimaryExpressions.BinaryExpression;
import com.Krypton.Utilities.CustomExceptions.BinaryOperators.InvalidOperationException;
import com.Krypton.Utilities.Environment;
import com.Krypton.Utilities.Printer;
import com.Krypton.Utilities.Typing;

import java.util.ArrayList;
import java.util.List;

public class Adder implements ArithmeticOperator {
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
            EvalResult leftRes = _left.evaluate(env); // this should throw an error
            EvalResult rightRes = _right.evaluate(env);
            return operateOnValues(leftRes, rightRes);
        } catch (Exception e) {
            _diagnostics.addAll(_left.getDiagnostics());
            _diagnostics.addAll(_right.getDiagnostics());
            _diagnostics.add(e.getMessage());
            throw e;
        }
    }


    public EvalResult operateOnValues(EvalResult leftRes, EvalResult rightRes) throws InvalidOperationException {
        if ( Typing.isAnInt(leftRes) && Typing.isAnInt(rightRes) )
            return addInts(leftRes, rightRes);

        if (Typing.isFloatOrInt(leftRes) && Typing.isFloatOrInt(rightRes))
            return addFloats(leftRes, rightRes);


        if(Typing.isList(leftRes) && Typing.isList(rightRes))
            return concatLists(leftRes, rightRes);


        if(Typing.isList(leftRes))
            return appendItemToList(leftRes, rightRes);


        if (Typing.isString(leftRes) || Typing.isString(rightRes))
            return concatStrings(leftRes, rightRes);

        throw new InvalidOperationException("Invalid operator '+' for types " + leftRes.getType() + " and " + rightRes.getType() + " at line number " + _lineNumber);
    }

    @Override
    public List<String> getDiagnostics() {
        return _diagnostics;
    }


    private EvalResult concatStrings(EvalResult leftRes, EvalResult rightRes) {
        String leftStr = Printer.getPrintableValue(leftRes).toString();
        String rightStr = Printer.getPrintableValue(rightRes).toString();
        return new EvalResult(leftStr + rightStr, "string");
    }

    private EvalResult appendItemToList(EvalResult leftRes, EvalResult rightRes) {
        List<EvalResult> leftListCopy = ArithematicOperatorFacade.copyList((List) leftRes.getValue());
        leftListCopy.add(rightRes);
        return new EvalResult(leftListCopy, "list");
    }

    private EvalResult addInts(EvalResult leftRes, EvalResult rightRes) {
        return new EvalResult((int) leftRes.getValue() + (int) rightRes.getValue(), "int");
    }

    private EvalResult addFloats(EvalResult leftRes, EvalResult rightRes) {
        return new EvalResult(Typing.parseFloat(leftRes) + Typing.parseFloat(rightRes), "float");
    }

    private EvalResult concatLists(EvalResult leftRes, EvalResult rightRes) {
        List<EvalResult> leftListCopy = ArithematicOperatorFacade.copyList((List) leftRes.getValue());
        List<EvalResult> rightListCopy = ArithematicOperatorFacade.copyList((List) rightRes.getValue());

        List<EvalResult> concatenatedList = new ArrayList<>();
        concatenatedList.addAll(leftListCopy);
        concatenatedList.addAll(rightListCopy);
        return new EvalResult(concatenatedList, "list");
    }
}