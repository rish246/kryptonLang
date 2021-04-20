package com.Rishabh;

import com.Rishabh.Utilities.Environment;


import java.util.List;

public class SyntaxTree {
    public ExpressionType _type;

    public int getLineNumber() {
        return _lineNumber;
    }

    public int _lineNumber;

    public SyntaxTree(ExpressionType type, int lineNumber) {
        _type = type;
        _lineNumber = lineNumber;
    }

    public ExpressionType getType() {
        return _type;
    }

    public EvalResult evaluate(Environment env) throws Exception {
        return null;
    }

    public void prettyPrint(String indent) {
    }

    public List<String> getDiagnostics() {
        return List.of();
    }

    public boolean isExpressionPrimary() {
        return true;
    }

    public boolean isStatement() {
        return false;
    }

}
