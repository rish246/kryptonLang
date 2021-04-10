package com.Rishabh.Expression;

import com.Rishabh.EvalResult;
import com.Rishabh.ExpressionType;
import com.Rishabh.Token;
import com.Rishabh.TokenType;
import com.Rishabh.Utilities.Environment;
import com.Rishabh.Utilities.Symbol;

import java.util.ArrayList;
import java.util.List;

public class ReturnExpression extends Expression {
    Expression _body;
    //    ExpressionType _type;
    List<String> _diagnostics = new ArrayList<>();

    public ReturnExpression(Expression body) {
        super(ExpressionType.ReturnExpression);
        _body = body;
    }

    @Override
    public void prettyPrint(String indent) {

        System.out.println("Return expression");
        System.out.println(indent + "|");
        System.out.print(indent + "├──");
//        _left.prettyPrint(indent + "    ");
        _body.prettyPrint(indent + "    ");
    }

    @Override
    public List<String> getDiagnostics() {
        return _diagnostics;
    }


    public EvalResult evaluate(Environment env) throws Exception {

        EvalResult bodyResult = _body.evaluate(env);
        _diagnostics.addAll(_body.getDiagnostics());
        if(bodyResult._value == null || bodyResult._type == null) {
            return null;
        }

        return bodyResult;

    }

}