package com.Rishabh.Expression.Statements;

import com.Rishabh.EvalResult;
import com.Rishabh.ExpressionType;
import com.Rishabh.Syntax.Expression;
import com.Rishabh.Syntax.Statement;
import com.Rishabh.SyntaxTree;
import com.Rishabh.Utilities.Environment;

import java.util.ArrayList;
import java.util.List;
public class ReturnStatement extends Statement {
    public Expression _body;
    //    ExpressionType _type;
    List<String> _diagnostics = new ArrayList<>();

    public ReturnStatement(Expression body, int lineNumber) {
        super(ExpressionType.ReturnExpression, lineNumber);
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

        if(bodyResult == null) {
            _diagnostics.add("Error in the body of return statement"+ " at line number " + getLineNumber());
            return null;
        }


        if(bodyResult._value == null && bodyResult._type != "null") {
            _diagnostics.add("Error in the body of return statement"+ " at line number " + getLineNumber());
            return null;
        }

        return bodyResult;

    }

    public boolean isExpressionPrimary() {
        return false;
    }

}