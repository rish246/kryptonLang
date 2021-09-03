package test.Syntax.PrimaryExpression.BinaryExpression.AssignmentExpression;

import com.Krypton.EvalResult;
import com.Krypton.Syntax.Expression;
import com.Krypton.Syntax.PrimaryExpressions.ArrayAccessExpression;
import com.Krypton.Syntax.PrimaryExpressions.BinaryExpression;
import com.Krypton.Syntax.PrimaryExpressions.IdentifierExpression;
import com.Krypton.Syntax.Values.*;
import com.Krypton.TokenType;
import com.Krypton.Utilities.CustomExceptions.BinaryOperators.BadAssignmentException;
import com.Krypton.Utilities.Environment;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class SimpleAssignmentTest {
    protected Expression left;
    protected Expression right;
    protected int dummyLineNumber = 1;
    protected Environment env;


    /*
        a = 4; // Try this assignment expression
        // Try multiple types of assignments
        // Binding Objects to objects
        // Binding Lists to lists
        // Binding ArrayAccessExpression
        .... And so on

     */
    @Before
    public void setUp() {
        left = new IdentifierExpression("a", dummyLineNumber);
        right = new NumberExpression(5, dummyLineNumber);
        env = new Environment(null);
    }

    @Test
    public void testAssignIntegerToIdentifier() throws Exception {
        left = new IdentifierExpression("a", dummyLineNumber);
        var assignmentExp = new BinaryExpression(left, TokenType.AssignmentToken, right, dummyLineNumber);
        EvalResult result = assignmentExp.evaluate(env);
        String identifierLexeme = ((IdentifierExpression) left).getLexeme();
        assertEquals(result, env.get(identifierLexeme));
    }

    @Test
    public void testAssignStringToIdentifier() throws Exception {
        left = new IdentifierExpression("a", dummyLineNumber);
        right = new StringExpression("hello value", dummyLineNumber);
        var assignmentExp = new BinaryExpression(left, TokenType.AssignmentToken, right, dummyLineNumber);
        EvalResult result = assignmentExp.evaluate(env);
        String identifierLexeme = ((IdentifierExpression) left).getLexeme();
        assertEquals(result, env.get(identifierLexeme));
    }

    @Test
    public void testAssignListToIdentifier() throws Exception {
        left = new IdentifierExpression("a", dummyLineNumber);
        List<Expression> rightElements = Arrays.asList(
                new NumberExpression(3, dummyLineNumber),
                new BoolExpression(true, dummyLineNumber),
                new StringExpression("hello", dummyLineNumber));

        ListExpression rightList = new ListExpression(rightElements, dummyLineNumber);

        var assignmentExp = new BinaryExpression(left, TokenType.AssignmentToken, rightList, dummyLineNumber);
        EvalResult result = assignmentExp.evaluate(env);
        String identifierLexeme = ((IdentifierExpression) left).getLexeme();
        assertEquals(result, env.get(identifierLexeme));
    }

    @Test(expected = Exception.class)
    public void testAssignListToIdentifierThrowsException() throws Exception {
        left = new IdentifierExpression("a", dummyLineNumber);
        List<Expression> rightElements = Arrays.asList(
                new IdentifierExpression("z", dummyLineNumber),
                new BoolExpression(true, dummyLineNumber),
                new StringExpression("hello", dummyLineNumber));

        ListExpression rightList = new ListExpression(rightElements, dummyLineNumber);

        var assignmentExp = new BinaryExpression(left, TokenType.AssignmentToken, rightList, dummyLineNumber);
        assignmentExp.evaluate(env);
    }



    //////////////////////////////// Binding Lists //////////////////////////////
    @Test
    public void testAssignListToList() throws Exception {
        List<Expression> leftElements = Arrays.asList(
                new IdentifierExpression("a", dummyLineNumber),
                new IdentifierExpression("b", dummyLineNumber),
                new IdentifierExpression("c", dummyLineNumber));

        List<Expression> rightElements = Arrays.asList(
                new NumberExpression(3, dummyLineNumber),
                new BoolExpression(true, dummyLineNumber),
                new StringExpression("hello", dummyLineNumber));

        ListExpression leftList = new ListExpression(leftElements, dummyLineNumber);
        ListExpression rightList = new ListExpression(rightElements, dummyLineNumber);

        var assignmentExp = new BinaryExpression(leftList, TokenType.AssignmentToken, rightList, dummyLineNumber);
        EvalResult result = assignmentExp.evaluate(env);
        List<EvalResult> resultList = (List<EvalResult>) result.getValue();
        for (int i=0; i<leftElements.size(); i++) {
            String name = ((IdentifierExpression) leftElements.get(i)).getLexeme();
            assertEquals(resultList.get(i), env.get(name));
        }
    }

    @Test(expected = BadAssignmentException.class)
    public void testAssignListToListOfDifferentSizeThrowsException() throws Exception {
        List<Expression> leftElements = Arrays.asList(
                new IdentifierExpression("a", dummyLineNumber),
                new IdentifierExpression("b", dummyLineNumber),
                new IdentifierExpression("c", dummyLineNumber));

        List<Expression> rightElements = Arrays.asList(
                new NumberExpression(3, dummyLineNumber),
                new BoolExpression(true, dummyLineNumber),
                new StringExpression("hello", dummyLineNumber),
                new StringExpression("hello", dummyLineNumber));

        ListExpression leftList = new ListExpression(leftElements, dummyLineNumber);
        ListExpression rightList = new ListExpression(rightElements, dummyLineNumber);

        var assignmentExp = new BinaryExpression(leftList, TokenType.AssignmentToken, rightList, dummyLineNumber);
        assignmentExp.evaluate(env);
    }

    @Test(expected = Exception.class)
    public void testAssignListToListThrowsException() throws Exception {
        List<Expression> leftElements = Arrays.asList(
                new IdentifierExpression("a", dummyLineNumber),
                new IdentifierExpression("b", dummyLineNumber),
                new IdentifierExpression("c", dummyLineNumber));

        List<Expression> rightElements = Arrays.asList(
                new IdentifierExpression("z", dummyLineNumber),
                new BoolExpression(true, dummyLineNumber),
                new StringExpression("hello", dummyLineNumber),
                new StringExpression("hello", dummyLineNumber));

        ListExpression leftList = new ListExpression(leftElements, dummyLineNumber);
        ListExpression rightList = new ListExpression(rightElements, dummyLineNumber);

        var assignmentExp = new BinaryExpression(leftList, TokenType.AssignmentToken, rightList, dummyLineNumber);
        assignmentExp.evaluate(env);
    }


    //////////////////////////////////////////////////////////////////////////////////
    //////////////////////          Binding Objects         //////////////////////////
    /*
        test-> { name, age } = {"name" : "Rishabh", "age" : 19 }
                name -> "Rishabh", age -> 19
     */

    @Test
    public void testAssignObjectToObject() throws Exception {
        Map<Expression, Expression> leftObject = new HashMap<>();
        /*
            leftObject = {"name" : name, "age" : age};
         */
        leftObject.put(new StringExpression("name", dummyLineNumber), new IdentifierExpression("name", dummyLineNumber));
        leftObject.put(new StringExpression("age", dummyLineNumber), new IdentifierExpression("age", dummyLineNumber));


        Map<Expression, Expression> rightObject = new HashMap<>();
        /*
            rightObject = {"name" : "Rishabh", "age" : 18};
         */
        rightObject.put(new StringExpression("name", dummyLineNumber), new StringExpression("Rishabh", dummyLineNumber));
        rightObject.put(new StringExpression("age", dummyLineNumber), new NumberExpression(18, dummyLineNumber));

        var leftList = new ObjectExpression(leftObject, dummyLineNumber);
        var rightList = new ObjectExpression(rightObject, dummyLineNumber);

        var assignmentExp = new BinaryExpression(leftList, TokenType.AssignmentToken, rightList, dummyLineNumber);
        EvalResult result = assignmentExp.evaluate(env);
        Map<String, EvalResult> resultObject = (HashMap<String, EvalResult>)result.getValue();

        assertEquals(env.get("name"), new EvalResult("Rishabh", "string"));
        assertEquals(env.get("age"), new EvalResult(18, "int"));
    }


    @Test(expected = BadAssignmentException.class)
    public void testAssignObjectToObjectThrowsBadAssignmentException() throws Exception {
        Map<Expression, Expression> leftObject = new HashMap<>();
        /*
            leftObject = {"name" : name, "age" : age};
         */
        leftObject.put(new StringExpression("name", dummyLineNumber), new IdentifierExpression("name", dummyLineNumber));
        leftObject.put(new StringExpression("age", dummyLineNumber), new IdentifierExpression("age", dummyLineNumber));


        Map<Expression, Expression> rightObject = new HashMap<>();
        /*
            rightObject = {"name" : "Rishabh", "age" : 18};
         */
        rightObject.put(new StringExpression("name", dummyLineNumber), new StringExpression("Rishabh", dummyLineNumber));
        rightObject.put(new StringExpression("name", dummyLineNumber), new NumberExpression(18, dummyLineNumber));

        var leftList = new ObjectExpression(leftObject, dummyLineNumber);
        var rightList = new ObjectExpression(rightObject, dummyLineNumber);

        var assignmentExp = new BinaryExpression(leftList, TokenType.AssignmentToken, rightList, dummyLineNumber);
        EvalResult result = assignmentExp.evaluate(env);
    }


    /////////////////////////// Test ArrayAccessExpression /////////////////////////////////
    @Test
    public void testChangeValueOfListElementSuccess() throws Exception {
        /*
            a = [1, 2, 3];
            left = a[0]
            right = -4
            >>> a[0] = -4;
         */

        // a = [1, 2, 3]
        var list = ListRes(Arrays.asList(IntRes(1), IntRes(2), IntRes(3)));
        env.set("a", list);

        // index = [0]
        var index = Int(0);

        // leftExp -> a[0]
        var leftExp = new ArrayAccessExpression(Id("a"), index, dummyLineNumber);

        // rightExp -> -4
        var rightExp = Int(-4);

        // a[0] = -4
        var assignmentExp = new BinaryExpression(leftExp, TokenType.AssignmentToken, rightExp, dummyLineNumber);
        assignmentExp.evaluate(env);
        var expectedResult = ListRes(Arrays.asList(IntRes(-4), IntRes(2), IntRes(3)));
        assertEquals(env.get("a"), expectedResult);
    }

    @Test
    public void testAssignValueToANewKeyObjectSuccess() throws Exception {
        /*
            a = {};
            left = a[0]
            right = -4
            >>> a[0] = -4;
         */

        // a = {}
        var list = new EvalResult(new HashMap<>(), "object");
        env.set("a", list);

        // index = [0]
        var index = Int(0);

        // leftExp -> a[0]
        var leftExp = new ArrayAccessExpression(Id("a"), index, dummyLineNumber);

        // rightExp -> -4
        var rightExp = Int(-4);

        // a[0] = -4
        var assignmentExp = new BinaryExpression(leftExp, TokenType.AssignmentToken, rightExp, dummyLineNumber);
        assignmentExp.evaluate(env);

        var newHashmap = new HashMap<String, EvalResult>();
        newHashmap.put("0", IntRes(-4));
        var expectedResult = new EvalResult(newHashmap, "object");
        assertEquals(env.get("a"), expectedResult);
    }

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

}

// No code.. Test our parser one different ParsingUnits


