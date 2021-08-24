package com.Krypton.Syntax.PrimaryExpressions;

import com.Krypton.EvalResult;
import com.Krypton.ExpressionType;
import com.Krypton.Syntax.Expression;
import com.Krypton.TokenType;
import com.Krypton.Utilities.Environment;
import com.Krypton.Utilities.Evaluator;

import java.util.ArrayList;
import java.util.List;


public class AssignmentExpression extends Expression {
    Expression _left;
    TokenType _operatorToken; // Assignment Token Always
    Expression _right;
    List<String> _diagnostics = new ArrayList<>();
    Evaluator evaluator;

    public AssignmentExpression(Expression left, TokenType operatorToken, Expression right, int lineNumber) {
        super(ExpressionType.AssignmentExpression, lineNumber);
        _left = left;
        _operatorToken = operatorToken;
        _right = right;
        evaluator = new Evaluator(lineNumber);
    }

    @Override
    public void prettyPrint(String indent) {

        System.out.println(_operatorToken);
        System.out.println(indent + "|");
        System.out.print(indent + "├──");
        _left.prettyPrint(indent + "    ");
        System.out.println(indent + "|");
        System.out.print(indent + "└──");
        _right.prettyPrint(indent + "    ");
    }

    @Override
    public List<String> getDiagnostics() {
        return _diagnostics;
    }

    public EvalResult evaluate(Environment env) throws Exception {
        try {
            EvalResult rightRes = _right.evaluate(env);
            evaluator.Bind(_left, rightRes, env);
            return rightRes;
        } catch (Exception e) {
            _diagnostics.addAll(_right.getDiagnostics());
            _diagnostics.addAll(evaluator.get_diagnostics());
            throw e;
        }
    }
}

// Crumble Assignment Expression