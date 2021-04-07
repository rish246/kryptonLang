package com.Rishabh;

public class Token {
    public TokenType _type;
    public String _lexeme;
    public Object _value;

    Token(TokenType type, String lexeme, Object value) {
        _type = type;
        _lexeme = lexeme;
        _value = value;
    }
}
