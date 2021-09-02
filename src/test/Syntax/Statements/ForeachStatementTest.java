package test.Syntax.Statements;

import com.Krypton.Syntax.Expression;
import com.Krypton.Syntax.Statements.BlockStatement;
import com.Krypton.Syntax.Statements.ForEachStatement;
import com.Krypton.Syntax.Values.ObjectExpression;
import com.Krypton.SyntaxTree;
import com.Krypton.Utilities.Environment;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static test.TestUtil.*;

public class ForeachStatementTest {
    protected Expression iterator;
    protected Expression iterable;

    protected Expression sumInit;
    protected Expression sumCompute;

    protected SyntaxTree body;
    protected int dummyLineNumber = 1;
    protected Environment env;


    @Before
    public void setUp() {
        // sum = 0;
        // for(i in [1, 2, 3])
        // {
        //     sum = sum + i;
        //}

        // initExp: i = 0
        iterator = Id("i");

        // validdityExp: i < 3
        iterable = List(Arrays.asList(Int(1), Int(2), Int(3)));

        // sumInit: sum = 0
        sumInit = Assign(Id("sum"), Int(0));

        // sumCompute: sum = sum + i
        sumCompute = Assign(Id("sum"), Add(Id("sum"),Id("i")));

        // body = { sum = sum + i; }
        body = new BlockStatement(Arrays.asList(sumCompute), dummyLineNumber);

        env = new Environment(null);
    }

    @Test
    public void testForeachLoopSuccess() throws Exception {
        sumInit.evaluate(env);
        var foreachLoop = new ForEachStatement(iterator, iterable, body, dummyLineNumber);
        foreachLoop.evaluate(env);
        assertEquals(env.get("sum"), IntRes(6));
    }

    @Test(expected = Exception.class)
    public void testForeachLoopErrorInIterableExpThrowsException() throws Exception {
        // sum = 0;
        // for(i in vals) <- error
        // {
        //     sum = sum + i;
        //}
        sumInit.evaluate(env);
        iterable = Id("vals");
        var foreachLoop = new ForEachStatement(iterator, iterable, body, dummyLineNumber);
        foreachLoop.evaluate(env);
        assertEquals(env.get("sum"), IntRes(6));
    }

    @Test(expected = Exception.class)
    public void testForeachLoopErrorInBodyThrowsException() throws Exception {
        // sum = 0;
        // for(i in vals) <- error
        // {
        //     sum = sum + i;
        //}
        sumInit.evaluate(env);
        sumCompute = Assign(Id("sum"), Add(Id("sum"),Id("j")));

        // body = { sum = sum + i; }
        body = new BlockStatement(Arrays.asList(sumCompute), dummyLineNumber);
        var foreachLoop = new ForEachStatement(iterator, iterable, body, dummyLineNumber);
        foreachLoop.evaluate(env);
        assertEquals(env.get("sum"), IntRes(6));
    }

    @Test
    public void testForeachLoopErrorInBodyReturnsValidErrorMessage() throws Exception {
        // sum = 0;
        // for(i in vals)
        // {
        //     sum = sum + j;
        //}
        sumInit.evaluate(env);
        sumCompute = Assign(Id("sum"), Add(Id("sum"),Id("j")));

        // body = { sum = sum + i; }
        body = new BlockStatement(Arrays.asList(sumCompute), dummyLineNumber);
        var foreachLoop = new ForEachStatement(iterator, iterable, body, dummyLineNumber);
        try {
            foreachLoop.evaluate(env);
        } catch (Exception e) {
            assertTrue(foreachLoop.getDiagnostics().size() > 0);
        }
    }



    /// * test for objects * ////
    @Test
    public void testForeachLoopObjectSuccess() throws Exception {
        /*
            d = {1 : 1, "w" : 'h"}
            sum = "";
            for( k in d ) {
                sum = sum + k;
            }
         */
        iterator = Id("k");

        //  d = {1 : 1, "w" : 'h"}
        iterable = ObjectExp();

        // sumInit: sum = ""
        sumInit = Assign(Id("sum"), Str(""));

        // sumCompute: sum = sum + k;
        sumCompute = Assign(Id("sum"), Add(Id("sum"),Id("k")));

        // body = { sum = sum + k; }
        body = new BlockStatement(Arrays.asList(sumCompute), dummyLineNumber);

        sumInit.evaluate(env);
        var foreachLoop = new ForEachStatement(iterator, iterable, body, dummyLineNumber);
        foreachLoop.evaluate(env);
        assertEquals(env.get("sum"), StrRes("1w"));
    }

