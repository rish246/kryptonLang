package com.Krypton.Syntax.Statements;

import com.Krypton.EvalResult;
import com.Krypton.ExpressionType;
import com.Krypton.Syntax.Statement;
import com.Krypton.SyntaxTree;
import com.Krypton.Token;
import com.Krypton.Utilities.Environment;

import java.util.ArrayList;
import java.util.List;

public class ClassStatement extends Statement {
    public Token _name;
    public Environment _itsEnv;
    public Token _parentClass;
    public List<SyntaxTree> _features;
    private boolean isChildClass;

    public List<String> _diagnostics = new ArrayList<>();

    public ClassStatement(Token name, Token parentClass,SyntaxTree featureBlock, int lineNumber) {
        super(ExpressionType.ClassStatement, lineNumber);
        _name = name;
        _features = ((BlockStatement) featureBlock)._expressionList;
        _parentClass = parentClass;
        isChildClass = _parentClass != null;
        _itsEnv = new Environment(null);
    }

    public EvalResult evaluate(Environment env) throws Exception {

        Environment parentEnvironment = env;
        if(_parentClass != null) {
            String parentClassName = _parentClass._lexeme;
            EvalResult parentClassEntry = env.get(parentClassName);
            if(parentClassEntry == null) {
                _diagnostics.add("Undefined variable " + parentClassName + " at line number " + getLineNumber());
                return null;
            }


            parentEnvironment = (Environment) parentClassEntry.getValue();
        }
        

        _itsEnv._ParentEnv = parentEnvironment;

        for(SyntaxTree newFeature : _features) {
            // features must be of the type... functionStatement or Assignment expression
            if(newFeature.getType() != ExpressionType.FuncExpression 
            && newFeature.getType() != ExpressionType.AssignmentExpression) {
                _diagnostics.add("Invalid Feature type " + newFeature.getType() + " at line number " + getLineNumber());
                return null;
            }

            newFeature.evaluate(_itsEnv);
        }

        _itsEnv.set("isChild", new EvalResult(isChildClass, "boolean"));

        _itsEnv.set("__ClassName__", new EvalResult(_name._lexeme, "string"));
        _itsEnv.set("ParentClass", null);
        if(_parentClass != null) {
            _itsEnv.set("ParentClass", new EvalResult(parentEnvironment, _parentClass._lexeme));
        }

        EvalResult classEntry = new EvalResult(_itsEnv, _name._lexeme);
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

// bind super here ->.. Then what super -> super -> ( parentEnv ) 
// super.Inforence() -> 