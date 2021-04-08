package com.Rishabh.Expression;
import com.Rishabh.EvalResult;
import com.Rishabh.ExpressionType;
import com.Rishabh.Utilities.Environment;

import java.util.ArrayList;
import java.util.List;

public class BlockExpression extends Expression{
    public List<Expression> _expressionList;
    private List<String> _diagnostics = new ArrayList<>();
    public BlockExpression(List<Expression> expressions) {
        super(ExpressionType.BlockExpression);
        _expressionList = expressions;
    }

    public EvalResult evaluate(Environment env) throws Exception {
        // Create a new Env and as a parent, add the env
        Environment currentBlockEnv = new Environment(env);

        for(Expression exp : _expressionList) {
            exp.evaluate(currentBlockEnv);
            _diagnostics.addAll(exp.getDiagnostics());
        }

        return new EvalResult(null, "Statement block");
    }

    public void prettyPrint(String indent) {
        System.out.println("Block Expression");
        System.out.println(indent + "|");
        for(Expression exp : _expressionList) {
            System.out.print(indent + "├──");
            exp.prettyPrint(indent + "    ");
        }
    }

    public List<String> getDiagnostics() {
        return _diagnostics;
    }



}
