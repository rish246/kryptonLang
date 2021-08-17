
package com.Krypton.Syntax.PrimaryExpressions;

import com.Krypton.EvalResult;
import com.Krypton.ExpressionType;
import com.Krypton.Syntax.Expression;
import com.Krypton.Syntax.Values.ClosureExpression;
import com.Krypton.Syntax.Values.ListExpression;
import com.Krypton.SyntaxTree;
import com.Krypton.Token;
import com.Krypton.Utilities.Environment;

import java.util.ArrayList;
import java.util.List;

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
        EvalResult result = null;
        EvalResult ourIdentifierEntry = env.get(_identifier._lexeme);

        if (isNull(ourIdentifierEntry)) {
            _diagnostics.add("Invalid identifier " + _identifier._lexeme + " at line number " + getLineNumber());
        } else {
            result = Evaluate(env, ourIdentifierEntry);
        }
        return result;
    }

    private EvalResult Evaluate(Environment env, EvalResult ourEntry) throws Exception {
        EvalResult Result = ourEntry;
        for(Expression index : _indices) {
            Result = EvaluateIndexInEnv(env, Result, index);
            if(isNull(Result)) return null;
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
            AssignmentExpression.Bind(curFormalArg, actualArgRes, newEnv, _diagnostics, getLineNumber());
        }
        return newEnv;
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

    private boolean isNull(Object object) {
        return object == null;
    }

    public List<String> getDiagnostics() {
        return _diagnostics;
    }
}