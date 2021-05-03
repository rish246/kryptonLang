package com.Rishabh.Syntax.PrimaryExpressions;

import com.Rishabh.EvalResult;
import com.Rishabh.ExpressionType;
import com.Rishabh.Syntax.Expression;
import com.Rishabh.TokenType;
import com.Rishabh.Utilities.Environment;
import com.Rishabh.Utilities.Symbol;
import com.Rishabh.Syntax.Values.*;

import java.util.*;


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

        return AssignmentExpression.Bind(_left, rightRes, env, _diagnostics, getLineNumber());
    }

    // make a method Bind which binds an expression to another expression
    public static EvalResult Bind(Expression left, EvalResult right, Environment env, List<String> _diagnostics, int lineNumber) throws Exception{
        if(left.getType() == ExpressionType.IdentifierExpression) {
            return AssignmentExpression.assignIdentifier(left, env, right, _diagnostics, lineNumber);
        }
        if(left.getType() == ExpressionType.ListExpression) {
            return AssignmentExpression.assignList(left, env, right, _diagnostics, lineNumber);
        }
        if(left.getType() == ExpressionType.ObjectExpression) {
            return AssignmentExpression.assignObject(left, env, right, _diagnostics, lineNumber);
        }
        if(left.getType() == ExpressionType.ArrayAccessExpression) {

            var curExp = (ArrayAccessExpression) left;

            Symbol ourEntry = env.get(curExp._identifier._lexeme);

            if (ourEntry == null) {
                _diagnostics.add("Invalid identifier " + curExp._identifier._lexeme + " at line number " +  lineNumber);
                return null;
            }

            return AssignmentExpression.assignIterable(env, right, curExp, ourEntry, _diagnostics, lineNumber);
        }


        _diagnostics.add("Expression of type " + left.getType() + " is not a valid lvalue" + " at line number " + lineNumber);
        return null;
    }

    private static EvalResult assignObject(Expression left, Environment env, EvalResult right, List<String> _diagnostics, int lineNumber) throws Exception {
        // Get the left object
        var leftObject = (com.Rishabh.Syntax.Values.ObjectExpression) left;
        Map<Expression, Expression> leftObjectContents = leftObject._contents;

        // Typecast the right obj
        if(right._type != "object") {
            _diagnostics.add("Expected an object, found " + right._type + " at line number " + lineNumber);
            return null;
        }
        Map<String, EvalResult> rightObject = (HashMap) right._value;

        for(Map.Entry<Expression, Expression> leftEntry : leftObjectContents.entrySet()) {
            Expression keyExp = leftEntry.getKey();
            String key;
            if(keyExp.getType() == ExpressionType.IntExpression) {
                var intKey = (NumberExpression) keyExp;
                key = Integer.toString(intKey._value);
            }
            else if(keyExp.getType() == ExpressionType.StringExpression) {
                var strKey = (StringExpression) keyExp;
                key = strKey._value;
            }
            else {
                _diagnostics.add("Invalid key type in object, at line number " + lineNumber);
                return null;
            }

            Expression leftExp = leftObjectContents.get(keyExp);
            EvalResult rightRes = rightObject.get(key);

            if(rightRes == null) {
                _diagnostics.add("Key " + key + " is not present in the RHS of the assignement at line number " + lineNumber);
                return null;
            }
            AssignmentExpression.Bind(leftExp, rightRes, env, _diagnostics, lineNumber);
        }

        return right;
    }

    public static EvalResult assignList(Expression left, Environment env, EvalResult right, List<String> _diagnostics, int lineNumber) throws Exception {

        String rightType = right._type;
        Object rightValue = right._value;
        // Get the elements fro
        var LeftList = (ListExpression) left;
        // For Each element check it is of type identifierExpression
        List<Expression> listElements = LeftList._elements;

        // if everyThing is alright
        if(!rightType.equals("list")) {
            _diagnostics.add("Cannot de-structure " + rightType + " into a list. Error at line number " + lineNumber);
            return null;
        }

        List<EvalResult> rightList = (List) rightValue; // We got the right res
        if(rightList.size() != listElements.size()) {
            _diagnostics.add("Dimension mismatch in expression.. the elements in lvalue and rvalue must be same, Error at line number " + lineNumber);
            return null;
        }

        for(int i = 0; i < listElements.size(); i++) {
            AssignmentExpression.Bind(listElements.get(i), rightList.get(i), env, _diagnostics, lineNumber);
        }

        return new EvalResult(rightList, "list");

    }

    public static EvalResult assignIdentifier(Expression left, Environment env, EvalResult right, List<String> _diagnostics, int lineNumber) {
        String rightType = right._type;
        Object rightValue = right._value;

        IdentifierExpression I_left = (IdentifierExpression) left;
        Symbol res = env.set(I_left._lexeme, new Symbol(I_left._lexeme, rightValue, rightType));
        return new EvalResult(res._value, res._type);
    }

    public static EvalResult assignIterable(Environment env, EvalResult rightRes, ArrayAccessExpression curExp, Symbol ourEntry, List<String> _diagnostics, int lineNumber) throws Exception {
        if(!Objects.equals(ourEntry._type, "list") && !Objects.equals(ourEntry._type, "object")) {
            _diagnostics.add("Data of Type " + ourEntry._type + " is not indexable" + " at line number " + lineNumber);
            return null;
        }

        EvalResult Initial = new EvalResult(ourEntry._value, ourEntry._type);
        for(Expression index : curExp._indices) {
            Initial = AssignmentExpression.getValue(Initial, index, env, _diagnostics, lineNumber);
            if(Initial == null) {
                return null;
            }
        }

        Initial._value = rightRes._value;
        Initial._type = rightRes._type;
        return Initial;
    }

    public static EvalResult getValue(EvalResult curIterable, Expression indexI, Environment env, List<String> _diagnostics, int lineNumber) throws Exception {
        if(curIterable._type != "list" && curIterable._type != "object") {
            _diagnostics.add("Data of type " + curIterable._type + " is not indexable" + " at line number " + lineNumber);
            return null;
        }

        EvalResult indexRes = indexI.evaluate(env);
        if(curIterable._type == "list") {
            if(indexRes._type != "int") {
                _diagnostics.add("Array indices should be of type int, found " + indexRes._type + " at line number " +lineNumber);
                return null;
            }

            List<EvalResult> ourList = (List) curIterable._value;

            int curIdx = (int) indexRes._value;
            if(curIdx >= ourList.size()) {
                _diagnostics.add("Index " + curIdx + " too large for array of size " + ourList.size() + " at line number " + lineNumber);
                return null;
            }

            return ourList.get((int) indexRes._value);

        }
        else {
            if(indexRes._type != "int" && indexRes._type != "string") {
                _diagnostics.add("Object indices should be of type int or string, found " + indexRes._type + " at line number " + lineNumber);
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

// why did it end in an infinite Loop -> Why?

