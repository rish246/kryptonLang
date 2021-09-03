package test.Syntax.PrimaryExpression.ArrayAccessExpression;

import com.Krypton.EvalResult;
import com.Krypton.Syntax.Expression;
import com.Krypton.Syntax.PrimaryExpressions.ArrayAccessExpression;
import com.Krypton.Syntax.Values.ListExpression;
import com.Krypton.Syntax.Values.NullExpression;
import com.Krypton.Syntax.Values.NumberExpression;
import com.Krypton.Syntax.Values.StringExpression;
import com.Krypton.Utilities.CustomExceptions.IndexOutOfBoundException;
import com.Krypton.Utilities.Environment;
import com.Krypton.Utilities.Printer;
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
        index = Int(1);
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
        index = Int(4);
        var listAccessExp = new ArrayAccessExpression(list, index, dummyLineNumber);
        listAccessExp.evaluate(env);
    }


    @Test
    public void testHandleNegativeIndexReturnsValuesFromLast() throws Exception {
        index = Int(-1);
        var listAccessExp = new ArrayAccessExpression(list, index, dummyLineNumber);
        var result = listAccessExp.evaluate(env);
        assertEquals(result, new EvalResult("world", "string"));
    }

    @Test(expected = IndexOutOfBoundException.class)
    public void testHandleNegativeIndexThrowsIndexOutOfBoundException() throws Exception {
        index = Int(-5);
        var listAccessExp = new ArrayAccessExpression(list, index, dummyLineNumber);
        var result = listAccessExp.evaluate(env);
        assertEquals(result, new EvalResult("world", "string"));
    }


    @Test(expected = Exception.class)
    public void testHandleNullIndexInvalidOperationException() throws Exception {
        index = new NullExpression(dummyLineNumber);
        var listAccessExp = new ArrayAccessExpression(list, index, dummyLineNumber);
        try {
            listAccessExp.evaluate(env);
        } catch (Exception e) {
            Printer.printDiagnostics(listAccessExp.getDiagnostics());
            throw e;
        }
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