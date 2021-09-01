package test.Syntax.PrimaryExpression;

import com.Krypton.EvalResult;
import com.Krypton.Syntax.Expression;
import com.Krypton.Syntax.PrimaryExpressions.BinaryExpression;
import com.Krypton.Syntax.PrimaryExpressions.FunctionCallExpression;
import com.Krypton.Syntax.PrimaryExpressions.IdentifierExpression;
import com.Krypton.Syntax.Statements.BlockStatement;
import com.Krypton.Syntax.Statements.FunctionStatement;
import com.Krypton.Syntax.Statements.IfStatement;
import com.Krypton.Syntax.Statements.ReturnStatement;
import com.Krypton.Syntax.Values.ClosureExpression;
import com.Krypton.Syntax.Values.ListExpression;
import com.Krypton.Syntax.Values.NumberExpression;
import com.Krypton.Syntax.Values.StringExpression;
import com.Krypton.SyntaxTree;
import com.Krypton.TokenType;
import com.Krypton.Utilities.Environment;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FunctionCallTest {
    protected ClosureExpression left;
    protected Expression right;
    protected int dummyLineNumber = 1;
    protected Environment env;
    protected FunctionStatement incrementFunction;

    /*
        f(a) { return a + 1; }
     */

    @Before
    public void setUp() {
        String funcName = "increment";
        var functionBody = new ReturnStatement(Add(Id("a"), Int(1)), dummyLineNumber);
        List<Expression> formalArgs = Arrays.asList(Id("a"));
        incrementFunction = new FunctionStatement(funcName, functionBody, formalArgs, dummyLineNumber);
        env = new Environment(null);
    }

    @Test
    public void testFunctionCallWithParam1Returns2() throws Exception {
        /*
            increment(1) == 2
         */
        List<Expression> actualArgs = Arrays.asList(Int(1));
        incrementFunction.evaluate(env);
        var functionCall = new FunctionCallExpression(Id("increment"), actualArgs, dummyLineNumber);
        var result = functionCall.evaluate(env);
        assertEquals(result, new EvalResult(2, "int"));
    }

    @Test
    public void testRecursiveFunctionSuccess() throws Exception {
        /*
            def fib(x) {
                if (x <= 2) { return 1; }
                return fib(x - 1) + fib(x - 2);
            }
         */
        /*
            Create a function statement
         */
        String functionName = "fib";
        SyntaxTree body = new BlockStatement(Arrays.asList(If(), Return()), dummyLineNumber);
        List<Expression> formalArgs = Arrays.asList(Id("x"));

        var functionExp = new FunctionStatement(functionName, body, formalArgs, dummyLineNumber);
        functionExp.evaluate(env);


        // function call
        List<Expression> actualArgs1 = Arrays.asList(Int(5));
        var functionCall1 = new FunctionCallExpression(Id("fib"), actualArgs1, dummyLineNumber);
        var f1Result = functionCall1.evaluate(env);
        assertEquals(f1Result, new EvalResult(5, "int"));


        List<Expression> actualArgs2 = Arrays.asList(Int(6));
        var functionCall2 = new FunctionCallExpression(Id("fib"), actualArgs2, dummyLineNumber);
        var f2Result = functionCall2.evaluate(env);
        assertEquals(f2Result, new EvalResult(8, "int"));

    }

    @Test
    public void testFunctionCallReturnsCorrectErrorMessage() throws Exception {
        /*
            f(a) { return x; } - raise error
         */
        String funcName = "increment";
        var functionBody = new ReturnStatement(Add(Id("noParam"), Int(1)), dummyLineNumber);
        List<Expression> formalArgs = Arrays.asList(Id("a"));
        incrementFunction = new FunctionStatement(funcName, functionBody, formalArgs, dummyLineNumber);

        List<Expression> actualArgs = Arrays.asList(Int(1));
        incrementFunction.evaluate(env);
        var functionCall = new FunctionCallExpression(Id("increment"), actualArgs, dummyLineNumber);
        functionCall.evaluate(env);
        assertTrue(functionCall.getDiagnostics().size() > 0);
    }



    private ReturnStatement Return() {
        // return fib(x - 1) + fib(x - 2);
        var leftExp = new FunctionCallExpression(Id("fib"), Arrays.asList(Add(Id("x"), Int(-1))), dummyLineNumber);
        var rightExp = new FunctionCallExpression(Id("fib"), Arrays.asList(Add(Id("x"), Int(-2))), dummyLineNumber);
        var body = Add(leftExp, rightExp);
        return new ReturnStatement(body, dummyLineNumber);
    }



    private IfStatement If() {
        /*
        if (x <= 2) { return 1; }
         */
        // x <= 2
        var conditionalBranch = new BinaryExpression(Id("x"), TokenType.LessThanEqualToken, Int(2), dummyLineNumber);
        // return 1;
        var thenBranch = new ReturnStatement(Int(1), dummyLineNumber);
        return new IfStatement(conditionalBranch, thenBranch, null, dummyLineNumber);
    }


    ////////////////// Handling diagnostics ///////////////////////////

    private IdentifierExpression Id(String lexeme) {
        return new IdentifierExpression(lexeme, dummyLineNumber);
    }

    private NumberExpression Int(int value) {
        return new NumberExpression(value, dummyLineNumber);
    }

    private StringExpression Str(String value) {
        return new StringExpression(value, dummyLineNumber);
    }

    private ListExpression List(List<Expression> elements) {
        return new ListExpression(elements, dummyLineNumber);
    }

    private ListExpression List(Expression element) {
        return new ListExpression(Arrays.asList(element), dummyLineNumber);
    }

    private EvalResult ListRes(List<EvalResult> elements) {
        return new EvalResult(elements, "list");
    }

    private EvalResult IntRes(int value) {
        return new EvalResult(value, "int");
    }

    private EvalResult StrRes(String value) {
        return new EvalResult(value, "string");
    }


    private BinaryExpression Add(Expression left, Expression right) {
        return new BinaryExpression(left, TokenType.AddToken, right, dummyLineNumber);
    }

}
