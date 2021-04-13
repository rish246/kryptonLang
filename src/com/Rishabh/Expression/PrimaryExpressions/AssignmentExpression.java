package com.Rishabh.Expression.PrimaryExpressions;

import com.Rishabh.EvalResult;
import com.Rishabh.ExpressionType;
import com.Rishabh.Token;
import com.Rishabh.TokenType;
import com.Rishabh.Expression.Expression;
import com.Rishabh.Utilities.Environment;
import com.Rishabh.Utilities.Symbol;

import java.util.ArrayList;
import java.util.List;

public class AssignmentExpression extends Expression {
    Expression _left;
    TokenType _operatorToken; // Assignment Token Always
    Expression _right;
    //    ExpressionType _type;
    List<String> _diagnostics = new ArrayList<>();

    public AssignmentExpression(Expression left, TokenType operatorToken, Expression right) {
        super(ExpressionType.AssignmentExpression);
        _left = left;
        _operatorToken = operatorToken;
        _right = right;
    }

    @Override
    public void prettyPrint(String indent) {

        System.out.println(_operatorToken);
        System.out.println(indent + "|");
        System.out.print(indent + "├──");
//        _left.prettyPrint(indent + "    ");
        // System.out.println(_left. + indent);
        _left.prettyPrint(indent + "    ");
        // Add some long lines here
        System.out.println(indent + "|");

        System.out.print(indent + "└──");
        _right.prettyPrint(indent + "    ");

    }

    @Override
    public List<String> getDiagnostics() {
        return _diagnostics;
    }


    public EvalResult evaluate(Environment env) throws Exception {

        EvalResult rightRes = _right.evaluate(env);
        _diagnostics.addAll(_right.getDiagnostics());

        if(rightRes == null)
            return null;


        String rightType = rightRes._type;
        Object rightValue = rightRes._value;

        // left's lexeme
        if(_left._type == ExpressionType.IdentifierExpression) {
            IdentifierExpression left = (IdentifierExpression) _left;
            Symbol res = env.set(left._lexeme, new Symbol(left._lexeme, rightValue, rightType));
    //        System.out.println(res._value); // res is null here
            return new EvalResult(res._value, res._type);
        }

        if(_left._type == ExpressionType.ArrayAccessExpression) {
            ArrayAccessExpression left = (ArrayAccessExpression) _left;
            String envEntry = left._identifier._lexeme;
            Expression index = left._index;

            int IndexValue = (int) index.evaluate(env)._value;

            // Get the list from env
            Symbol envList = env.get(envEntry);
            List originalList = (List) envList._value;

            originalList.set(IndexValue, rightRes);
            envList._value = originalList;

            // make changes in env
            env.set(envEntry, envList);
        }

        else {
            _diagnostics.add("Expression of type " + _left.getType() + " is not subsciptable");
        }

        return null;

//        return new EvalResult(res._value, res._type);

    }

}
