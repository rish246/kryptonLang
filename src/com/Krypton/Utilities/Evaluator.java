package com.Krypton.Utilities;

import java.util.List;

public class Evaluator {
    private int lineNumber;
    private List<String> _diagnostics;

    public Evaluator(int lineNumber, List<String> _diagnostics) {
        this.lineNumber = lineNumber;
        this._diagnostics = _diagnostics;
    }


    // Lets implement the main functionality here... It will just evaluate Basic Expressions






    public int getLineNumber() {
        return lineNumber;
    }

    public List<String> get_diagnostics() {
        return _diagnostics;
    }
}
