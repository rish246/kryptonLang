package com.Rishabh;

public class Token {
    TokenType _type;
    String _lexeme;
    Object _value;

    Token(TokenType type, String lexeme, Object value) {
        _type = type;
        _lexeme = lexeme;
        _value = value;
    }
}
