package com.Krypton.Utilities.BinaryOperators.ArithematicOperators;

public class ModuloOperator {

//    public ModuloOperator(Expression left, Expression right) {
//       super(left, right);
//    }
//
//
//    @Override
//    public EvalResult operateOnValues(EvalResult left, EvalResult right) throws InvalidOperationException {
//        if ( Typing.isAnInt(left) && Typing.isAnInt(right) )
//            return moduloInts(left, right);
//
//        return raiseInvalidOperatorTypeException("%");
//    }
//
//    private EvalResult moduloInts(EvalResult left, EvalResult right) throws InvalidOperationException {
//        if (right.getValue().equals(0)) {
//            addNewDiagnostic("ZeroDivisionError at line number " + getLineNumber());
//            throw new InvalidOperationException("ZeroDivisionError at line number " + getLineNumber());
//        }
//
//        return new EvalResult((int) left.getValue() % (int) right.getValue(), "int");
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
