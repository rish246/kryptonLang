package com.Krypton;

import java.util.Objects;

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
            if (oE.getValue() == null && Objects.equals(oE.getType(), "null") )
                return (this.getValue() == null && this.getType().equals("null"));
            return oE.getValue().equals(this.getValue()) && oE.getType().equals(this.getType());
        }

        return false;
    }

    @Override
    public String toString() {
        if (_value == null)
            return "null";
        return _value.toString();
    }
}
