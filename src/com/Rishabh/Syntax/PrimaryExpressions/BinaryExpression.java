package com.Rishabh.Syntax.PrimaryExpressions;

import com.Rishabh.EvalResult;
import com.Rishabh.ExpressionType;
import com.Rishabh.Syntax.Expression;
import com.Rishabh.TokenType;
import com.Rishabh.Utilities.Environment;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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



    @Override
    public EvalResult evaluate(Environment env) throws Exception {

        // Logical operators are not evaluated the same way... take them out
        if(isLogicalOperator(_operatorToken)) {
            EvalResult result = evalLogicalExpression(env);
            if(result == null) {
                _diagnostics.add("Invalid logical operation"+ " at line number " + getLineNumber());
            }
            return result;
        }




        EvalResult leftRes = _left.evaluate(env);
        _diagnostics.addAll(_left.getDiagnostics());

        EvalResult rightRes = _right.evaluate(env);
        _diagnostics.addAll(_right.getDiagnostics());

        if(leftRes == null || rightRes == null) {
            _diagnostics.add("Error in the Binary Expression"+ " at line number " + getLineNumber());
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
        else if(leftType.equals("float") && rightType.equals("float")) {

            Float leftValue = (float) leftRes._value;
            Float rightValue = (float) rightRes._value;

            switch(_operatorToken) {
                    case AddToken:
                        return new EvalResult(leftValue + rightValue, "float");
                    case SubToken:
                        return new EvalResult(leftValue - rightValue, "float");
                    case MultToken:
                        return new EvalResult(leftValue  * rightValue, "float");
                    case DivToken:
                        return new EvalResult(leftValue /  rightValue, "float");
                    case LessThanToken:
                        return new EvalResult(leftValue < rightValue, "boolean");
                    case LessThanEqualToken:
                        return new EvalResult(leftValue <= rightValue, "boolean");
                    case GreaterThanToken:
                        return new EvalResult(leftValue > rightValue, "boolean");
                    case GreaterThanEqualToken:
                        return new EvalResult(leftValue >= rightValue, "boolean");
                    case EqualityToken:
                        return new EvalResult(leftRes._value == rightRes._value, "boolean");
                    case NotEqualsToken:
                        return new EvalResult(leftRes._value != rightRes._value, "boolean");
                }
        }
        else if(leftType.equals("float") && rightType.equals("int") 
        || rightType.equals("float") && leftType.equals("int")) {

            Float[] leftAndRightValues = caseLeftAndRightValuesToFloat(leftRes, rightRes);
            float leftValue = leftAndRightValues[0];
            float rightValue = leftAndRightValues[1];
        switch(_operatorToken) {
            case MultToken: {
                float product = leftValue * rightValue;
                return new EvalResult(product, "float");
            }

            case DivToken: {
                return new EvalResult(leftValue / rightValue, "float");
            }

            case AddToken: {
                return new EvalResult(leftValue + rightValue, "float");
            }

            case SubToken: {
                return new EvalResult(leftValue - rightValue, "float");
            }
        }
    }

        else if(leftType.equals("list") && rightType.equals("list")) {
            // return new EvalResult of type list
            List<EvalResult> leftList = (List) leftRes._value;
            List<EvalResult> rightList = (List) rightRes._value;

            switch(_operatorToken) {
                case AddToken: {
                    List<EvalResult> concatenatedList = new ArrayList<>();
                    concatenatedList.addAll(copyList(leftList));
                    concatenatedList.addAll(copyList(rightList));
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
                    List<EvalResult> newList = new ArrayList<>();
                    int nTimes = (int) rightRes._value;
                    if(nTimes < 0) {
                        _diagnostics.add("Invalid value in the array assign expression"+ " at line number " + getLineNumber());
                        return null;
                    }

                    for(int i=0; i<nTimes; i++) {
                        List<EvalResult> copiedList = copyList(leftList);
                        newList.addAll(copiedList);
                    }

                    return new EvalResult(newList, "list");
                }

                case AddToken: {
                    List leftList = (List) leftRes._value;
                    List<Object> newList = new ArrayList<>();
                    newList.addAll(copyList(leftList));
                    newList.add(rightRes);

                    return new EvalResult(newList, "list");
                }
            }

        }
        
        else if(leftType.equals("list")) {

            switch(_operatorToken) {
                case AddToken: {
                    List<EvalResult> leftList = (List) leftRes._value;
                    List<Object> newList = new ArrayList<>();
                    newList.addAll(copyList(leftList));
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

        else if(rightType.equals("null")) {
            switch(_operatorToken) {
                case EqualityToken: {
                    return new EvalResult(leftType.equals("null"), "boolean");
                }

                case NotEqualsToken: {
                    return new EvalResult(!leftType.equals("null"), "boolean");
                }
            }
        }

        

        _diagnostics.add("Undefined operator " + _operatorToken + " for types " + leftType + " and " + rightType+ " at line number " + getLineNumber());
        return null;

    }

    private Float[] caseLeftAndRightValuesToFloat(EvalResult leftRes, EvalResult rightRes) {
        String leftType = leftRes._type;
        String rightType = rightRes._type;
        float leftValue = 0.0f;
        float rightValue = 0.0f;
        if(leftType.equals("int") && rightType.equals("float")) {
            leftValue = (int) leftRes._value * 1.0f;
            rightValue = (float) rightRes._value;
        }
        else if(rightType.equals("int") && leftType.equals("float")) {
            leftValue = (float) leftRes._value;
            rightValue = (int) rightRes._value * 1.0f;
        }

        Float[] result = {leftValue, rightValue};
        return result;
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
                if(!rightType.equals("boolean")) {
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

    private List<EvalResult> copyList(List<EvalResult> list) {
        List<EvalResult> result = new ArrayList<>();
        for(EvalResult exp : list) {
            result.add(new EvalResult(exp._value, exp._type));
        }
        return result;
    }

}
