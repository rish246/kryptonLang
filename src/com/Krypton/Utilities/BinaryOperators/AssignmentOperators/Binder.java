package com.Krypton.Utilities.BinaryOperators.AssignmentOperators;

import com.Krypton.EvalResult;
import com.Krypton.ExpressionType;
import com.Krypton.Syntax.Expression;
import com.Krypton.Syntax.PrimaryExpressions.ArrayAccessExpression;
import com.Krypton.Syntax.PrimaryExpressions.IdentifierExpression;
import com.Krypton.Syntax.PrimaryExpressions.MemberAccessExpression;
import com.Krypton.Syntax.Values.ListExpression;
import com.Krypton.Syntax.Values.ObjectExpression;
import com.Krypton.Syntax.Values.StringExpression;
import com.Krypton.Token;
import com.Krypton.Utilities.CustomExceptions.BinaryOperators.BadAssignmentException;
import com.Krypton.Utilities.CustomExceptions.BinaryOperators.InvalidOperationException;
import com.Krypton.Utilities.Environment;

import java.util.*;

public class Binder {
    private int _lineNumber;
    private List<String> _diagnostics = new ArrayList<>();

    public Binder(int lineNumber) {
        _lineNumber = lineNumber;
    }

    public EvalResult bind(Expression leftSide, Expression rightSide, Environment env) throws Exception {
        try {
            EvalResult rightRes = rightSide.evaluate(env);
            return bindExpressionToEvalResult(leftSide, rightRes, env);
        } catch (Exception e) {
            _diagnostics.add(e.getMessage());
            _diagnostics.addAll(rightSide.getDiagnostics());
            _diagnostics.addAll(leftSide.getDiagnostics());
            throw e;
        }
    }

    public EvalResult bindExpressionToEvalResult(Expression leftSide, EvalResult rightRes, Environment env) throws Exception {
        if ( leftSide.getType() == ExpressionType.IdentifierExpression)
            return bind((IdentifierExpression) leftSide, rightRes, env);

        if ( leftSide.getType() == ExpressionType.ListExpression)
            return bind((ListExpression) leftSide, rightRes, env);

        if (leftSide.getType() == ExpressionType.ObjectExpression)
            return bind((ObjectExpression) leftSide, rightRes, env);

        if (leftSide.getType() == ExpressionType.ArrayAccessExpression)
            return bind((ArrayAccessExpression) leftSide, rightRes, env);

        if (leftSide.getType() == ExpressionType.MemberAccessExpression)
            return bind((MemberAccessExpression) leftSide, rightRes, env);


        throw new BadAssignmentException("Invalid operator '=' for type " + leftSide.getType() + " and " + rightRes.getType() + " at line number " + _lineNumber);
    }

    private EvalResult bind(MemberAccessExpression leftSide, EvalResult rightRes, Environment env) throws Exception {
        Expression object = leftSide._object;
        Token member = leftSide._memberName;
        var memberExp = new StringExpression(member._lexeme, _lineNumber);
        EvalResult bodyRes = object.evaluate(env);
        switch (bodyRes.getType()) {
            case "object":
                return updateObjectValue(rightRes, env, bodyRes, memberExp);
            default:
                throw new InvalidOperationException("Object of type " + object.getType() + " is not updatable, error at line number " + _lineNumber);
        }
    }

    public List<String> getDiagnostics() {
        return _diagnostics;
    }

    private EvalResult bind(ArrayAccessExpression leftSide, EvalResult rightRes, Environment env) throws Exception {
        Expression body = leftSide._body;
        Expression index = leftSide.getIndex();
        EvalResult bodyRes = body.evaluate(env);
        switch (bodyRes.getType()) {
            case "object":
                return updateObjectValue(rightRes, env, bodyRes, index);
            case "list":
                return updateListIthValue(rightRes, env, bodyRes, index);
            default:
                throw new InvalidOperationException("Object of type " + body.getType() + " is not updatable, error at line number " + _lineNumber);
        }
    }

    private EvalResult updateObjectValue(EvalResult rightRes, Environment env, EvalResult bodyRes, Expression index) throws Exception {
        Map<String, EvalResult> bodyMap = (HashMap) bodyRes.getValue();
        EvalResult indexRes = index.evaluate(env);
        try {
            return bodyMap.put(indexRes.getValue().toString(), rightRes);
        } catch (Exception e) {
            _diagnostics.add("Expression of type " + indexRes.getType() + " is not hashable, error at line number " + _lineNumber);
            throw e;
        }
    }

    private EvalResult updateListIthValue(EvalResult rightRes, Environment env, EvalResult bodyRes, Expression index) throws Exception {
        List<EvalResult> leftList = (List) bodyRes.getValue();
        EvalResult indexRes = index.evaluate(env);
        if ( !Objects.equals(indexRes.getType(), "int") )
            throw new Exception("Expected an 'int', instead got " + indexRes.getType() + ", error at line number " + _lineNumber);
        return leftList.set((int) indexRes.getValue(), rightRes);

    }


    private EvalResult bind(ObjectExpression leftSide, EvalResult rightRes, Environment env) throws Exception {
        Map<Expression, Expression> leftObject = leftSide.getContents();
        Map<String, EvalResult> rightObject = (HashMap) rightRes.getValue();

        try {
            for (Expression key : leftObject.keySet()) {
                String keyString = ((StringExpression) key).getValue();
                Expression leftValue = leftObject.get(key);
                EvalResult rightValue = rightObject.get(keyString);
                if (rightValue == null)
                    throw new BadAssignmentException(keyString);

                bindExpressionToEvalResult(leftValue, rightValue, env);
            }
        } catch (NullPointerException e) {
            throw new BadAssignmentException("Key "+ e.getMessage() + " is not present in the object, error at line number" + _lineNumber);
        }


        return rightRes;
    }

    private EvalResult bind(ListExpression leftSide, EvalResult rightRes, Environment env) throws Exception {
        // assume they both are of same length
        List<Expression> leftList = leftSide.getElements();
        List<EvalResult> rightList = (List<EvalResult>) rightRes.getValue();

        if (leftList.size() != rightList.size())
            throw new BadAssignmentException("left and right lists should be of same size, error at line number " + leftSide.getLineNumber());

        for(int i=0; i < leftList.size(); i++)
            bindExpressionToEvalResult(leftList.get(i), rightList.get(i), env);

        return new EvalResult(rightList, "list");
    }

    private EvalResult bind(IdentifierExpression leftExp, EvalResult rightRes, Environment env) {
        String identifierName = leftExp.getLexeme();
        return env.set(identifierName, rightRes);
    }



}