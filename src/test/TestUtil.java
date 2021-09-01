package test;

import com.Krypton.EvalResult;
import com.Krypton.Syntax.Expression;
import com.Krypton.Syntax.PrimaryExpressions.BinaryExpression;
import com.Krypton.Syntax.PrimaryExpressions.IdentifierExpression;
import com.Krypton.Syntax.Values.ListExpression;
import com.Krypton.Syntax.Values.NumberExpression;
import com.Krypton.Syntax.Values.StringExpression;
import com.Krypton.TokenType;

import java.util.Arrays;
import java.util.List;

public class TestUtil {
    public static int dummyLineNumber = 1;

    public static IdentifierExpression Id(String lexeme) {
        return new IdentifierExpression(lexeme, dummyLineNumber);
    }

    public static NumberExpression Int(int value) {
        return new NumberExpression(value, dummyLineNumber);
    }


    public static StringExpression Str(String value) {
        return new StringExpression(value, dummyLineNumber);
    }

    public static ListExpression List(List<Expression> elements) {
        return new ListExpression(elements, dummyLineNumber);
    }

    public static ListExpression List(Expression element) {
        return new ListExpression(Arrays.asList(element), dummyLineNumber);
    }

    public static EvalResult ListRes(List<EvalResult> elements) {
        return new EvalResult(elements, "list");
    }

    public static EvalResult IntRes(int value) {
        return new EvalResult(value, "int");
    }

    public static EvalResult StrRes(String value) {
        return new EvalResult(value, "string");
    }


    public static BinaryExpression Add(Expression left, Expression right) {
        return new BinaryExpression(left, TokenType.AddToken, right, dummyLineNumber);
    }

    public static BinaryExpression Divide(Expression left, Expression right) {
        return new BinaryExpression(left, TokenType.DivToken, right, dummyLineNumber);
    }

    public static BinaryExpression Multiply(Expression left, Expression right) {
        return new BinaryExpression(left, TokenType.MultToken, right, dummyLineNumber);
    }

    public static BinaryExpression Subtract(Expression left, Expression right) {
        return new BinaryExpression(left, TokenType.SubToken, right, dummyLineNumber);
    }

    public static BinaryExpression Modulo(Expression left, Expression right) {
        return new BinaryExpression(left, TokenType.ModuloToken, right, dummyLineNumber);
    }

    public static BinaryExpression LessThan(Expression left, Expression right) {
        return new BinaryExpression(left, TokenType.LessThanToken, right, dummyLineNumber);
    }
    public static BinaryExpression LessThanEqual(Expression left, Expression right) {
        return new BinaryExpression(left, TokenType.LessThanEqualToken, right, dummyLineNumber);
    }
    public static BinaryExpression GreaterThan(Expression left, Expression right) {
        return new BinaryExpression(left, TokenType.GreaterThanToken, right, dummyLineNumber);
    }
    public static BinaryExpression GreaterThanEqual(Expression left, Expression right) {
        return new BinaryExpression(left, TokenType.GreaterThanEqualToken, right, dummyLineNumber);
    }
    public static BinaryExpression Equals(Expression left, Expression right) {
        return new BinaryExpression(left, TokenType.EqualityToken, right, dummyLineNumber);
    }
    public static BinaryExpression NotEquals(Expression left, Expression right) {
        return new BinaryExpression(left, TokenType.EqualityToken, right, dummyLineNumber);
    }

    public static BinaryExpression LogicalAnd(Expression left, Expression right) {
        return new BinaryExpression(left, TokenType.LogicalAndToken, right, dummyLineNumber);
    }

    public static BinaryExpression LogicalOr(Expression left, Expression right) {
        return new BinaryExpression(left, TokenType.LogicalOrToken, right, dummyLineNumber);
    }




}
