package com.Krypton.Utilities.ArrayAccessUtils;

import com.Krypton.EvalResult;
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
    public EvalResult extract(EvalResult string, EvalResult index) throws Exception {
        String stringBody = (String) string.getValue();
        try {
            return extractValuesFromIndices(stringBody, index);
        } catch (Exception e) {
            _diagnostics.add(e.getMessage());
            throw e;
        }
    }

//
    private EvalResult extractValuesFromIndices(String string, EvalResult index) throws IndexOutOfBoundException {
        return getIthValueFromString(string, index);
    }

    private EvalResult getIthValueFromString(String string, EvalResult index) throws IndexOutOfBoundException {
        int i = (int) index.getValue();
        if (i < 0) i = string.length() + i;

        if (i >= string.length() || i < 0)
            throw new IndexOutOfBoundException(i, string.length(), _lineNumber);

        return new EvalResult("" + string.charAt(i), "string");
    }
}
