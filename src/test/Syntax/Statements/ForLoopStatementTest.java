package test.Syntax.Statements;

import com.Krypton.EvalResult;
import com.Krypton.Syntax.Expression;
import com.Krypton.Syntax.PrimaryExpressions.ArrayAccessExpression;
import com.Krypton.Syntax.Statements.BlockStatement;
import com.Krypton.Syntax.Statements.ForStatement;
import com.Krypton.SyntaxTree;
import com.Krypton.Utilities.Environment;
import com.Krypton.Utilities.Printer;
import org.junit.Before;
import org.junit.Test;
import test.TestUtil;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static test.TestUtil.*;

public class ForLoopStatementTest {
    protected Expression initExpression;
    protected Expression validityExpression;
    protected Expression progressExpression;

    protected Expression sumInit;
    protected Expression sumCompute;

    protected SyntaxTree body;
    protected int dummyLineNumber = 1;
    protected Environment env;


    @Before
    public void setUp() {
        // sum = 0;
        // for(i = 0; i < 3; i = i + 1)
        // {
        //     sum = sum + i;
        //}

        // initExp: i = 0
        initExpression = Assign(Id("i"), Int(0));

        // validdityExp: i < 3
        validityExpression = LessThan(Id("i"), Int(3));

        // progressExp: i = i + 1
        progressExpression = Assign(Id("i"), Add(Id("i"), Int(1)));

        // sumInit: sum = 0
        sumInit = Assign(Id("sum"), Int(0));

        // sumCompute: sum = sum + i
        sumCompute = Assign(Id("sum"), Add(Id("sum"),Id("i")));

        // body = { sum = sum + i; }
        body = new BlockStatement(Arrays.asList(sumCompute), dummyLineNumber);

        env = new Environment(null);
    }

    @Test
    public void testForLoopSuccess() throws Exception {
        // sum = 0;
        // for(i = 0; i < 3; i = i + 1)
        // {
        //     sum = sum + i;
        //}
        sumInit.evaluate(env);
        var forLoop = new ForStatement(initExpression, validityExpression, progressExpression, body, dummyLineNumber);
        forLoop.evaluate(env);
        assertEquals(env.get("sum"), new EvalResult(3, "int"));
    }

    @Test(expected = Exception.class)
    public void testForLoopErrorInBodyThrowsException() throws Exception {
        // for(i = 0; i < 3; i = i + 1)
        // {
        //     sum = sum + i;
        //}
//        sumInit.evaluate(env);
        var forLoop = new ForStatement(initExpression, validityExpression, progressExpression, body, dummyLineNumber);
        forLoop.evaluate(env);
        assertEquals(env.get("sum"), new EvalResult(3, "int"));
    }

    @Test(expected = Exception.class)
    public void testForLoopErrorInProgressExpThrowsException() throws Exception {
        // sum = 0;
        // for(i = 0; i < 3; i = j + 1) <- error
        // {
        //     sum = sum + i;
        //}
        sumInit.evaluate(env);
        progressExpression = Assign(Id("i"), Add(Id("j"), Int(1)));
        var forLoop = new ForStatement(initExpression, validityExpression, progressExpression, body, dummyLineNumber);
        forLoop.evaluate(env);
    }

    @Test(expected = Exception.class)
    public void testForLoopInvalidTypeOfHaltingCondition() throws Exception {
        // sum = 0;
        // for(i = 0; i + 3; i = j + 1) <- error
        // {
        //     sum = sum + i;
        //}
        sumInit.evaluate(env);
        validityExpression = Add(Id("i"), Int(3));
        var forLoop = new ForStatement(initExpression, validityExpression, progressExpression, body, dummyLineNumber);
        forLoop.evaluate(env);
        assertEquals(env.get("sum"), new EvalResult(3, "int"));
    }

    @Test(expected = Exception.class)
    public void testForLoopInInitExpressions() throws Exception { // expected
        // sum = 0;
        // for(j = 0; i < 3; i = j + 1) <- error
        // {
        //     sum = sum + i;
        //}
        sumInit.evaluate(env);
        initExpression = Assign(Id("j"), Int(0));
        var forLoop = new ForStatement(initExpression, validityExpression, progressExpression, body, dummyLineNumber);
        forLoop.evaluate(env);
        assertEquals(env.get("sum"), new EvalResult(3, "int"));
    }

    @Test
    public void testForLoopErrorInBodyReturnsAGoodErrorMessage() throws Exception { // expected
        // list = [1]
        // sum = 0
        // for(i = 0; i < 3; i = i + 1)
        // {
        //     sum = sum + list[i]; <- error
        //}

        // sum = 0
        sumInit.evaluate(env);

        // list = [1]
        var list = ListRes(Arrays.asList(IntRes(1)));
        env.set("list", list);
        sumCompute = Assign(Id("sum"), Add(Id("sum"), List_I()));

        // body = { sum = sum + i; }
        body = new BlockStatement(Arrays.asList(sumCompute), dummyLineNumber);

        // change the body
        var forLoop = new ForStatement(initExpression, validityExpression, progressExpression, body, dummyLineNumber);
        try {
            forLoop.evaluate(env);
        } catch (Exception e) {
        } finally {
            assertTrue(forLoop.getDiagnostics().size() > 0);
        }
    }

    @Test
    public void testForLoopErrorInExpressionReturnsAGoodErrorMessage() throws Exception { // expected
        // sum = 0;
        // for(j = 0; i < 3; i = j + 1) <- error
        // {
        //     sum = sum + i;
        //}
        sumInit.evaluate(env);
        sumCompute = Assign(Id("sum"), Add(Id("sum"), List_I()));

        // body = { sum = sum + list[i]; }
        body = new BlockStatement(Arrays.asList(sumCompute), dummyLineNumber);

        var forLoop = new ForStatement(initExpression, validityExpression, progressExpression, body, dummyLineNumber);
        try {
            forLoop.evaluate(env);
        } catch (Exception e) {
        } finally {
            Printer.printDiagnostics(forLoop.getDiagnostics());
            assertTrue(forLoop.getDiagnostics().size() > 0);
        }
    }


    private ArrayAccessExpression List_I() {
        /*
            returns list[i]
         */
        return new ArrayAccessExpression(TestUtil.Id("list"), TestUtil.List(Arrays.asList(TestUtil.Id("i"))), dummyLineNumber);
    }
}
