package test.Syntax.PrimaryExpression.BinaryExpression.LogicalExpression;

import com.Krypton.EvalResult;
import com.Krypton.Syntax.Expression;
import com.Krypton.Syntax.PrimaryExpressions.BinaryExpression;
import com.Krypton.Syntax.Values.BoolExperssion;
import com.Krypton.Syntax.Values.FloatExpression;
import com.Krypton.Syntax.Values.NumberExpression;
import com.Krypton.TokenType;
import com.Krypton.Utilities.CustomExceptions.BinaryOperators.InvalidOperationException;
import com.Krypton.Utilities.Environment;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LessThanEqualTest {
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
        var lessThanEqualExp = new BinaryExpression(left, TokenType.LessThanEqualToken, right, dummyLineNumber);
        EvalResult result = lessThanEqualExp.evaluate(env);
        assertEquals(result.getValue(), true);
    }

    @Test
    public void testCompareFloats() throws Exception {
        left = new FloatExpression(4.0f, dummyLineNumber);
        right = new FloatExpression(2.0f, dummyLineNumber);
        var lessThanEqualExp = new BinaryExpression(left, TokenType.LessThanEqualToken, right, dummyLineNumber);
        EvalResult resultFalse = lessThanEqualExp.evaluate(env);
        assertEquals(resultFalse.getValue(), false);

        lessThanEqualExp = new BinaryExpression(right, TokenType.LessThanEqualToken, left, dummyLineNumber);
        EvalResult resultTrue = lessThanEqualExp.evaluate(env);
        assertEquals(resultTrue.getValue(), true);
    }


    @Test(expected = InvalidOperationException.class)
    public void testCompareInvalidTypesThrowsInvalidOperationException() throws Exception {
        right = new BoolExperssion(true, dummyLineNumber);
        var lessThanEqualExp = new BinaryExpression(left, TokenType.LessThanEqualToken, right, dummyLineNumber);
        lessThanEqualExp.evaluate(env);
    }

}