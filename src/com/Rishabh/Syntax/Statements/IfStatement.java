package com.Rishabh.Syntax.Statements;

import com.Rishabh.EvalResult;
import com.Rishabh.ExpressionType;
import com.Rishabh.Syntax.Expression;
import com.Rishabh.Syntax.Statement;
import com.Rishabh.SyntaxTree;
import com.Rishabh.Utilities.Environment;

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
        EvalResult condBranchResult = _conditionalBranch.evaluate(env);
        _diagnostics.addAll(_conditionalBranch.getDiagnostics());

        if (condBranchResult == null) {
            _diagnostics.add("Error in the conditional branch of the if Expression"+ " at line number " + getLineNumber());
            return null;
        }
        else if (!condBranchResult._type.equals("boolean")) {
            _diagnostics.add("The conditional branch in if expression need to be of type boolean"+ " at line number " + getLineNumber());
            return null;
        } else if(_thenBranch == null) {
            _diagnostics.add("Then branch of If expression can not be null"+ " at line number " + getLineNumber());
            return null;
        }


        boolean condBranchOutput = (boolean) condBranchResult._value;

        if (condBranchOutput) {
            EvalResult ifExpResult = _thenBranch.evaluate(env);
            _diagnostics.addAll(_thenBranch.getDiagnostics());

            if(_diagnostics.size() > 0) {
                return null;
            }

            return ifExpResult;
        }
        else if (_elseBranch != null) {
            EvalResult ifExpResult = _elseBranch.evaluate(env);
            _diagnostics.addAll(_elseBranch.getDiagnostics());
            if(_diagnostics.size() > 0) {
                return null;
            }

            return ifExpResult;
        }


        return new EvalResult(null, "ifExpression");

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