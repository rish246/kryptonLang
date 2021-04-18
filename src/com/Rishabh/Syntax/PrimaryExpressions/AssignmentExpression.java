package com.Rishabh.Syntax.PrimaryExpressions;

import com.Rishabh.EvalResult;
import com.Rishabh.ExpressionType;
import com.Rishabh.Syntax.Expression;
import com.Rishabh.SyntaxTree;
import com.Rishabh.TokenType;
import com.Rishabh.Utilities.Environment;
import com.Rishabh.Utilities.Symbol;

import java.util.ArrayList;
import java.util.List;

public class AssignmentExpression extends Expression {
    SyntaxTree _left;
    TokenType _operatorToken; // Assignment Token Always
    SyntaxTree _right;
    //    ExpressionType _type;
    List<String> _diagnostics = new ArrayList<>();

    public AssignmentExpression(SyntaxTree left, TokenType operatorToken, SyntaxTree right) {
        super(ExpressionType.AssignmentExpression);
        _left = left;
        _operatorToken = operatorToken;
        _right = right;
    }

    @Override
    public void prettyPrint(String indent) {

        System.out.println(_operatorToken);
        System.out.println(indent + "|");
        System.out.print(indent + "├──");
//        _left.prettyPrint(indent + "    ");
        // System.out.println(_left. + indent);
        _left.prettyPrint(indent + "    ");
        // Add some long lines here
        System.out.println(indent + "|");

        System.out.print(indent + "└──");
        _right.prettyPrint(indent + "    ");

    }

    @Override
    public List<String> getDiagnostics() {
        return _diagnostics;
    }


    public EvalResult evaluate(Environment env) throws Exception {


        EvalResult rightRes = _right.evaluate(env);
        _diagnostics.addAll(_right.getDiagnostics());

        if(rightRes == null)
            return null;

        // get the errors in left expression as well


        String rightType = rightRes._type;
        Object rightValue = rightRes._value;

        // left's lexeme
        if(_left._type == ExpressionType.IdentifierExpression) {
            IdentifierExpression left = (IdentifierExpression) _left;
            Symbol res = env.set(left._lexeme, new Symbol(left._lexeme, rightValue, rightType));
            return new EvalResult(res._value, res._type);
        }

        if(_left._type == ExpressionType.ArrayAccessExpression) {

            com.Rishabh.Expression.PrimaryExpressions.ArrayAccessExpression curExp = (com.Rishabh.Expression.PrimaryExpressions.ArrayAccessExpression) _left;

            Symbol ourListEntry = env.get(curExp._identifier._lexeme);
            if(ourListEntry == null) {
                _diagnostics.add("Invalid identifier " + curExp._identifier._lexeme);
                return null;
            }

            EvalResult finalResult = new EvalResult((List) ourListEntry._value, ourListEntry._type);

            for(SyntaxTree index : curExp._indices) {
                if(finalResult._type != "list") {
                    _diagnostics.add("Types " + finalResult._type + " are not subscriptable");
                    return null;
                }

                EvalResult curIdx = index.evaluate(env);
                String curIdxType = curIdx._type;
                if(!curIdxType.equals("int")) {
                    _diagnostics.add("Indices must be integers only.. Found " + curIdxType);
                    return null;
                }

                int curIdxVal = (int) curIdx._value;

                List<EvalResult> curList = (List) finalResult._value;

                if(curIdxVal >= curList.size()) {
                    _diagnostics.add("Length " + curIdxVal + " out of bound for array " + curExp._identifier._lexeme);
                    return null;
                }

                finalResult = curList.get(curIdxVal);
            }

            // finalResult -> pointing at value to be changed
            finalResult._value = rightRes._value;
            finalResult._type = rightRes._type;

            return finalResult;
        }

        else {
            _diagnostics.add("Expression of type " + _left.getType() + " is not subsciptable");
        }

        return null;

//        return new EvalResult(res._value, res._type);

    }

}
