package com.Rishabh.Expression.PrimaryExpressions;

import com.Rishabh.EvalResult;
import com.Rishabh.ExpressionType;
import com.Rishabh.Syntax.Expression;
import com.Rishabh.Syntax.PrimaryExpressions.IdentifierExpression;
import com.Rishabh.Syntax.Values.ClosureExpression;
import com.Rishabh.SyntaxTree;
import com.Rishabh.Utilities.Environment;
import com.Rishabh.Utilities.Symbol;

import java.util.ArrayList;
import java.util.List;

public class FunctionCallExpression extends Expression {
    public String _functionName;
    public List<Expression> _actualArgs;
    public List<String> _diagnostics = new ArrayList<>();


    public FunctionCallExpression(String lexeme, List<Expression> actualArgs, int lineNumber) {
        super(ExpressionType.FunctionCallExpression, lineNumber);
        _functionName = lexeme;
        _actualArgs = actualArgs;
    }

    @Override
    public void prettyPrint(String indent) {
        System.out.println("Function Call");
        System.out.println("|");
        System.out.println(indent + _functionName);
        System.out.println("|");
        for(SyntaxTree aArg : _actualArgs) {
            System.out.print(indent + "|-"); aArg.prettyPrint(indent + "    ");
        }

    }

    public List<String> getDiagnostics() {
        return _diagnostics;
    }

    @Override
    public EvalResult evaluate(Environment env) throws  Exception {
//        return new EvalResult(_value, _type);
        Symbol res = env.get(_functionName);

        if(res == null) {
            _diagnostics.add("Undefined function : " + _functionName+ " at line number " + getLineNumber());
            return null;
        }


        // Get the closure from the symbol
        ClosureExpression closure = (ClosureExpression) res._value;

        Environment newEnv = new Environment(null);

        List<IdentifierExpression> formalArgs = closure._formalArgs;

        if(formalArgs.size() != _actualArgs.size()) {
            _diagnostics.add("Invalid number of arguements passed in function " + _functionName + ", expected " + formalArgs.size() + ", got " + _actualArgs.size()+ " at line number " + getLineNumber());
            return null;
        }


        // Map function name to the closure in the body
        newEnv.set(_functionName, res);

        // Functionname -> res()

        // evaluate the actual arg and add the entry in newEnv
        if (bindFormalArgsWithActualArgs(env, newEnv, formalArgs)) return null;

        newEnv._ParentEnv = closure._closureEnv;

        SyntaxTree funcBody = closure._functionBody;

        EvalResult funcResult = funcBody.evaluate(newEnv);

        _diagnostics.addAll(funcBody.getDiagnostics());        // If the value != null... then value and type of the function is same
        if(funcResult == null) {
            return null;
        }
        _diagnostics.addAll(funcBody.getDiagnostics());


        return funcResult;

    }

    private boolean bindFormalArgsWithActualArgs(Environment env, Environment newEnv, List<IdentifierExpression> formalArgs) throws Exception {
        for(int i=0; i<_actualArgs.size(); i++) {
            EvalResult curArgResult = _actualArgs.get(i).evaluate(env);
            _diagnostics.addAll(_actualArgs.get(i).getDiagnostics());
            if(_diagnostics.size() > 0) {
                return true;
            }

            if(curArgResult._value == null && curArgResult._type != "null") {
                _diagnostics.add("Invalid function arguement of type " + curArgResult._type+ " at line number " + getLineNumber());
                return true;
            }

            Symbol newBinding = new Symbol(null, curArgResult._value, curArgResult._type);
            String nextFormalArg = formalArgs.get(i)._lexeme;
            newEnv.set(nextFormalArg, newBinding);
        }
        return false;
    }

}

