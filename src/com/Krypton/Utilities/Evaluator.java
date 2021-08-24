package com.Krypton.Utilities;

import com.Krypton.EvalResult;
import com.Krypton.ExpressionType;
import com.Krypton.Syntax.Expression;
import com.Krypton.Syntax.PrimaryExpressions.*;
import com.Krypton.Syntax.Values.*;
import com.Krypton.SyntaxTree;
import com.Krypton.Token;

import java.util.*;

public class Evaluator {
    private int lineNumber;
    private List<String> _diagnostics = new ArrayList<>();

    public Evaluator(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    /* Keep the code not related to ArrayExpression.java and restJustSendBackThere  */
    /* More generalization along the way */
    public EvalResult EvaluateArrayAccessExpression (Environment env, ArrayAccessExpression expression) throws Exception {
        Token identifier = expression._identifier;
        String identifierName = identifier._lexeme;
        EvalResult ourIdentifierEntry = env.get(identifierName);
        if(ourIdentifierEntry == null) {
            _diagnostics.add("Undefined Symbol " + identifier._lexeme + " at line number " + lineNumber);
            return null;
        }
        Expression[] _indices = expression._indices;
        EvalResult Result = ourIdentifierEntry;
        for (Expression index : _indices) { // We'll Have to catch that at a higher level I guess
            Result = EvaluateIndexInEnv(env, Result, index);
            if(Result == null) break;
        }
        return Result;
    }

    private EvalResult EvaluateIndexInEnv(Environment env, EvalResult Result, Expression index) throws Exception {
        if (isRaisingNullPointerException(Result)) return null;

        if(index.getType() == ExpressionType.ParensExpression)
            Result = callFunction(Result, index, env);
        else if(index.getType() == ExpressionType.MemberAccessExpression)
            Result = getMemberFromClassEnv(Result, (MemberAccessExpression) index);
        else
            Result = getValue(Result, index, env);
        return Result;
    }

    public EvalResult getValue(EvalResult curIterable, Expression indexI, Environment env) throws Exception {
        EvalResult indexRes = indexI.evaluate(env); // a[x] -> evaluated in current env
        if(indexRes == null) {
            _diagnostics.addAll(indexI.getDiagnostics());
            return null;
        }

        return getCurIndexValue(curIterable, indexRes);
    }

    private EvalResult getCurIndexValue(EvalResult curIterable, EvalResult indexRes) {
        if (curIterable._type.equals("list")) {
            return getValueFromList(curIterable, indexRes);
        }
        Map<String, EvalResult> ourMap = (HashMap<String, EvalResult>) curIterable._value;
        String curIdx = (indexRes._value).toString();
        if(ourMap.get(curIdx) == null)
            ourMap.put(curIdx, new EvalResult(null, "null"));
        return ourMap.get(curIdx);
    }


    private EvalResult getValueFromList(EvalResult curIterable, EvalResult indexRes) {
        if(!indexRes._type.equals("int")) {
            _diagnostics.add("Array indices should be of type int, found " + indexRes._type + " at line number " + lineNumber);
            return null;
        }
        return getIthElementFromList(curIterable, indexRes);
    }

    private EvalResult getIthElementFromList(EvalResult curIterable, EvalResult indexRes) {
        List<EvalResult> ourList = (List<EvalResult>) curIterable._value;
        int curIdx = (int) indexRes._value;
        if(curIdx >= ourList.size()) {
            _diagnostics.add("Index " + curIdx + " too large for array of size " + ourList.size() + " at line number " + lineNumber);
            return null;
        }
        return ourList.get((int) indexRes._value);
    }

    private boolean isRaisingNullPointerException(EvalResult Result) {
        if(isNull(Result) || isNull(Result.getValue())) {
            _diagnostics.add("NullPointerException: Cannot access property from null, at line number " + getLineNumber());
            return true;
        }
        return false;
    }

    private EvalResult getMemberFromClassEnv(EvalResult Result, MemberAccessExpression memberName) throws Exception {
        String className = memberName._memberName._lexeme;
        Environment objectEnv = (Environment) Result._value;
        EvalResult memberEntry =  memberName.evaluate(objectEnv);
        return evaluateMember(Result, className, objectEnv, memberEntry);
    }

    private EvalResult evaluateMember(EvalResult Result, String className, Environment objectEnv, EvalResult memberEntry) {
        if (!isNull(memberEntry)) {
            Result = memberEntry;
            boolean isMemberAMethod = memberEntry.getType().equals("Closure");
            if (isMemberAMethod)
                initializeMethodEnvironment(className, objectEnv, memberEntry);
        }
        return Result;
    }

    private void initializeMethodEnvironment(String className, Environment objectEnv, EvalResult memberEntry) {
        ClosureExpression closure = (ClosureExpression) memberEntry._value;
        closure._closureEnv = objectEnv;
        closure._closureEnv.set("this", new EvalResult(closure._closureEnv, className));
    }

    // @TODO: Refactor this later -> (Final assignment for today) -> Make sure everything works again
    private EvalResult callFunction(EvalResult Initial, Expression index, Environment env) throws Exception {
        if(Initial.getType() != "Closure") {
            _diagnostics.add("Expression of type " + Initial._type + " can not be called, at line number " + getLineNumber());
            return null;
        }

        var function = (ClosureExpression) Initial._value; // FormalArgsHaven't been bound yet
        try {
            return evaluateFunctionInEnv((ParensExpression) index, env, function);
        } catch (Exception e) {
            _diagnostics.addAll(function.getDiagnostics());
            _diagnostics.add(e.getMessage());
            throw e;
        }
    }

    private EvalResult evaluateFunctionInEnv(ParensExpression index, Environment env, ClosureExpression function) throws Exception {
        List<Expression> actualArgs = getActualArgs(index);
        List<Expression> formalArgs = function._formalArgs;

        if(formalArgs.size() != actualArgs.size())
            throw new Exception("Expected " + formalArgs.size() + " arguements, got " + actualArgs.size() + " at line number " + getLineNumber());

        Environment functionArgsEnv = bindFormalArgsWithActualArgs(env, formalArgs, actualArgs);
        SyntaxTree functionBody = function._functionBody;
        functionArgsEnv._ParentEnv = function._closureEnv;
        return evaluateFunctionBody(functionBody, functionArgsEnv);
    }

    private EvalResult evaluateFunctionBody(SyntaxTree functionBody, Environment env) throws Exception {
        try {
            return functionBody.evaluate(env);
        } catch (Exception e) {
            _diagnostics.addAll(functionBody.getDiagnostics());
            throw e;
        }
    }

    private List<Expression> getActualArgs(ParensExpression index) {
        var actualArgsList = (ListExpression) index._body;
        return actualArgsList._elements;
    }

    private Environment bindFormalArgsWithActualArgs(Environment env, List<Expression> formalArgs, List<Expression> _actualArgs) throws Exception {
        Environment newEnv = new Environment(null);
        for(int i=0; i<_actualArgs.size(); i++) {
            Expression curFormalArg = formalArgs.get(i);
            Expression curActualArg = _actualArgs.get(i);
            bindArgs(env, newEnv, curFormalArg, curActualArg);
        }
        return newEnv;
    }

    private void bindArgs(Environment env, Environment newEnv, Expression curFormalArg, Expression curActualArg) throws Exception {
        try {
            EvalResult actualArgRes = curActualArg.evaluate(env);
            Bind(curFormalArg, actualArgRes, newEnv);
        } catch (Exception e) {
            _diagnostics.addAll(curActualArg.getDiagnostics());
            throw e;
        }
    }

    public void Bind(Expression left, EvalResult right, Environment env) throws Exception {
        // Another great idea... Each Expression Knows how to assign an item to it.. Add the whole logic to each separate Expression
        switch (left.getType()) {
            case IdentifierExpression:
                assignIdentifier(left, env, right);
                break;
            case ListExpression:
                assignList(left, env, right);
                break;
            case ObjectExpression:
                assignObject(left, env, right);
                break;
            case ArrayAccessExpression:
                var curExp = (ArrayAccessExpression) left;
                EvalResult ourEntry = env.get(curExp._identifier._lexeme);
                if ( ourEntry == null ) {
                    _diagnostics.add("Invalid identifier " + curExp._identifier._lexeme + " at line number " + lineNumber);
                    return;
                }
                assignIterable(env, right, curExp, ourEntry);
                break;
            default:
//                System.out.println("Got Here");
                _diagnostics.add("Expression of type " + left.getType() + " is not a valid lvalue" + " at line number " + lineNumber);
                throw new Exception("Expression of type " + left.getType() + " is not a valid lvalue" + " at line number " + lineNumber);
        }
    }

    /*
        Test It Later ----------> @TODO
     */
    public EvalResult assignIterable(Environment env, EvalResult rightRes, ArrayAccessExpression curExp, EvalResult ourEntry) throws Exception {
        EvalResult Result = evaluateCurrentIndex(env, curExp, ourEntry);
        if (Result == null) return null;
        Result._value = rightRes._value;
        Result._type = rightRes._type;
        return Result;
    }

    private EvalResult evaluateCurrentIndex(Environment env, ArrayAccessExpression curExp, EvalResult Result) throws Exception {
        for(Expression index : curExp._indices) {
            if(isNull(Result)) {
                _diagnostics.add("NullPointerException: Cannot access property from null, at line number " + lineNumber);
                return null;
            }
            Result = evaluateCurrentIndex(env, Result, index);
        }
        return Result;
    }

    private EvalResult evaluateCurrentIndex(Environment env, EvalResult Result, Expression index) throws Exception {
        /* Refactor this First... Then we'll have to think about Refactoring these two */
        if(index.getType() == ExpressionType.MemberAccessExpression)
            Result = evaluateMemberAccessExpression(Result, (MemberAccessExpression) index);
        else
            Result = getValue(Result, index, env);
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


    public EvalResult assignIdentifier(Expression left, Environment env, EvalResult right) {
        IdentifierExpression leftIdentifierExpression = (IdentifierExpression) left;
        return env.set(leftIdentifierExpression._lexeme, right);
    }

    /* List Bindings */
    /* Exception Handling at later stages */
    public EvalResult assignList(Expression left, Environment env, EvalResult right) throws Exception {
        String rightType = right._type;
        if(!rightType.equals("list")) {
            _diagnostics.add("Cannot de-structure " + rightType + " into a list. Error at line number " + lineNumber);
            return null;
        }
        return BindListsInEnv((ListExpression) left, env, right);
    }

    private EvalResult BindListsInEnv(ListExpression left, Environment env, EvalResult right) throws Exception {
        var rightList = (List<EvalResult>) right._value;
        List<Expression> leftList = left._elements;
        if(rightList.size() != leftList.size()) {
            _diagnostics.add("Dimension mismatch in expression.. the elements in lvalue and rvalue must be same, Error at line number " + lineNumber);
            return null;
        }
        return BindLists(env, rightList, leftList);
    }

    private EvalResult BindLists(Environment env, List<EvalResult> rightList, List<Expression> leftList) throws Exception {
        for(int i = 0; i < leftList.size(); i++)
            Bind(leftList.get(i), rightList.get(i), env);
        return new EvalResult(rightList, "list");
    }

    /*******************************    Function object Bindings    ********************************/
    public EvalResult assignObject(Expression left, Environment env, EvalResult right) throws Exception {
        Map<Expression, Expression> leftObject = getLeftObject((ObjectExpression) left);
        Map<String, EvalResult> rightObject = getRightObject(right);
        if (rightObject == null) return null;
        boolean foundInvalidKey = BindObjectsInEnv(env, leftObject, rightObject);
        if (foundInvalidKey) right = null;
        return right;
    }

    private boolean BindObjectsInEnv(Environment env, Map<Expression, Expression> leftObject, Map<String, EvalResult> rightObject) throws Exception {
        boolean foundInvalidKey = false;
        for(Expression leftObjectKey : leftObject.keySet()) {
            foundInvalidKey = !isKeyValid(leftObjectKey);
            if(foundInvalidKey) break;
            String key = getKeyFromExpression(leftObjectKey);
            Expression leftExp = leftObject.get(leftObjectKey);
            EvalResult rightRes = rightObject.get(key);

            if(rightRes == null) {
                _diagnostics.add("Key " + key + " is not present in the RHS of the assignement at line number " + lineNumber);
                foundInvalidKey = true;
                break;
            }
            Bind(leftExp, rightRes, env);
        }
        return foundInvalidKey;
    }

    private boolean isKeyValid(Expression key) {
        boolean isKeyOfValidType = isIntegerKey(key) || isStringKey(key);
        if (!isKeyOfValidType)
            _diagnostics.add("Invalid key type in object, at line number " + lineNumber);
        return isKeyOfValidType;
    }

    private String getKeyFromExpression(Expression keyExp) {
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

    private boolean isStringKey(Expression keyExp) {
        return keyExp.getType() == ExpressionType.StringExpression;
    }

    private boolean isIntegerKey(Expression keyExp) {
        return keyExp.getType() == ExpressionType.IntExpression;
    }

    private Map<String, EvalResult> getRightObject(EvalResult right) {
        if(!Objects.equals(right._type, "object")) {
            _diagnostics.add("Expected an object, found " + right._type + " at line number " + lineNumber);
            return null;
        }
        return (HashMap<String, EvalResult>) right.getValue();
    }

    private Map<Expression, Expression> getLeftObject(ObjectExpression left) {
        return left._contents;
    }



    private boolean isNull(Object object) {
        return object == null;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public List<String> get_diagnostics() {
        return _diagnostics;
    }
}


// Take all the static methods and copy here... Make sure we have no dependency on AssignmentExpressio