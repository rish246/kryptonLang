package test.Syntax.PrimaryExpression.BinaryExpression;

import com.Krypton.EvalResult;
import com.Krypton.Syntax.Expression;
import com.Krypton.Syntax.PrimaryExpressions.BinaryExpression;
import com.Krypton.Syntax.Values.*;
import com.Krypton.TokenType;
import com.Krypton.Utilities.CustomExceptions.BinaryOperators.InvalidOperationException;
import com.Krypton.Utilities.Environment;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class MultiplyTest {
    protected Expression left;
    protected Expression right;
    protected int dummyLineNumber = 1;
    protected Environment env;

    @Before
    public void setUp() {
        left = new NumberExpression(4, dummyLineNumber);
        right = new NumberExpression(5, dummyLineNumber);
        env = new Environment(null);
    }

    @Test(expected = InvalidOperationException.class)
    public void testMultiplyThrowsInvalidOperationException() throws Exception {
        left = new BoolExpression(true, dummyLineNumber);
        var sumExpression = new BinaryExpression(left, TokenType.MultToken, right, dummyLineNumber);
        sumExpression.evaluate(env);
    }

    @Test
    public void testMultiplyInts() throws Exception {
        var sumExpression = new BinaryExpression(left, TokenType.MultToken, right, dummyLineNumber);
        EvalResult sumTwo = sumExpression.evaluate(env);
        assertEquals(sumTwo.getValue(), 20);
    }

    @Test
    public void testMultiplyFloats() throws Exception {
        left = new FloatExpression(4.58f, dummyLineNumber);
        right = new FloatExpression(8.45F, dummyLineNumber);
        var sumExpression = new BinaryExpression(left, TokenType.MultToken, right, dummyLineNumber);
        EvalResult sumTwo = sumExpression.evaluate(env);
        Assert.assertEquals((float)sumTwo._value, (4.58F * 8.45F), 0.0002f);
    }



    @Test
    public void testMultiplyFloatAndInt() throws Exception {
        left = new NumberExpression(4, dummyLineNumber);
        right = new FloatExpression(8.45F, dummyLineNumber);
        var sumExpression = new BinaryExpression(left, TokenType.MultToken, right, dummyLineNumber);
        EvalResult sumTwo = sumExpression.evaluate(env);
        Assert.assertEquals(sumTwo, new EvalResult((4.0f * 8.45F), "float"));
    }


    @Test
    public void testMultiplyListAndInt() throws Exception {
        List<Expression> leftList = Arrays.asList(
                new NumberExpression(3, dummyLineNumber),
                new StringExpression("value", dummyLineNumber));

        List<EvalResult> result = Arrays.asList(
                new EvalResult(3, "int"),
                new EvalResult("value", "string"),
                new EvalResult(3, "int"),
                new EvalResult("value", "string"));


        left = new ListExpression(leftList, dummyLineNumber);
        right = new NumberExpression(2, dummyLineNumber);

        var prodExpression = new BinaryExpression(left, TokenType.MultToken, right, dummyLineNumber);
        EvalResult productTwo = prodExpression.evaluate(env);
        List<EvalResult> finalRes = (List) productTwo.getValue();
        Assert.assertEquals(finalRes, result);
    }

    @Test(expected = InvalidOperationException.class)
    public void testMultiplyInvalidTypesThrowsInvalidOperationException() throws Exception {
        right = new StringExpression("value", dummyLineNumber);
        var prodExpression = new BinaryExpression(left, TokenType.MultToken, right, dummyLineNumber);
        prodExpression.evaluate(env);
    }

}

/*
    Write tests to make sure pass by ref is working properly..
 */