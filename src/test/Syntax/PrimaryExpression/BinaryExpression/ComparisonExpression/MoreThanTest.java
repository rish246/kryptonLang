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
import test.TestUtil;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MoreThanTest {
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
        var moreThanExp = new BinaryExpression(left, TokenType.GreaterThanToken, right, dummyLineNumber);
        EvalResult result = moreThanExp.evaluate(env);
        assertEquals(result.getValue(), false);
    }
//
//
    @Test
    public void testCompareFloats() throws Exception {
        left = new FloatExpression(4.0f, dummyLineNumber);
        right = new FloatExpression(2.0f, dummyLineNumber);
        var moreThanExp = new BinaryExpression(left, TokenType.GreaterThanToken, right, dummyLineNumber);
        EvalResult resultFalse = moreThanExp.evaluate(env);
        assertEquals(resultFalse.getValue(), true);

        moreThanExp = new BinaryExpression(right, TokenType.GreaterThanToken, left, dummyLineNumber);
        EvalResult resultTrue = moreThanExp.evaluate(env);
        assertEquals(resultTrue.getValue(), false);
    }


    @Test(expected = InvalidOperationException.class)
    public void testCompareInvalidTypesThrowsInvalidOperationException() throws Exception {
        right = new BoolExpression(true, dummyLineNumber);
        var lessThanExpression = new BinaryExpression(left, TokenType.LessThanToken, right, dummyLineNumber);
        lessThanExpression.evaluate(env);
    }

    @Test
    public void testCompareInvalidExpressionsReturnsError() throws Exception {
        left = TestUtil.Id("noParam");
        right = TestUtil.Int(1);

        var sumExpression = TestUtil.GreaterThan(left, right);
        try {
            sumExpression.evaluate(env);
        } catch (Exception e) {
            assertTrue(sumExpression.getDiagnostics().size() > 0);
        }
    }

}
