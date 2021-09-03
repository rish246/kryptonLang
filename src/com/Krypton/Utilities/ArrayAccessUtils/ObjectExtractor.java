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

    public EvalResult extract(EvalResult object, EvalResult index) throws Exception {
        try {
            return extractValueFromObject(object, index);
        } catch (Exception e) {
            _diagnostics.add(e.getMessage());
            throw e;
        }
    }

    private EvalResult extractValueFromObject(EvalResult objectBody, EvalResult index) throws InvalidOperationException {
        Map<String, EvalResult> objectMap = (HashMap) objectBody.getValue();
        return extractValue(objectMap, index);
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
