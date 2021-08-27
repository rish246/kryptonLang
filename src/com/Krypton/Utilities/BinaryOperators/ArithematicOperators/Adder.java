package com.Krypton.Utilities.BinaryOperators.ArithematicOperators;

public class Adder {

//    public Adder(Expression left, Expression right) {
//        super(left, right);
//    }
//
//    @Override
//    public EvalResult operateOnValues(EvalResult leftRes, EvalResult rightRes) throws InvalidOperationException {
//        if ( Typing.isAnInt(leftRes) && Typing.isAnInt(rightRes) )
//            return addInts(leftRes, rightRes);
//
//        if (Typing.isFloatOrInt(leftRes) && Typing.isFloatOrInt(rightRes))
//            return addFloats(leftRes, rightRes);
//
//
//        if(Typing.isList(leftRes) && Typing.isList(rightRes))
//            return concatLists(leftRes, rightRes);
//
//
//        if(Typing.isList(leftRes))
//            return appendItemToList(leftRes, rightRes);
//
//
//        if (Typing.isString(leftRes) || Typing.isString(rightRes))
//            return concatStrings(leftRes, rightRes);
//
//        return raiseInvalidOperatorTypeException("+");
//    }
//
//
//
//
//    private EvalResult concatStrings(EvalResult leftRes, EvalResult rightRes) {
//        String leftStr = Printer.getPrintableValue(leftRes).toString();
//        String rightStr = Printer.getPrintableValue(rightRes).toString();
//        return new EvalResult(leftStr + rightStr, "string");
//    }
//
//    private EvalResult appendItemToList(EvalResult leftRes, EvalResult rightRes) {
//        List<EvalResult> leftListCopy = copyList((List) leftRes.getValue());
//        leftListCopy.add(rightRes);
//        return new EvalResult(leftListCopy, "list");
//    }
//
//    private EvalResult addInts(EvalResult leftRes, EvalResult rightRes) {
//        return new EvalResult((int) leftRes.getValue() + (int) rightRes.getValue(), "int");
//    }
//
//    private EvalResult addFloats(EvalResult leftRes, EvalResult rightRes) {
//        return new EvalResult(parseFloat(leftRes) + parseFloat(rightRes), "float");
//    }
//
//    private EvalResult concatLists(EvalResult leftRes, EvalResult rightRes) {
//        List<EvalResult> leftListCopy = copyList((List) leftRes.getValue());
//        List<EvalResult> rightListCopy = copyList((List) rightRes.getValue());
//
//        List<EvalResult> concatenatedList = new ArrayList<>();
//        concatenatedList.addAll(leftListCopy);
//        concatenatedList.addAll(rightListCopy);
//        return new EvalResult(concatenatedList, "list");
//    }
//
//    @Override
//    public EvalResult operateOn(Expression left, Expression right, Environment env) throws Exception {
//        return null; // We'll have to think about iit
//    }
//
//    @Override
//    public List<String> getDiagnostics() {
//        return null;
//    }
}