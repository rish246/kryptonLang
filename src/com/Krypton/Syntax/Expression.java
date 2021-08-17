package com.Krypton.Syntax;


import com.Krypton.ExpressionType;
import com.Krypton.SyntaxTree;

public class Expression extends SyntaxTree {
    public ExpressionType _type;

    public Expression(ExpressionType type, int lineNumber) {
        super(type, lineNumber);
    }

}
