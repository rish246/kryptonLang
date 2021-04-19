package com.Rishabh.Syntax;

import com.Rishabh.ExpressionType;
import com.Rishabh.SyntaxTree;

public class Statement extends SyntaxTree {
    public ExpressionType _type;

    public static int EnclosingStatements = 0;

    public Statement(ExpressionType type) {
        super(type);
    }

    public boolean isStatement() {
        return true;
    }
}


