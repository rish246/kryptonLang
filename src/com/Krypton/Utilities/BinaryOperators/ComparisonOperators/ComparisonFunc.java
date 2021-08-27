package com.Krypton.Utilities.BinaryOperators.ComparisonOperators;

import com.Krypton.Utilities.CustomExceptions.BinaryOperators.InvalidOperationException;

public interface ComparisonFunc {
    boolean compare(Object l, Object r) throws InvalidOperationException;
}
