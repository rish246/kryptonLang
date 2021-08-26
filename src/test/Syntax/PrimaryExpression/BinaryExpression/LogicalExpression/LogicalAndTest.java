package test.Syntax.PrimaryExpression.BinaryExpression.LogicalExpression;

import com.Krypton.EvalResult;
import com.Krypton.Syntax.Expression;
import com.Krypton.Syntax.PrimaryExpressions.BinaryExpression;
import com.Krypton.Syntax.Values.BoolExpression;
import com.Krypton.Syntax.Values.NullExpression;
import com.Krypton.Syntax.Values.NumberExpression;
import com.Krypton.TokenType;
import com.Krypton.Utilities.CustomExceptions.BinaryOperators.InvalidOperationException;
import com.Krypton.Utilities.Environment;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LogicalAndTest {
    protected Expression left;
    protected Expression right;
    protected int dummyLineNumber = 1;
    protected Environment env;

    @Before
    public void setUp() {
        left = new BoolExpression(true, dummyLineNumber);
        right = new BoolExpression(true, dummyLineNumber);
        env = new Environment(null);
    }

    @Test
    public void testCompareTrueAndTrueReturnsTrue() throws Exception {
        var logicalAndExpression = new BinaryExpression(left, TokenType.LogicalAndToken, right, dummyLineNumber);
        EvalResult result = logicalAndExpression.evaluate(env);
        assertEquals(result.getValue(), true);
    }



    @Test
    public void testCompareFalseAndTrueReturnFalse() throws Exception {
        left = new BoolExpression(false, dummyLineNumber);
        var logicalAndExpression = new BinaryExpression(left, TokenType.LogicalAndToken, right, dummyLineNumber);
        EvalResult result = logicalAndExpression.evaluate(env);
        assertEquals(result.getValue(), false);
    }


    @Test
    public void testCompareTrueAndFalseReturnFalse() throws Exception {
        right = new BoolExpression(false, dummyLineNumber);
        var logicalAndExpression = new BinaryExpression(left, TokenType.LogicalAndToken, right, dummyLineNumber);
        EvalResult result = logicalAndExpression.evaluate(env);
        assertEquals(result.getValue(), false);
    }

    @Test
    public void testCompareFalseAndFalseReturnFalse() throws Exception {
        left = new BoolExpression(false, dummyLineNumber);
        right = new BoolExpression(false, dummyLineNumber);

        var logicalAndExpression = new BinaryExpression(left, TokenType.LogicalAndToken, right, dummyLineNumber);
        EvalResult result = logicalAndExpression.evaluate(env);
        assertEquals(result.getValue(), false);
    }

    @Test(expected = InvalidOperationException.class)
    public void testCompareInvalidTypesThrowsInvalidOperationException() throws Exception {
        left = new NumberExpression(1, dummyLineNumber);
        var logicalAndExpression = new BinaryExpression(left, TokenType.LogicalAndToken, right, dummyLineNumber);
        EvalResult result = logicalAndExpression.evaluate(env);
        System.out.println(result.getValue());
    }


    @Test(expected = InvalidOperationException.class)
    public void testCompareNumberToFalseThrowsInvalidOperationException() throws Exception {
        right = new BoolExpression(false, dummyLineNumber);
        left = new NumberExpression(1, dummyLineNumber);
        var logicalAndExpression = new BinaryExpression(left, TokenType.LogicalAndToken, right, dummyLineNumber);
        EvalResult result = logicalAndExpression.evaluate(env);
        System.out.println(result.getValue());
    }

    @Test(expected = InvalidOperationException.class)
    public void testCompareWithNullThrowsInvalidOperationException() throws Exception {
        left = new NullExpression(dummyLineNumber);
        var logicalAndExpression = new BinaryExpression(left, TokenType.LogicalAndToken, right, dummyLineNumber);
        EvalResult result = logicalAndExpression.evaluate(env);
        System.out.println(result.getValue());
    }
}
