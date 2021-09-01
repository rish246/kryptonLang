package test.Syntax.PrimaryExpression;

import com.Krypton.EvalResult;
import com.Krypton.Syntax.Expression;
import com.Krypton.Syntax.PrimaryExpressions.LengthExpression;
import com.Krypton.Syntax.Values.ObjectExpression;
import com.Krypton.Syntax.Values.StringExpression;
import com.Krypton.Utilities.Environment;
import org.junit.Before;
import org.junit.Test;
import test.TestUtil;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LengthExpressionTest {
    protected Expression body;
    protected int dummyLineNumber = 1;
    protected Environment env;
    /*
        f(a) { return a + 1; }
     */

    @Before
    public void setUp() {
        body = TestUtil.List(Arrays.asList(TestUtil.Int(1), TestUtil.Int(2)));
    }

    @Test
    public void testLengthListSuccess() throws Exception {
        var lenExpression = Length(body);
        var result = lenExpression.evaluate(env);
        assertEquals(new EvalResult(2, "int"), result);
    }

    @Test
    public void testLengthStringSuccess() throws Exception {
        body = new StringExpression("value", dummyLineNumber);
        var lenExpression = Length(body);
        var result = lenExpression.evaluate(env);
        assertEquals(new EvalResult(5, "int"), result);
    }

    @Test
    public void testLengthObjectSuccess() throws Exception {
        Map<Expression, Expression> contents = new HashMap<>();
        contents.put(TestUtil.Int(1), TestUtil.Int(2));
        contents.put(TestUtil.Int(2), TestUtil.Int(2));

        body = new ObjectExpression(contents, dummyLineNumber);
        var lenExpression = Length(body);
        var result = lenExpression.evaluate(env);
        assertEquals(new EvalResult(2, "int"), result);
    }

    @Test(expected = Exception.class)
    public void testLengthInvalidTypeThrowsException() throws Exception {
        body = TestUtil.Int(2);
        var lenExpression = Length(body);
        var result = lenExpression.evaluate(env);
        assertEquals(new EvalResult(2, "int"), result);
    }

    @Test(expected = Exception.class)
    public void testErrorInBodyThrowsException() throws Exception {
        body = TestUtil.Id("NotAvailableId");
        var lenExpression = Length(body);
        var result = lenExpression.evaluate(env);
    }

    @Test
    public void testErrorInBodyReturnsErrors() throws Exception {
        body = TestUtil.Id("NotAvailableId");
        var lenExpression = Length(body);
        try {
            lenExpression.evaluate(env);
        } catch (Exception e) {
        }
        finally {
            assertTrue(lenExpression.getDiagnostics().size() > 0);
        }
    }

    private LengthExpression Length(Expression body) {
        return new LengthExpression(body, dummyLineNumber);
    }
}
