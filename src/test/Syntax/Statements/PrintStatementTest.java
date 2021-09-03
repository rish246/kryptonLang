package test.Syntax.Statements;

import com.Krypton.Syntax.Expression;
import com.Krypton.Syntax.Statements.PrintStatement;
import com.Krypton.Utilities.Environment;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static test.TestUtil.*;

public class PrintStatementTest {
    protected Expression init_I;

    protected int dummyLineNumber = 1;
    protected Environment env;


    @Before
    public void setUp() {
    /*
        i = 3;
        print(i);
     */


        init_I = Assign(Id("i"), Int(4));

        env = new Environment(null);
    }

    @Test
    public  void testPrintStatementSuccess() throws Exception {
        /*
            i = 3;
            print(i);
         */
        init_I.evaluate(env);

        var printExp = new PrintStatement(Id("i"), dummyLineNumber);

        printExp.evaluate(env);
    }

    @Test(expected = Exception.class)
    public  void testPrintStatementErrorThrowsException() throws Exception {
        /*
            print(i);
         */
//        init_I.evaluate(env);

        var printExp = new PrintStatement(Id("i"), dummyLineNumber);

        printExp.evaluate(env);
    }

    @Test
    public  void testPrintStatementErrorReturnsMeaningfulErrorMessage() throws Exception {
        /*
            print(i);
         */
//        init_I.evaluate(env);

        var printExp = new PrintStatement(Id("i"), dummyLineNumber);

        try {
            printExp.evaluate(env);
        } catch (Exception e) {
            assertTrue(printExp.getDiagnostics().size() > 0);
        }
    }


}
