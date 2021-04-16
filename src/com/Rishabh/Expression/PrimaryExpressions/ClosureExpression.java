package com.Rishabh.Expression.PrimaryExpressions;

import com.Rishabh.EvalResult;
import com.Rishabh.Expression.Expression;
import com.Rishabh.Expression.FunctionExpression;
import com.Rishabh.ExpressionType;
import com.Rishabh.Utilities.Environment;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class ClosureExpression extends Expression {
    public FunctionExpression _functionExp;
    public Environment _closureEnv;

    public List<String> _diagnostics = new ArrayList<>();

    public ClosureExpression(FunctionExpression funcBody, Environment closureEnv) {
        super(ExpressionType.FuncExpression);
        _functionExp = funcBody;
        _closureEnv = closureEnv;
    }

    public EvalResult evaluate(Environment env) throws Exception {
        Expression funBody = _functionExp._body;
        funBody.evaluate(_closureEnv);
        _diagnostics.addAll(funBody.getDiagnostics());

        return new EvalResult(null, "functionExpression");

    }

    public void prettyPrint(String indent) {
        System.out.println("Closure");

        System.out.println(indent + "|");
        System.out.print(indent + "|-");_functionExp.prettyPrint(indent + "    ");

    }

    public List<String> getDiagnostics() {
        return _diagnostics;
    }
}