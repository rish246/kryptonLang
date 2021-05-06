package com.Rishabh.Syntax.PrimaryExpressions;

import com.Rishabh.EvalResult;
import com.Rishabh.ExpressionType;
import com.Rishabh.Syntax.Expression;
import com.Rishabh.Syntax.Values.ClosureExpression;
import com.Rishabh.Syntax.Values.ListExpression;
import com.Rishabh.Token;
import com.Rishabh.Utilities.Environment;

import java.util.ArrayList;
import java.util.List;

public class CreateClassInstance extends Expression {
    public Token _className;
    public Expression _constructorCall;
    public List<String> _diagnostics = new ArrayList<>();
    public Environment _objectState;
    public CreateClassInstance(Token className, Expression initConstructorCall, int lineNumber) {
        super(ExpressionType.ClassInstanceExpression, lineNumber);
        _className = className;
        _constructorCall = initConstructorCall;
    }

    @Override
    public void prettyPrint(String indent) {
        System.out.println(_className._lexeme);
    }

    public List<String> getDiagnostics() {
        return _diagnostics;
    }

    @Override
    // a.x
    public EvalResult evaluate(Environment env) throws Exception {

        String className = _className._lexeme;
        EvalResult classEntry = env.get(className);


        if(classEntry == null) {
            _diagnostics.add("Undefined Symbol " + className + " at line number " + getLineNumber());
            return null;
        }

        // Extract the constructor method
        Environment classMethods = (Environment) classEntry._value;
        // and callItAMethod
        EvalResult constructorMethodEntry = classMethods.get(className);

        var constructorMethod = (ClosureExpression) constructorMethodEntry._value;

        // Call the constructor method in env it was called.. evaluate it in env
        List<Expression> formalArgs = constructorMethod._formalArgs;

        var constructorCall = (ArrayAccessExpression) _constructorCall;

        Expression argsList = constructorCall._indices[0];

        // I'll need to extract the actual args
        var actualArgsExp = (ListExpression) ((ParensExpression) argsList)._body;

        List<Expression> actualArgsList = actualArgsExp._elements;


        _objectState = new Environment(null);
        _objectState.set(className, classEntry);
        _objectState._ParentEnv = classMethods;

        // function args env -> bind formal to actual args
        Environment functionArgsBinding = new Environment(_objectState);
        for(int i=0; i < actualArgsList.size(); i++) {
            EvalResult argRes = actualArgsList.get(i).evaluate(env);
            AssignmentExpression.Bind(formalArgs.get(i), argRes, functionArgsBinding, _diagnostics, getLineNumber());
        }

        functionArgsBinding.set("this", new EvalResult(_objectState, className));

        constructorMethod._closureEnv = functionArgsBinding;

        constructorMethod.evaluate(_objectState);

        return new EvalResult(_objectState, className);
    }
}

// We have to bind member name to inorderTraversal() ->
// assignment expression
// a.x = 5;
// this.x = 5;
// a.x ->