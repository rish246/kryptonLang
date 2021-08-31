package com.Krypton.Utilities.CustomExceptions;

public class IndexOutOfBoundException extends Exception {
    public IndexOutOfBoundException(int index, int listSize, int lineNumber) {
        super("IndexOutOfBoundException: index " + index + " is too large for an array of size " + listSize + ", error at line number " + lineNumber);
    }
}
