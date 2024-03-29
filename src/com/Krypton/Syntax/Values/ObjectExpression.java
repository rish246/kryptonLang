package com.Krypton.Syntax.Values;


import com.Krypton.EvalResult;
import com.Krypton.ExpressionType;
import com.Krypton.Syntax.Expression;
import com.Krypton.Utilities.Environment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ObjectExpression extends Expression {
    public Map<Expression, Expression> _contents;
    public List<String> _diagnostics;

    public ObjectExpression(Map<Expression, Expression> contents, int lineNumber) {
        super(ExpressionType.ObjectExpression, lineNumber);
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
        Map<String, EvalResult> finalResult = new HashMap<>();
        for(Map.Entry<Expression, Expression> binding : _contents.entrySet()) {
            Expression key = binding.getKey();
            Expression value = binding.getValue();


            EvalResult keyRes = key.evaluate(env);
            _diagnostics.addAll(key.getDiagnostics());

            EvalResult valueRes = value.evaluate(env);
            _diagnostics.addAll(key.getDiagnostics());

            if(_diagnostics.size() > 0) {
                return null;
            }


            if(keyRes._type != "int" && keyRes._type != "string") {
                _diagnostics.add("Key should be of type int or string, found " + keyRes._type+ " at line number " + getLineNumber());
                return null;
            }

            if(valueRes == null || (valueRes._value == null && valueRes._type != "null")) {
                _diagnostics.add("Invalid expression in the object body"+ " at line number " + getLineNumber());
                return null;
            }

            finalResult.put((String) ((keyRes._value).toString()), valueRes);
        }
        return new EvalResult(finalResult, "object");
    }

    public List<String> getDiagnostics() {
        return _diagnostics;
    }
}
