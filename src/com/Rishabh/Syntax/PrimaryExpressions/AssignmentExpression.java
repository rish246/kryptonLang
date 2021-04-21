package com.Rishabh.Syntax.PrimaryExpressions;

import com.Rishabh.EvalResult;
import com.Rishabh.ExpressionType;
import com.Rishabh.Syntax.Expression;
import com.Rishabh.TokenType;
import com.Rishabh.Utilities.Environment;
import com.Rishabh.Utilities.Symbol;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AssignmentExpression extends Expression {
    Expression _left;
    TokenType _operatorToken; // Assignment Token Always
    Expression _right;
    List<String> _diagnostics = new ArrayList<>();

    public AssignmentExpression(Expression left, TokenType operatorToken, Expression right, int lineNumber) {
        super(ExpressionType.AssignmentExpression, lineNumber);
        _left = left;
        _operatorToken = operatorToken;
        _right = right;
    }

    @Override
    public void prettyPrint(String indent) {

        System.out.println(_operatorToken);
        System.out.println(indent + "|");
        System.out.print(indent + "├──");
//        _left.prettyPrint(indent + "    ");
        // System.out.println(_left. + indent);
        _left.prettyPrint(indent + "    ");
        // Add some long lines here
        System.out.println(indent + "|");

        System.out.print(indent + "└──");
        _right.prettyPrint(indent + "    ");

    }

    @Override
    public List<String> getDiagnostics() {
        return _diagnostics;
    }


    public EvalResult evaluate(Environment env) throws Exception {

        EvalResult rightRes = _right.evaluate(env);
        _diagnostics.addAll(_right.getDiagnostics());

        if(rightRes == null)
            return null;

        String rightType = rightRes._type;
        Object rightValue = rightRes._value;

        // left's lexeme
        if(_left.getType() == ExpressionType.IdentifierExpression) {
            return assignIdentifier(env, rightType, rightValue);
        }
        if(_left.getType() == ExpressionType.ArrayAccessExpression) {

            com.Rishabh.Expression.PrimaryExpressions.ArrayAccessExpression curExp = (com.Rishabh.Expression.PrimaryExpressions.ArrayAccessExpression) _left;

            Symbol ourEntry = env.get(curExp._identifier._lexeme);

            if (ourEntry == null) {
                _diagnostics.add("Invalid identifier " + curExp._identifier._lexeme + " at line number " + getLineNumber());
                return null;
            }

            // Assign values to iterables like arrays and objects
            return assignIterable(env, rightRes, curExp, ourEntry);

        }

        else {
            _diagnostics.add("Expression of type " + _left.getType() + " is not a valid lvalue" + " at line number " + getLineNumber());
        }

        return null;
    }

    private EvalResult assignIdentifier(Environment env, String rightType, Object rightValue) {
        IdentifierExpression left = (IdentifierExpression) _left;
        Symbol res = env.set(left._lexeme, new Symbol(left._lexeme, rightValue, rightType));
        return new EvalResult(res._value, res._type);
    }

    private EvalResult assignIterable(Environment env, EvalResult rightRes, com.Rishabh.Expression.PrimaryExpressions.ArrayAccessExpression curExp, Symbol ourEntry) throws Exception {
        if(ourEntry._type != "list" && ourEntry._type != "object") {
            _diagnostics.add("Data of Type " + ourEntry._type + " is not indexable" + " at line number " + getLineNumber());
            return null;
        }

        EvalResult Initial = new EvalResult(ourEntry._value, ourEntry._type);
        for(Expression index : curExp._indices) {
            Initial = getValue(Initial, index, env);
            if(Initial == null) {
                return null;
            }
        }


        Initial._value = rightRes._value;
        Initial._type = rightRes._type;
        return Initial;
    }

    private EvalResult getValue(EvalResult curIterable, Expression indexI, Environment env) throws Exception {
        if(curIterable._type != "list" && curIterable._type != "object") {
            _diagnostics.add("Data of type " + curIterable._type + " is not indexable" + " at line number " + getLineNumber());
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

            if(ourMap.get(curIdx) == null) {
                ourMap.put(curIdx, new EvalResult(0, "int"));
            }

            return ourMap.get(curIdx);

        }

    }

}
// x[0] -> x -> insert an evalResult() return that


// I think i am ready for the adding line numbers to runTimeErrors