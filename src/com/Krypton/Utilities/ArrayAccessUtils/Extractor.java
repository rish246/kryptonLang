package com.Krypton.Utilities.ArrayAccessUtils;

import com.Krypton.EvalResult;

import java.util.List;

public interface Extractor {
    static Extractor getRightExtractor(String operandType, int lineNumber) throws Exception {
        if (operandType.equals("list"))
            return new ListExtractor(lineNumber);

        if (operandType.equals("object"))
            return new ObjectExtractor(lineNumber);

        if (operandType.equals("string"))
            return new StringExtractor(lineNumber);

        throw new Exception("Expression of type " + operandType + " is not extractable, error at line number " + lineNumber);
    }
    List<String> getDiagnostics();
    EvalResult extract(EvalResult extractable, EvalResult index) throws Exception;
}
