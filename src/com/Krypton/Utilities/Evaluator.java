package com.Krypton.Utilities;

import com.Krypton.EvalResult;
import com.Krypton.ExpressionType;
import com.Krypton.Syntax.Expression;
import com.Krypton.Syntax.PrimaryExpressions.ArrayAccessExpression;
import com.Krypton.Syntax.PrimaryExpressions.AssignmentExpression;
import com.Krypton.Syntax.PrimaryExpressions.MemberAccessExpression;
import com.Krypton.Syntax.PrimaryExpressions.ParensExpression;
import com.Krypton.Syntax.Values.ClosureExpression;
import com.Krypton.Syntax.Values.ListExpression;
import com.Krypton.SyntaxTree;
import com.Krypton.Token;

import java.util.ArrayList;
import java.util.List;

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
        for (Expression index : _indices) {
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
            Result = AssignmentExpression.getValue(Result, index, env, _diagnostics, getLineNumber());
        return Result;
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
        var function = (ClosureExpression) Initial._value;
        Environment functionEnv = function._closureEnv;
        SyntaxTree functionBody = function._functionBody;
        return evaluateFunctionInEnv((ParensExpression) index, env, function, functionEnv, functionBody);
    }

    private EvalResult evaluateFunctionInEnv(ParensExpression index, Environment env, ClosureExpression function, Environment functionEnv, SyntaxTree functionBody) throws Exception {
        List<Expression> actualArgs = getActualArgs(index);
        List<Expression> formalArgs = function._formalArgs;

        if(formalArgs.size() != actualArgs.size()) {
            _diagnostics.add("Expected " + formalArgs.size() + " arguements, got " + actualArgs.size() + " at line number " + getLineNumber());
            return null;
        }

        Environment functionArgsEnv = bindFormalArgsWithActualArgs(env, formalArgs, actualArgs);
        functionArgsEnv._ParentEnv = functionEnv;
        return evaluateFunctionBody(functionBody, functionArgsEnv);
    }

    private EvalResult evaluateFunctionBody(SyntaxTree functionBody, Environment env) throws Exception {
        EvalResult finalRes = functionBody.evaluate(env);
        _diagnostics.addAll(functionBody.getDiagnostics());
        return finalRes;
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
            EvalResult actualArgRes = curActualArg.evaluate(env);
            Bind(curFormalArg, actualArgRes, newEnv);
        }
        return newEnv;
    }

    public EvalResult Bind(Expression left, EvalResult right, Environment env) throws Exception{
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


// Take all the static methods and copy here... Make sure we have no dependency on AssignmentExpression