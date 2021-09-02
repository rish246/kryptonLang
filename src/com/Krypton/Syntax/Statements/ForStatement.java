package com.Krypton.Syntax.Statements;

import com.Krypton.EvalResult;
import com.Krypton.ExpressionType;
import com.Krypton.Syntax.Expression;
import com.Krypton.Syntax.Statement;
import com.Krypton.SyntaxTree;
import com.Krypton.Utilities.Environment;

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

        try {
            Environment newEnv = new Environment(env);
            _initializationCond.evaluate(newEnv);
            EvalResult haltCondResult = _haltingCondition.evaluate(newEnv);

            while((boolean)haltCondResult.getValue()) {
                EvalResult bodyResult = _body.evaluate(newEnv);

                if(!bodyResult.equals(new EvalResult(null, "null")))
                    return bodyResult;

                _progressExp.evaluate(newEnv);
                haltCondResult = _haltingCondition.evaluate(newEnv);
            }
            return new EvalResult(null, "null");
        } catch (Exception e) {
            _diagnostics.addAll(_initializationCond.getDiagnostics());
            _diagnostics.addAll(_haltingCondition.getDiagnostics());
            _diagnostics.addAll(_body.getDiagnostics());
            _diagnostics.addAll(_progressExp.getDiagnostics());
            throw e;
        }
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


}