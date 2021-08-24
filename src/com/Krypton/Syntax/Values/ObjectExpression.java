package com.Krypton.Syntax.Values;


import com.Krypton.EvalResult;
import com.Krypton.ExpressionType;
import com.Krypton.Syntax.Expression;
import com.Krypton.Utilities.Environment;

import java.util.*;

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
            try {
                EvalResult keyRes = key.evaluate(env);
                EvalResult valueRes = value.evaluate(env);
                if( !Objects.equals(keyRes._type, "int") && !Objects.equals(keyRes._type, "string") )
                    throw new IllegalArgumentException(keyRes._type);
                finalResult.put(keyRes._value.toString(), valueRes);
            }
            catch (IllegalArgumentException e) {
                _diagnostics.add("Key should be of type int or string, found " + e.getMessage() + " at line number " + getLineNumber());
                throw e;
            }
            catch (Exception e) {
                _diagnostics.addAll(key.getDiagnostics());
                _diagnostics.addAll(value.getDiagnostics());
                throw e;
            }
        }
        return new EvalResult(finalResult, "object");
    }

    public List<String> getDiagnostics() {
        return _diagnostics;
    }
}
