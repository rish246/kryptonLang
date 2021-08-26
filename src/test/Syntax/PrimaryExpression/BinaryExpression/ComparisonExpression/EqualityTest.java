package test.Syntax.PrimaryExpression.BinaryExpression.ComparisonExpression;

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
import java.util.HashMap;
import java.util.Map;

import static junit.framework.Assert.assertEquals;

public class EqualityTest {
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
    public void testCompareIntsReturnFalse() throws Exception {
        var equalityExpression = new BinaryExpression(left, TokenType.EqualityToken, right, dummyLineNumber);
        EvalResult result = equalityExpression.evaluate(env);
        assertEquals(result.getValue(), false);
    }

    @Test
    public void testCompareIntsReturnTrue() throws Exception {
        left = new NumberExpression(5, dummyLineNumber);
        var equalityExpression = new BinaryExpression(left, TokenType.EqualityToken, right, dummyLineNumber);
        EvalResult result = equalityExpression.evaluate(env);
        assertEquals(result.getValue(), true);
    }

    @Test
    public void testCompareFloatsReturnFalse() throws Exception {
        left = new FloatExpression(4.0f, dummyLineNumber);
        right = new FloatExpression(4.02f, dummyLineNumber);

        var equalityExpression = new BinaryExpression(left, TokenType.EqualityToken, right, dummyLineNumber);
        EvalResult result = equalityExpression.evaluate(env);
        assertEquals(result.getValue(), false);
    }

    @Test
    public void testCompareFloatsReturnTrue() throws Exception {
        left = new FloatExpression(4.0f, dummyLineNumber);
        right = new FloatExpression(4.0f, dummyLineNumber);
        var moreThanExp = new BinaryExpression(left, TokenType.EqualityToken, right, dummyLineNumber);
        EvalResult result = moreThanExp.evaluate(env);
        assertEquals(result.getValue(), true);
    }


    @Test
    public void testCompareBooleansReturnFalse() throws Exception {
        left = new BoolExpression(true, dummyLineNumber);
        right = new BoolExpression(false, dummyLineNumber);

        var equalityExpression = new BinaryExpression(left, TokenType.EqualityToken, right, dummyLineNumber);
        EvalResult result = equalityExpression.evaluate(env);
        assertEquals(result.getValue(), false);
    }

    @Test
    public void testCompareBooleansReturnTrue() throws Exception {
        left = new BoolExpression(true, dummyLineNumber);
        right = new BoolExpression(true, dummyLineNumber);
        var moreThanExp = new BinaryExpression(left, TokenType.EqualityToken, right, dummyLineNumber);
        EvalResult result = moreThanExp.evaluate(env);
        assertEquals(result.getValue(), true);
    }


    @Test
    public void testCompareStringsReturnFalse() throws Exception {
        left = new StringExpression("one", dummyLineNumber);
        right = new StringExpression("false", dummyLineNumber);

        var equalityExpression = new BinaryExpression(left, TokenType.EqualityToken, right, dummyLineNumber);
        EvalResult result = equalityExpression.evaluate(env);
        assertEquals(result.getValue(), false);
    }

    @Test
    public void testCompareStringsReturnTrue() throws Exception {
        left = new StringExpression("true", dummyLineNumber);
        right = new StringExpression("true", dummyLineNumber);
        var moreThanExp = new BinaryExpression(left, TokenType.EqualityToken, right, dummyLineNumber);
        EvalResult result = moreThanExp.evaluate(env);
        assertEquals(result.getValue(), true);
    }

    @Test
    public void testCompareListsReturnFalse() throws Exception {
        left = new ListExpression(Arrays.asList(new NumberExpression(1, dummyLineNumber),
                new StringExpression("1", dummyLineNumber)), dummyLineNumber);
        right = new ListExpression(Arrays.asList(new NumberExpression(1, dummyLineNumber),
                new StringExpression("2", dummyLineNumber)), dummyLineNumber);

        var equalityExpression = new BinaryExpression(left, TokenType.EqualityToken, right, dummyLineNumber);
        EvalResult result = equalityExpression.evaluate(env);
        assertEquals(result.getValue(), false);
    }

