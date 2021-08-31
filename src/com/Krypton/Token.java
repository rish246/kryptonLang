package com.Krypton;

public class Token {
    public TokenType _type;
    public String _lexeme;
    public Object _value;

    public Token(TokenType type, String lexeme, Object value) {
        _type = type;
        _lexeme = lexeme;
        _value = value;
    }

    public TokenType getType() {
        return _type;
    }
}
