package com.Krypton.Utilities.BinaryOperators.LogicalOperators;

import com.Krypton.EvalResult;
import com.Krypton.Syntax.Expression;
import com.Krypton.Utilities.BinaryOperators.Operator;
import com.Krypton.Utilities.CustomExceptions.BinaryOperators.InvalidOperationException;
import com.Krypton.Utilities.Environment;

public abstract class LogicalOperator extends Operator {
    public LogicalOperator(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    public EvalResult operateUnder(Environment env) throws Exception {
        try {
            return evaluateLogicalOperatorUnder(env);
        } catch (Exception e) {
            addDiagnostics(_left.getDiagnostics());
            addDiagnostics(_right.getDiagnostics());
            throw e;
        }
    }

    public abstract EvalResult evaluateLogicalOperatorUnder(Environment env) throws Exception;

    public void assertBoolean(EvalResult result, String operator) throws InvalidOperationException {
        if (!isBool(result))
            raiseInvalidOperatorTypeException(operator);
    }

    @Override
    public EvalResult operateOnValues(EvalResult left, EvalResult right) throws Exception {
        return raiseInvalidOperatorTypeException("&&");
    }
}
