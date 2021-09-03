package com.Krypton.Utilities.ArrayAccessUtils;

import com.Krypton.EvalResult;
import com.Krypton.Utilities.CustomExceptions.IndexOutOfBoundException;

import java.util.ArrayList;
import java.util.List;

public class ListExtractor implements Extractor {
    private int _lineNumber;
    private List<String> _diagnostics = new ArrayList<>();

    public ListExtractor(int lineNumber) {
        _lineNumber = lineNumber;
    }
    public List<String> getDiagnostics() {
        return _diagnostics;
    }

    public EvalResult extract(EvalResult list, EvalResult index) throws Exception {
        List<EvalResult> array = (List) list.getValue();
        try {
            return extractValueFromList(array, index);
        } catch (Exception e) {
            _diagnostics.add(e.getMessage());
            throw e;
        }
    }

    private EvalResult extractValueFromList(List<EvalResult> array, EvalResult index) throws IndexOutOfBoundException {
       return getIthValueFromList(array, index);
    }

    private EvalResult getIthValueFromList(List<EvalResult> array, EvalResult index) throws IndexOutOfBoundException {
        int i = (int) index.getValue();
        if (i < 0) i = array.size() + i;

        if (i >= array.size() || i < 0)
            throw new IndexOutOfBoundException(i, array.size(), _lineNumber);

        return array.get(i);
    }
}
