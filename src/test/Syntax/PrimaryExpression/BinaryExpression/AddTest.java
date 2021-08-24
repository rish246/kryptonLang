package test.Syntax.PrimaryExpression.BinaryExpression;

import com.Krypton.EvalResult;
import com.Krypton.Syntax.Expression;
import com.Krypton.Syntax.PrimaryExpressions.BinaryExpression;
import com.Krypton.Syntax.Values.*;
import com.Krypton.TokenType;
import com.Krypton.Utilities.CustomExceptions.BinaryOperators.InvalidOperationException;
import com.Krypton.Utilities.Environment;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static junit.framework.Assert.assertEquals;

public class AddTest {
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
    public void testThrowsInvalidOperationException() throws Exception {
        var sumExpression = new BinaryExpression(left, TokenType.SubToken, right, dummyLineNumber);
        sumExpression.evaluate(env);
    }

    // Write some tests for add
    @Test
    public void testAddInts() throws Exception {
        var sumExpression = new BinaryExpression(left, TokenType.AddToken, right, dummyLineNumber);
        EvalResult sumTwo = sumExpression.evaluate(env);
        assertEquals(sumTwo._value, 9);
    }


    //////////////// Floats ///////////////////////

    @Test
    public void testAddFloats() throws Exception {
        left = new FloatExpression(4.58f, dummyLineNumber);
        right = new FloatExpression(8.45F, dummyLineNumber);
        var sumExpression = new BinaryExpression(left, TokenType.AddToken, right, dummyLineNumber);
        EvalResult sumTwo = sumExpression.evaluate(env);
        assertEquals((float)sumTwo._value, (4.58F + 8.45F), 0.0002f);
    }


    //////////////// Floats and ints together ////////////////
    @Test
    public void testAFloatAndInt() throws Exception {
        left = new NumberExpression(4, dummyLineNumber);
        right = new FloatExpression(8.45F, dummyLineNumber);
        var sumExpression = new BinaryExpression(left, TokenType.AddToken, right, dummyLineNumber);
        EvalResult sumTwo = sumExpression.evaluate(env);
        assertEquals(sumTwo, new EvalResult((4.0f + 8.45F), "float"));
    }


    @Test
    public void testAppendLists() throws Exception {
        List<Expression> leftList = Arrays.asList(
                new NumberExpression(3, dummyLineNumber),
                new StringExpression("value", dummyLineNumber));
        List<Expression> rightList = Arrays.asList(
                new NumberExpression(6, dummyLineNumber),
                new BoolExperssion(true, dummyLineNumber));

        List<EvalResult> result = Arrays.asList(
                new EvalResult(3, "int"),
                new EvalResult("value", "string"),
                new EvalResult(6, "int"),
                new EvalResult(true, "boolean"));
        left = new ListExpression(leftList, dummyLineNumber);
        right = new ListExpression(rightList, dummyLineNumber);

        var sumExpression = new BinaryExpression(left, TokenType.AddToken, right, dummyLineNumber);
        EvalResult sumTwo = sumExpression.evaluate(env);
        List<EvalResult> finalRes = (List) sumTwo.getValue();
        assertEquals(finalRes, result);
    }




    @Test
    public void testAppendEmptyLists() throws Exception {
        List<Expression> leftList = List.of();
        List<Expression> rightList = List.of();
        List<EvalResult> result = List.of();
        left = new ListExpression(leftList, dummyLineNumber);
        right = new ListExpression(rightList, dummyLineNumber);

        var sumExpression = new BinaryExpression(left, TokenType.AddToken, right, dummyLineNumber);
        EvalResult sumTwo = sumExpression.evaluate(env);
        List<EvalResult> finalRes = (List) sumTwo.getValue();
        assertEquals(finalRes, result);
    }

    private List<Expression> generateListExpression() {
        return Arrays.asList(
                new  NumberExpression(3, dummyLineNumber),
                new  StringExpression("value", dummyLineNumber));
    }


    private List<EvalResult> generateListEvalResult() {
        return Arrays.asList(
                new EvalResult(3, "int"),
                new EvalResult("value", "string"),
                new EvalResult(6, "int"),
                new EvalResult(true, "boolean"));
    }

    @Test
    public void testAppendElementToList() throws Exception {
        List<Expression> leftList = Arrays.asList(
                new NumberExpression(3, dummyLineNumber),
                new StringExpression("value", dummyLineNumber));

        List<EvalResult> result = Arrays.asList(
                new EvalResult(3, "int"),
                new EvalResult("value", "string"),
                new EvalResult(8.03f, "float"));

        left = new ListExpression(leftList, dummyLineNumber);
        right = new FloatExpression(8.03f, dummyLineNumber);

        var sumExpression = new BinaryExpression(left, TokenType.AddToken, right, dummyLineNumber);
        EvalResult sumTwo = sumExpression.evaluate(env);
        List<EvalResult> finalRes = (List) sumTwo.getValue();
        assertEquals(finalRes, result);
    }


    @Test
    public void testAddStrings() throws Exception {
        left = new StringExpression("Hello ", dummyLineNumber);
        right = new StringExpression("world", dummyLineNumber);

        var sumExpression = new BinaryExpression(left, TokenType.AddToken, right, dummyLineNumber);
        EvalResult sumTwo = sumExpression.evaluate(env);
        assertEquals(sumTwo.getValue(), "Hello world");

    }


    @Test
    public void testAddStringAndInt() throws Exception {
        left = new StringExpression("Hello ", dummyLineNumber);
        right = new NumberExpression(4, dummyLineNumber);

        var sumExpression = new BinaryExpression(left, TokenType.AddToken, right, dummyLineNumber);
        EvalResult sumTwo = sumExpression.evaluate(env);
        assertEquals(sumTwo.getValue(), "Hello 4");

    }

    @Test
    public void testAddStringAndFloat() throws Exception {
        left = new FloatExpression(4.0f, dummyLineNumber);
        right = new StringExpression("world", dummyLineNumber);

        var sumExpression = new BinaryExpression(left, TokenType.AddToken, right, dummyLineNumber);
        EvalResult sumTwo = sumExpression.evaluate(env);
        assertEquals(sumTwo.getValue(), "4.0world");

    }

    @Test
    public void testAddStringAndList() throws Exception {
        right = new ListExpression(Arrays.asList(new NumberExpression(1, dummyLineNumber), new FloatExpression(4.0f, dummyLineNumber)), dummyLineNumber);
        left = new StringExpression("world", dummyLineNumber);

        var sumExpression = new BinaryExpression(left, TokenType.AddToken, right, dummyLineNumber);
        EvalResult sumTwo = sumExpression.evaluate(env);
        assertEquals("world[1, 4.0]", sumTwo.getValue());

    }

}
    // Minor refactor first



