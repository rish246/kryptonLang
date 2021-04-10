package com.Rishabh.Expression;

import com.Rishabh.EvalResult;
import com.Rishabh.ExpressionType;
import com.Rishabh.Utilities.Environment;
import com.Rishabh.Utilities.Symbol;

import java.util.ArrayList;
import java.util.List;

public class FunctionCallExpression extends Expression {
    public String _functionName;
    public List<Expression> _actualArgs;
    public List<String> _diagnostics = new ArrayList<>();


    public FunctionCallExpression(String lexeme, List<Expression> actualArgs) {
        super(ExpressionType.FunctionCallExpression);
        _functionName = lexeme;
        _actualArgs = actualArgs;
    }

    @Override
    public void prettyPrint(String indent) {
        System.out.println("Function Call");
        System.out.println("|");
        System.out.println(indent + _functionName);
        System.out.println("|");
        for(Expression aArg : _actualArgs) {
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
            _diagnostics.add("Undefined function : " + _functionName);
            return null;
        }


        // Get the closure from the symbol
        ClosureExpression closure = (ClosureExpression) res._value;

        Environment newEnv = new Environment(null);
        // Bind formal args with actual args
        List<IdentifierExpression> formalArgs = closure._functionExp._formalArgs;

        if(formalArgs.size() != _actualArgs.size()) {
            _diagnostics.add("Invalid number of arguements passed in function " + _functionName + ", expected " + formalArgs.size() + ", got " + _actualArgs.size());
            return null;
        }


        // Map function name to the closure in the body
        newEnv.set(_functionName, res);

        // evaluate the actual arg and add the entry in newEnv
        for(int i=0; i<_actualArgs.size(); i++) {
            EvalResult curArgResult = _actualArgs.get(i).evaluate(env);

            Symbol newBinding = new Symbol(null, curArgResult._value, curArgResult._type);
            newEnv.set(formalArgs.get(i)._lexeme, newBinding);
        }

        newEnv._ParentEnv = closure._closureEnv;

        Expression funcBody = closure._functionExp._body;

        EvalResult funcResult = funcBody.evaluate(newEnv);
        // If the value != null... then value and type of the function is same
        _diagnostics.addAll(funcBody.getDiagnostics());

        return funcResult;

    }

}

