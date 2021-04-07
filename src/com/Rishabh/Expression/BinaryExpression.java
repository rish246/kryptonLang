package com.Rishabh.Expression;

import com.Rishabh.EvalResult;
import com.Rishabh.ExpressionType;
import com.Rishabh.TokenType;
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

        EvalResult leftRes = _left.evaluate(env);
        _diagnostics.addAll(_left.getDiagnostics());

        EvalResult rightRes = _right.evaluate(env);
        _diagnostics.addAll(_right.getDiagnostics());

        if(_diagnostics.size() > 0)
            return null;


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
                case LogicalAndToken:
                    return new EvalResult(((boolean) leftRes._value && (boolean) rightRes._value), "boolean");
                case LogicalOrToken:
                    return new EvalResult(((boolean) leftRes._value || (boolean) rightRes._value), "boolean");
                case EqualityToken:
                    return new EvalResult(leftRes._value == rightRes._value, "boolean");
                case NotEqualsToken:
                    return new EvalResult(leftRes._value != rightRes._value, "boolean");
            }
        }

        

        _diagnostics.add("Undefined operator " + _operatorToken + " for types " + leftType + " and " + rightType);
        return null;

    }

}
