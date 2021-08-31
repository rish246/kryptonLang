package test.Syntax.PrimaryExpression.ArrayAccessExpression;

import com.Krypton.EvalResult;
import com.Krypton.Syntax.Expression;
import com.Krypton.Syntax.PrimaryExpressions.ArrayAccessExpression;
import com.Krypton.Syntax.Values.ListExpression;
import com.Krypton.Syntax.Values.NumberExpression;
import com.Krypton.Syntax.Values.StringExpression;
import com.Krypton.Utilities.CustomExceptions.BinaryOperators.InvalidOperationException;
import com.Krypton.Utilities.CustomExceptions.IndexOutOfBoundException;
import com.Krypton.Utilities.Environment;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class ListAccessExpression {
    protected Expression list;
    protected Expression index;
    protected int dummyLineNumber = 1;
    protected Environment env;

    /*
        list -> [1, 2, "hello", "world"]
        index -> [1]
        // Expression -> list index -> [1, 2, "hello", "world"][1]
     */
    @Before
    public void setUp() {
        list = new ListExpression(Arrays.asList(Int(1), Int(2), Str("hello"), Str("world")),
                dummyLineNumber);
        index = Index(Int(1));
        env = new Environment(null);
    }


    @Test
    public void testGetElementFromListSuccess() throws Exception {
        var listAccessExp = new ArrayAccessExpression(list, index, dummyLineNumber);
        var result = listAccessExp.evaluate(env);
        assertEquals(result, new EvalResult(2, "int"));
    }



    @Test(expected = IndexOutOfBoundException.class)
    public void testGetElementFromListThrowsOutBoundException() throws Exception {
        index = Index(Int(4));
        var listAccessExp = new ArrayAccessExpression(list, index, dummyLineNumber);
        listAccessExp.evaluate(env);
    }


    @Test
    public void testHandleNegativeIndexReturnsValuesFromLast() throws Exception {
        index = Index(Int(-1));
        var listAccessExp = new ArrayAccessExpression(list, index, dummyLineNumber);
        var result = listAccessExp.evaluate(env);
        assertEquals(result, new EvalResult("world", "string"));
    }

    @Test(expected = IndexOutOfBoundException.class)
    public void testHandleNegativeIndexThrowsIndexOutOfBoundException() throws Exception {
        index = Index(Int(-5));
        var listAccessExp = new ArrayAccessExpression(list, index, dummyLineNumber);
        var result = listAccessExp.evaluate(env);
        assertEquals(result, new EvalResult("world", "string"));
    }

    @Test
    public void testHandleMultipleIndicesSuccess() throws Exception {
        index = new ListExpression(Arrays.asList(Int(1), Int(2), Int(3)), dummyLineNumber);
        var listAccessExp = new ArrayAccessExpression(list, index, dummyLineNumber);
        var result = listAccessExp.evaluate(env);
        var expectedResult = new EvalResult(Arrays.asList(IntRes(2), StrRes("hello"), StrRes("world")), "list");
        assertEquals(result, expectedResult);
    }

    @Test(expected = IndexOutOfBoundException.class)
    public void testHandleMultipleIndicesThrowsIndexOutOfBoundException() throws Exception {
        index = new ListExpression(Arrays.asList(Int(1), Int(4), Int(3)), dummyLineNumber);
        var listAccessExp = new ArrayAccessExpression(list, index, dummyLineNumber);
        listAccessExp.evaluate(env);
    }


    @Test(expected = InvalidOperationException.class)
    public void testHandleEmptyIndexInvalidOperationException() throws Exception {
        index = new ListExpression(Arrays.asList(), dummyLineNumber);
        var listAccessExp = new ArrayAccessExpression(list, index, dummyLineNumber);
        listAccessExp.evaluate(env);
    }

    private NumberExpression Int(int value) {
        return new NumberExpression(value, dummyLineNumber);
    }

    private StringExpression Str(String value) {
        return new StringExpression(value, dummyLineNumber);
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
}