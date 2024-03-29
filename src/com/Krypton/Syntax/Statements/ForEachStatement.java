package com.Krypton.Syntax.Statements;

import com.Krypton.EvalResult;
import com.Krypton.ExpressionType;
import com.Krypton.Syntax.Expression;
import com.Krypton.Syntax.Statement;
import com.Krypton.SyntaxTree;
import com.Krypton.Utilities.Environment;
import com.Krypton.Utilities.Evaluator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ForEachStatement extends Statement {
    public Expression _iterator;
    public Expression _iterable;
    public SyntaxTree _body;
    private Evaluator evaluator;
    public List<String> _diagnostics = new ArrayList<>();

    public ForEachStatement(Expression iterator, Expression iterable, SyntaxTree body, int lineNumber){
        super(ExpressionType.ForLoopExpression, lineNumber);
        _iterator = iterator;
        _iterable = iterable;
        _body = body;
        evaluator = new Evaluator(lineNumber);
    }

    public EvalResult evaluate(Environment env) throws Exception {
        EvalResult ourIterable = _iterable.evaluate(env);
        _diagnostics.addAll(_iterable.getDiagnostics());
        if(_diagnostics.size() > 0) {
            return null;
        }
        if(ourIterable._type == "list" || ourIterable._type == "object") {
            return Bind(_iterator, env, ourIterable);
        }
        else {
            _diagnostics.add("Items of type " + ourIterable._type + " are not iterable");
        }

        return new EvalResult(null, "forEachExpression");

    }

    /////////////////////////////////////////////////////////////////////////////////

    private EvalResult Bind(Expression left, Environment env, EvalResult right) throws Exception {

        if(right._type == "list") {
            return iterateList(left, env, right);
        }

        if(right._type == "object") {
            return iterateObject(left, env, right);
        }
        return null;
    }

    private EvalResult iterateList(Expression _iterator, Environment env, EvalResult ourIterable) throws Exception {
        List<EvalResult> ourList = (List) ourIterable._value;

        for (EvalResult element : ourList) {
//            AssignmentExpression.Bind(_iterator, element, env, _diagnostics, getLineNumber());
            evaluator.Bind(_iterator, element, env);
            EvalResult bodyResult = _body.evaluate(env);

            _diagnostics.addAll(_body.getDiagnostics());
            if(_diagnostics.size() > 0) {
                return null;
            }

            if(bodyResult._value != null) {
                return bodyResult;
            }
        }
        return null;
    }

    // Iterate through the object
    private EvalResult iterateObject(Expression left, Environment env, EvalResult ourIterable) throws Exception {
        Map<String, EvalResult> ourObject = (HashMap) ourIterable._value;

        for(Map.Entry<String, EvalResult> binding : ourObject.entrySet()) {
            EvalResult keyValuePair = getKeyValuePair(binding);
            evaluator.Bind(_iterator, keyValuePair, env);

            if(_diagnostics.size() > 0)
                return null;

            EvalResult bodyResult = _body.evaluate(env);
            _diagnostics.addAll(_body.getDiagnostics());

            if(_diagnostics.size() > 0)
                return null;

            if(bodyResult._value != null)
                return bodyResult;

        }
        return null;
    }

    private EvalResult getKeyValuePair(Map.Entry<String, EvalResult> binding) {
        EvalResult key = new EvalResult(binding.getKey(), "string");
        EvalResult value = binding.getValue();

        List<EvalResult> keyValueBinding = new ArrayList<>();
        keyValueBinding.add(key);
        keyValueBinding.add(value);
        return new EvalResult(keyValueBinding, "list");
    }

    public void prettyPrint(String indent) {
        System.out.println("ForEachExpression");

        System.out.print(indent + "|-");_iterator.prettyPrint(indent + "    ");
        System.out.println(indent + "|");

        System.out.print(indent + "|-");_iterable.prettyPrint(indent + "    ");
        System.out.println(indent + "|");

        System.out.print(indent + "|-");_body.prettyPrint(indent + "    ");

    }

    public List<String> getDiagnostics() {
        return _diagnostics;
    }

}