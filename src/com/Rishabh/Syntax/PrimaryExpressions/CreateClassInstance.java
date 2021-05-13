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
        EvalResult constructorMethodEntry = classMethods.get("init");

        // check if the constructor exists or not
        if(constructorMethodEntry == null) {
            _diagnostics.add("Didn't find a constructor method for class " + className + ", error at line number " + getLineNumber());
            return null;
        }


        var constructorMethod = (ClosureExpression) constructorMethodEntry._value;

        List<Expression> formalArgs = constructorMethod._formalArgs;

        var constructorCall = (ArrayAccessExpression) _constructorCall;

        Expression argsList = constructorCall._indices[0];

        // I'll need to extract the actual args
        var actualArgsExp = (ListExpression) ((ParensExpression) argsList)._body;

        List<Expression> actualArgsList = actualArgsExp._elements;


        _objectState = new Environment(null);
        _objectState.set(className, classEntry);
        _objectState._ParentEnv = classMethods; 

        //////////////////////////////////////////////
        if(formalArgs.size() != actualArgsList.size()) {
            _diagnostics.add("Invalid number of arguements passed ... Expected " + formalArgs.size() + ", got " + actualArgsList.size() + " at line number " + getLineNumber());
            return null;
        }

        // function args env -> bind formal to actual args
        Environment functionArgsBinding = new Environment(_objectState);
        for(int i=0; i < actualArgsList.size(); i++) {
            EvalResult argRes = actualArgsList.get(i).evaluate(env);
            AssignmentExpression.Bind(formalArgs.get(i), argRes, functionArgsBinding, _diagnostics, getLineNumber());
        }

        functionArgsBinding.set("this", new EvalResult(_objectState, className)); // Bind 

        functionArgsBinding.set(className, classEntry);

        constructorMethod._closureEnv = functionArgsBinding;

        constructorMethod.evaluate(_objectState);

        return new EvalResult(_objectState, className);
    }
}

