package com.Krypton.Syntax.Statements;

import com.Krypton.EvalResult;
import com.Krypton.ExpressionType;
import com.Krypton.Syntax.Expression;
import com.Krypton.Syntax.Statement;
import com.Krypton.Syntax.Values.ClosureExpression;
import com.Krypton.SyntaxTree;
import com.Krypton.Utilities.Environment;

import java.util.ArrayList;
import java.util.List;

public class FunctionStatement extends Statement {
    public SyntaxTree _body;
    public String _name;
    public List<Expression> _formalArgs = new ArrayList<>();

    public List<String> _diagnostics = new ArrayList<>();

    public FunctionStatement(String name, SyntaxTree body, List<Expression> formalArgs, int lineNumber) {
        super(ExpressionType.FuncExpression, lineNumber);
        _name = name;
        _body = body;
        _formalArgs = formalArgs;
    }

    public EvalResult evaluate(Environment env) throws Exception {

        Environment closureEnv = new Environment(env._table, env._ParentEnv);

        ClosureExpression funcClosure = new ClosureExpression(_name, _body, closureEnv, _formalArgs, getLineNumber());

        EvalResult newClosure = new EvalResult(funcClosure, "Closure"); // take this.. Extract funcClosure from this

        env.set(_name, newClosure);

        return new EvalResult(null, "functionExpression");
    }

    public void prettyPrint(String indent) {
        System.out.println("Function");
        System.out.println(indent + "|- " + indent + _name);
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
