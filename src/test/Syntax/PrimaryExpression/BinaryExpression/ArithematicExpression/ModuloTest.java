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

public class ModuloTest {
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
    public void testModuloInts() throws Exception {
        var sumExpression = new BinaryExpression(left, TokenType.ModuloToken, right, dummyLineNumber);
        EvalResult result = sumExpression.evaluate(env);
        assertEquals(result.getValue(), (4 % 5));
    }

    @Test(expected = InvalidOperationException.class)
    public void testModuloByZeroThrowsInvalidOperationException() throws Exception {
        right = new NumberExpression(0, dummyLineNumber);
        var moduloExpression = new BinaryExpression(left, TokenType.ModuloToken, right, dummyLineNumber);
        moduloExpression.evaluate(env);
    }

    @Test(expected = InvalidOperationException.class)
    public void testModuloInvalidTypes() throws Exception {
        right = new FloatExpression(0.3f, dummyLineNumber);
        var moduloExpression = new BinaryExpression(left, TokenType.ModuloToken, right, dummyLineNumber);
        moduloExpression.evaluate(env);
    }
}
