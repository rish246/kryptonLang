package com.Rishabh.Syntax.PrimaryExpressions;

import com.Rishabh.EvalResult;
import com.Rishabh.ExpressionType;
import com.Rishabh.Syntax.Expression;
import com.Rishabh.Token;
import com.Rishabh.Utilities.Environment;
import com.Rishabh.Utilities.Symbol;

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
    public EvalResult evaluate(Environment env) {
        Symbol classEntry = env.get(_className._lexeme);
        Environment classEnv = (Environment) classEntry._value;
        _objectState = new Environment(classEnv);
        _objectState.set("this", new Symbol("this", _objectState, _className._lexeme));

        // we have to update the _objectState
        return new EvalResult(_objectState, _className._lexeme);
    }
}


// assignment expression
// a.x = 5;
// this.x = 5;
// a.x ->