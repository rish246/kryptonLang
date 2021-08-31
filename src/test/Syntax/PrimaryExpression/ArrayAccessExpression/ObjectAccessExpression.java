package test.Syntax.PrimaryExpression.ArrayAccessExpression;

import com.Krypton.EvalResult;
import com.Krypton.Syntax.Expression;
import com.Krypton.Syntax.PrimaryExpressions.ArrayAccessExpression;
import com.Krypton.Syntax.Values.*;
import com.Krypton.Utilities.CustomExceptions.BinaryOperators.InvalidOperationException;
import com.Krypton.Utilities.Environment;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class ObjectAccessExpression {
    protected Expression object;
    protected Expression index;
    protected int dummyLineNumber = 1;
    protected Environment env;

    /*
        object-> {"hello" : "world", 1 : "tweet"}
        index -> [1]
        // Expression -> object[1]
     */
    @Before
    public void setUp() {
        Map<Expression, Expression> objectMap = new HashMap<>();
        objectMap.put(Str("hello"), Str("world"));
        objectMap.put(Int(1), Str("tweet"));
        object = new ObjectExpression(objectMap, dummyLineNumber);
        index = Index(Int(1));
        env = new Environment(null);
    }


    @Test
    public void testGetElementFromObjectSuccess() throws Exception {
        /*
            >>> object[1]
                "tweet"
         */
        var listAccessExp = new ArrayAccessExpression(object, index, dummyLineNumber);
        var result = listAccessExp.evaluate(env);
        assertEquals(result, new EvalResult("tweet", "string"));
    }
//
//
//
    @Test
    public void testGetInvalidKeyReturnsNull() throws Exception {
        /*
            object[4] -> null
         */
        index = Index(Int(4));
        var listAccessExp = new ArrayAccessExpression(object, index, dummyLineNumber);
        EvalResult result = listAccessExp.evaluate(env);
        assertEquals(result, NullRes()); // This is giving npe
    }
//
    @Test
    public void testHandleMultipleIndicesSuccess() throws Exception {
        /*
            object[1, "hello"] = ["tweet", "world"]
         */
        index = new ListExpression(Arrays.asList(Int(1), Str("hello")), dummyLineNumber);
        var listAccessExp = new ArrayAccessExpression(object, index, dummyLineNumber);
        var result = listAccessExp.evaluate(env);
        var expectedResult = new EvalResult(Arrays.asList(StrRes("tweet"), StrRes("world")), "list");
        assertEquals(result, expectedResult);
    }

    @Test(expected = InvalidOperationException.class)
    public void testHandleEmptyIndexInvalidOperationException() throws Exception {
        index = new ListExpression(Arrays.asList(), dummyLineNumber);
        var listAccessExp = new ArrayAccessExpression(object, index, dummyLineNumber);
        listAccessExp.evaluate(env);
    }

    @Test(expected = InvalidOperationException.class)
    public void testHandleInvalidKeyTypeThrowsInvalidOperationException() throws Exception {
        index = new ListExpression(Arrays.asList(Null()), dummyLineNumber);
        var listAccessExp = new ArrayAccessExpression(object, index, dummyLineNumber);
        listAccessExp.evaluate(env);
    }

    private NumberExpression Int(int value) {
        return new NumberExpression(value, dummyLineNumber);
    }

    private StringExpression Str(String value) {
        return new StringExpression(value, dummyLineNumber);
    }

    private NullExpression Null() {
        return new NullExpression(dummyLineNumber);
    }

    private ListExpression Index(Expression value) {
        return new ListExpression(Arrays.asList(value), dummyLineNumber);
    }

    private EvalResult IntRes(int value) {
        return new EvalResult(value, "int");
    }

    private EvalResult StrRes(String value) {
        return new EvalResult(value, "string");
    }

    private EvalResult NullRes() {
        return new EvalResult(null, "null");
    }
}
