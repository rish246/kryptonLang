package com.Krypton.Utilities.BinaryOperators;

import com.Krypton.EvalResult;
import com.Krypton.Syntax.Expression;
import com.Krypton.Syntax.PrimaryExpressions.BinaryExpression;
import com.Krypton.TokenType;
import com.Krypton.Utilities.BinaryOperators.ArithematicOperators.*;
import com.Krypton.Utilities.BinaryOperators.ComparisonOperators.*;
import com.Krypton.Utilities.BinaryOperators.LogicalOperators.LogicalAndOperator;
import com.Krypton.Utilities.BinaryOperators.LogicalOperators.LogicalOrOperator;
import com.Krypton.Utilities.CustomExceptions.BinaryOperators.InvalidOperationException;
import com.Krypton.Utilities.Environment;

import java.util.ArrayList;
import java.util.List;


public abstract class Operator {
    private List<String> _diagnostics;

    protected Expression _left;
    protected Expression _right;
    private int lineNumber;

    public Operator(Expression left, Expression right) {
        _left = left;
        _right = right;
        _diagnostics = new ArrayList<>();
    }

    public Operator() {}

    public static Operator getRightOperator(BinaryExpression binExpression) throws InvalidOperationException {
        Expression left = binExpression.getLeft();
        Expression right = binExpression.getRight();
        TokenType operatorToken = binExpression.getOperatorToken();

        switch (operatorToken) {
            case AddToken:
                return new Adder(left, right);
            case SubToken:
                return new Subtractor(left, right);
            case MultToken:
                return new Multiplier(left, right);
            case DivToken:
                return new Divider(left, right);
            case ModuloToken:
                return new ModuloOperator(left, right);
            case LessThanToken:
                return new LessThanOperator(left, right);
            case GreaterThanToken:
                return new MoreThanOperator(left, right);
            case LessThanEqualToken:
                return new LessThanEqualOperator(left, right);
            case GreaterThanEqualToken:
                return new GreaterThanEqualOperator(left, right);
            case EqualityToken:
                return new EqualityOperator(left, right);
            case NotEqualsToken:
                return new NonEqualityOperator(left, right);
            case LogicalAndToken:
                return new LogicalAndOperator(left, right);
            case LogicalOrToken:
                return new LogicalOrOperator(left, right);
            default:
                throw new InvalidOperationException("Invalid token " + operatorToken + " at line number " + binExpression.getLineNumber());
        }
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public abstract EvalResult operateOnValues(EvalResult left, EvalResult right) throws Exception;


    public EvalResult operateUnder(Environment env) throws Exception {
        try {
            EvalResult leftRes = _left.evaluate(env);
            EvalResult rightRes = _right.evaluate(env);
            return operateOnValues(leftRes, rightRes);
        } catch (Exception e) {
            addDiagnostics(_left.getDiagnostics());
            addDiagnostics(_right.getDiagnostics());
            throw e;
        }
    }

    private String generateInvalidOperatorErrorMessage(String operator) {
        return "Invalid Binary operator '" + operator + "'  For types " + _left.getType() + " and " + _right.getType() + " at line number " + getLineNumber();
    }


    public EvalResult raiseInvalidOperatorTypeException(String operator) throws InvalidOperationException {
        addNewDiagnostic(generateInvalidOperatorErrorMessage(operator));
        throw new InvalidOperationException(generateInvalidOperatorErrorMessage(operator));
    }


    public List<String> getDiagnostics() {
        return _diagnostics;
    }

    public void addDiagnostics(List<String> moreDiagnostics) {
        _diagnostics.addAll(moreDiagnostics);
    }

    public boolean isString(EvalResult result) {
        return isType(result, "string");
    }

    public List<EvalResult> copyList(List<EvalResult> list) {
        List<EvalResult> result = new ArrayList<>();
        for(EvalResult exp : list) {
            result.add(new EvalResult(exp.getValue(), exp.getType()));
        }
        return result;
    }

    public void addNewDiagnostic(String message) {
        _diagnostics.add(message);
    }

    public boolean isType(EvalResult result, String type) {
        return result.getType().equals(type);
    }

    public boolean isBool(EvalResult result) {
        return isType(result, "boolean");
    }

    public boolean isAnInt(EvalResult result) {
        return isType(result, "int");
    }

    public boolean isFloatOrInt(EvalResult result) {
        return isFloat(result) || isAnInt(result);
    }

    public boolean isList(EvalResult result) {
        return isType(result, "list");
    }

    public boolean isFloat(EvalResult result) {
        return isType(result, "float");
    }

    public boolean isBoolAndTrue(EvalResult result) {
        return result.equals(new EvalResult(true, "boolean"));
    }

    public boolean isBoolAndFalse(EvalResult result) {
        return result.equals(new EvalResult(false, "boolean"));
    }

    public Expression getLeft() {
        return _left;
    }

    public void setLeft(Expression _left) {
        this._left = _left;
    }

    public Expression getRight() {
        return _right;
    }

    public void setRight(Expression _right) {
        this._right = _right;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }


    public Float parseFloat(EvalResult o) {
        return Float.parseFloat(o.getValue().toString());
    }


}
