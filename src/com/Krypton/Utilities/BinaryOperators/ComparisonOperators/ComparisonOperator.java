package com.Krypton.Utilities.BinaryOperators.ComparisonOperators;

import com.Krypton.Utilities.BinaryOperators.ComparisonOperators.Utilities.ComparisonFunc;

import java.util.List;

public interface ComparisonOperator {
    static ComparisonFunc getComparisonFunc() { return null; }

    List<String> getDiagnostics();
}

// Contains methods that have to applied by ComparisonOperators Specifically