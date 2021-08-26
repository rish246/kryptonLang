package test.Syntax.PrimaryExpression.BinaryExpression.ArithematicExpression;

import com.Krypton.EvalResult;
import com.Krypton.Syntax.Expression;
import com.Krypton.Syntax.PrimaryExpressions.BinaryExpression;
import com.Krypton.Syntax.Values.FloatExpression;
import com.Krypton.Syntax.Values.NumberExpression;
import com.Krypton.TokenType;
import com.Krypton.Utilities.CustomExceptions.BinaryOperators.InvalidOperationException;
import com.Krypton.Utilities.Environment;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DivideTest {
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
    public void testDivideInts() throws Exception {
        var sumExpression = new BinaryExpression(left, TokenType.DivToken, right, dummyLineNumber);
        EvalResult result = sumExpression.evaluate(env);
        assertEquals(result.getValue(), (4.0f / 5.0f));
    }


    @Test(expected = InvalidOperationException.class)
    public void testDivideByZeroThrowsInvalidOperationException() throws Exception {
        right = new NumberExpression(0, dummyLineNumber);
        var sumExpression = new BinaryExpression(left, TokenType.DivToken, right, dummyLineNumber);
        sumExpression.evaluate(env);
    }

    @Test
    public void testDivideFloats() throws Exception {
        left = new FloatExpression(4.0f, dummyLineNumber);
        right = new FloatExpression(2.0f, dummyLineNumber);
        var divExpression = new BinaryExpression(left, TokenType.DivToken, right, dummyLineNumber);
        var result = divExpression.evaluate(env);
        assertEquals(result.getValue(), (4.0f / 2.0f));
    }


    @Test(expected = InvalidOperationException.class)
    public void testDivideInvalidTypesThrowsInvalidOperationException() throws Exception {
        right = new NumberExpression(0, dummyLineNumber);
        var sumExpression = new BinaryExpression(left, TokenType.DivToken, right, dummyLineNumber);
        sumExpression.evaluate(env);
    }
}
