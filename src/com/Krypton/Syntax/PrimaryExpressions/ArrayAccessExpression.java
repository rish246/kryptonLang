
package com.Krypton.Syntax.PrimaryExpressions;

import com.Krypton.EvalResult;
import com.Krypton.ExpressionType;
import com.Krypton.Syntax.Expression;
import com.Krypton.SyntaxTree;
import com.Krypton.Token;
import com.Krypton.Utilities.Environment;
import com.Krypton.Utilities.Evaluator;

import java.util.ArrayList;
import java.util.List;

public class ArrayAccessExpression extends Expression {

    public Token _identifier;
    public Expression[] _indices;
    public List<String> _diagnostics = new ArrayList<>();
    public Evaluator evaluator;

    public ArrayAccessExpression(Token identifierToken, List<Expression> indices, int lineNumber) {
        super(ExpressionType.ArrayAccessExpression, lineNumber);
        _identifier = identifierToken;
        _indices = indices.toArray(new Expression[0]);
        evaluator = new Evaluator(lineNumber);

    }

    public EvalResult evaluate(Environment env) throws Exception {
        EvalResult result = evaluator.EvaluateArrayAccessExpression(env, this);
        _diagnostics.addAll(evaluator.get_diagnostics());
        return result;
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

// Nice.. Time to start working on the ErrorHandlingAspectOFKrypton()