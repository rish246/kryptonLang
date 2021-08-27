package com.Krypton.Utilities.BinaryOperators.ArithematicOperators;

public class Divider {

//    public Divider(Expression left, Expression right) {
//        super(left, right);
//    }
//
//    @Override
//    public EvalResult operateOnValues(EvalResult left, EvalResult right) throws InvalidOperationException {
//        if ( Typing.isFloatOrInt(left) && Typing.isFloatOrInt(right) )
//            return divideInts(left, right);
//
//        return raiseInvalidOperatorTypeException("/");
//    }
//
//    private EvalResult divideInts(EvalResult left, EvalResult right) throws InvalidOperationException {
//        if (right.getValue().equals(0)) {
//            addNewDiagnostic("ZeroDivisionError at line number " + getLineNumber());
//            throw new InvalidOperationException("ZeroDivisionError at line number " + getLineNumber());
//        }
//
//        return new EvalResult(Typing.parseFloat(left) / Typing.parseFloat(right), "float");
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
