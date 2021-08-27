package com.Krypton.Utilities.BinaryOperators;

import com.Krypton.EvalResult;
import com.Krypton.Syntax.Expression;
import com.Krypton.Syntax.PrimaryExpressions.BinaryExpression;
import com.Krypton.TokenType;
import com.Krypton.Utilities.BinaryOperators.ComparisonOperators.ComparisonOperatorImpl;
import com.Krypton.Utilities.BinaryOperators.LogicalOperators.LogicalBinaryOperatorImpl;
import com.Krypton.Utilities.CustomExceptions.BinaryOperators.InvalidOperationException;
import com.Krypton.Utilities.Environment;

import java.util.ArrayList;
import java.util.List;


public class BinaryOperatorImpl implements BinaryOperator {
    private List<String> _diagnostics = new ArrayList<>();
    protected Expression _left;
    protected Expression _right;
    private int _lineNumber;
    private BinaryOperator binaryOperator;


    public BinaryOperatorImpl(int lineNumber) {
        _lineNumber = lineNumber;
    }


    private BinaryOperator getRightOperator(BinaryExpression binExpression) throws InvalidOperationException {
        TokenType operatorToken = binExpression.getOperatorToken();

        // Embed these cases in the body of implementations later
        // Like LogicalBinaryOperatorImpl.isLogicalBinaryOperator(operatorToken)
        switch (operatorToken) {
            case LogicalOrToken:
            case LogicalAndToken:
                return new LogicalBinaryOperatorImpl(operatorToken);
            case LessThanToken:
            case LessThanEqualToken:
            case GreaterThanToken:
            case GreaterThanEqualToken:
            case EqualityToken:
            case NotEqualsToken:
                return new ComparisonOperatorImpl(operatorToken);

            default:
                throw new InvalidOperationException("Invalid token " + operatorToken + " at line number " + binExpression.getLineNumber());
        }
    }


    public EvalResult operateOn(BinaryExpression binaryExpression, Environment env) throws Exception {
        binaryOperator = getRightOperator(binaryExpression); // We have our LogicalBinaryOperatorHere
        try {
            return binaryOperator.operateOn(binaryExpression, env);
        } catch (InvalidOperationException e) {
            addDiagnostics(binaryOperator.getDiagnostics());
            throw e;
        }
    }


    public List<String> getDiagnostics() {
        return _diagnostics;
    }


    public void addDiagnostics(List<String> moreDiagnostics) {
        _diagnostics.addAll(moreDiagnostics);
    }


//    public Expression getLeft() {
//        return _left;
//    }
//
//    public void setLeft(Expression _left) {
//        this._left = _left;
//    }
//
//    public Expression getRight() {
//        return _right;
//    }
//
//    public void setRight(Expression _right) {
//        this._right = _right;
//    }
//
//    public void setLineNumber(int _lineNumber) {
//        this._lineNumber = _lineNumber;
//    }
//
//
//    public int getLineNumber() {
//        return _lineNumber;
//    }
//
//
////    public List<EvalResult> copyList(List<EvalResult> list) {
////        List<EvalResult> result = new ArrayList<>();
////        for(EvalResult exp : list) {
////            result.add(new EvalResult(exp.getValue(), exp.getType()));
////        }
////        return result;
////    }
////
////    public void addNewDiagnostic(String message) {
////        _diagnostics.add(message);
////    }

}
