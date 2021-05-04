package com.Rishabh.Syntax.Statements;

import com.Rishabh.EvalResult;
import com.Rishabh.ExpressionType;
import com.Rishabh.Syntax.Statement;
import com.Rishabh.SyntaxTree;
import com.Rishabh.Token;
import com.Rishabh.Utilities.Environment;
import com.Rishabh.Utilities.Symbol;

import java.util.ArrayList;
import java.util.List;

public class ClassStatement extends Statement {
    public Token _name;
    public Environment _itsEnv;
    public List<SyntaxTree> _features;

    public List<String> _diagnostics = new ArrayList<>();

    public ClassStatement(Token name, SyntaxTree featureBlock, int lineNumber) {
        super(ExpressionType.ForLoopExpression, lineNumber);
        _name = name;
        _features = ((BlockStatement) featureBlock)._expressionList;
        _itsEnv = new Environment(null);
    }

    public EvalResult evaluate(Environment env) throws Exception {
        for(SyntaxTree newMethod : _features) {
            newMethod.evaluate(_itsEnv);
        }

        Symbol classEntry = new Symbol(_name._lexeme, _itsEnv, _name._lexeme);
        env.set(_name._lexeme, classEntry);

        return new EvalResult(null, "null");
    }

    public void prettyPrint(String indent) {
        System.out.println("Class " + _name._lexeme);

        System.out.print(indent + "|-");
        for(SyntaxTree feature : _features) {
            feature.prettyPrint(indent + "    ");
        }
    }

    public List<String> getDiagnostics() {
        return _diagnostics;
    }

}

// new Class();