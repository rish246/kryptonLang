package com.Krypton.Utilities;

import com.Krypton.EvalResult;

public class Typing {
    public static boolean isString(EvalResult result) {
        return isType(result, "string");
    }


    public static boolean isType(EvalResult result, String type) {
        return result.getType().equals(type);
    }

    public static boolean isBool(EvalResult result) {
        return isType(result, "boolean");
    }

    public static boolean isAnInt(EvalResult result) {
        return isType(result, "int");
    }

    public static boolean isFloatOrInt(EvalResult result) {
        return isFloat(result) || isAnInt(result);
    }

    public static boolean isList(EvalResult result) {
        return isType(result, "list");
    }

    public static boolean isFloat(EvalResult result) {
        return isType(result, "float");
    }

    public static boolean isBoolAndTrue(EvalResult result) {
        return result.equals(new EvalResult(true, "boolean"));
    }

    public static boolean isBoolAndFalse(EvalResult result) {
        return result.equals(new EvalResult(false, "boolean"));
    }


    public static Float parseFloat(Object o) {
        return Float.parseFloat(o.toString());
    }

}
