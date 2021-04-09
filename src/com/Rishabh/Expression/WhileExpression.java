package com.Rishabh.Expression;

import com.Rishabh.EvalResult;
import com.Rishabh.ExpressionType;
import com.Rishabh.Utilities.Environment;

import java.util.ArrayList;
import java.util.List;

public class WhileExpression extends Expression {
    public Expression _conditionalBranch;
    public Expression _body;

    public List<String> _diagnostics = new ArrayList<>();

    public WhileExpression(Expression conditionalBranch, Expression body) {
        super(ExpressionType.WhileExpression);
        _conditionalBranch = conditionalBranch;
        _body = body;
    }

    public EvalResult evaluate(Environment env) throws Exception {
        // evaluate conditionalBranch
        EvalResult condBranchResult = _conditionalBranch.evaluate(env);

        if (!condBranchResult._type.equals("boolean")) {
            _diagnostics.add("The conditional branch in while expression need to be of type boolean");
            return null;
        }

        while((boolean) _conditionalBranch.evaluate(env)._value) {
            _body.evaluate(env);
            _diagnostics.addAll(_body.getDiagnostics());
        }

        return new EvalResult(null, "whileExpression");

    }

    public void prettyPrint(String indent) {
        System.out.println(indent + "WhileExpression");

        System.out.print(indent + "|-");_conditionalBranch.prettyPrint(indent + "    ");
        System.out.print(indent + "|-");_body.prettyPrint(indent + "    ");

    }

    public List<String> getDiagnostics() {
        return _diagnostics;
    }
}

//List ->
//      , Comma separated Expressions
//      --> Evaluate those
//      , EvalResult <Value, Type>
//      ArrayList<EvalResult>
//      [
//          parseList()
//          --> ListExpressions separated by ;
//      ]
