package com.Krypton.Utilities.BinaryOperators;

import com.Krypton.EvalResult;
import com.Krypton.Syntax.Expression;
import com.Krypton.Syntax.PrimaryExpressions.BinaryExpression;
import com.Krypton.TokenType;
import com.Krypton.Utilities.BinaryOperators.ArithmeticOperators.ArithematicOperatorFacade;
import com.Krypton.Utilities.BinaryOperators.AssignmentOperators.SimpleAssignmentOperator;
import com.Krypton.Utilities.BinaryOperators.ComparisonOperators.ComparisonOperatorFacade;
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
                return new ComparisonOperatorFacade(operatorToken);
            case AddToken:
            case SubToken:
            case MultToken:
            case DivToken:
            case ModuloToken:
                return new ArithematicOperatorFacade(operatorToken);

            case AssignmentToken:
                return new SimpleAssignmentOperator();

            default:
                throw new InvalidOperationException("Invalid token " + operatorToken + " at line number " + binExpression.getLineNumber());
        }
    }


    public EvalResult operateOn(BinaryExpression binaryExpression, Environment env) throws Exception {
        binaryOperator = getRightOperator(binaryExpression); // We have our LogicalBinaryOperatorHere
        try {
            return binaryOperator.operateOn(binaryExpression, env);
        } catch (Exception e) {
            _diagnostics.addAll(binaryOperator.getDiagnostics());
            throw e;
        }
    }


    public List<String> getDiagnostics() {
        return _diagnostics;
    }


    public void addDiagnostics(List<String> moreDiagnostics) {
        _diagnostics.addAll(moreDiagnostics);
    }
}
