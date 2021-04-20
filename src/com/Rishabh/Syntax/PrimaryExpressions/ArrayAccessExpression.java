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

        if(ourEntry._type != "list" && ourEntry._type != "object") {
            _diagnostics.add("Data of type " + ourEntry._type + " is not indexable");
            return null;
        }

        EvalResult Initial = new EvalResult(ourEntry._value, ourEntry._type);
        for(Expression index : _indices) {
            Initial = getValue(Initial, index, env);
            if(Initial == null)
                return null;
        }


        return Initial;


    }

    private EvalResult getValue(EvalResult curIterable, Expression indexI, Environment env) throws Exception {
        if(curIterable._type != "list" && curIterable._type != "object") {
            _diagnostics.add("Data of type " + curIterable._type + " is not indexable");
            return null;
        }

        EvalResult indexRes = indexI.evaluate(env);
        if(curIterable._type == "list") {
            if(indexRes._type != "int") {
                _diagnostics.add("Array indices should be of type int, found " + indexRes._type);
                return null;
            }

            List<EvalResult> ourList = (List) curIterable._value;

            int curIdx = (int) indexRes._value;
            if(curIdx >= ourList.size()) {
                _diagnostics.add("Index " + curIdx + " too large for array of size " + ourList.size());
                return null;
            }

            return ourList.get((int) indexRes._value);

        }
        else {
            if(indexRes._type != "int" && indexRes._type != "string") {
                _diagnostics.add("Object indices should be of type int or string, found " + indexRes._type);
                return null;
            }

            Map<String, EvalResult> ourMap = (HashMap) curIterable._value;
            String curIdx = (indexRes._value).toString();


            return ourMap.get(curIdx) == null ? (new EvalResult(0, "int")) : ourMap.get(curIdx);
        }

    }


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


// Get the EvalIterable
