package com.Rishabh.Expression.PrimaryExpressions;

import com.Rishabh.EvalResult;
import com.Rishabh.Expression.Values.ListExpression;
import com.Rishabh.ExpressionType;
import com.Rishabh.Syntax.Expression;
import com.Rishabh.Syntax.PrimaryExpressions.IdentifierExpression;
import com.Rishabh.Syntax.PrimaryExpressions.ParensExpression;
import com.Rishabh.Syntax.Values.ClosureExpression;
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

    public ArrayAccessExpression(Token identifierToken, List<Expression> indices, int lineNumber) {
        super(ExpressionType.ArrayAccessExpression, lineNumber);
        _identifier = identifierToken;
        _indices = indices.toArray(new Expression[0]);
    }


    public EvalResult evaluate(Environment env) throws Exception {

        Symbol ourEntry = env.get(_identifier._lexeme);
        // Check if the lexeme is valid one
        if(ourEntry == null) {
            _diagnostics.add("Invalid identifier " + _identifier._lexeme + " at line number " + getLineNumber());
            return null;
        }

        if(ourEntry._type != "list" && ourEntry._type != "object" && ourEntry._type != "Closure") {
            _diagnostics.add("Data of type " + ourEntry._type + " is not indexable" + " at line number " + getLineNumber());
            return null;
        }


        EvalResult Initial = new EvalResult(ourEntry._value, ourEntry._type);
        for(Expression index : _indices) {
            if(index.getType() == ExpressionType.ParensExpression) {
                Initial = callFunction(Initial, index, env);
            }
            else {
                Initial = getValue(Initial, index, env); // If index._type == parensExpression -> functionCall
            }

            if(Initial == null)
                return null;
        }

        return Initial;
    }

    private EvalResult callFunction(EvalResult Initial, Expression index, Environment env) throws Exception {
        if(Initial._type != "Closure") {
            _diagnostics.add("Expression of type " + Initial._type + " can not be called, at line number " + getLineNumber());
            return null;
        }
        ClosureExpression function = (ClosureExpression) Initial._value;
        Environment functionEnv = function._closureEnv;
        SyntaxTree functionBody = function._functionBody;
        com.Rishabh.Expression.Values.ListExpression actualArgsList = (ListExpression) ((ParensExpression) index)._body;
        // elements
        List<Expression> actualArgs = actualArgsList._elements;
        List<IdentifierExpression> formalArgs = function._formalArgs;

        if(formalArgs.size() != actualArgs.size()) {
            _diagnostics.add("Expected " + formalArgs.size() + " arguements, got " + actualArgs.size() + " at line number " + getLineNumber());
            return null;
        }


        Environment functionArgsEnv = bindFormalArgsWithActualArgs(env, formalArgs, actualArgs);
        functionArgsEnv._ParentEnv = functionEnv;

        EvalResult finalRes = functionBody.evaluate(functionArgsEnv);
        _diagnostics.addAll(functionBody.getDiagnostics());


        return finalRes;
    }
    private Environment bindFormalArgsWithActualArgs(Environment env, List<IdentifierExpression> formalArgs, List<Expression> _actualArgs) throws Exception {
        Environment newEnv = new Environment(null);

        for(int i=0; i<_actualArgs.size(); i++) {
            EvalResult curArgResult = _actualArgs.get(i).evaluate(env);
            _diagnostics.addAll(_actualArgs.get(i).getDiagnostics());
            if(_diagnostics.size() > 0) {
                return null;
            }

            if(curArgResult._value == null && curArgResult._type != "null") {
                _diagnostics.add("Invalid function arguement of type " + curArgResult._type+ " at line number " + getLineNumber());
                return null;
            }

            Symbol newBinding = new Symbol(null, curArgResult._value, curArgResult._type);
            String nextFormalArg = formalArgs.get(i)._lexeme;
            newEnv.set(nextFormalArg, newBinding);
        }
        return newEnv;
    }


    private EvalResult getValue(EvalResult curIterable, Expression indexI, Environment env) throws Exception {
        if(curIterable._type != "list" && curIterable._type != "object") {
            _diagnostics.add("Data of type " + curIterable._type + " is not indexable at line number " + getLineNumber());
            return null;
        }

        EvalResult indexRes = indexI.evaluate(env);
        if(curIterable._type == "list") {
            if(indexRes._type != "int") {
                _diagnostics.add("Array indices should be of type int, found " + indexRes._type + " at line number " + getLineNumber());
                return null;
            }

            List<EvalResult> ourList = (List) curIterable._value;

            int curIdx = (int) indexRes._value;
            if(curIdx >= ourList.size()) {
                _diagnostics.add("Index " + curIdx + " too large for array of size " + ourList.size() + " at line number " + getLineNumber());
                return null;
            }

            return ourList.get((int) indexRes._value);

        }
        else {
            if(indexRes._type != "int" && indexRes._type != "string") {
                _diagnostics.add("Object indices should be of type int or string, found " + indexRes._type + " at line number " + getLineNumber());
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
