package com.Rishabh.Expression;

import com.Rishabh.EvalResult;
import com.Rishabh.ExpressionType;
import com.Rishabh.Utilities.Environment;
import com.Rishabh.Utilities.Symbol;

import java.util.ArrayList;
import java.util.List;

public class FunctionCallExpression extends Expression {
    String _functionName;
    List<String> _diagnostics = new ArrayList<>();


    public FunctionCallExpression(String lexeme) {
        super(ExpressionType.FunctionCallExpression);
        _functionName = lexeme;
    }

    @Override
    public void prettyPrint(String indent) {
        System.out.println("Call -> " + _functionName);
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
        // get env and function expression from closre
        Environment closureEnv = closure._closureEnv;
        Expression funcBody = closure._functionExp._body;

        funcBody.evaluate(closureEnv);
        _diagnostics.addAll(funcBody.getDiagnostics());

        return new EvalResult(null, "Function call");


    }

}

