package com.Rishabh.Syntax;


import com.Rishabh.ExpressionType;
import com.Rishabh.SyntaxTree;

public class Expression extends SyntaxTree {
    public ExpressionType _type;

    public Expression(ExpressionType type) {
        super(type);
    }

}
