package com.Krypton.Syntax;

import com.Krypton.ExpressionType;
import com.Krypton.SyntaxTree;

public class Statement extends SyntaxTree {
    public ExpressionType _type;

    public static int EnclosingStatements = 0;

    public Statement(ExpressionType type, int lineNumber) {
        super(type, lineNumber);
    }

    public boolean isStatement() {
        return true;
    }

    @Override
    public boolean isExpressionPrimary() {
        return false;
    }
}


