package com.Krypton.Syntax.Statements;

import com.Krypton.EvalResult;
import com.Krypton.ExpressionType;
import com.Krypton.Syntax.Expression;
import com.Krypton.Syntax.Statement;
import com.Krypton.SyntaxTree;
import com.Krypton.Utilities.BinaryOperators.AssignmentOperators.Binder;
import com.Krypton.Utilities.CustomExceptions.BinaryOperators.InvalidOperationException;
import com.Krypton.Utilities.Environment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ForEachStatement extends Statement {
    public Expression _iterator;
    public Expression _iterable;
    public SyntaxTree _body;
    private Binder _binder;
    public List<String> _diagnostics = new ArrayList<>();

    public ForEachStatement(Expression iterator, Expression iterable, SyntaxTree body, int lineNumber){
        super(ExpressionType.ForLoopExpression, lineNumber);
        _iterator = iterator;
        _iterable = iterable;
        _body = body;
        _binder = new Binder(lineNumber);
    }

    public EvalResult evaluate(Environment env) throws Exception {
        try {
            EvalResult ourIterable = _iterable.evaluate(env);
            switch (ourIterable.getType()) {
                case "list":
                case "object":
                case "string":
                    return Bind(_iterator, env, ourIterable);
                default:
                    throw new InvalidOperationException("Object of type " + ourIterable.getType() + " is not iterable, error at line " + getLineNumber());
            }

        } catch (InvalidOperationException e) {
            _diagnostics.add(e.getMessage());
            throw e;
        } catch (Exception e) {
            _diagnostics.addAll(_iterable.getDiagnostics());
            throw e;
        }
    }

    /////////////////////////////////////////////////////////////////////////////////

    private EvalResult Bind(Expression left, Environment env, EvalResult right) throws Exception {
        if( right._type.equals("list") ) {
            return iterateList(left, env, right);
        }

        if( right._type.equals("object") ) {
            return iterateObject(left, env, right);
        }
        return null;
    }

    private EvalResult iterateList(Expression _iterator, Environment env, EvalResult ourIterable) throws Exception {
        List<EvalResult> ourList = (List) ourIterable._value;

        try {
            for (EvalResult element : ourList) {
                _binder.bindExpressionToEvalResult(_iterator, element, env);
                EvalResult bodyResult = _body.evaluate(env);
                if (!bodyResult.equals(new EvalResult(null, "null")))
                    return bodyResult;
            }
            return new EvalResult(null, "null");
        } catch (Exception e) {
            _diagnostics.addAll(_body.getDiagnostics());
            throw e;
        }
    }

    // Iterate through the object
    private EvalResult iterateObject(Expression left, Environment env, EvalResult ourIterable) throws Exception {
        Map<String, EvalResult> ourObject = (HashMap) ourIterable._value;
        try {
            for(String key : ourObject.keySet()) {
                _binder.bindExpressionToEvalResult(_iterator, new EvalResult(key, "string"), env);
                EvalResult bodyResult = _body.evaluate(env);
                if(!bodyResult.equals(new EvalResult(null, "null")))
                    return bodyResult;
            }
            return new EvalResult(null, "null");
        } catch (Exception e) {
            _diagnostics.addAll(_body.getDiagnostics());
            throw e;
        }
    }

//    private EvalResult getKeyValuePair(Map.Entry<String, EvalResult> binding) {
//        EvalResult key = new EvalResult(binding.getKey(), "string");
//        EvalResult value = binding.getValue();
//
//        List<EvalResult> keyValueBinding = new ArrayList<>();
//        keyValueBinding.add(key);
//        keyValueBinding.add(value);
//        return new EvalResult(keyValueBinding, "list");
//    }

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