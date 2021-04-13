package com.Rishabh.Expression.Statements;

import com.Rishabh.EvalResult;
import com.Rishabh.ExpressionType;
import com.Rishabh.Expression.Expression;
import com.Rishabh.Utilities.Environment;

import java.util.ArrayList;
import java.util.List;

public class ForExpression extends Expression {
    public Expression _initializationCond;
    public Expression _haltingCondition;
    public Expression _progressExp;
    public Expression _body;

    public List<String> _diagnostics = new ArrayList<>();

    public ForExpression(Expression initializationCond, Expression haltingCondition, Expression progressExp, Expression body) {
        super(ExpressionType.ForLoopExpression);
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
            _diagnostics.add("Error in the for loop body");
            return null;
        }

        while((boolean)haltCondResult._value) {
            _body.evaluate(newEnv);
            _diagnostics.addAll(_body.getDiagnostics());

            _progressExp.evaluate(newEnv);
            _diagnostics.addAll(_progressExp.getDiagnostics());
            if(_diagnostics.size() > 0) {
                return null;
            }

            haltCondResult = _haltingCondition.evaluate(newEnv);
        }

        return new EvalResult(null, "forExpression");

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