package com.Rishabh.Expression.PrimaryExpressions;

import com.Rishabh.EvalResult;
import com.Rishabh.Expression.Expression;
import com.Rishabh.Expression.Values.ListExpression;
import com.Rishabh.ExpressionType;
import com.Rishabh.Token;
import com.Rishabh.Utilities.Environment;
import com.Rishabh.Utilities.Symbol;

import java.util.ArrayList;
import java.util.List;

public class ArrayAccessExpression extends Expression {

    public Token _identifier;
    public ListExpression _index;
    public List<String> _diagnostics = new ArrayList<>();

    public ArrayAccessExpression(Token identifierToken, ListExpression index) {
        super(ExpressionType.ArrayAccessExpression);
        _identifier = identifierToken;
        _index = index;
    }


    public EvalResult evaluate(Environment env) throws Exception {
//        return null;
        EvalResult indexRes = _index.evaluate(env); // Evaluate to a list of ints
        List<EvalResult> indices = (List) indexRes._value;
        _diagnostics.addAll(_index.getDiagnostics());

        // If there are any errors in indexRes
        if(_diagnostics.size() > 0) {
            _diagnostics.add("Error in the array access expression");
            return null;
        }

        Symbol ourList = env.get(_identifier._lexeme);
        if(ourList == null) {
            _diagnostics.add("Identifier " + _identifier._lexeme + " is not defined");
            return null;
        }


        List<EvalResult> ourItemList = (List) ourList._value;

        EvalResult firstIdx = indices.get(0);

        if(firstIdx._type != "int") {
            _diagnostics.add("Index must evaluate to an integer.. found " + firstIdx._type);
            return null;
        }

        int firstIdxVal = (int) firstIdx._value;

        if(firstIdxVal >= ourItemList.size()) {
            _diagnostics.add("Index " + firstIdxVal + " is out of bounds for an array of size " + ourItemList.size());
            return null;
        }




        EvalResult nextItem = ourItemList.get(firstIdxVal);

        for(int i=1; i < indices.size(); i++) {
            EvalResult idx = indices.get(i);
            String idxType = idx._type;


            // Make sure the nextItem is of type list
            if(nextItem._type != "list") {
                _diagnostics.add("Dimension mismatch in the array access expression");
                return null;
            }

            if(idxType != "int") {
                _diagnostics.add("Index must evaluate to an integer.. found " + idxType);
                return null;
            }

            int idxVal = (int) idx._value;

            // Make sure the idx is not out of bounds
            List<EvalResult> nextList = (List) nextItem._value;

            if(idxVal >= nextList.size()) {
                _diagnostics.add("Index " + idxVal + " too large for array of size " + nextList.size());
                return null;
            }

            nextItem = nextList.get(idxVal);

        }

        return new EvalResult(nextItem._value, nextItem._type);
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

