package com.Rishabh.Syntax.Statements;

import com.Rishabh.EvalResult;
import com.Rishabh.Syntax.Expression;
import com.Rishabh.ExpressionType;
import com.Rishabh.Syntax.Statement;
import com.Rishabh.SyntaxTree;
import com.Rishabh.Utilities.Environment;


import java.util.ArrayList;
import java.util.List;

public class ForStatement extends Statement {
    public Expression _initializationCond;
    public Expression _haltingCondition;
    public Expression _progressExp;
    public SyntaxTree _body;

    public List<String> _diagnostics = new ArrayList<>();

    public ForStatement(Expression initializationCond, Expression haltingCondition, Expression progressExp, SyntaxTree body, int lineNumber) {
        super(ExpressionType.ForLoopExpression, lineNumber);
        _initializationCond = initializationCond;
        _haltingCondition = haltingCondition;
        _progressExp = progressExp;
        _body = body;
    }

    public EvalResult evaluate(Environment env) throws Exception {
        // evaluate conditionalBranch
        // Create a new env
        Environment newEnv = new Environment(env);

        _initializationCond.evaluate(newEnv);
        _diagnostics.addAll(_initializationCond.getDiagnostics());

        EvalResult haltCondResult = _haltingCondition.evaluate(newEnv);
        _diagnostics.addAll(_haltingCondition.getDiagnostics());

        if(_diagnostics.size() > 0) {
            _diagnostics.add("Error in the for loop body"+ " at line number " + getLineNumber());
            return null;
        }

        while((boolean)haltCondResult._value) {
            EvalResult bodyResult = _body.evaluate(newEnv);
            _diagnostics.addAll(_body.getDiagnostics());

            if(bodyResult == null) {
                return null;
            }

            if(bodyResult._value != null) {
                return bodyResult;
            }

            _progressExp.evaluate(newEnv);
            _diagnostics.addAll(_progressExp.getDiagnostics());


            haltCondResult = _haltingCondition.evaluate(newEnv);
            _diagnostics.addAll(_haltingCondition.getDiagnostics());

            if(_diagnostics.size() > 0) {
                return null;
            }

        }

        return new EvalResult(null, "null");

    }

    public void prettyPrint(String indent) {
        System.out.println("ForExpression");

        System.out.print(indent + "|-");_initializationCond.prettyPrint(indent + "    ");
        System.out.println(indent + "|");

        System.out.print(indent + "|-");_haltingCondition.prettyPrint(indent + "    ");
        System.out.println(indent + "|");

        System.out.print(indent + "|-");_progressExp.prettyPrint(indent + "    ");
        System.out.println(indent + "|");

        System.out.print(indent + "|-");_body.prettyPrint(indent + "    ");

    }

    public List<String> getDiagnostics() {
        return _diagnostics;
    }

    public boolean isExpressionPrimary() {
        return false;
    }
}