    @Test
    public void testCompareListsReturnTrue() throws Exception {
        left = new ListExpression(Arrays.asList(new NumberExpression(1, dummyLineNumber),
                new StringExpression("1", dummyLineNumber)), dummyLineNumber);
        right = new ListExpression(Arrays.asList(new NumberExpression(1, dummyLineNumber),
                new StringExpression("1", dummyLineNumber)), dummyLineNumber);

        var equalityExpression = new BinaryExpression(left, TokenType.EqualityToken, right, dummyLineNumber);
        EvalResult result = equalityExpression.evaluate(env);
        assertEquals(result.getValue(), true);
    }

    @Test
    public void testCompareObjectsReturnFalse() throws Exception {
        Map<Expression, Expression> leftMap = new HashMap<>();
        leftMap.put(new StringExpression("1", dummyLineNumber), new NumberExpression(1, dummyLineNumber)); // create "1" : 1
        leftMap.put(new NumberExpression(1, dummyLineNumber), new NumberExpression(1, dummyLineNumber)); // create 1 : 1

        Map<Expression, Expression> rightMap = new HashMap<>();
        rightMap.put(new NumberExpression(1, dummyLineNumber), new NumberExpression(1, dummyLineNumber)); // create 1 : 1
        rightMap.put(new StringExpression("2", dummyLineNumber), new NumberExpression(1, dummyLineNumber)); // create "1" : 1

        left = new ObjectExpression(leftMap, dummyLineNumber);
        right = new ObjectExpression(rightMap, dummyLineNumber);

        var equalityExpression = new BinaryExpression(left, TokenType.EqualityToken, right, dummyLineNumber);
        EvalResult result = equalityExpression.evaluate(env);
        assertEquals(result.getValue(), false);
    }

    @Test
    public void testCompareObjectsReturnTrue() throws Exception {
        Map<Expression, Expression> leftMap = new HashMap<>();
        leftMap.put(new StringExpression("1", dummyLineNumber), new NumberExpression(1, dummyLineNumber)); // create "1" : 1
        leftMap.put(new NumberExpression(1, dummyLineNumber), new NumberExpression(1, dummyLineNumber)); // create 1 : 1

        Map<Expression, Expression> rightMap = new HashMap<>();
        rightMap.put(new NumberExpression(1, dummyLineNumber), new NumberExpression(1, dummyLineNumber)); // create 1 : 1
        rightMap.put(new StringExpression("1", dummyLineNumber), new NumberExpression(1, dummyLineNumber)); // create "1" : 1



        left = new ObjectExpression(leftMap, dummyLineNumber);
        right = new ObjectExpression(rightMap, dummyLineNumber);

        var equalityExpression = new BinaryExpression(left, TokenType.EqualityToken, right, dummyLineNumber);
        EvalResult result = equalityExpression.evaluate(env);
        assertEquals(result.getValue(), true);
    }

    @Test
    public void testCompareAnythingToNullReturnFalse() throws Exception {
        left = new NullExpression(dummyLineNumber);
        var equalityExpression = new BinaryExpression(left, TokenType.EqualityToken, right, dummyLineNumber);
        EvalResult result = equalityExpression.evaluate(env);
        assertEquals(result.getValue(), false);
    }

    @Test
    public void testCompareNullAndOtherTypeReturnFalse() throws Exception {
        right = new NullExpression(dummyLineNumber);
        var equalityExpression = new BinaryExpression(left, TokenType.EqualityToken, right, dummyLineNumber);
        EvalResult result = equalityExpression.evaluate(env);
        assertEquals(result.getValue(), false);
    }



    @Test
    public void testCompareAnythingToNullReturnTrue() throws Exception {
        left = new NullExpression(dummyLineNumber);
        right = new NullExpression(dummyLineNumber);

        var equalityExpression = new BinaryExpression(left, TokenType.EqualityToken, right, dummyLineNumber);
        EvalResult result = equalityExpression.evaluate(env);
        assertEquals(result.getValue(), true);
    }

    @Test(expected = InvalidOperationException.class)
    public void testCompareDifferentTypesRaisesInvalidOperationException() throws Exception {
        left = new NumberExpression(1, dummyLineNumber);
        right = new StringExpression("hey", dummyLineNumber);
        var equalityExpression = new BinaryExpression(left, TokenType.EqualityToken, right, dummyLineNumber);
        EvalResult result = equalityExpression.evaluate(env);
        assertEquals(result.getValue(), false);
    }
}

