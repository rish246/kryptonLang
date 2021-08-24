package com.Krypton.Utilities.BinaryOperators;

import com.Krypton.EvalResult;

import java.util.ArrayList;
import java.util.List;

public class Operator {
    private final List<String> _diagnostics;

    public Operator() {
        _diagnostics = new ArrayList<>();
    }

    public List<String> getDiagnostics() {
        return _diagnostics;
    }

    public void addDiagnostics(List<String> moreDiagnostics) {
        _diagnostics.addAll(moreDiagnostics);
    }

    public boolean isString(EvalResult result) {
        return isType(result, "string");
    }

    public List<EvalResult> copyList(List<EvalResult> list) {
        List<EvalResult> result = new ArrayList<>();
        for(EvalResult exp : list) {
            result.add(new EvalResult(exp.getValue(), exp.getType()));
        }
        return result;
    }

    public boolean isType(EvalResult result, String type) {
        return result.getType().equals(type);
    }

    public boolean isAnInt(EvalResult result) {
        return isType(result, "int");
    }

    public boolean isFloatOrInt(EvalResult result) {
        return isFloat(result) || isAnInt(result);
    }

    public boolean isList(EvalResult result) {
        return isType(result, "list");
    }

    public boolean isFloat(EvalResult result) {
        return isType(result, "float");
    }
}
