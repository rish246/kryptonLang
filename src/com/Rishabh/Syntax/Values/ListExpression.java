package com.Rishabh.Expression.Values;


import com.Rishabh.EvalResult;
import com.Rishabh.Syntax.Expression;
import com.Rishabh.ExpressionType;
import com.Rishabh.SyntaxTree;
import com.Rishabh.Utilities.Environment;

import java.util.ArrayList;
import java.util.List;

public class ListExpression extends Expression {
    public List<Expression> _elements;
    public List<String> _diagnostics;

    public ListExpression(List<Expression> elements, int lineNumber) {
        super(ExpressionType.ListExpression, lineNumber);
        _elements = elements;
        _diagnostics = new ArrayList<>();
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
            _diagnostics.addAll(exp.getDiagnostics());
            if(_diagnostics.size() > 0) {
                return null;
            }
        }

        return new EvalResult(_results, "list");
    }

    public List<String> getDiagnostics() {
        return _diagnostics;
    }

}
