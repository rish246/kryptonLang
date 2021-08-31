package com.Krypton.Utilities.BinaryOperators.ArithmeticOperators;

import com.Krypton.EvalResult;
import com.Krypton.Syntax.PrimaryExpressions.BinaryExpression;
import com.Krypton.Utilities.Environment;

import java.util.List;

public interface ArithmeticOperator {
    EvalResult operateOn(BinaryExpression exp, Environment env) throws Exception;
    List<String> getDiagnostics();
}
