package com.Rishabh.Expression.PrimaryExpressions;

import com.Rishabh.EvalResult;
import com.Rishabh.Syntax.Expression;
import com.Rishabh.ExpressionType;
import com.Rishabh.SyntaxTree;
import com.Rishabh.Token;
import com.Rishabh.Utilities.Environment;
import com.Rishabh.Utilities.Symbol;

import java.util.ArrayList;
import java.util.List;

public class ArrayAccessExpression extends Expression {

    public Token _identifier;
    public SyntaxTree[] _indices;
    public List<String> _diagnostics = new ArrayList<>();

    public ArrayAccessExpression(Token identifierToken, List<SyntaxTree> indices) {
        super(ExpressionType.ArrayAccessExpression);
        _identifier = identifierToken;
        _indices = indices.toArray(new Expression[0]);
    }


    public EvalResult evaluate(Environment env) throws Exception {

        Symbol ourListEntry = env.get(_identifier._lexeme);
        // Check if the lexeme is valid one
        if(ourListEntry == null) {
            _diagnostics.add("Invalid identifier " + _identifier._lexeme);
            return null;
        }

        EvalResult ourList = new EvalResult((List) ourListEntry._value, ourListEntry._type);
        EvalResult finalResult = ourList;
        for(SyntaxTree idx : _indices) {

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

