package com.Rishabh;

import com.Rishabh.Utilities.Environment;


import java.util.List;

public class SyntaxTree {
    public ExpressionType _type;

    public SyntaxTree(ExpressionType type) {
        _type = type;
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
