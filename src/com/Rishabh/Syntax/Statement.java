package com.Rishabh.Syntax;

import com.Rishabh.ExpressionType;
import com.Rishabh.SyntaxTree;

public class Statement extends SyntaxTree {
    public ExpressionType _type;

    public static boolean isEnclosingStmt = false;

    public Statement(ExpressionType type) {
        super(type);
    }

    public boolean isStatement() {
        return true;
    }
}


