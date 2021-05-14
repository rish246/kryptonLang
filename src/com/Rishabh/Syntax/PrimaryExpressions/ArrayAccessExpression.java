package com.Rishabh.Syntax.PrimaryExpressions;

import com.Rishabh.EvalResult;
import com.Rishabh.ExpressionType;
import com.Rishabh.Syntax.Expression;
import com.Rishabh.Syntax.Values.ClosureExpression;
import com.Rishabh.Syntax.Values.ListExpression;
import com.Rishabh.SyntaxTree;
import com.Rishabh.Token;
import com.Rishabh.Utilities.Environment;

import jdk.javadoc.internal.doclets.formats.html.SourceToHTMLConverter;

import java.util.ArrayList;
import java.util.List;

public class ArrayAccessExpression extends Expression {

    public Token _identifier;
    public Expression[] _indices;
    public List<String> _diagnostics = new ArrayList<>();


    public ArrayAccessExpression(Token identifierToken, List<Expression> indices, int lineNumber) {
        super(ExpressionType.ArrayAccessExpression, lineNumber);
        _identifier = identifierToken;
        _indices = indices.toArray(new Expression[0]);
    }

    /*
        Evaluating a([i]|(x, y, z))+
        1. Get the value of the identifier (a) from the env
        2. check if the identifier exists
        3. if it does not, throw error
        4. if it is not of proper type, throw error
        5. else evaluate the whole expression by calling Evaluate

     */
    public EvalResult evaluate(Environment env) throws Exception {
        EvalResult result = null;
        EvalResult ourIdentifierEntry = env.get(_identifier._lexeme);
        // Check if the lexeme is valid one
        if (ourIdentifierEntry == null) {
            _diagnostics.add("Invalid identifier " + _identifier._lexeme + " at line number " + getLineNumber());
        } else {
            result = Evaluate(env, ourIdentifierEntry);
        }

        return result;
    }

    /*
        Iterative evaluation of exp
        a[1][2](3, "hello) -> ((a[1])[2])(3, "hello")

     */
    private EvalResult Evaluate(Environment env, EvalResult ourEntry) throws Exception {
        EvalResult Result = ourEntry;
        // this->Environment
        for(Expression index : _indices) {

            if(Result == null || Result._value == null) {
                _diagnostics.add("NullPointerException: Cannot access property from null, at line number " + getLineNumber());
                return null;
            }

            if(index.getType() == ExpressionType.ParensExpression) {
                Result = callFunction(Result, index, env);
            }
            else if(index.getType() == ExpressionType.MemberAccessExpression) {
                // Get the member from Initial's environment
                var memberName = (MemberAccessExpression) index;
                String className = memberName._memberName._lexeme;
                Environment objectEnv = (Environment) Result._value;

                EvalResult methodClosure =  memberName.evaluate(objectEnv);


                // ForwardDeclaration -> () -> { print (" x "); }                
                if(methodClosure == null) {
                    return null;
                }

                if(!methodClosure.getType().equals("Closure")) {
                    Result = methodClosure;
                    continue;
                }

                

                ClosureExpression closure = (ClosureExpression) methodClosure._value;

                /*
                    @TODO: 
                        Change this... this is going to make this work

                        // Make something to recognize " super -> and thus will be "
                        // If the binding is super... dont change the closure's closureEnv 
                */
                boolean isSuperAccess = (boolean) objectEnv.get("isSuper")._value;

                // If super.init() -> getClosureValue() 
                if(!isSuperAccess) 
                    closure._closureEnv = objectEnv;
                else
                    closure._closureEnv = (Environment) objectEnv.get("evalEnvironment").getValue();

                // Lets see in which environment it is getting evaluated 

                closure._closureEnv.set("this", new EvalResult(closure._closureEnv, className));
                System.out.println("-----------------------------------------------");
                System.out.println(Result._type + " --> " + className);
                System.out.println("-----------------------------------------------");
                closure._closureEnv.printEnv();
                System.out.println("-----------------------------------------------");


                Environment closureEnv = (Environment) closure._closureEnv.get("this").getValue();
                closureEnv.printEnv();
                System.out.println("-----------------------------------------------");
                
                Result = methodClosure;
            }
            else {
                Result = AssignmentExpression.getValue(Result, index, env, _diagnostics, getLineNumber()); // If index._type == parensExpression -> functionCall

            }

            if(Result == null)
                return null;
        }
        return Result;
    }

    private EvalResult callFunction(EvalResult Initial, Expression index, Environment env) throws Exception {
        if(Initial._type != "Closure") {
            _diagnostics.add("Expression of type " + Initial._type + " can not be called, at line number " + getLineNumber());
            return null;
        }
        var function = (ClosureExpression) Initial._value;
        Environment functionEnv = function._closureEnv;
        SyntaxTree functionBody = function._functionBody;
        var actualArgsList = (ListExpression) ((ParensExpression) index)._body;
        // elements
        List<Expression> actualArgs = actualArgsList._elements;
        List<Expression> formalArgs = function._formalArgs;

        if(formalArgs.size() != actualArgs.size()) {
            _diagnostics.add("Expected " + formalArgs.size() + " arguements, got " + actualArgs.size() + " at line number " + getLineNumber());
            return null;
        }


        Environment functionArgsEnv = bindFormalArgsWithActualArgs(env, formalArgs, actualArgs);
        functionArgsEnv._ParentEnv = functionEnv;

        EvalResult finalRes = functionBody.evaluate(functionArgsEnv);
        _diagnostics.addAll(functionBody.getDiagnostics());


        return finalRes;
    }

    /*
        @TODO:
            2. Make the ability to de-structure formal args --> Nicely done
     */
    private Environment bindFormalArgsWithActualArgs(Environment env, List<Expression> formalArgs, List<Expression> _actualArgs) throws Exception {
        Environment newEnv = new Environment(null);

        for(int i=0; i<_actualArgs.size(); i++) {
            Expression curFormalArg = formalArgs.get(i);
            Expression curActualArg = _actualArgs.get(i);

            EvalResult actualArgRes = curActualArg.evaluate(env);

            AssignmentExpression.Bind(curFormalArg, actualArgRes, newEnv, _diagnostics, getLineNumber());
        }

        return newEnv;
    }

    public void prettyPrint(String indent) {
        System.out.println("Array Access");
        System.out.print(indent + "|-");
        System.out.println(_identifier._value);
        System.out.println(indent + "|-");
        System.out.println(indent + "|");
        System.out.print(indent + "|_");
        for(SyntaxTree index : _indices) {
            index.prettyPrint(indent + "    ");
        }

    }

    public List<String> getDiagnostics() {
        return _diagnostics;
    }

}


// Get the EvalIterable
