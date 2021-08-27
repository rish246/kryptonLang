package com.Krypton.Utilities.BinaryOperators.ArithematicOperators;

public class Multiplier {

//    public Multiplier(Expression left, Expression right) {
//        super(left, right);
//    }
//
//    @Override
//    public EvalResult operateOnValues(EvalResult left, EvalResult right) throws InvalidOperationException {
//        if ( Typing.isAnInt(left) && Typing.isAnInt(right) )
//            return multiplyInts(left, right);
//
//        if (Typing.isFloatOrInt(left) && Typing.isFloatOrInt(right))
//            return multiplyFloats(left, right);
//
//        if (Typing.isList(left) && Typing.isAnInt(right))
//            return duplicateList(left, right);
//
//        return raiseInvalidOperatorTypeException("+");
//    }
//
//    private EvalResult duplicateList(EvalResult left, EvalResult right) throws InvalidOperationException {
//        int nTimes = (int) right.getValue();
//        if(nTimes < 0) throw new InvalidOperationException("Expected a positive integer, got " + nTimes);
//
//        List<EvalResult> leftList = (List) left.getValue();
//        return duplicateListNTimes(leftList, nTimes);
//    }
//
//    private EvalResult duplicateListNTimes(List leftList, int nTimes) {
//        List<EvalResult> newList = new ArrayList<>();
//        for(int i = 0; i< nTimes; i++) {
//            List<EvalResult> copiedList = copyList(leftList);
//            newList.addAll(copiedList);
//        }
//        return new EvalResult(newList, "list");
//    }
//
//    private EvalResult multiplyFloats(EvalResult left, EvalResult right) {
//        return new EvalResult(Typing.parseFloat(left) * Typing.parseFloat(right), "float");
//    }
//
//    private EvalResult multiplyInts(EvalResult left, EvalResult right) {
//        return new EvalResult((int) left.getValue() * (int) right.getValue(), "int");
//    }
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
