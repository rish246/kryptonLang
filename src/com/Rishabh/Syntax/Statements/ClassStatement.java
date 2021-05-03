package com.Rishabh.Syntax.Statements;

import com.Rishabh.EvalResult;
import com.Rishabh.ExpressionType;
import com.Rishabh.Syntax.Statement;
import com.Rishabh.SyntaxTree;
import com.Rishabh.Token;
import com.Rishabh.Utilities.Environment;

import java.util.ArrayList;
import java.util.List;

public class ClassStatement extends Statement {
    public Token _name;
    public SyntaxTree _body;

    public List<String> _diagnostics = new ArrayList<>();

    public ClassStatement(Token name, SyntaxTree body, int lineNumber) {
        super(ExpressionType.ForLoopExpression, lineNumber);
        _name = name;
        _body = body;
    }

    public EvalResult evaluate(Environment env) throws Exception {
        return null;
    }

    public void prettyPrint(String indent) {
        System.out.println("Class " + _name._lexeme);

        System.out.print(indent + "|-");_body.prettyPrint(indent + "    ");

    }

    public List<String> getDiagnostics() {
        return _diagnostics;
    }

    public boolean isExpressionPrimary() {
        return false;
    }
}