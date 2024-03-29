package com.Krypton.Syntax.Statements;

import com.Krypton.EvalResult;
import com.Krypton.ExpressionType;
import com.Krypton.Syntax.Expression;
import com.Krypton.Syntax.Statement;
import com.Krypton.SyntaxTree;
import com.Krypton.Utilities.Environment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
        EvalResult condBranchResult = _conditionalBranch.evaluate(env);
        _diagnostics.addAll(_conditionalBranch.getDiagnostics());

        if(_diagnostics.size() > 0)
            return null;

        if (!condBranchResult._type.equals("boolean")) {
            _diagnostics.add("The conditional branch in while expression need to be of type boolean"+ " at line number " + getLineNumber());
            return null;
        }

        while((boolean) _conditionalBranch.evaluate(env)._value) {
            EvalResult bodyResult = _body.evaluate(env);
            _diagnostics.addAll(_body.getDiagnostics());
            if(_diagnostics.size() > 0) {
                return null;
            }

            if(bodyResult._value != null && !Objects.equals(bodyResult._type, "null")) {
                return bodyResult;
            }
        }

        return new EvalResult(null, "null");

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
