package com.Rishabh;

import java.util.ArrayList;
import java.util.List;

class Lexer {
    // Care about lexer errors
    String _line;
    int _position;

    List<String> _diagnostics;

    Lexer(String line) {
        _line = line;
        _position = 0;
        _diagnostics = new ArrayList<String>();
    }

    Token nextToken() {
        if (_line == null || _position >= _line.length())
            return new Token(TokenType.EndOfLineToken, null, null);
        // if current char is a digit
        int start = _position;
        if (Character.isDigit(_line.charAt(_position))) {
            while ((_position < _line.length()) && Character.isDigit(_line.charAt(_position)))
                _position++;

            // Create a new Lexeme
            String lexeme = _line.substring(start, _position);
            return new Token(TokenType.IntToken, lexeme, Integer.parseInt(lexeme));

        }

        // Reject spaces
        // GIving bugs... fix this error now
        if (_line.charAt(_position) == ' ') {
//            System.out.println("Found space");
            while (_line.charAt(_position) == ' ') {
                _position++;
            }

            return nextToken();
        }

        if (_line.charAt(_position) == '+') {
            _position++;
            return new Token(TokenType.AddToken, "+", null);
        } else if (_line.charAt(_position) == '-') {
            _position++;
            return new Token(TokenType.SubToken, "-", null);
        } else if (_line.charAt(_position) == '*') {
            _position++;
            return new Token(TokenType.MultToken, "*", null);
        } else if (_line.charAt(_position) == '/') {
            _position++;
            return new Token(TokenType.DivToken, "/", null);
        } else if (_line.charAt(_position) == '(') {
            ++_position;
            return new Token(TokenType.OpenParensToken, "(", null);
        } else if (_line.charAt(_position) == ')') {
            ++_position;
            return new Token(TokenType.ClosedParensToken, ")", null);

        }


        // Add an error here
        _diagnostics.add("Invalid token : " + _line.charAt(_position));

        return new Token(TokenType.ErrorToken, "" + _line.charAt(_position++), null);

    }


}
