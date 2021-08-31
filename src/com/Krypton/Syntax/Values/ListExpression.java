package com.Krypton.Syntax.Values;


import com.Krypton.EvalResult;
import com.Krypton.ExpressionType;
import com.Krypton.Syntax.Expression;
import com.Krypton.Utilities.Environment;

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
            try {
                _results.add(exp.evaluate(env));
            } catch (Exception e) {
                _diagnostics.addAll(exp.getDiagnostics());
                String errorMessage = "Error in the list body, at line number " + getLineNumber();
                _diagnostics.add(errorMessage);
                throw new Exception(errorMessage);
            }
        }
        return new EvalResult(_results, "list");
    }

    public List<String> getDiagnostics() {
        return _diagnostics;
    }

    public List<Expression> getElements() {
        return _elements;
    }


}
