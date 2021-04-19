package com.Rishabh.Expression.Statements;

import com.Rishabh.EvalResult;
import com.Rishabh.ExpressionType;
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
    public IdentifierExpression _iterator;
    public IdentifierExpression _iterable;
    public SyntaxTree _body;

    public List<String> _diagnostics = new ArrayList<>();

    public ForEachStatement(IdentifierExpression iterator, IdentifierExpression iterable, SyntaxTree body){
        super(ExpressionType.ForLoopExpression);
        _iterator = iterator;
        _iterable = iterable;
        _body = body;
    }

    public EvalResult evaluate(Environment env) throws Exception {
//        // evaluate conditionalBranch
//        // Create a new env
//        Environment newEnv = new Environment(env);
//
//        _initializationCond.evaluate(newEnv);
//        _diagnostics.addAll(_initializationCond.getDiagnostics());
//
//        EvalResult haltCondResult = _haltingCondition.evaluate(newEnv);
//        _diagnostics.addAll(_haltingCondition.getDiagnostics());
//
//        if(_diagnostics.size() > 0) {
//            _diagnostics.add("Error in the for loop body");
//            return null;
//        }
//
//        while((boolean)haltCondResult._value) {
//            _body.evaluate(newEnv);
//            _diagnostics.addAll(_body.getDiagnostics());
//
//            _progressExp.evaluate(newEnv);
//            _diagnostics.addAll(_progressExp.getDiagnostics());
//            if(_diagnostics.size() > 0) {
//                return null;
//            }
//
//            haltCondResult = _haltingCondition.evaluate(newEnv);
//        }

        Symbol ourIterable = env.get(_iterable._lexeme);
        if(ourIterable == null) {
            _diagnostics.add("Undefined variable " + _iterable._lexeme);
        }

        if(ourIterable._type == "list") {
            List<EvalResult> ourList = (List) ourIterable._value;

            for (EvalResult element : ourList) {
                Symbol iteratorBinding = new Symbol(_iterator._lexeme, element._value, element._type);
                env.set(_iterator._lexeme, iteratorBinding);
                _body.evaluate(env);
                _diagnostics.addAll(_body.getDiagnostics());
                if(_diagnostics.size() > 0) {
                    return null;
                }
            }
        }
        else if(ourIterable._type == "object") {

            Map<String, EvalResult> ourObject = (HashMap) ourIterable._value;
            for(Map.Entry<String, EvalResult> binding : ourObject.entrySet()) {
                EvalResult key = new EvalResult(binding.getKey(), "string");
                EvalResult value = binding.getValue();
                List<EvalResult> keyValueBinding = new ArrayList<>();
                keyValueBinding.add(key);
                keyValueBinding.add(value);
                Symbol iteratorBinding = new Symbol(_iterator._lexeme, keyValueBinding, "list");

                env.set(_iterator._lexeme, iteratorBinding);
                _body.evaluate(env);
            }



        }

        else {
            _diagnostics.add("Items of type " + ourIterable._type + " are not iterable");
        }

        return new EvalResult(null, "forEachExpression");

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