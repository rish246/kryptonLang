package com.Krypton.Syntax.PrimaryExpressions;

import com.Krypton.EvalResult;
import com.Krypton.ExpressionType;
import com.Krypton.Syntax.Expression;
import com.Krypton.Syntax.Values.ClosureExpression;
import com.Krypton.SyntaxTree;
import com.Krypton.Utilities.Environment;

import java.util.ArrayList;
import java.util.List;

public class LambdaExpression extends Expression {
    public SyntaxTree _body;
    public List<Expression> _formalArgs = new ArrayList<>();

    public List<String> _diagnostics = new ArrayList<>();

    public LambdaExpression(SyntaxTree body, List<Expression> formalArgs, int lineNumber) {
        super(ExpressionType.LambdaExpression, lineNumber);
        _body = body;
        _formalArgs = formalArgs;
    }

    public EvalResult evaluate(Environment env) throws Exception {
        Environment closureEnv = new Environment(env._table, env._ParentEnv);
        ClosureExpression funcClosure = new ClosureExpression(null, _body, closureEnv, _formalArgs, getLineNumber());
        return new EvalResult(funcClosure, "Closure");
    }

    public void prettyPrint(String indent) {
        System.out.println("Lambda");
        System.out.println(indent + "|");
        System.out.println(indent + "|- Formal Args");
        System.out.println(indent + "    " + "|");
        for(Expression fArg : _formalArgs) {
            System.out.print(indent + "    " + "|-"); fArg.prettyPrint(indent + "    ");
        }

        System.out.print(indent + "|-");_body.prettyPrint(indent + "    ");

    }

    public List<String> getDiagnostics() {
        return _diagnostics;
    }

}
