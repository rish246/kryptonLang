package test.Syntax.PrimaryExpression.BinaryExpression.ArithematicExpression;

import com.Krypton.EvalResult;
import com.Krypton.Syntax.Expression;
import com.Krypton.Syntax.PrimaryExpressions.BinaryExpression;
import com.Krypton.Syntax.Values.FloatExpression;
import com.Krypton.Syntax.Values.NumberExpression;
import com.Krypton.Syntax.Values.StringExpression;
import com.Krypton.TokenType;
import com.Krypton.Utilities.CustomExceptions.BinaryOperators.InvalidOperationException;
import com.Krypton.Utilities.Environment;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import test.TestUtil;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SubtractTest {
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
    public void testSubtractInts() throws Exception {
        var sumExpression = new BinaryExpression(left, TokenType.SubToken, right, dummyLineNumber);
        EvalResult sumTwo = sumExpression.evaluate(env);
        assertEquals(sumTwo.getValue(), -1);
    }

    @Test
    public void testSubtractFloats() throws Exception {
        left = new FloatExpression(4.0f, dummyLineNumber);
        right = new FloatExpression(2.0f, dummyLineNumber);
        var sumExpression = new BinaryExpression(left, TokenType.SubToken, right, dummyLineNumber);
        EvalResult sumTwo = sumExpression.evaluate(env);
        assertEquals(sumTwo.getValue(), 2.0f);
    }

    @Test
    public void testSubtractFloatAndInt() throws Exception {
        left = new NumberExpression(4, dummyLineNumber);
        right = new FloatExpression(8.45F, dummyLineNumber);
        var sumExpression = new BinaryExpression(left, TokenType.SubToken, right, dummyLineNumber);
        EvalResult sumTwo = sumExpression.evaluate(env);
        Assert.assertEquals(sumTwo, new EvalResult((4.0f - 8.45F), "float"));
    }


    @Test(expected = InvalidOperationException.class)
    public void testSubtractInvalidTypesThrowsInvalidOperationException() throws Exception {
        left = new StringExpression("Hello", dummyLineNumber);
        right = new FloatExpression(4.0f, dummyLineNumber);
        var expression = new BinaryExpression(left, TokenType.SubToken, right, dummyLineNumber);
        expression.evaluate(env);
    }

    @Test
    public void testAddInvalidExpressionsReturnsError() throws Exception {
        left = TestUtil.Id("noParam");
        right = TestUtil.Int(1);

        var sumExpression = TestUtil.Subtract(left, right);
        try {
            sumExpression.evaluate(env);
        } catch (Exception e) {
            assertTrue(sumExpression.getDiagnostics().size() > 0);
        }
    }
}
