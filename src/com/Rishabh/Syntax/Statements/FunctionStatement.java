package com.Rishabh.Syntax.Statements;

import com.Rishabh.EvalResult;
import com.Rishabh.ExpressionType;
import com.Rishabh.Syntax.PrimaryExpressions.IdentifierExpression;
import com.Rishabh.Syntax.Statement;
import com.Rishabh.Syntax.Values.ClosureExpression;
import com.Rishabh.SyntaxTree;
import com.Rishabh.Utilities.Environment;
import com.Rishabh.Utilities.Symbol;

import java.util.ArrayList;
import java.util.List;

public class FunctionStatement extends Statement {
    public SyntaxTree _body;
    public String _name;
    public List<IdentifierExpression> _formalArgs = new ArrayList<>();

    public List<String> _diagnostics = new ArrayList<>();

    public FunctionStatement(String name, SyntaxTree body, List<IdentifierExpression> formalArgs, int lineNumber) {
        super(ExpressionType.FuncExpression, lineNumber);
        _name = name;
        _body = body;
        _formalArgs = formalArgs;
    }

    public EvalResult evaluate(Environment env) throws Exception {

        Environment closureEnv = new Environment(env._table, env._ParentEnv);

        ClosureExpression funcClosure = new ClosureExpression(_name, _body, closureEnv, _formalArgs, getLineNumber());

        Symbol newClosure = new Symbol("closure", funcClosure, "Closure");

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

    public boolean isExpressionPrimary() {
        return _name == null;
    }
}
