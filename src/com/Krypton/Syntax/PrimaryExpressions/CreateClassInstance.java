package com.Krypton.Syntax.PrimaryExpressions;

import com.Krypton.EvalResult;
import com.Krypton.ExpressionType;
import com.Krypton.Syntax.Expression;
import com.Krypton.Token;
import com.Krypton.Utilities.Environment;

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
    public EvalResult evaluate(Environment env) throws Exception {
        String className = _className._lexeme;
        EvalResult classEntry = env.get(className);
        return createNewClassInstance(env, className, classEntry);
    }

    private EvalResult createNewClassInstance(Environment env, String className, EvalResult classEntry) throws Exception {
//        if(classEntry == null) {
//            _diagnostics.add("Undefined Symbol " + className + " at line number " + getLineNumber());
//            return null;
//        }
//
//        Environment classEnvironment = (Environment) classEntry.getValue();
//        EvalResult constructorMethodEntry = classEnvironment.get("init");
//        return callConstructorMethod(env, className, classEntry, classEnvironment, constructorMethodEntry);
        return null;
    }
//
//    private EvalResult callConstructorMethod(Environment env, String className, EvalResult classEntry, Environment classEnvironment, EvalResult constructorMethodEntry) throws Exception {
//        if(constructorMethodEntry == null) {
//            _diagnostics.add("Didn't find a constructor method for class " + className + ", error at line number " + getLineNumber());
//            return null;
//        }
//
//        var constructorMethod = (ClosureExpression) constructorMethodEntry._value;
//        List<Expression> formalArgs = constructorMethod._formalArgs;
//        List<Expression> actualArgsList = getActualArgs();
//        if(!areSameSize(formalArgs, actualArgsList)) {
//            _diagnostics.add("Invalid number of arguments passed ... Expected " + formalArgs.size() + ", got " + actualArgsList.size() + " at line number " + getLineNumber());
//            return null;
//        }
//
//        initializeObjectState(className, classEntry, classEnvironment);
//        Environment functionArgsBinding = createConstructorEnvironment(env, className, formalArgs, actualArgsList);
//
//        setupInheritanceLogic(className, classEnvironment, functionArgsBinding);
//        functionArgsBinding.set(className, classEntry);
//        constructorMethod._closureEnv = functionArgsBinding;
//        constructorMethod.evaluate(_objectState);
//        return new EvalResult(_objectState, className);
//    }
//
//    private boolean areSameSize(List<Expression> formalArgs, List<Expression> actualArgsList) {
//        return formalArgs.size() == actualArgsList.size();
//    }
//
//    // @TODO: Rename and Refactor this function later... This is to make the super keyword to work
//    private void setupInheritanceLogic(String className, Environment classEnvironment, Environment functionArgsBinding) throws Exception {
//        boolean isChildClass = (boolean) classEnvironment.get("isChild")._value;
//        if(isChildClass) {
//            var immParentEnv = (Environment) classEnvironment.get("ParentClass")._value;
//            var immParentEnvCopy = Environment.copy(immParentEnv);
//            immParentEnvCopy.set("isSuper", new EvalResult(true, "boolean"));
//            immParentEnvCopy.set("evalEnvironment", new EvalResult(Environment.copy(_objectState), className));
//            functionArgsBinding.set("super", new EvalResult(immParentEnvCopy, className));
//        }
//    }
//
//    private void initializeObjectState(String className, EvalResult classEntry, Environment classEnvironment) {
//        _objectState = new Environment(null);
//        _objectState.set(className, classEntry);
//        _objectState._ParentEnv = classEnvironment;
//    }
////
//    private Environment createConstructorEnvironment(Environment env, String className, List<Expression> formalArgs, List<Expression> actualArgsList) throws Exception {
//        Environment functionArgsBinding = new Environment(null);
//        functionArgsBinding.set("this", new EvalResult(_objectState, className));
//        for(int i = 0; i < actualArgsList.size(); i++) {
//            EvalResult argRes = actualArgsList.get(i).evaluate(env);
//            evaluator.Bind(formalArgs.get(i), argRes, functionArgsBinding);
//        }
//        functionArgsBinding._ParentEnv = _objectState;
//        return functionArgsBinding;
//    }
//
//    private List<Expression> getActualArgs() {
//        var constructorCall = (ArrayAccessExpression) _constructorCall;
//        Expression argsList = constructorCall._indices[0];
//        var actualArgsExp = (ListExpression) ((ParensExpression) argsList)._body;
//        return actualArgsExp._elements;
//    }
}