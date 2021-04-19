package com.Rishabh.Expression.Values;


import com.Rishabh.EvalResult;
import com.Rishabh.ExpressionType;
import com.Rishabh.Syntax.Expression;
import com.Rishabh.Utilities.Environment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ObjectExpression extends Expression {
    public Map<Expression, Expression> _contents;
    public List<String> _diagnostics;

    public ObjectExpression(Map<Expression, Expression> contents) {
        super(ExpressionType.ObjectExpression);
        _contents = contents;
        _diagnostics = new ArrayList<>();
    }


    @Override
    public void prettyPrint(String indent) {
        System.out.println("Object");
        for(Map.Entry<Expression, Expression> binding : _contents.entrySet()) {
            Expression key = binding.getKey();
            Expression value = binding.getValue();
            System.out.println("|");
            System.out.println("Key");
            System.out.print("|--");
            key.prettyPrint(indent + "    ");
            System.out.println("Value");
            System.out.print("|--");
            value.prettyPrint(indent + "    ");

        }

    }

    @Override
    public EvalResult evaluate(Environment env) throws Exception{
        Map<Object, EvalResult> finalResult = new HashMap<>();
        for(Map.Entry<Expression, Expression> binding : _contents.entrySet()) {
            Expression key = binding.getKey();
            Expression value = binding.getValue();
            EvalResult keyRes = key.evaluate(env);
            EvalResult valueRes = value.evaluate(env);
            finalResult.put(keyRes._value, valueRes);

        }
        return new EvalResult(finalResult, "object");
    }

    public List<String> getDiagnostics() {
        return _diagnostics;
    }

}
