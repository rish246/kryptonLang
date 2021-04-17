package com.Rishabh.Expression.PrimaryExpressions;

import com.Rishabh.EvalResult;
import com.Rishabh.ExpressionType;
import com.Rishabh.Token;
import com.Rishabh.TokenType;
import com.Rishabh.Expression.Expression;
import com.Rishabh.Utilities.Environment;
import com.Rishabh.Utilities.Symbol;
import com.Rishabh.Expression.Values.ListExpression;
import java.util.ArrayList;
import java.util.List;

public class AssignmentExpression extends Expression {
    Expression _left;
    TokenType _operatorToken; // Assignment Token Always
    Expression _right;
    //    ExpressionType _type;
    List<String> _diagnostics = new ArrayList<>();

    public AssignmentExpression(Expression left, TokenType operatorToken, Expression right) {
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

            ArrayAccessExpression left = (ArrayAccessExpression) _left;
            String envEntry = left._identifier._lexeme;

            ListExpression index = left._index;

            List<EvalResult> indices = (List) index.evaluate(env)._value;


            EvalResult firstIdx = indices.get(0);



            if(firstIdx._type != "int") {
                _diagnostics.add("Expected an int for index.. got a " + firstIdx._type);
                return null;

            }



            int firstIdxVal = (int) firstIdx._value;


            Symbol envList = env.get(envEntry);
            if(envList == null) {
                _diagnostics.add("Identifier " + envEntry + " is undefined");
                return null;
            }

            List<EvalResult> originalList = (List) envList._value;

            EvalResult curList = originalList.get(firstIdxVal); //


            for(int i=1; i < indices.size() - 1; i++) {
                if(curList._type != "list") {
                    _diagnostics.add("Dimension mismatch in the array access expression");
                    return null;
                }

                EvalResult idx = indices.get(i);

                String idxType = idx._type;
                if(idxType != "int") {
                    _diagnostics.add("Index must evaluate to an integer.. found " + idxType);
                    return null;
                }

                int idxVal = (int) idx._value;

                List<EvalResult> nextList = (List) curList._value;

                if(idxVal >= nextList.size()) {
                    _diagnostics.add("Index " + idxVal + " too large for array of size " + nextList.size());
                    return null;
                }

                curList = nextList.get(idxVal);

            }

            int finalIndex = 0;
            if(indices.size() > 1) {
                List<EvalResult> finalRes = (List) curList._value;
                EvalResult lastIdx = indices.get(indices.size() - 1);
                if(lastIdx._type != "int") {
                    _diagnostics.add("Expected an int for index.. got a " + curList._type);
                    return null;
                }
                finalIndex = (int) lastIdx._value;

                if(finalIndex >= finalRes.size()) {
                    _diagnostics.add("Index " + finalIndex + " to large for array of size " + finalRes.size());
                    return null;
                }

                finalRes.set(finalIndex, rightRes);
            }
            else {


                finalIndex = (int) curList._value;
                originalList.set(finalIndex, rightRes);
            }

            envList._value = originalList;
////
            env.set(envEntry, envList);
            return rightRes; // Can't return list... have to return the value updated
        }

        else {
            _diagnostics.add("Expression of type " + _left.getType() + " is not subsciptable");
        }

        return null;

//        return new EvalResult(res._value, res._type);

    }

}
