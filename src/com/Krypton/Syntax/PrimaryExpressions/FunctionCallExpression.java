package com.Krypton.Syntax.PrimaryExpressions;

import com.Krypton.EvalResult;
import com.Krypton.ExpressionType;
import com.Krypton.Syntax.Expression;
import com.Krypton.Syntax.Values.ClosureExpression;
import com.Krypton.Utilities.Environment;

import java.util.ArrayList;
import java.util.List;

public class FunctionCallExpression extends Expression {
    Expression _function;
    List<Expression> _actualArguments;
    List<String> _diagnostics = new ArrayList<>();

    public FunctionCallExpression(Expression function, List<Expression> actualArguments, int lineNumber) {
        super(ExpressionType.FunctionCallExpression, lineNumber);
        _function = function;
        _actualArguments = actualArguments;
    }

    @Override
    public void prettyPrint(String indent) {

        System.out.println("FUNCTION CALL");
        _function.prettyPrint(indent + "    ");

        for(Expression argument : _actualArguments) {
            System.out.println(indent + "|");
            System.out.print(indent + "├──");
            argument.prettyPrint(indent + "    ");
        }
    }

    @Override
    public List<String> getDiagnostics() {
        return _diagnostics;
    }

    public EvalResult evaluate(Environment env) throws Exception {
        EvalResult function = _function.evaluate(env);
        ClosureExpression functionClosure = (ClosureExpression) function.getValue();
        try {
            Environment functionEvalEnv = functionClosure.bindFormalArgsWithActualArgs(_actualArguments, env);
            return functionClosure.evaluate(functionEvalEnv);
        } catch (Exception e) {
            _diagnostics.add(e.getMessage());
            _diagnostics.addAll(functionClosure.getDiagnostics());
            throw e;
        }
    }
}

// What else is there to test in functions