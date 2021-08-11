package com.Rishabh.Syntax.PrimaryExpressions;

import com.Rishabh.EvalResult;
import com.Rishabh.ExpressionType;
import com.Rishabh.Syntax.Expression;
import com.Rishabh.Syntax.Values.ListExpression;
import com.Rishabh.Syntax.Values.NumberExpression;
import com.Rishabh.Syntax.Values.ObjectExpression;
import com.Rishabh.Syntax.Values.StringExpression;
import com.Rishabh.TokenType;
import com.Rishabh.Utilities.Environment;

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
        _left.prettyPrint(indent + "    ");
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

            EvalResult ourEntry = env.get(curExp._identifier._lexeme);

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
        Map<Expression, Expression> leftObject = getLeftObject((ObjectExpression) left);
        Map<String, EvalResult> rightObject = getRightObject(right, _diagnostics, lineNumber);
        if (rightObject == null) return null;
        boolean foundInvalidKey = BindObjectsInEnv(env, _diagnostics, lineNumber, leftObject, rightObject);
        if (foundInvalidKey) right = null;
        return right;
    }

    // Fix this and we are done for now
    // Chill for some time after this
    private static boolean BindObjectsInEnv(Environment env, List<String> _diagnostics, int lineNumber, Map<Expression, Expression> leftObject, Map<String, EvalResult> rightObject) throws Exception {
        boolean foundInvalidKey = false;
        for(Expression leftObjectKey : leftObject.keySet()) {
            foundInvalidKey = !isKeyValid(_diagnostics, lineNumber, leftObjectKey);
            if(foundInvalidKey) break;
            String key = getKeyFromExpression(leftObjectKey);
            Expression leftExp = leftObject.get(leftObjectKey);
            EvalResult rightRes = rightObject.get(key);

            if(rightRes == null) {
                _diagnostics.add("Key " + key + " is not present in the RHS of the assignement at line number " + lineNumber);
                foundInvalidKey = true;
                break;
            }
            AssignmentExpression.Bind(leftExp, rightRes, env, _diagnostics, lineNumber);
        }
        return foundInvalidKey;
    }

    private static boolean isKeyValid(List<String> _diagnostics, int lineNumber, Expression leftObjectKey) {
        boolean isKeyOfValidType = isIntegerKey(leftObjectKey) || isStringKey(leftObjectKey);
        if (!isKeyOfValidType)
            _diagnostics.add("Invalid key type in object, at line number " + lineNumber);
        return isKeyOfValidType;
    }

    private static String getKeyFromExpression(Expression keyExp) {
        String key;
        if (isIntegerKey(keyExp)) {
            var intKey = (NumberExpression) keyExp;
            key = Integer.toString(intKey._value);
        }
        else {
            var strKey = (StringExpression) keyExp;
            key = strKey._value;
        }
        return key;
    }

    private static boolean isStringKey(Expression keyExp) {
        return keyExp.getType() == ExpressionType.StringExpression;
    }

    private static boolean isIntegerKey(Expression keyExp) {
        return keyExp.getType() == ExpressionType.IntExpression;
    }

    private static Map<String, EvalResult> getRightObject(EvalResult right, List<String> _diagnostics, int lineNumber) {
        if(right._type != "object") {
            _diagnostics.add("Expected an object, found " + right._type + " at line number " + lineNumber);
            return null;
        }
        return (HashMap<String, EvalResult>) right.getValue();
    }

    private static Map<Expression, Expression> getLeftObject(ObjectExpression left) {
        var leftObject = left;
        Map<Expression, Expression> leftObjectContents = leftObject._contents;
        return leftObjectContents;
    }

    public static EvalResult assignList(Expression left, Environment env, EvalResult right, List<String> _diagnostics, int lineNumber) throws Exception {
        String rightType = right._type;
        if(!rightType.equals("list")) {
            _diagnostics.add("Cannot de-structure " + rightType + " into a list. Error at line number " + lineNumber);
            return null;
        }
        return BindListsInEnv((ListExpression) left, env, right, _diagnostics, lineNumber);
    }

    private static EvalResult BindListsInEnv(ListExpression left, Environment env, EvalResult right, List<String> _diagnostics, int lineNumber) throws Exception {
        var rightList = (List<EvalResult>) right._value;
        List<Expression> leftList = left._elements;
        if(rightList.size() != leftList.size()) {
            _diagnostics.add("Dimension mismatch in expression.. the elements in lvalue and rvalue must be same, Error at line number " + lineNumber);
            return null;
        }
        return BindLists(env, _diagnostics, lineNumber, rightList, leftList);
    }

    private static EvalResult BindLists(Environment env, List<String> _diagnostics, int lineNumber, List<EvalResult> rightList, List<Expression> leftList) throws Exception {
        for(int i = 0; i < leftList.size(); i++)
            AssignmentExpression.Bind(leftList.get(i), rightList.get(i), env, _diagnostics, lineNumber);
        return new EvalResult(rightList, "list");
    }

    public static EvalResult assignIdentifier(Expression left, Environment env, EvalResult right, List<String> _diagnostics, int lineNumber) {
        IdentifierExpression leftIdentifierExpression = (IdentifierExpression) left;
        return env.set(leftIdentifierExpression._lexeme, right);
    }

    /*
        Iterables are like array access expression
        a.x = 3, a[3] = 2 etc

     */

