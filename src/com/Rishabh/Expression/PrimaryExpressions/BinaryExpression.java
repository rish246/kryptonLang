package com.Rishabh.Expression.PrimaryExpressions;

import com.Rishabh.EvalResult;
import com.Rishabh.ExpressionType;
import com.Rishabh.TokenType;
import com.Rishabh.Expression.Expression;
import com.Rishabh.Utilities.Environment;


import java.util.ArrayList;
import java.util.List;

public class BinaryExpression extends Expression {
    Expression _left, _right;
    TokenType _operatorToken;
//    ExpressionType _type;
    List<String> _diagnostics = new ArrayList<>();

    public BinaryExpression(Expression left, TokenType operatorToken, Expression right) {
        super(ExpressionType.BinaryExpression);
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



    @Override
    public EvalResult evaluate(Environment env) throws Exception {

        // Logical operators are not evaluated the same way... take them out
        if(isLogicalOperator(_operatorToken)) {
            EvalResult result = evalLogicalExpression(env);
            if(result == null) {
                _diagnostics.add("Invalid logical operation");
            }
            return result;
        }




        EvalResult leftRes = _left.evaluate(env);
        _diagnostics.addAll(_left.getDiagnostics());

        EvalResult rightRes = _right.evaluate(env);
        _diagnostics.addAll(_right.getDiagnostics());

        if(leftRes == null || rightRes == null) {
            _diagnostics.add("Error in the Binary Expression");
            return null;
        }


        // Get the Types of the leftRes and rightRes
        String leftType = leftRes._type;
        String rightType = rightRes._type;

        if (leftType.equals("int") && rightType.equals("int")) {
            switch (_operatorToken) {
                case AddToken:
                    return new EvalResult(((int) leftRes._value + (int) rightRes._value), "int");
                case SubToken:
                    return new EvalResult(((int) leftRes._value - (int) rightRes._value), "int");
                case MultToken:
                    return new EvalResult(((int) leftRes._value * (int) rightRes._value), "int");
                case DivToken:
                    return new EvalResult(((int) leftRes._value / (int) rightRes._value), "int");
                case ModuloToken:
                    return new EvalResult(((int) leftRes._value % (int) rightRes._value), "int");
                case LessThanToken:
                    return new EvalResult(((int) leftRes._value < (int) rightRes._value), "boolean");
                case LessThanEqualToken:
                    return new EvalResult(((int) leftRes._value <= (int) rightRes._value), "boolean");
                case GreaterThanToken:
                    return new EvalResult(((int) leftRes._value > (int) rightRes._value), "boolean");
                case GreaterThanEqualToken:
                    return new EvalResult(((int) leftRes._value >= (int) rightRes._value), "boolean");
                case EqualityToken:
                    return new EvalResult(leftRes._value == rightRes._value, "boolean");
                case NotEqualsToken:
                    return new EvalResult(leftRes._value != rightRes._value, "boolean");
            }


        }
        else if (leftType.equals("boolean") && rightType.equals("boolean")) {
            switch (_operatorToken) {
                case EqualityToken:
                    return new EvalResult(leftRes._value == rightRes._value, "boolean");
                case NotEqualsToken:
                    return new EvalResult(leftRes._value != rightRes._value, "boolean");
            }
        }
        else if(leftType.equals("list") && rightType.equals("list")) {
            // return new EvalResult of type list
            List leftList = (List) leftRes._value;
            List rightList = (List) rightRes._value;

            switch(_operatorToken) {
                case AddToken: {
                    List<Expression> concatenatedList = new ArrayList<>();
                    concatenatedList.addAll(leftList);
                    concatenatedList.addAll(rightList);
                    return new EvalResult(concatenatedList, "list");
                }

                case EqualityToken: {
                    return new EvalResult(leftList.equals(rightList), "boolean");
                }

                case NotEqualsToken: {
                    return new EvalResult(!leftList.equals(rightList), "boolean");
                }


            }


        }

        else if(leftType.equals("list") && rightType.equals("int")) {

            switch(_operatorToken) {
                case MultToken: {
                    List leftList = (List) leftRes._value;
                    List<Object> newList = new ArrayList<>();
                    int nTimes = (int) rightRes._value;
                    if(nTimes < 0) {
                        _diagnostics.add("Invalid value in the array assign expression");
                        return null;
                    }

                    for(int i=0; i<nTimes; i++) {
                        newList.addAll(leftList);
                    }

                    return new EvalResult(newList, "list");
                }

                case AddToken: {
                    List leftList = (List) leftRes._value;
                    List<Object> newList = new ArrayList<>();
                    newList.addAll(leftList);
                    newList.add(rightRes);

                    return new EvalResult(newList, "list");
                }
            }

        }
        
        else if(leftType.equals("list")) {

            switch(_operatorToken) {
                case AddToken: {
                    List leftList = (List) leftRes._value;
                    List<Object> newList = new ArrayList<>();
                    newList.addAll(leftList);
                    newList.add(rightRes);

                    return new EvalResult(newList, "list");
                }
            }

        }



        else if(leftType.equals("string")) {
            switch(_operatorToken) {
                case AddToken:
                    return new EvalResult(leftRes._value + rightRes._value.toString(), "string");
                case EqualityToken:
                    return new EvalResult(leftRes._value.equals(rightRes._value), "boolean");
                case NotEqualsToken:
                    return new EvalResult(!leftRes._value.equals(rightRes._value), "boolean");
            }
        }

        

        _diagnostics.add("Undefined operator " + _operatorToken + " for types " + leftType + " and " + rightType);
        return null;

    }

    private EvalResult evalLogicalExpression(Environment env) throws Exception {

        switch (_operatorToken) {
            case LogicalAndToken: {
                EvalResult leftRes = _left.evaluate(env);
                String leftType = leftRes._type;
                boolean leftValue = (boolean) leftRes._value;
                if(leftType != "boolean") {
                    _diagnostics.add("Undefined operator " + _operatorToken + " for type " + leftType);
                    return null;
                }

                if(!leftValue)
                    return new EvalResult(false, "boolean");

                EvalResult rightRes = _right.evaluate(env);
                String rightType = leftRes._type;
                if(rightType != "boolean") {
                    _diagnostics.add("Undefined operator " + _operatorToken + " for type " + leftType);
                    return null;
                }

                boolean rightValue = (boolean) rightRes._value;
                return new EvalResult(rightValue, "boolean");

            }

            case LogicalOrToken: {
                EvalResult leftRes = _left.evaluate(env);
                String leftType = leftRes._type;
                boolean leftValue = (boolean) leftRes._value;
                if(leftType != "boolean") {
                    _diagnostics.add("Undefined operator " + _operatorToken + " for type " + leftType);
                    return null;
                }

                if(leftValue)
                    return new EvalResult(true, "boolean");

                EvalResult rightRes = _right.evaluate(env);
                String rightType = leftRes._type;
                if(rightType != "boolean") {
                    _diagnostics.add("Undefined operator " + _operatorToken + " for type " + leftType);
                    return null;
                }

                boolean rightValue = (boolean) rightRes._value;
                return new EvalResult(rightValue, "boolean");
            }

        }

        _diagnostics.add("Invalid Operator " + _operatorToken);
        return null;
    }

    private boolean isLogicalOperator(TokenType operatorToken) {
        return operatorToken == TokenType.LogicalAndToken || operatorToken == TokenType.LogicalOrToken;
    }

}
