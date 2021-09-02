package com.Krypton.Syntax.Statements;

import com.Krypton.EvalResult;
import com.Krypton.ExpressionType;
import com.Krypton.Syntax.Expression;
import com.Krypton.Syntax.Statement;
import com.Krypton.SyntaxTree;
import com.Krypton.Utilities.Environment;

import java.util.ArrayList;
import java.util.List;

public class WhileStatement extends Statement {
    public Expression _conditionalBranch;
    public SyntaxTree _body;

    public List<String> _diagnostics = new ArrayList<>();

    public WhileStatement(Expression conditionalBranch, SyntaxTree body, int lineNumber) {
        super(ExpressionType.WhileExpression, lineNumber);
        _conditionalBranch = conditionalBranch;
        _body = body;
    }

    public EvalResult evaluate(Environment env) throws Exception {
        // evaluate conditionalBranch
        try {
            EvalResult condBranchResult = _conditionalBranch.evaluate(env);
            if (!condBranchResult.getType().equals("boolean"))
                throw new Exception("Conditional expression in while loop should be of type 'boolean', instead got " + condBranchResult.getType() + ", error at line number " + getLineNumber());

            while((boolean) _conditionalBranch.evaluate(env).getValue()) {
                EvalResult bodyResult = _body.evaluate(env);
                if (!bodyResult.equals(new EvalResult(null, "null")))
                    return bodyResult;
            }

            return new EvalResult(null, "null");
        } catch (Exception e) {
            _diagnostics.addAll(_conditionalBranch.getDiagnostics());
            _diagnostics.addAll(_body.getDiagnostics());
            _diagnostics.add(e.getMessage());
            throw e;
        }
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

}
