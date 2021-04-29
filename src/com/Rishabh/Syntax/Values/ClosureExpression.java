package com.Rishabh.Syntax.Values;

import com.Rishabh.EvalResult;
import com.Rishabh.Syntax.Expression;
import com.Rishabh.Syntax.PrimaryExpressions.IdentifierExpression;
import com.Rishabh.ExpressionType;
import com.Rishabh.SyntaxTree;
import com.Rishabh.Utilities.Environment;

import java.util.ArrayList;
import java.util.List;

public class ClosureExpression extends Expression {
    public SyntaxTree _functionBody;
    public Environment _closureEnv;
    public String _name;
    public List<Expression> _formalArgs;

    public List<String> _diagnostics = new ArrayList<>();

    public ClosureExpression(String name, SyntaxTree funcBody, Environment closureEnv, List<Expression> formalArgs, int lineNumber) {
        super(ExpressionType.ClosureExpression, lineNumber);
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

// FunctionBody -> closureExpression