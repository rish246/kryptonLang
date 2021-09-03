package test.Utilities;

import com.Krypton.EvalResult;
import com.Krypton.Syntax.Expression;
import com.Krypton.Syntax.PrimaryExpressions.ArrayAccessExpression;
import com.Krypton.Syntax.Values.ObjectExpression;
import com.Krypton.Utilities.Environment;
import org.junit.Before;
import org.junit.Test;
import test.TestUtil;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static test.TestUtil.*;

public class BinderTest {
    // Test error reporting and handling of the binder
    protected Expression left;
    protected Expression right;

    protected Environment env;
    @Before
    public void setUp() {
        /*
            list = [1, 2, 3]
            list[0] = -4
            list == [-4, 2, 3]
         */
        left = TestUtil.List(Arrays.asList(Int(1), Int(2), Int(3)));

        right = Int(-4);

        env = new Environment(null);
    }

    @Test
    public void testUpdateListItemSuccess() throws Exception {
        // list = [1, 2, 3]
        env.set("list", left.evaluate(env));
        // list[0] = -4
        var list_0 = new ArrayAccessExpression(Id("list"), Int(0), dummyLineNumber);

        var assignExp = Assign(list_0, right);

        assignExp.evaluate(env);

        assertEquals(env.get("list"), ListRes(Arrays.asList(IntRes(-4), IntRes(2), IntRes(3))));
    }

    @Test
    public void testUpdateListItemPassByRefWorks() throws Exception {
        /*
            list = [1, [2, 3], 3];
            t = list[1]
            list[1] = -4
            t = ?
         */
        // list = [1, [2, 3], 3];
        var list = TestUtil.List(List.of(Int(1), TestUtil.List(Arrays.asList(Int(2), Int(3))), Int(3)));
        env.set("list", list.evaluate(env));

        //  t = list[1]
        var init_T = Assign(Id("t"), new ArrayAccessExpression(Id("list"), Int(1), dummyLineNumber));
        init_T.evaluate(env);

        // update list: list[1] = -4
        var rightExp = Int(-4);
        var leftExp = new ArrayAccessExpression(Id("list"), Int(1), dummyLineNumber);
        var updateList = Assign(leftExp, rightExp);
        updateList.evaluate(env);
        assertNotEquals(env.get("t"), IntRes(-4));
    }

    @Test
    public void testSwapListElements() throws Exception {
        /*
            list = [1, 2, 3];
            [list[0], list[1]] = [list[1], list[0]]
            list = [2, 1, 3]
         */
        // list = [1, 2, 3];
        var list = TestUtil.List(List.of(Int(1), Int(2), Int(3)));
        env.set("list", list.evaluate(env));

        //  [list[0], list[1]] = [list[1], list[0]]
        var list_0 = new ArrayAccessExpression(Id("list"),Int(0), dummyLineNumber);
        var list_1 = new ArrayAccessExpression(Id("list"), Int(1), dummyLineNumber);

         // [list[0], list[1]]
        var leftExp = TestUtil.List(Arrays.asList(list_0, list_1));
        //  [list[1], list[0]]
        var rightExp = TestUtil.List(Arrays.asList(list_1, list_0));

        // [list[0], list[1]] = [list[1], list[0]]
        var updateList = Assign(leftExp, rightExp);
        updateList.evaluate(env);

        System.out.println(env.get("list"));
        assertEquals(env.get("list"), ListRes(Arrays.asList(IntRes(2), IntRes(1), IntRes(3))));
    }


    /////////////////////// Bind To ObjectAccessExpression //////////////////
    @Test
    public void testAssignValueToObjectSuccess() throws Exception {
        /*
            x = {}
            x[4] = 3
         */

        // x = {}
        var newObject = new ObjectExpression((new HashMap<Expression, Expression>()), dummyLineNumber);
        var init_X = Assign(Id("x"), newObject);
        init_X.evaluate(env);
        // x[4] = 3
        var x_4 = new ArrayAccessExpression(Id("x"), Int(4), dummyLineNumber);
        var three = Int(3);
        var updateX_exp = Assign(x_4, three);

        EvalResult result = updateX_exp.evaluate(env);
        System.out.println(result);
    }


}
