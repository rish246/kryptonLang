package com.Rishabh.Expression;

import com.Rishabh.EvalResult;
import com.Rishabh.ExpressionType;
import com.Rishabh.Utilities.Environment;



import java.util.List;

public class Expression {
    public ExpressionType _type;

    public Expression(ExpressionType type) {
        _type = type;
    }

    public ExpressionType getType() {
        return _type;
    }

    public EvalResult evaluate(Environment env) throws Exception {
        return null;
    }

    public void prettyPrint(String indent) {
    }

    public List<String> getDiagnostics() {
        return List.of();
    }

    public boolean isExpressionPrimary() {
        return true;
    }

}
