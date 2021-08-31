package com.Krypton.Utilities.ArrayAccessUtils;

import com.Krypton.EvalResult;
import com.Krypton.Utilities.CustomExceptions.BinaryOperators.InvalidOperationException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ObjectExtractor implements Extractor {
    private int _lineNumber;
    private List<String> _diagnostics = new ArrayList<>();

    public ObjectExtractor(int lineNumber) {
        _lineNumber = lineNumber;
    }
    public List<String> getDiagnostics() {
        return _diagnostics;
    }

    public EvalResult extract(EvalResult object, List<EvalResult> indices) throws Exception {

        if (indices.size() == 0)
            throw new InvalidOperationException("InvalidOperation: indices can't be an empty list, error at line number " + _lineNumber);

        try {
            return extractValueFromObject(object, indices);
        } catch (Exception e) {
            _diagnostics.add(e.getMessage());
            throw e;
        }
    }

    private EvalResult extractValueFromObject(EvalResult objectBody, List<EvalResult> indices) throws InvalidOperationException {
        Map<String, EvalResult> objectMap = (HashMap) objectBody.getValue();

        if ( indices.size() == 1 ) {
            EvalResult key = indices.get(0);
            return extractValue(objectMap, key);
        }

        var result = new ArrayList<EvalResult>();
        for (EvalResult index : indices)
            result.add(extractValue(objectMap, index));
        return new EvalResult(result, "list");
    }

    private EvalResult extractValue(Map<String, EvalResult> objectBody, EvalResult key) throws InvalidOperationException {
        if ( !isHashable(key) )
            throw new InvalidOperationException("Key of type " + key.getType() + " is not hashable, error at line number " + _lineNumber);

        String keyStr = key.toString();
        EvalResult entry = objectBody.get(keyStr);  // it was a ref to the original object
        if (entry == null)
            objectBody.put(keyStr, new EvalResult(null, "null"));

        return objectBody.get(keyStr);  // it was a ref to the original object
    }

    private boolean isHashable(EvalResult key) {
        return key.getType().equals("int")
                || key.getType().equals("string")
                || key.getType().equals("float")
                || key.getType().equals("boolean");
    }
}
