package com.Rishabh.Expression.PrimaryExpressions;

import com.Rishabh.EvalResult;
import com.Rishabh.ExpressionType;
import com.Rishabh.Syntax.Expression;
import com.Rishabh.SyntaxTree;
import com.Rishabh.Token;
import com.Rishabh.Utilities.Environment;
import com.Rishabh.Utilities.Symbol;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArrayAccessExpression extends Expression {

    public Token _identifier;
    public Expression[] _indices;
    public List<String> _diagnostics = new ArrayList<>();

    public ArrayAccessExpression(Token identifierToken, List<Expression> indices) {
        super(ExpressionType.ArrayAccessExpression);
        _identifier = identifierToken;
        _indices = indices.toArray(new Expression[0]);
    }


    public EvalResult evaluate(Environment env) throws Exception {

        Symbol ourEntry = env.get(_identifier._lexeme);
        // Check if the lexeme is valid one
        if(ourEntry == null) {
            _diagnostics.add("Invalid identifier " + _identifier._lexeme);
            return null;
        }

        if(ourEntry._type == "list") {
            EvalResult ourList = new EvalResult((List) ourEntry._value, ourEntry._type);
            EvalResult finalResult = ourList;
            for(Expression idx : _indices) {

                // check the finalResult should be a list
                if(finalResult._type != "list") {
                    _diagnostics.add("Types " + finalResult._type + " are not subscriptable");
                    return null;
                }

                EvalResult curIdx = idx.evaluate(env);
                // check if the idx is of type int
                String curIdxType = curIdx._type;
                if(!curIdxType.equals("int")) {
                    _diagnostics.add("Indices must be integers only.. Found " + curIdxType);
                    return null;
                }

                int curIdxValue = (int) curIdx._value;
                // Valid shouldn't be out of bounds
                List<EvalResult> curList = (List) finalResult._value;
                if(curIdxValue >= curList.size()) {
                    _diagnostics.add("Length " + curIdxValue + " out of bound for array " + _identifier._lexeme);
                    return null;
                }

                finalResult = curList.get(curIdxValue);
            }


            return new EvalResult(finalResult._value, finalResult._type);

        }

        else if(ourEntry._type == "object"){

            Map<Object, EvalResult> ourObject = (HashMap) ourEntry._value;

            EvalResult firstIndex = _indices[0].evaluate(env);

            EvalResult returnValue = ourObject.get(firstIndex._value);

            return returnValue;

        }


        return null;


    }
    // Lets handle some exceptions now


    public void prettyPrint(String indent) {
        System.out.println("Array Access");
        System.out.print(indent + "|-");
        System.out.println(_identifier._value);
        System.out.println(indent + "|-");
        System.out.println(indent + "|");
        System.out.print(indent + "|_");
        for(SyntaxTree index : _indices) {
            index.prettyPrint(indent + "    ");
        }

    }

    public List<String> getDiagnostics() {
        return _diagnostics;
    }

}

