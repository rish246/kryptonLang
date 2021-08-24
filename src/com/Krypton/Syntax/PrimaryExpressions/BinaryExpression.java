package com.Krypton.Syntax.PrimaryExpressions;

import com.Krypton.EvalResult;
import com.Krypton.ExpressionType;
import com.Krypton.Syntax.Expression;
import com.Krypton.TokenType;
import com.Krypton.Utilities.BinaryOperators.Adder;
import com.Krypton.Utilities.CustomExceptions.BinaryOperators.InvalidOperationException;
import com.Krypton.Utilities.Environment;

import java.util.ArrayList;
import java.util.List;

public class BinaryExpression extends Expression {
    Expression _left, _right;
    TokenType _operatorToken;
    //    ExpressionType _type;
    List<String> _diagnostics = new ArrayList<>();
    Adder adder;

    public BinaryExpression(Expression left, TokenType operatorToken, Expression right, int lineNumber) {
        super(ExpressionType.BinaryExpression, lineNumber);
        _left = left;
        _operatorToken = operatorToken;
        _right = right;
        adder = new Adder(_left, _right);
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
    public List<String> getDiagnostics() {
        return _diagnostics;
    }


    // It'll take a lot of fucking time to do... (Tomorrow ---> Do this)
    @Override
    public EvalResult evaluate(Environment env) throws Exception {
        // Try running this code now
        try {
            switch (_operatorToken) {
                // Only check for adding different members
                case AddToken: {
                    return adder.add(env);
                }
            }

            throw new InvalidOperationException("Invalid Binary operator " + _operatorToken + " For types " + _left.getType() + " and " + _right.getType());
        } catch (InvalidOperationException e) {
            _diagnostics.addAll(adder.getDiagnostics());
            throw e;
        }


    }




}
