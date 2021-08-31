package com.Krypton.Utilities.ArrayAccessUtils;

import com.Krypton.EvalResult;
import com.Krypton.Utilities.CustomExceptions.BinaryOperators.InvalidOperationException;
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

    public EvalResult extract(EvalResult list, List<EvalResult> indices) throws Exception {
        List<EvalResult> array = (List) list.getValue();

        if (indices.size() == 0)
            throw new InvalidOperationException("InvalidOperation: indices can't be an empty list, error at line number " + _lineNumber);

        try {
            return extractValuesFromIndices(array, indices);
        } catch (Exception e) {
            _diagnostics.add(e.getMessage());
            throw e;
        }
    }

    private EvalResult extractValuesFromIndices(List<EvalResult> array, List<EvalResult> indices) throws IndexOutOfBoundException {
        List<EvalResult> result = new ArrayList<>();

        for(EvalResult i : indices)
            result.add(getIthValueFromList(array, i));

        if (result.size() == 1)
            return result.get(0);

        return new EvalResult(result, "list");
    }

    private EvalResult getIthValueFromList(List<EvalResult> array, EvalResult index) throws IndexOutOfBoundException {
        int i = (int) index.getValue();
        if (i < 0) i = array.size() + i;

        if (i >= array.size() || i < 0)
            throw new IndexOutOfBoundException(i, array.size(), _lineNumber);

        return array.get(i);
    }
}
