package com.Rishabh.Expression;

import com.Rishabh.EvalResult;
import com.Rishabh.ExpressionType;
import com.Rishabh.Utilities.Environment;
import com.Rishabh.Utilities.Symbol;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FunctionExpression extends Expression {
    public Expression _body;
    public String _name;
    public List<IdentifierExpression> _formalArgs = new ArrayList<>();

    public List<String> _diagnostics = new ArrayList<>();

    public FunctionExpression(String name, Expression body, List<IdentifierExpression> formalArgs) {
        super(ExpressionType.FuncExpression);
        _name = name;
        _body = body;
        _formalArgs = formalArgs;
    }

    public EvalResult evaluate(Environment env) throws Exception {
        // Make a closure
        // Add an entry to the env
        Environment closureEnv = new Environment(env._table, env._ParentEnv);

        ClosureExpression funcClosure = new ClosureExpression(this, closureEnv);
        Symbol newClosure = new Symbol("closure", funcClosure, "closure");
        env.set(_name, newClosure);

        return new EvalResult(null, "functionExpression");

    }

    public void prettyPrint(String indent) {
        System.out.println("Function");
        System.out.println(indent + "|- " + indent + _name);
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
