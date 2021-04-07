package com.Rishabh.Expression;

import com.Rishabh.EvalResult;
import com.Rishabh.ExpressionType;
import com.Rishabh.Token;
import com.Rishabh.TokenType;
import com.Rishabh.Utilities.Environment;
import com.Rishabh.Utilities.Symbol;

import java.util.ArrayList;
import java.util.List;

public class AssignmentExpression extends Expression {
    Token _left;
    TokenType _operatorToken; // Assignment Token Always
    Expression _right;
    //    ExpressionType _type;
    List<String> _diagnostics = new ArrayList<>();

    public AssignmentExpression(Token left, TokenType operatorToken, Expression right) {
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
        System.out.println(_left._lexeme + indent);
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
        // Create an entry in the environment
        // evaluate right under env
        // Create a new IdentifierExpression -> (value and type of right's res)
        // Create an entry in env
        EvalResult rightRes = _right.evaluate(env);
        _diagnostics.addAll(_right.getDiagnostics());

        if(_diagnostics.size() > 0)
            return null;


        String rightType = rightRes._type;
        Object rightValue = rightRes._value;

        Symbol res = env.addEntry(_left._lexeme, new Symbol(_left._lexeme, rightValue, rightType));
//        System.out.println(res._value); // res is null here
        return new EvalResult(res._value, res._type);

//        return new EvalResult(res._value, res._type);

    }

}
