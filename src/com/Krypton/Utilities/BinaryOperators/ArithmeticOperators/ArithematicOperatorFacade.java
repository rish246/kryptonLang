package com.Krypton.Utilities.BinaryOperators.ArithmeticOperators;

import com.Krypton.EvalResult;
import com.Krypton.Syntax.PrimaryExpressions.BinaryExpression;
import com.Krypton.TokenType;
import com.Krypton.Utilities.BinaryOperators.BinaryOperator;
import com.Krypton.Utilities.CustomExceptions.BinaryOperators.InvalidOperationException;
import com.Krypton.Utilities.Environment;

import java.util.ArrayList;
import java.util.List;

public class ArithematicOperatorFacade implements BinaryOperator {
    private TokenType _operatorToken;
    private List<String> _diagnostics = new ArrayList<>();
    public ArithematicOperatorFacade(TokenType operatorToken) {
        _operatorToken = operatorToken;
    }

    @Override
    public EvalResult operateOn(BinaryExpression binExp, Environment env) throws Exception {
        ArithmeticOperator arithematicOperator = getRightOperator();

        try {
            return arithematicOperator.operateOn(binExp, env);
        } catch (Exception e) {
            _diagnostics.addAll(arithematicOperator.getDiagnostics());
            _diagnostics.add(e.getMessage());
            throw e;
        }

    }

    private ArithmeticOperator getRightOperator() throws InvalidOperationException {
        switch (_operatorToken) {
            case AddToken:
                return new Adder();

            case DivToken:
                return new Divider();

            case MultToken:
                return new Multiplier();

            case SubToken:
                return new Subtractor();

            case ModuloToken:
                return new ModuloOperator();

        }

        throw new InvalidOperationException("Invalid token " + _operatorToken);
    }

    public static List<EvalResult> copyList(List<EvalResult> list) {
        List<EvalResult> result = new ArrayList<>();
        for(EvalResult exp : list) {
            result.add(new EvalResult(exp.getValue(), exp.getType()));
        }
        return result;
    }


    @Override
    public List<String> getDiagnostics() {
        return _diagnostics;
    }
}
