package com.Krypton.Syntax.PrimaryExpressions;

import com.Krypton.EvalResult;
import com.Krypton.ExpressionType;
import com.Krypton.Syntax.Expression;
import com.Krypton.TokenType;
import com.Krypton.Utilities.BinaryOperators.BinaryOperatorImpl;
import com.Krypton.Utilities.Environment;

import java.util.ArrayList;
import java.util.List;

public class BinaryExpression extends Expression {
    Expression _left, _right;
    TokenType _operatorToken;
    List<String> _diagnostics = new ArrayList<>();
    private BinaryOperatorImpl binaryOperator;

    public BinaryExpression(Expression left, TokenType operatorToken, Expression right, int lineNumber) {
        super(ExpressionType.BinaryExpression, lineNumber);
        _left = left;
        _operatorToken = operatorToken;
        _right = right;
        binaryOperator = new BinaryOperatorImpl(lineNumber);
    }


    @Override
    public void prettyPrint(String indent) {

        System.out.println(_operatorToken);
        System.out.println(indent + "|");
        System.out.print(indent + "├──");
        _left.prettyPrint(indent + "    ");
        // Add some long lines here
        System.out.println("|");
        System.out.println(indent + "|");

        System.out.print(indent + "└──");
        _right.prettyPrint(indent + "    ");

    }



    @Override
    public EvalResult evaluate(Environment env) throws Exception {
        try {
            return binaryOperator.operateOn(this, env);
        } catch (Exception e) {
            _diagnostics.addAll(binaryOperator.getDiagnostics());
            throw e;
        }
    }

    public Expression getLeft() {
        return _left;
    }

    public Expression getRight() {
        return _right;
    }

    public TokenType getOperatorToken() {
        return _operatorToken;
    }

    @Override
    public List<String> getDiagnostics() {
        return _diagnostics;
    }

}
