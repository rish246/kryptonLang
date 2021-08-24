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

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;

        if (o instanceof EvalResult) {
            EvalResult oE = (EvalResult) o;
            return oE.getValue().equals(this.getValue()) && oE.getType().equals(this.getType());
        }

        return false;
    }

}
