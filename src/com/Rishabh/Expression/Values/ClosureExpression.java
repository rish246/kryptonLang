package com.Rishabh.Expression.Values;

import com.Rishabh.EvalResult;
import com.Rishabh.Expression.Expression;
import com.Rishabh.Expression.Statements.FunctionExpression;
import com.Rishabh.Expression.LambdaExpression;
import com.Rishabh.ExpressionType;
import com.Rishabh.Utilities.Environment;
import com.Rishabh.Expression.PrimaryExpressions.IdentifierExpression;

import java.util.ArrayList;
import java.util.List;

public class ClosureExpression extends Expression {
    public Expression _functionBody;
    public Environment _closureEnv;
    public String _name;
    public List<IdentifierExpression> _formalArgs;

    public List<String> _diagnostics = new ArrayList<>();

    public ClosureExpression(String name, Expression funcBody, Environment closureEnv, List<IdentifierExpression> formalArgs) {
        super(ExpressionType.ClosureExpression);
        _functionBody = funcBody;
        _closureEnv = closureEnv;
        _name = name;
        _formalArgs = formalArgs;
    }

    public EvalResult evaluate(Environment env) throws Exception {
        _functionBody.evaluate(_closureEnv);
        _diagnostics.addAll(_functionBody.getDiagnostics());
        return new EvalResult(null, "functionExpression");

    }

    public void prettyPrint(String indent) {
        System.out.println("Closure");
        System.out.println(indent + "|");
        System.out.print(indent + "|-");_functionBody.prettyPrint(indent + "    ");
    }

    // Do i want the refactor... Absolutely yes

    public List<String> getDiagnostics() {
        return _diagnostics;
    }
}