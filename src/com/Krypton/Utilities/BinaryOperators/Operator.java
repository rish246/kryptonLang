package com.Krypton.Utilities.BinaryOperators;

import com.Krypton.EvalResult;
import com.Krypton.Syntax.Expression;
import com.Krypton.Syntax.PrimaryExpressions.BinaryExpression;
import com.Krypton.TokenType;
import com.Krypton.Utilities.CustomExceptions.BinaryOperators.InvalidOperationException;
import com.Krypton.Utilities.Environment;

import java.util.ArrayList;
import java.util.List;

public abstract class Operator {
    private final List<String> _diagnostics;

    public Operator() {
        _diagnostics = new ArrayList<>();
    }

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
            default:
                throw new InvalidOperationException("Invalid token " + operatorToken + " at line number " + binExpression.getLineNumber());
        }

    }

    public abstract EvalResult operate(Environment env) throws Exception;

    public Float parseFloat(EvalResult o) {
        return Float.parseFloat(o.getValue().toString());
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

    public boolean isType(EvalResult result, String type) {
        return result.getType().equals(type);
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
}
