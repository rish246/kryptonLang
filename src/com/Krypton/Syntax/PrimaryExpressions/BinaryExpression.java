package com.Krypton.Syntax.PrimaryExpressions;

import com.Krypton.EvalResult;
import com.Krypton.ExpressionType;
import com.Krypton.Syntax.Expression;
import com.Krypton.TokenType;
import com.Krypton.Utilities.CustomExceptions.BinaryOperators.InvalidOperationException;
import com.Krypton.Utilities.Environment;
import com.Krypton.Utilities.Printer;

import java.util.ArrayList;
import java.util.List;

public class BinaryExpression extends Expression {
    Expression _left, _right;
    TokenType _operatorToken;
    //    ExpressionType _type;
    List<String> _diagnostics = new ArrayList<>();

    public BinaryExpression(Expression left, TokenType operatorToken, Expression right, int lineNumber) {
        super(ExpressionType.BinaryExpression, lineNumber);
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
        // Add some long lines here
        System.out.println("|");
        System.out.println(indent + "|");

        System.out.print(indent + "└──");
        _right.prettyPrint(indent + "    ");

    }

    @Override
    public List<String> getDiagnostics() {
        return _diagnostics;
    }


    // It'll take a lot of fucking time to do... (Tomorrow ---> Do this)
    @Override
    public EvalResult evaluate(Environment env) throws Exception {
        // Try running this code now
        switch (_operatorToken) {
            // Only check for adding different members
            case AddToken:
                return addExpressions(env);
        }

        throw new InvalidOperationException("Invalid Binary operator " + _operatorToken + " For types " + _left.getType() + " and " + _right.getType());

    }


    public EvalResult addExpressions(Environment env) throws Exception {
        // Add these two in env
        try {
            EvalResult leftRes = _left.evaluate(env);
            EvalResult rightRes = _right.evaluate(env);
            if ( isAnInt(leftRes) && isAnInt(rightRes) )
                return new EvalResult((int) leftRes.getValue() + (int) rightRes.getValue(), "int");

            if (isFloatOrInt(leftRes) && isFloatOrInt(rightRes))
                return new EvalResult(Float.parseFloat(leftRes.getValue().toString()) + Float.parseFloat(rightRes.getValue().toString()), "float");


            if(isList(leftRes) && isList(rightRes)) {
                List<EvalResult> leftListCopy = copyList((List)leftRes.getValue());
                List<EvalResult> rightListCopy = copyList((List) rightRes.getValue());

                List<EvalResult> concatenatedList = new ArrayList<>();
                concatenatedList.addAll(leftListCopy);
                concatenatedList.addAll(rightListCopy);
                return new EvalResult(concatenatedList, "list");
            }

            if(isList(leftRes)) {
                List<EvalResult> leftListCopy = copyList((List)leftRes.getValue());
                leftListCopy.add(rightRes);
                return new EvalResult(leftListCopy, "list");
            }

            if (isString(leftRes) || isString(rightRes)) {
                String leftStr = Printer.getPrintableValue(leftRes).toString();
                String rightStr = Printer.getPrintableValue(rightRes).toString();
                return new EvalResult(leftStr + rightStr, "string");
            }


            throw new InvalidOperationException("Invalid Binary operator " + _operatorToken + " For types " + _left.getType() + " and " + _right.getType());
        } catch (Exception e) {
            _diagnostics.addAll(_left.getDiagnostics());
            _diagnostics.addAll(_right.getDiagnostics());
            throw e;
        }
    }

    private boolean isString(EvalResult result) {
        return isType(result, "string");
    }

    private List<EvalResult> copyList(List<EvalResult> list) {
        List<EvalResult> result = new ArrayList<>();
        for(EvalResult exp : list) {
            result.add(new EvalResult(exp.getValue(), exp.getType()));
        }
        return result;
    }

    private boolean isType(EvalResult result, String type) {
        return result.getType().equals(type);
    }

    private boolean isAnInt(EvalResult result) {
        return isType(result, "int");
    }

    private boolean isFloatOrInt(EvalResult result) {
        return isFloat(result) || isAnInt(result);
    }

    private boolean isList(EvalResult result) {
        return isType(result, "list");
    }

    private boolean isFloat(EvalResult result) {
        return isType(result, "float");
    }

}
