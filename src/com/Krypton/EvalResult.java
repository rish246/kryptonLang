package com.Krypton;

public class EvalResult {
    public Object getValue() {
        return _value;
    }

    public String getType() {
        return _type;
    }

    public Object _value;
    public String _type;

    public EvalResult(Object value, String type) {
        _value = value;
        _type = type;
    }

}
