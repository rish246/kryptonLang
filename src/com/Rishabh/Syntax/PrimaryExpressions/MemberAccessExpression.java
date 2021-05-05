package com.Rishabh.Syntax.PrimaryExpressions;

import com.Rishabh.EvalResult;
import com.Rishabh.ExpressionType;
import com.Rishabh.Syntax.Expression;
import com.Rishabh.Token;
import com.Rishabh.Utilities.Environment;

import java.util.ArrayList;
import java.util.List;

public class MemberAccessExpression extends Expression {
    public EvalResult _objectEnv = null;
    public Token _memberName;
    public List<String> _diagnostics = new ArrayList<>();
    public MemberAccessExpression(Token memberName, int lineNumber) {
        super(ExpressionType.MemberAccessExpression, lineNumber);
        _memberName = memberName;
    }

    public EvalResult evaluate(Environment env) throws Exception {
        EvalResult entry = env.get(_memberName._lexeme);
        // insert node
        if(entry == null) {
            _diagnostics.add("Unknown member " + _memberName._lexeme + " at line number " + getLineNumber());
            return null;
        }

        return entry;
    }

    public void prettyPrint(String indent) {
        System.out.println(_memberName._lexeme);
    }

    public List<String> getDiagnostics() {
        return _diagnostics;
    }

}
// a.m() ->
//  -> a.m -> It will be evaluated in an environment where this will be bound to a
//      a.m
