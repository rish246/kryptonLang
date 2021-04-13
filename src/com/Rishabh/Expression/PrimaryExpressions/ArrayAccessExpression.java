package com.Rishabh.Expression.PrimaryExpressions;

import com.Rishabh.EvalResult;
import com.Rishabh.ExpressionType;
import com.Rishabh.Token;
import com.Rishabh.Utilities.Environment;
import com.Rishabh.Utilities.Symbol;
import com.Rishabh.Expression.Expression;

import java.util.ArrayList;
import java.util.List;

public class ArrayAccessExpression extends Expression {

    public Token _identifier;
    public Expression _index;
    public List<String> _diagnostics = new ArrayList<>();

    public ArrayAccessExpression(Token identifierToken, Expression index) {
        super(ExpressionType.ArrayAccessExpression);
        _identifier = identifierToken;
        _index = index;
    }


    public EvalResult evaluate(Environment env) throws Exception {
//        return null;
        EvalResult indexRes = _index.evaluate(env);
        if(indexRes == null || indexRes._value == null) {
            _diagnostics.add("Invalid expression in array access expression");
            return null;
        }

        if(indexRes._type != "int") {
            _diagnostics.add("Index need to evaluate to an Int");
            return null;
        }

        Symbol ourList = env.get(_identifier._lexeme);
        List itemList = (List) ourList._value;


        int listIndex = (int)indexRes._value;
        if(listIndex >= itemList.size()) {
            _diagnostics.add("Index " + listIndex + " out of bounds for list of length " + itemList.size());
            return null;
        }

        EvalResult ithValue = (EvalResult)itemList.get(listIndex);


        return new EvalResult(ithValue.getValue(), ithValue.getType());
    }

    public void prettyPrint(String indent) {
        System.out.println("Array Access");
        System.out.print(indent + "|-");
        System.out.println(_identifier._value);
        System.out.println(indent + "|-");
        System.out.println(indent + "|");
        System.out.print(indent + "|_"); _index.prettyPrint(indent + "    ");

    }

    public List<String> getDiagnostics() {
        return _diagnostics;
    }

}

// Get the list from env
// Take out the list
// Update the ith variable
// set the list to new symbol