    @Test(expected = Exception.class)
    public void testForeachLoopObjectErrorInBindingExpThrowsException() throws Exception {
        /*
            d = {1 : 1, "w" : h} <- error
            sum = "";
            for( k in d ) {
                sum = sum + k;
            }
         */
        iterator = Id("k");

        //  d = {1 : 1, "w" : h}
        iterable = ObjectExpWrong();
        // sumInit: sum = ""
        sumInit = Assign(Id("sum"), Str(""));

        // sumCompute: sum = sum + k;
        sumCompute = Assign(Id("sum"), Add(Id("sum"),Id("k")));

        // body = { sum = sum + k; }
        body = new BlockStatement(Arrays.asList(sumCompute), dummyLineNumber);

        sumInit.evaluate(env);
        var foreachLoop = new ForEachStatement(iterator, iterable, body, dummyLineNumber);
        foreachLoop.evaluate(env);
        assertEquals(env.get("sum"), StrRes("1w"));
    }

    @Test
    public void testForeachLoopObjectErrorInBindingReturnsErrorMessage() throws Exception {
        /*
            d = {1 : 1, "w" : h} <- error
            sum = "";
            for( k in d ) {
                sum = sum + k;
            }
         */
        iterator = Id("k");

        //  d = {1 : 1, "w" : h}
        iterable = ObjectExpWrong();
        // sumInit: sum = ""
        sumInit = Assign(Id("sum"), Str(""));

        // sumCompute: sum = sum + k;
        sumCompute = Assign(Id("sum"), Add(Id("sum"),Id("k")));

        // body = { sum = sum + k; }
        body = new BlockStatement(Arrays.asList(sumCompute), dummyLineNumber);

        sumInit.evaluate(env);
        var foreachLoop = new ForEachStatement(iterator, iterable, body, dummyLineNumber);
        try {
            foreachLoop.evaluate(env);
        } catch (Exception e) {
            assertTrue(foreachLoop.getDiagnostics().size() > 0);
        }
    }

    @Test(expected = Exception.class)
    public void testForeachLoopObjectErrorInBodyExpThrowsException() throws Exception {
        /*
            d = {1 : 1, "w" : "h"}
            for( k in d ) {
                sum = sum + k; <- error
            }
         */
        env = new Environment(null);
        iterator = Id("k");

        //  d = {1 : 1, "w" : "h"}
        iterable = ObjectExp();

        // sumInit: sum = ""
        sumInit = Assign(Id("sum"), Int(0));

        // sumCompute: sum = sum + k;
        sumCompute = Assign(Id("sum"), Add(Id("sum"),Id("t")));

        // body = { sum = sum + k; }
        body = new BlockStatement(List.of(sumCompute), dummyLineNumber);

        sumInit.evaluate(env);
        var foreachLoop = new ForEachStatement(iterator, iterable, body, dummyLineNumber);
        foreachLoop.evaluate(env);
        assertEquals(env.get("sum"), StrRes("1w"));
    }

    @Test
    public void testForeachLoopObjectErrorInBodyExpReturnsErrorMessage() throws Exception {
        /*
            d = {1 : 1, "w" : "h"}
            for( k in d ) {
                sum = sum + k; <- error
            }
         */
        env = new Environment(null);
        iterator = Id("k");

        //  d = {1 : 1, "w" : "h"}
        iterable = ObjectExp();

        // sumInit: sum = ""
        sumInit = Assign(Id("sum"), Int(0));

        // sumCompute: sum = sum + k;
        sumCompute = Assign(Id("sum"), Add(Id("sum"),Id("t")));

        // body = { sum = sum + k; }
        body = new BlockStatement(Arrays.asList(sumCompute), dummyLineNumber);

        sumInit.evaluate(env);
        var foreachLoop = new ForEachStatement(iterator, iterable, body, dummyLineNumber);
        try {
            foreachLoop.evaluate(env);
        } catch (Exception e) {
            assertTrue(foreachLoop.getDiagnostics().size() > 0);
        }
    }

    private ObjectExpression ObjectExpWrong() {
        Map<Expression, Expression> result = new HashMap<>();
        /*
            leftObject = {1 : 1, "w" : "h"};
         */
        result.put(Int(1), Int(1));
        result.put(Str("w"), Id("h"));
        return new ObjectExpression(result, dummyLineNumber);
    }


    private ObjectExpression ObjectExp() {
        Map<Expression, Expression> result = new HashMap<>();
        /*
            leftObject = {1 : 1, "w" : "h"};
         */
        result.put(Int(1), Int(1));
        result.put(Str("w"), Str("h"));
        return new ObjectExpression(result, dummyLineNumber);
    }
}
