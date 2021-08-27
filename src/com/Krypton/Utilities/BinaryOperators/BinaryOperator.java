package com.Krypton.Utilities.BinaryOperators;

import com.Krypton.EvalResult;
import com.Krypton.Syntax.PrimaryExpressions.BinaryExpression;
import com.Krypton.Utilities.Environment;

import java.util.List;

public interface BinaryOperator {
    EvalResult operateOn(BinaryExpression binExp, Environment env) throws Exception;
    List<String> getDiagnostics();
}
