package com.Rishabh.Syntax.Statements;

import com.Rishabh.EvalResult;
import com.Rishabh.ExpressionType;
import com.Rishabh.Syntax.Statement;
import com.Rishabh.SyntaxTree;
import com.Rishabh.Utilities.Environment;

import java.util.ArrayList;
import java.util.List;

public class WhileStatement extends Statement {
    public SyntaxTree _conditionalBranch;
    public SyntaxTree _body;

    public List<String> _diagnostics = new ArrayList<>();

    public WhileStatement(SyntaxTree conditionalBranch, SyntaxTree body) {
        super(ExpressionType.WhileExpression);
        _conditionalBranch = conditionalBranch;
        _body = body;
    }

    public EvalResult evaluate(Environment env) throws Exception {
        // evaluate conditionalBranch
        EvalResult condBranchResult = _conditionalBranch.evaluate(env);
        _diagnostics.addAll(_conditionalBranch.getDiagnostics());

        if(_diagnostics.size() > 0)
            return null;

        if (!condBranchResult._type.equals("boolean")) {
            _diagnostics.add("The conditional branch in while expression need to be of type boolean");
            return null;
        }

        while((boolean) _conditionalBranch.evaluate(env)._value) {
            _body.evaluate(env);
            _diagnostics.addAll(_body.getDiagnostics());
            if(_diagnostics.size() > 0) {
                return null;
            }
        }

        return new EvalResult(null, "whileExpression");

    }

    public void prettyPrint(String indent) {
        System.out.println("WhileExpression");

        System.out.print(indent + "|-");_conditionalBranch.prettyPrint(indent + "    ");
        System.out.println(indent + "|");
        System.out.print(indent + "|-");_body.prettyPrint(indent + "    ");

    }

    public List<String> getDiagnostics() {
        return _diagnostics;
    }

    public boolean isExpressionPrimary() {
        return false;
    }
}

// Now comma separated expressions.... These needs to be evaluated ... (How an Identifier is Evaluated) //

