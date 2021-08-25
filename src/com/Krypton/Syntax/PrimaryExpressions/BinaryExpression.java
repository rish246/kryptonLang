package com.Krypton.Syntax.PrimaryExpressions;

import com.Krypton.EvalResult;
import com.Krypton.ExpressionType;
import com.Krypton.Syntax.Expression;
import com.Krypton.TokenType;
import com.Krypton.Utilities.BinaryOperators.Operator;
import com.Krypton.Utilities.CustomExceptions.BinaryOperators.InvalidOperationException;
import com.Krypton.Utilities.Environment;

import java.util.ArrayList;
import java.util.List;

public class BinaryExpression extends Expression {
    Expression _left, _right;
    TokenType _operatorToken;
    List<String> _diagnostics = new ArrayList<>();

    public BinaryExpression(Expression left, TokenType operatorToken, Expression right, int lineNumber) {
        super(ExpressionType.BinaryExpression, lineNumber);
        _left = left;
        _operatorToken = operatorToken;
        _right = right;
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
        // Try running this code now
        Operator operator = Operator.getRightOperator(this);

        try {
            return operator.operate(env);
        } catch (InvalidOperationException e) {
            _diagnostics.addAll(operator.getDiagnostics());
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
