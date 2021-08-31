package com.Krypton.Utilities.ArrayAccessUtils;

import com.Krypton.EvalResult;
import com.Krypton.Utilities.CustomExceptions.BinaryOperators.InvalidOperationException;
import com.Krypton.Utilities.CustomExceptions.IndexOutOfBoundException;

import java.util.ArrayList;
import java.util.List;

public class StringExtractor implements Extractor {
    private int _lineNumber;
    private List<String> _diagnostics = new ArrayList<>();
    public StringExtractor(int lineNumber) {
        _lineNumber = lineNumber;
    }

    @Override
    public List<String> getDiagnostics() {
        return _diagnostics;
    }

    @Override
    public EvalResult extract(EvalResult string, List<EvalResult> indices) throws Exception {
        if (indices.size() == 0)
            throw new InvalidOperationException("InvalidOperation: indices can't be an empty string, error at line number " + _lineNumber);

        String stringBody = (String) string.getValue();
        try {
            return extractValuesFromIndices(stringBody, indices);
        } catch (Exception e) {
            _diagnostics.add(e.getMessage());
            throw e;
        }
    }

//
    private EvalResult extractValuesFromIndices(String string, List<EvalResult> indices) throws IndexOutOfBoundException {
        List<EvalResult> result = new ArrayList<>();

        for(EvalResult i : indices)
            result.add(getIthValueFromString(string, i));

        if (result.size() == 1)
            return result.get(0);

        return new EvalResult(result, "list");
    }

    private EvalResult getIthValueFromString(String string, EvalResult index) throws IndexOutOfBoundException {
        int i = (int) index.getValue();
        if (i < 0) i = string.length() + i;

        if (i >= string.length() || i < 0)
            throw new IndexOutOfBoundException(i, string.length(), _lineNumber);

        return new EvalResult("" + string.charAt(i), "string");
    }
}
