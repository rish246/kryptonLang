package com.Krypton.Utilities.BinaryOperators.AssignmentOperators;

import com.Krypton.EvalResult;
import com.Krypton.Syntax.Expression;
import com.Krypton.Syntax.PrimaryExpressions.BinaryExpression;
import com.Krypton.Utilities.BinaryOperators.BinaryOperator;
import com.Krypton.Utilities.Environment;

import java.util.ArrayList;
import java.util.List;

public class SimpleAssignmentOperator implements BinaryOperator {

    private Expression _left;
    private Expression _right;
    private int _lineNumber;
    private List<String> _diagnostics = new ArrayList<>();
    Binder _binder;

    @Override
    public EvalResult operateOn(BinaryExpression binExp, Environment env) throws Exception {
        _left = binExp.getLeft();
        _right = binExp.getRight();
        _lineNumber = binExp.getLineNumber();
        _binder = new Binder(_lineNumber);

        try {
            return _binder.bind(_left, _right, env);
        } catch (Exception e) {
            _diagnostics.addAll(_left.getDiagnostics());
            _diagnostics.addAll(_right.getDiagnostics());
            _diagnostics.add(e.getMessage());
            throw e;
        }
    }

    @Override
    public List<String> getDiagnostics() {
        return _diagnostics;
    }
}
