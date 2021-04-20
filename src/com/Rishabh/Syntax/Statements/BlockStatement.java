package com.Rishabh.Syntax.Statements;

import com.Rishabh.EvalResult;
import com.Rishabh.ExpressionType;
import com.Rishabh.Syntax.Statement;
import com.Rishabh.SyntaxTree;
import com.Rishabh.Utilities.Environment;

import java.util.ArrayList;
import java.util.List;

public class BlockStatement extends Statement {
    public List<SyntaxTree> _expressionList;
    private List<String> _diagnostics = new ArrayList<>();

    public BlockStatement(List<SyntaxTree> expressions) {
        super(ExpressionType.BlockExpression);
        _expressionList = expressions;
    }

    public EvalResult evaluate(Environment env) throws Exception {
        // Create a new Env and as a parent, add the env
        Environment currentBlockEnv = new Environment(env);

        for(SyntaxTree exp : _expressionList) {
            if(exp == null) {
                return new EvalResult(null, null);
            }

            EvalResult curExpResult = exp.evaluate(currentBlockEnv);
            _diagnostics.addAll(exp.getDiagnostics());

            if(_diagnostics.size() > 0) {
                return null;
            }

            if(exp.isStatement() && curExpResult != null && curExpResult._value != null) {
                return curExpResult;
            }

        }

        return new EvalResult(null, "null");
    }

    public void prettyPrint(String indent) {
        System.out.println("Block Expression");
        for(SyntaxTree exp : _expressionList) {
            System.out.print(indent + "├──");
            exp.prettyPrint(indent + "    ");
            System.out.println(indent + "|");

        }
    }

    public List<String> getDiagnostics() {
        return _diagnostics;
    }

    public boolean isExpressionPrimary() {
        return false;
    }



}
