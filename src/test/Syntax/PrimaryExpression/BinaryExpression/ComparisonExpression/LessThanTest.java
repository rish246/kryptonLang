package test.Syntax.PrimaryExpression.BinaryExpression.ComparisonExpression;

import com.Krypton.EvalResult;
import com.Krypton.Syntax.Expression;
import com.Krypton.Syntax.PrimaryExpressions.BinaryExpression;
import com.Krypton.Syntax.Values.BoolExpression;
import com.Krypton.Syntax.Values.FloatExpression;
import com.Krypton.Syntax.Values.NumberExpression;
import com.Krypton.TokenType;
import com.Krypton.Utilities.CustomExceptions.BinaryOperators.InvalidOperationException;
import com.Krypton.Utilities.Environment;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LessThanTest {
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

    @Test
    public void testCompareInts() throws Exception {
        var lessThanExpression = new BinaryExpression(left, TokenType.LessThanToken, right, dummyLineNumber);
        EvalResult result = lessThanExpression.evaluate(env);
        assertEquals(result.getValue(), true);
    }


    @Test
    public void testCompareFloats() throws Exception {
        left = new FloatExpression(4.0f, dummyLineNumber);
        right = new FloatExpression(2.0f, dummyLineNumber);
        var lessThanExpression = new BinaryExpression(left, TokenType.LessThanToken, right, dummyLineNumber);
        EvalResult resultFalse = lessThanExpression.evaluate(env);
        assertEquals(resultFalse.getValue(), false);

        lessThanExpression = new BinaryExpression(right, TokenType.LessThanToken, left, dummyLineNumber);
        EvalResult resultTrue = lessThanExpression.evaluate(env);
        assertEquals(resultTrue.getValue(), true);
    }


    @Test(expected = InvalidOperationException.class)
    public void testCompareInvalidTypesThrowsInvalidOperationException() throws Exception {
        right = new BoolExpression(true, dummyLineNumber);
        var lessThanExpression = new BinaryExpression(left, TokenType.LessThanToken, right, dummyLineNumber);
        lessThanExpression.evaluate(env);
    }

    @Test
    public void testCompareInvalidTypesGivesRightErrorMessage() throws Exception {
        right = new BoolExpression(true, dummyLineNumber);
        var lessThanExpression = new BinaryExpression(left, TokenType.LessThanToken, right, dummyLineNumber);
        String expectedMessage = "Invalid operator 'LessThanToken' for type int and boolean at line number " + dummyLineNumber;
        try {
            lessThanExpression.evaluate(env);
        } catch (InvalidOperationException e) {
            assertEquals(e.getMessage(), expectedMessage);
        }
    }
}