//    private static EvalResult evaluateIterableExpression(Environment env, ArrayAccessExpression curExp, List<String> _diagnostics, int lineNumber, EvalResult Result) throws Exception {
//        for(Expression index : curExp._indices) {
//
//            if(isRaisingNullPointerException(Result)) {
//                _diagnostics.add("NullPointerException: Cannot access property from null, at line number " + lineNumber);
//                return null;
//            }
//
//            if(index.getType() == ExpressionType.MemberAccessExpression) {
//                EvalResult memberEntry = getMemberEntry(index, );
//                if(memberEntry != null) { Result = memberEntry; }
//                else{
////                    EvalResult newEntry = new EvalResult(null, "null");
////                    /*@TODO: Check this for fixing the bug */
////                    objEnv.set(memberName, newEntry);
////                    Result = newEntry;
////                    continue;
//                    Result = new EvalResult(null, "null")
//                }
//
//
//            }
//
//            else {
//                Result = AssignmentExpression.getValue(Result, index, env, _diagnostics, lineNumber);
//            }
//
//            if(Result == null) {
//                return null;
//            }
//        }
//        return Result;
//    }
//
//    private static EvalResult getMemberEntry(MemberAccessExpression index, EvalResult Result) {
//        Environment objectEnv = (Environment) Result._value;
//        String memberName = index._memberName._lexeme;
//
//        /*@TODO: Check this for fixing the bug */
////                HashMap<String, EvalResult> table = objEnv._table;
//        return objectEnv.get(memberName);
//    }
//
//    private static boolean isRaisingNullPointerException(EvalResult Result) {
//        return Result == null || Result._value == null;
//    }
//
//    public static EvalResult getValue(EvalResult curIterable, Expression indexI, Environment env, List<String> _diagnostics, int lineNumber) throws Exception {
//
//        EvalResult indexRes = indexI.evaluate(env); // a[x] -> evaluated in current env
//        // a(x) -> evaluated in current env
//        if(curIterable._type.equals("list")) {
//            if(!indexRes._type.equals("int")) {
//                _diagnostics.add("Array indices should be of type int, found " + indexRes._type + " at line number " +lineNumber);
//                return null;
//            }
//
//            List<EvalResult> ourList = (List<EvalResult>) curIterable._value;
//
//            int curIdx = (int) indexRes._value;
//            if(curIdx >= ourList.size()) {
//                _diagnostics.add("Index " + curIdx + " too large for array of size " + ourList.size() + " at line number " + lineNumber);
//                return null;
//            }
//
//            return ourList.get((int) indexRes._value);
//
//        }
//        else {
//            if(indexRes._type != "int" && indexRes._type != "string") {
//                _diagnostics.add("Object indices should be of type int or string, found " + indexRes._type + " at line number " + lineNumber);
//                return null;
//            }
//
//            Map<String, EvalResult> ourMap = (HashMap<String, EvalResult>) curIterable._value;
//            String curIdx = (indexRes._value).toString();
//
//            if(ourMap.get(curIdx) == null) {
//                ourMap.put(curIdx, new EvalResult(0, "int"));
//            }
//
//            return ourMap.get(curIdx);
//
//        }
//
//    }
    public static EvalResult assignIterable(Environment env, EvalResult rightRes, ArrayAccessExpression curExp, EvalResult ourEntry, List<String> _diagnostics, int lineNumber) throws Exception {
        Environment objEnv = new Environment(null);
        EvalResult Result = evaluateCurrentIndex(env, curExp, _diagnostics, lineNumber, ourEntry);
        if (Result == null) return null;
        Result._value = rightRes._value;
        Result._type = rightRes._type;

        return Result;
    }

    private static EvalResult evaluateCurrentIndex(Environment env, ArrayAccessExpression curExp, List<String> _diagnostics, int lineNumber, EvalResult Result) throws Exception {
        for(Expression index : curExp._indices) {
            if(isNull(Result)) {
                _diagnostics.add("NullPointerException: Cannot access property from null, at line number " + lineNumber);
                return null;
            }
            Result = evaluateCurrentIndex(env, _diagnostics, lineNumber, Result, index);
        }
        return Result;
    }

    private static EvalResult evaluateCurrentIndex(Environment env, List<String> _diagnostics, int lineNumber, EvalResult Result, Expression index) throws Exception {
        /* Refactor this First... Then we'll have to think about Refactoring these two */
        if(index.getType() == ExpressionType.MemberAccessExpression)
            Result = evaluateMemberAccessExpression(Result, (MemberAccessExpression) index);
        else
            Result = AssignmentExpression.getValue(Result, index, env, _diagnostics, lineNumber);
        return Result;
    }

    private static EvalResult evaluateMemberAccessExpression(EvalResult Result, MemberAccessExpression index) {
        Environment objEnv = (Environment) Result._value;
        String memberName = index._memberName._lexeme;
        HashMap<String, EvalResult> table = objEnv._table;
        EvalResult memberEntry = table.get(memberName);

        if (memberEntry == null) {
            memberEntry = new EvalResult(null, "null");
            table.put(memberName, memberEntry);
        }
        return memberEntry;
    }

    private static boolean isNull(EvalResult Result) {
        return Result == null || Result._value == null;
    }


    /* Only This one left for refactoring */
    public static EvalResult getValue(EvalResult curIterable, Expression indexI, Environment env, List<String> _diagnostics, int lineNumber) throws Exception {

        EvalResult indexRes = indexI.evaluate(env); // a[x] -> evaluated in current env
        // a(x) -> evaluated in current env
        if(curIterable._type.equals("list")) {
            if(!indexRes._type.equals("int")) {
                _diagnostics.add("Array indices should be of type int, found " + indexRes._type + " at line number " +lineNumber);
                return null;
            }

            List<EvalResult> ourList = (List<EvalResult>) curIterable._value;

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

            Map<String, EvalResult> ourMap = (HashMap<String, EvalResult>) curIterable._value;
            String curIdx = (indexRes._value).toString();

            if(ourMap.get(curIdx) == null) {
                ourMap.put(curIdx, new EvalResult(0, "int"));
            }

            return ourMap.get(curIdx);

        }

    }

}

// I need to create a better hierarchy for these classes... A lot of code redundancy here