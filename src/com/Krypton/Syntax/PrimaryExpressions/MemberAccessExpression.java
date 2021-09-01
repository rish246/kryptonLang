package com.Krypton.Syntax.PrimaryExpressions;

import com.Krypton.EvalResult;
import com.Krypton.ExpressionType;
import com.Krypton.Syntax.Expression;
import com.Krypton.Token;
import com.Krypton.Utilities.Environment;

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

// Do this after implementing class instance