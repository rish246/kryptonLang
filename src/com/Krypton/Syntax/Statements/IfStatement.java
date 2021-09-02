package com.Krypton.Syntax.Statements;

import com.Krypton.EvalResult;
import com.Krypton.ExpressionType;
import com.Krypton.Syntax.Expression;
import com.Krypton.Syntax.Statement;
import com.Krypton.SyntaxTree;
import com.Krypton.Utilities.Environment;

import java.util.ArrayList;
import java.util.List;

public class IfStatement extends Statement {
    public Expression _conditionalBranch;
    public SyntaxTree _thenBranch;
    public SyntaxTree _elseBranch;

    public List<String> _diagnostics = new ArrayList<>();

    public IfStatement(Expression conditionalBranch, SyntaxTree thenBranch, SyntaxTree elseBranch, int lineNumber) {
        super(ExpressionType.IfExpression, lineNumber);
        _conditionalBranch = conditionalBranch;
        _thenBranch = thenBranch;
        _elseBranch = elseBranch;
    }

    public EvalResult evaluate(Environment env) throws Exception {
        // evaluate conditionalBranch
        try {
            EvalResult condBranchResult = _conditionalBranch.evaluate(env);
            if (!condBranchResult.getType().equals("boolean"))
                throw new Exception("The conditional branch in if expression need to be of type boolean"+ " at line number " + getLineNumber());

            boolean condBranchOutput = (boolean) condBranchResult.getValue();

            if (condBranchOutput)
                return _thenBranch.evaluate(env);

            if (_elseBranch != null)
                return _elseBranch.evaluate(env);

            return new EvalResult(null, "null");
        } catch (Exception e) {
            _diagnostics.addAll(_conditionalBranch.getDiagnostics());
            _diagnostics.addAll(_thenBranch.getDiagnostics());
            _diagnostics.addAll(_elseBranch.getDiagnostics());
            _diagnostics.add(e.getMessage());
            throw e;
        }
    }

    public void prettyPrint(String indent) {
        System.out.println(indent + "IfExpression");

        System.out.print(indent + "|-");_conditionalBranch.prettyPrint(indent + "    ");
        System.out.println(indent + "|");

        System.out.print(indent + "|-");_thenBranch.prettyPrint(indent + "    ");
        if(_elseBranch != null) {
            System.out.print(indent + "|_");
            _elseBranch.prettyPrint(indent + "    ");
        }
    }

    public List<String> getDiagnostics() {
        return _diagnostics;
    }

}

/*
    IfStatementNext()
 */