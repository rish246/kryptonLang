package com.Krypton.Syntax.PrimaryExpressions;

import com.Krypton.EvalResult;
import com.Krypton.Syntax.Expression;
import com.Krypton.ExpressionType;
import com.Krypton.TokenType;
import com.Krypton.Utilities.Environment;

import java.util.ArrayList;
import java.util.List;

public class UnaryExpression extends Expression {
    Expression _body;
    TokenType _operatorToken;
    List<String> _diagnostics = new ArrayList<>();
//    ExpressionType _type;

    public UnaryExpression(TokenType operatorToken, Expression body, int lineNumber) {
        super(ExpressionType.UnaryExpression, lineNumber);
        _operatorToken = operatorToken;
        _body = body;
    }

    @Override
    public void prettyPrint(String indent) {

        System.out.println(_operatorToken);
        System.out.println(indent + "|");
        System.out.print(indent + "├──");
        _body.prettyPrint(indent + "    ");

    }

    @Override
    public EvalResult evaluate(Environment env) throws Exception {
        EvalResult bodyRes = _body.evaluate(env);
        _diagnostics.addAll(_body.getDiagnostics());

        if(_diagnostics.size() > 0) {
            return null;
        }

        String bodyResType = bodyRes._type;


        switch (_operatorToken) {
            case AddToken:
                if (bodyResType.equals("int")) return bodyRes;
                break;
            case SubToken:
                if (bodyResType.equals("int"))
                    return new EvalResult(-((int) bodyRes._value), "int");
                break;
            case LogicalNotToken:
                if (bodyResType.equals("boolean"))
                    return new EvalResult(!(boolean) bodyRes._value, "boolean");

                break;
            default:
                throw new Exception("Unknown binary operator" + _operatorToken+ " at line number " + getLineNumber());
        }

        _diagnostics.add("Invalid type for Unary operator " + _operatorToken + ".. Expected int got " + bodyResType+ " at line number " + getLineNumber());

        return null;
    }

    @Override
    public List<String> getDiagnostics() {
        _diagnostics.addAll(_body.getDiagnostics());
        return _diagnostics;
    }

}
//  A better error reporting in the Lexer ... Yay boi
