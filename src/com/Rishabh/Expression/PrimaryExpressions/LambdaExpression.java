package com.Rishabh.Expression;

import com.Rishabh.EvalResult;
import com.Rishabh.ExpressionType;
import com.Rishabh.Expression.PrimaryExpressions.ClosureExpression;
import com.Rishabh.Expression.PrimaryExpressions.IdentifierExpression;
import com.Rishabh.Utilities.Environment;
import com.Rishabh.Utilities.Symbol;

import java.util.ArrayList;
import java.util.List;

public class LambdaExpression extends Expression {
    public Expression _body;
    public List<IdentifierExpression> _formalArgs = new ArrayList<>();

    public List<String> _diagnostics = new ArrayList<>();

    public LambdaExpression(Expression body, List<IdentifierExpression> formalArgs) {
        super(ExpressionType.LambdaExpression);
        _body = body;
        _formalArgs = formalArgs;
    }

    public EvalResult evaluate(Environment env) throws Exception {

        Environment closureEnv = new Environment(env._table, env._ParentEnv);

        ClosureExpression funcClosure = new ClosureExpression(null, _body, closureEnv, _formalArgs);

        return new EvalResult(funcClosure, "Closure");

    }

    public void prettyPrint(String indent) {
        System.out.println("Lambda");
        System.out.println(indent + "|");
        System.out.println(indent + "|- Formal Args");
        System.out.println(indent + "    " + "|");
        for(IdentifierExpression fArg : _formalArgs) {
            System.out.print(indent + "    " + "|-"); fArg.prettyPrint(indent + "    ");
        }

        System.out.print(indent + "|-");_body.prettyPrint(indent + "    ");

    }

    public List<String> getDiagnostics() {
        return _diagnostics;
    }

}
