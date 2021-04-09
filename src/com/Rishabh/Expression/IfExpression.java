package com.Rishabh.Expression;

import com.Rishabh.EvalResult;
import com.Rishabh.ExpressionType;
import com.Rishabh.Utilities.Environment;

import java.util.ArrayList;
import java.util.List;

public class IfExpression extends Expression {
    public Expression _conditionalBranch;
    public Expression _thenBranch;
    public Expression _elseBranch;

    public List<String> _diagnostics = new ArrayList<>();

    public IfExpression(Expression conditionalBranch, Expression thenBranch, Expression elseBranch) {
        super(ExpressionType.IfExpression);
        _conditionalBranch = conditionalBranch;
        _thenBranch = thenBranch;
        _elseBranch = elseBranch;
    }

    public EvalResult evaluate(Environment env) throws Exception {
        // evaluate conditionalBranch
        EvalResult condBranchResult = _conditionalBranch.evaluate(env);
        _diagnostics.addAll(_conditionalBranch.getDiagnostics());

        if (condBranchResult == null) {
            _diagnostics.add("Error in the conditional branch of the if Expression");
            return null;
        }


        if (!condBranchResult._type.equals("boolean")) {
            _diagnostics.add("The conditional branch in if expression need to be of type boolean");
            return null;
        } else if(_thenBranch == null) {
            _diagnostics.add("Then branch of If expression can not be null");
            return null;
        }


        boolean condBranchOutput = (boolean) condBranchResult._value;
        EvalResult ifExpResult = null;

        if (condBranchOutput) {
            ifExpResult = _thenBranch.evaluate(env);
            _diagnostics.addAll(_thenBranch.getDiagnostics());
        }
        else if (_elseBranch != null) {
            ifExpResult = _elseBranch.evaluate(env);
            _diagnostics.addAll(_elseBranch.getDiagnostics());
        }


        if (ifExpResult == null) {
            return null;
        }


        return new EvalResult(ifExpResult._value, "ifExpression");
    }

    public void prettyPrint(String indent) {
        System.out.println(indent + "IfExpression");

        System.out.print(indent + "|-");_conditionalBranch.prettyPrint(indent + "    ");
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
