package com.Rishabh.Expression;

import com.Rishabh.EvalResult;
import com.Rishabh.ExpressionType;

import java.util.ArrayList;
import java.util.List;

public class Expression {
    ExpressionType _type;

    Expression(ExpressionType type) {
        _type = type;
    }

    ExpressionType getType() {
        return _type;
    }

    public EvalResult evaluate() throws Exception {
        return null;
    }

    public void prettyPrint(String indent) {
    }

    public List<String> getDiagnostics() {
        return new ArrayList();
    }

}
