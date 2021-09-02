package com.Krypton.Syntax.Statements;

import com.Krypton.EvalResult;
import com.Krypton.ExpressionType;
import com.Krypton.Syntax.Statement;
import com.Krypton.SyntaxTree;
import com.Krypton.Utilities.Environment;

import java.util.ArrayList;
import java.util.List;

public class BlockStatement extends Statement {
    public List<SyntaxTree> _expressionList;
    private List<String> _diagnostics = new ArrayList<>();

    public BlockStatement(List<SyntaxTree> expressions, int lineNumber) {
        super(ExpressionType.BlockExpression, lineNumber);
        _expressionList = expressions;
    }

    public EvalResult evaluate(Environment env) throws Exception {
        // Create a new Env and as a parent, add the env
        Environment currentBlockEnv = new Environment(env);
        for(SyntaxTree exp : _expressionList) {
            if ( exp == null )
                return new EvalResult(null, "null");

            EvalResult curExpResult = evaluateCurTree(currentBlockEnv, exp);
            if ( !curExpResult.equals(new EvalResult(null, "null")) )
                return curExpResult;
        }
        return new EvalResult(null, "null");
    }

    private EvalResult evaluateCurTree(Environment currentBlockEnv, SyntaxTree exp) throws Exception {
        try {
            EvalResult curExpResult = exp.evaluate(currentBlockEnv);
            if ( exp.isStatement() && curExpResult != null ) {
                if ( curExpResult.getValue() != null ) return curExpResult;
            }
            return new EvalResult(null, "null");
        } catch (Exception e) {
            _diagnostics.addAll(exp.getDiagnostics());
            throw e;
        }
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





}
