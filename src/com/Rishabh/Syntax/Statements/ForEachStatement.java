package com.Rishabh.Expression.Statements;

import com.Rishabh.EvalResult;
import com.Rishabh.Expression.Values.ListExpression;
import com.Rishabh.ExpressionType;
import com.Rishabh.Syntax.Expression;
import com.Rishabh.Syntax.PrimaryExpressions.IdentifierExpression;
import com.Rishabh.Syntax.Statement;
import com.Rishabh.SyntaxTree;
import com.Rishabh.Utilities.Environment;
import com.Rishabh.Utilities.Symbol;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ForEachStatement extends Statement {
    public Expression _iterator;
    public Expression _iterable;
    public SyntaxTree _body;

    public List<String> _diagnostics = new ArrayList<>();

    public ForEachStatement(Expression iterator, Expression iterable, SyntaxTree body, int lineNumber){
        super(ExpressionType.ForLoopExpression, lineNumber);
        _iterator = iterator;
        _iterable = iterable;
        _body = body;
    }

    public EvalResult evaluate(Environment env) throws Exception {

        EvalResult ourIterable = _iterable.evaluate(env);
        _diagnostics.addAll(_iterable.getDiagnostics());
        if(_diagnostics.size() > 0) {
            return null;
        }

        if(ourIterable._type == "list") {
            return iterateList(env, ourIterable);
        }
        else if(ourIterable._type == "object") {
            return iterateObject(env, ourIterable);
        }
        else {
            _diagnostics.add("Items of type " + ourIterable._type + " are not iterable");
        }

        return new EvalResult(null, "forEachExpression");

    }

    private EvalResult iterateObject(Environment env, EvalResult ourIterable) throws Exception {
        Map<String, EvalResult> ourObject = (HashMap) ourIterable._value;

        for(Map.Entry<String, EvalResult> binding : ourObject.entrySet()) {
            List<EvalResult> keyValuePair = getKeyValuePair(binding);

            BindIteratorToKeyValPair(env, keyValuePair);

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

    private void BindIteratorToKeyValPair(Environment env, List<EvalResult> keyValuePair) {
        if(_iterator.getType() == ExpressionType.IdentifierExpression) {
            IdentifierExpression ourIdentifierIterator = (IdentifierExpression) _iterator;
            Symbol iteratorBinding = new Symbol(ourIdentifierIterator._lexeme, keyValuePair, "list");
            env.set(ourIdentifierIterator._lexeme, iteratorBinding);
        }

        else if(_iterator.getType() == ExpressionType.ListExpression) {
            BindListIteratorToList(env, keyValuePair);
        }

        else {
            _diagnostics.add("Expression of type " + _iterator.getType() + " is not valid in foreach loop");
        }
    }

    private void BindListIteratorToList(Environment env, List<EvalResult> ourList) {
        var elements = (ListExpression) _iterator;
        List<Expression> leftList =  elements._elements;

        if(ourList.size() != leftList.size()) {
            _diagnostics.add("Dimension mismatch in expression.. the elements in lvalue and rvalue must be same, Error at line number " + getLineNumber());
        }

        for(int i = 0; i < ourList.size(); i++) {
            var curIdentifier = (IdentifierExpression) leftList.get(i);
            EvalResult rightExpResult = ourList.get(i);
            env.set(curIdentifier._lexeme, new Symbol(curIdentifier._lexeme, rightExpResult._value, rightExpResult._type));
        }
    }


    private List<EvalResult> getKeyValuePair(Map.Entry<String, EvalResult> binding) {
        EvalResult key = new EvalResult(binding.getKey(), "string");
        EvalResult value = binding.getValue();

        List<EvalResult> keyValueBinding = new ArrayList<>();
        keyValueBinding.add(key);
        keyValueBinding.add(value);
        return keyValueBinding;
    }

    private EvalResult iterateList(Environment env, EvalResult ourIterable) throws Exception {
        List<EvalResult> ourList = (List) ourIterable._value;

        for (EvalResult element : ourList) {
            BindIdentifierIteratorToListElement(env, element);
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

    private void BindIdentifierIteratorToListElement(Environment env, EvalResult element) {
        if(_iterator.getType() == ExpressionType.IdentifierExpression) {
            var ourIterator = (IdentifierExpression) _iterator;
            Symbol iteratorBinding = new Symbol(ourIterator._lexeme, element._value, element._type);
            env.set(ourIterator._lexeme, iteratorBinding);
            return;
        }
        else if(_iterator.getType() == ExpressionType.ListExpression) {
            if(element._type != "list") {
                _diagnostics.add("Cannot bind a list to a type " + element._type + " at line number " + getLineNumber());
                return;
            }

            List<EvalResult> currentListElement = (List) element._value;
            BindListIteratorToList(env, currentListElement);
            return;
        }

        _diagnostics.add("Invalid iterator type " + _iterator.getType() + " at line number " + getLineNumber());
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

    public boolean isExpressionPrimary() {
        return false;
    }
}