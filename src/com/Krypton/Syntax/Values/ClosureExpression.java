package com.Krypton.Syntax.Values;

import com.Krypton.EvalResult;
import com.Krypton.ExpressionType;
import com.Krypton.Syntax.Expression;
import com.Krypton.SyntaxTree;
import com.Krypton.Utilities.BinaryOperators.AssignmentOperators.Binder;
import com.Krypton.Utilities.Environment;

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
        try {
//            _closureEnv._ParentEnv = env; /// This is technically wrong
            return _functionBody.evaluate(env);
        } catch (Exception e) {
            _diagnostics.add(e.getMessage());
            _diagnostics.addAll(_functionBody.getDiagnostics());
        }
        return new EvalResult(null, "functionExpression");
    }

    public Environment bindFormalArgsWithActualArgs(List<Expression> actualArgs, Environment env) throws Exception {
        Environment functionArgsEnv = new Environment(null);
        Binder binder = new Binder(getLineNumber());

        try {
            for(int i=0; i<actualArgs.size(); i++) {
                Expression curActualArg = actualArgs.get(i);
                Expression curFormalArg = _formalArgs.get(i);
                EvalResult actualArgRes = curActualArg.evaluate(env);
                binder.bindExpressionToEvalResult(curFormalArg, actualArgRes, functionArgsEnv);
            }
            functionArgsEnv._ParentEnv = _closureEnv;
            return functionArgsEnv;
        }
        catch (Exception e) {
            _diagnostics.add(e.getMessage());
            _diagnostics.addAll(binder.getDiagnostics());
            throw e;
        }
    }

    public void prettyPrint(String indent) {
        System.out.println("Closure");
        System.out.println(indent + "|");
        System.out.print(indent + "|-");_functionBody.prettyPrint(indent + "    ");
    }

    public List<String> getDiagnostics() {
        return _diagnostics;
    }
}

// FunctionBody -> closureExpression