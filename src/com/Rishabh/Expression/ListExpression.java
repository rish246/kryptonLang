package com.Rishabh.Expression;


import com.Rishabh.EvalResult;
import com.Rishabh.ExpressionType;
import com.Rishabh.Utilities.Environment;

import java.util.ArrayList;
import java.util.List;

public class ListExpression extends Expression {
    public List<Expression> _elements;

    public ListExpression(List<Expression> elements) {
        super(ExpressionType.ListExpression);
        _elements = elements;
//        super(ExpressionType.BinaryExpression);
    }

    @Override
    public void prettyPrint(String indent) {
//        System.out.print("|");
        System.out.println(_elements);
    }

    @Override
    public EvalResult evaluate(Environment env) throws Exception{
        List<EvalResult> _results = new ArrayList<>();

        for(Expression exp : _elements) {
            _results.add(exp.evaluate(env));
        }

        return new EvalResult(_results, "list");
    }

}
