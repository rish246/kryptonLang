package test.Syntax.Statements;

import com.Krypton.EvalResult;
import com.Krypton.Syntax.Expression;
import com.Krypton.Syntax.PrimaryExpressions.ArrayAccessExpression;
import com.Krypton.Syntax.Statements.BlockStatement;
import com.Krypton.Syntax.Statements.WhileStatement;
import com.Krypton.SyntaxTree;
import com.Krypton.Utilities.Environment;
import org.junit.Before;
import org.junit.Test;
import test.TestUtil;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class WhileStatementTest {
    protected Expression conditionalBranch;
    protected SyntaxTree body;
    protected int dummyLineNumber = 1;
    protected Environment env;
    protected Expression iterator;
    protected Expression incrementExp;


    @Before
    public void setUp() {
        // iterator: i = 0
        iterator = TestUtil.Assign(TestUtil.Id("i"), TestUtil.Int(0));
        // conditionalBranch = i < 3
        conditionalBranch = TestUtil.LessThan(TestUtil.Id("i"), TestUtil.Int(3));
        // incrementalExp = i = i + 1
        incrementExp = TestUtil.Assign(TestUtil.Id("i"),
                                        TestUtil.Add(TestUtil.Id("i"), TestUtil.Int(1)));
        env = new Environment(null);
    }

    @Test
    public void testWhileLoopSuccess() throws Exception {
        /*
             i = 0
             sum = 0
             while (i < 3) {
                sum = sum + i;
                i = i + 1;
             }
         */
        // i = 0
        iterator.evaluate(env);

        // sum = 0
        env.set("sum", TestUtil.IntRes(0));

        // generate the body
//        {
//            sum = sum + i;
//            i = i + 1;
//        }
        body = new BlockStatement(Arrays.asList(
                TestUtil.Assign(TestUtil.Id("sum"), TestUtil.Add(TestUtil.Id("sum"), TestUtil.Int(1))),
                incrementExp
        ), dummyLineNumber);


        // generate while loop body
        /*
        while (i < 3) body
         */
        var whileExp = new WhileStatement(conditionalBranch, body, dummyLineNumber);

        /*
            evaluate the body
         */
        whileExp.evaluate(env);

        /*
            sum should be equal to 10
         */
        assertEquals(env.get("sum"), new EvalResult(3, "int"));
    }

    @Test(expected = Exception.class)
    public void testWhileLoopErrorInBodyThrowsException() throws Exception {
        /*
             list = [1, 2]
             i = 0
             sum = 0
             while (i < 3) {
                sum = sum + list[i];
                i = i + 1;
             }
         */
        // list = [1, 2]
        env.set("list", TestUtil.ListRes(Arrays.asList(TestUtil.IntRes(1), TestUtil.IntRes(3))));

        // i = 0
        iterator.evaluate(env);

        // sum = 0
        env.set("sum", TestUtil.IntRes(0));

        // generate the body
//        {
//            sum = sum + list[i];
//            i = i + 1;
//        }
        body = new BlockStatement(Arrays.asList(
                TestUtil.Assign(TestUtil.Id("sum"), TestUtil.Add(TestUtil.Id("sum"), List_I())),
                incrementExp
        ), dummyLineNumber);


        // generate while loop body
        /*
        while (i < 3) body
         */
        var whileExp = new WhileStatement(conditionalBranch, body, dummyLineNumber);

        /*
            evaluate the body
         */
        whileExp.evaluate(env);

        /*
            sum should be equal to 10
         */

//        assertEquals(env.get("sum"), new EvalResult(3, "int"));
    }

    @Test(expected = Exception.class)
    public void testWhileLoopErrorInConditionalBranchThrowsException() throws Exception {
        /*
             while (i < 3) { <- error here
                i = i + 1;
             }
         */
        // {
//            i = i + 1;
//        }
        body = new BlockStatement(Arrays.asList(incrementExp), dummyLineNumber);


        // generate while loop body
        /*
        while (i < 3) body
         */
        var whileExp = new WhileStatement(conditionalBranch, body, dummyLineNumber);

        /*
            evaluate the body
         */
        whileExp.evaluate(env);

        /*
            sum should be equal to 10
         */
//        assertEquals(env.get("sum"), new EvalResult(3, "int"));
    }

    @Test
    public void testWhileLoopInvalidStatementReturnsValidErrorMessage() throws Exception {
        /*
             while (i < 3) { <- error here
                i = i + 1;
             }
         */
        // {
//            i = i + 1;
//        }
        body = new BlockStatement(Arrays.asList(incrementExp), dummyLineNumber);


        // generate while loop body
        /*
        while (i < 3) body
         */
        var whileExp = new WhileStatement(conditionalBranch, body, dummyLineNumber);

        /*
            evaluate the body
         */
        try {
            whileExp.evaluate(env);
        } catch (Exception e) {
        } finally {
            assertTrue(whileExp.getDiagnostics().size() > 0);
        }

        /*
            sum should be equal to 10
         */
//        assertEquals(env.get("sum"), new EvalResult(3, "int"));
    }

    private ArrayAccessExpression List_I() {
        /*
            returns list[i]
         */
        return new ArrayAccessExpression(TestUtil.Id("list"), TestUtil.List(Arrays.asList(TestUtil.Id("i"))), dummyLineNumber);
    }
}
