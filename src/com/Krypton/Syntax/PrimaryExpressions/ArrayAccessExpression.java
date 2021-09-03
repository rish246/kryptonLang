
package com.Krypton.Syntax.PrimaryExpressions;

import com.Krypton.EvalResult;
import com.Krypton.ExpressionType;
import com.Krypton.Syntax.Expression;
import com.Krypton.Token;
import com.Krypton.Utilities.ArrayAccessUtils.Extractor;
import com.Krypton.Utilities.CustomExceptions.BinaryOperators.InvalidOperationException;
import com.Krypton.Utilities.Environment;

import java.util.ArrayList;
import java.util.List;

public class ArrayAccessExpression extends Expression {

    public Token _identifier;
    public List<String> _diagnostics = new ArrayList<>();
    public Expression _index;
    public Expression _body;

    public Expression getIndex() {
        return _index;
    }

    public void setIndex(Expression _index) {
        this._index = _index;
    }

    public Expression getBody() {
        return _body;
    }

    public void setBody(Expression _body) {
        this._body = _body;
    }

    private Extractor extractor;

    public ArrayAccessExpression(Expression body, Expression index, int lineNumber) {
        super(ExpressionType.ArrayAccessExpression, lineNumber);
        _body = body;
        _index = index;
        _lineNumber = lineNumber;
    }

    public EvalResult evaluate(Environment env) throws Exception {
        try {
            EvalResult arrayBody = _body.evaluate(env);
            EvalResult indexVal = _index.evaluate(env);
            if (!isValidIndex(indexVal))
                throw new InvalidOperationException("Invalid index type " + indexVal.getType() + ", error at line number " + getLineNumber());

            extractor = Extractor.getRightExtractor(arrayBody.getType(), _lineNumber);
            return extractor.extract(arrayBody, indexVal);
        }
        catch (InvalidOperationException e) {
            _diagnostics.add(e.getMessage());
            throw e;
        }
        catch (Exception e) {
            _diagnostics.addAll(_body.getDiagnostics());
            _diagnostics.addAll(_index.getDiagnostics());
            _diagnostics.addAll(extractor.getDiagnostics());
            throw e;
        }
    }

    private boolean isValidIndex(EvalResult indexVal) {
        switch (indexVal.getType()) {
            case "string":
            case "int":
                return true;

                default:
                    return false;
        }
    }

    public void prettyPrint(String indent) {
        System.out.println("Array Access");
        System.out.print(indent + "|-");
        _body.prettyPrint(indent + "    ");
        System.out.println(indent + "|-");
        System.out.println(indent + "|");
        System.out.print(indent + "|_");
        _index.prettyPrint(indent + "    ");
    }

    public List<String> getDiagnostics() {
        return _diagnostics;
    }
}
