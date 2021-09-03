package test.Syntax.Statements;

import com.Krypton.EvalResult;
import com.Krypton.Syntax.Expression;
import com.Krypton.Syntax.Statements.BlockStatement;
import com.Krypton.Syntax.Statements.IfStatement;
import com.Krypton.SyntaxTree;
import com.Krypton.Utilities.Environment;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static test.TestUtil.*;

public class IfStatementTest {
    protected Expression conditionalExpression;
    protected Expression thenBranchExp;
    protected Expression elseBranchExp;

    protected Expression init_I;
    protected Expression update_I;

    protected SyntaxTree thenBranch;
    protected SyntaxTree elseBranch;

    protected int dummyLineNumber = 1;
    protected Environment env;


    @Before
    public void setUp() {
        /*
            i = 4;
            if (i < 3) {
                i = -1;
            } else {
                i = 0;
            }
         */

        // i < 3
        conditionalExpression = LessThan(Id("i"), Int(3));

        // i = -1
        thenBranchExp = Assign(Id("i"), Int(-1));

        thenBranch = new BlockStatement(List.of(thenBranchExp), dummyLineNumber);

        //i = 0;
        elseBranchExp = Assign(Id("i"), Int(0));

        elseBranch = new BlockStatement(List.of(elseBranchExp), dummyLineNumber);

        // i = 4;
        init_I = Assign(Id("i"), Int(4));

        env = new Environment(null);
    }

    @Test
    public void testIfElseExpSuccess() throws Exception {
        init_I.evaluate(env);
        var ifElseStatement = new IfStatement(conditionalExpression, thenBranch, elseBranch, dummyLineNumber);
        ifElseStatement.evaluate(env);
        assertEquals(env.get("i"), new EvalResult(0, "int"));
    }

    @Test(expected = Exception.class)
    public void testIfElseExpErrorInConditionalExpThrowsException() throws Exception {
        /*
            i = 4;
            if (j < 3) {
                i = -1;
            }
         */
        init_I.evaluate(env);
        conditionalExpression = LessThan(Id("j"), Int(3));
        var ifElseStatement = new IfStatement(conditionalExpression, thenBranch, elseBranch, dummyLineNumber);
        ifElseStatement.evaluate(env);
    }

    @Test
    public void testIfElseExpErrorInConditionalExpGivesErrorMessage() throws Exception {
        /*
            i = 4;
            if (j < 3) { <- error
                i = -1;
            }
         */
        init_I.evaluate(env);
        conditionalExpression = LessThan(Id("j"), Int(3));
        var ifElseStatement = new IfStatement(conditionalExpression, thenBranch, elseBranch, dummyLineNumber);

        try {
            ifElseStatement.evaluate(env);
        } catch (Exception e) {
            assertTrue(ifElseStatement.getDiagnostics().size() > 0);
        }
    }

    @Test(expected = Exception.class)
    public void testIfElseExpErrorInThenBranchThrowsException() throws Exception {
        /*
            i = 2;
            if (i < 3) {
                i = st;
            }
         */
        init_I = Assign(Id("i"), Int(2));
        init_I.evaluate(env);
        thenBranchExp = Assign(Id("i"), Id("st"));
        thenBranch = new BlockStatement(List.of(thenBranchExp), dummyLineNumber);
        var ifElseStatement = new IfStatement(conditionalExpression, thenBranch, elseBranch, dummyLineNumber);
        ifElseStatement.evaluate(env);
    }

    @Test
    public void testIfElseExpErrorInThenBranchReturnsErrorMessage() throws Exception {
        /*
            i = 2;
            if (i < 3) {
                i = st;
            }
         */
        init_I = Assign(Id("i"), Int(2));
        init_I.evaluate(env);
        thenBranchExp = Assign(Id("i"), Id("st"));
        thenBranch = new BlockStatement(List.of(thenBranchExp), dummyLineNumber);
        var ifElseStatement = new IfStatement(conditionalExpression, thenBranch, elseBranch, dummyLineNumber);
        try {
            ifElseStatement.evaluate(env);
        } catch (Exception e) {
            assertTrue(ifElseStatement.getDiagnostics().size() > 0);
        }
    }

    @Test(expected = Exception.class)
    public void testIfElseExpErrorInElseBranchThrowsException() throws Exception {
        /*
            i = 4;
            if (j < 3) {
                i = -1;
            }
         */
        init_I.evaluate(env);
        elseBranchExp = Assign(Id("i"), Id("j"));
        elseBranch = new BlockStatement(List.of(elseBranchExp), dummyLineNumber);
        var ifElseStatement = new IfStatement(conditionalExpression, thenBranch, elseBranch, dummyLineNumber);
        ifElseStatement.evaluate(env);
    }

    @Test
    public void testIfElseExpErrorInElseBranchReturnsErrorMessage() throws Exception {
        /*
            i = 4;
            if (j < 3) {
                i = -1;
            }
         */
        init_I.evaluate(env);
        elseBranchExp = Assign(Id("i"), Id("j"));
        elseBranch = new BlockStatement(List.of(elseBranchExp), dummyLineNumber);
        var ifElseStatement = new IfStatement(conditionalExpression, thenBranch, elseBranch, dummyLineNumber);
        try {
            ifElseStatement.evaluate(env);
        } catch (Exception e) {
            assertTrue(ifElseStatement.getDiagnostics().size() > 0);
        }
    }

}
