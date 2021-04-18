package com.Rishabh.Expression.Values;


import com.Rishabh.EvalResult;
import com.Rishabh.Syntax.Expression;
import com.Rishabh.ExpressionType;
import com.Rishabh.SyntaxTree;
import com.Rishabh.Utilities.Environment;

import java.util.ArrayList;
import java.util.List;

public class ListExpression extends Expression {
    public List<SyntaxTree> _elements;
    public List<String> _diagnostics;

    public ListExpression(List<SyntaxTree> elements) {
        super(ExpressionType.ListExpression);
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

        for(SyntaxTree exp : _elements) {
            _results.add(exp.evaluate(env));
            _diagnostics.addAll(exp.getDiagnostics());
        }

        return new EvalResult(_results, "list");
    }

    public List<String> getDiagnostics() {
        return _diagnostics;
    }

}
