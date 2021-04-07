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


        // If it is a letter --> return new LetterToken
        if(Character.isLetter(_line.charAt(_position))) {
            while ((_position < _line.length()) && Character.isLetter(_line.charAt(_position)))
                _position++;

            // Create a new Lexeme
            String lexeme = _line.substring(start, _position);

            // If keyword
            switch (lexeme) {
                case "true":
                case "false":
                    return new Token(TokenType.BoolTokenKeyword, lexeme, Boolean.parseBoolean(lexeme));
                default:
                    return new Token(TokenType.IdentifierToken, lexeme, lexeme);
            }
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

        // Tokens
        switch (_line.charAt(_position)) {
            case '+':
                _position++;
                return new Token(TokenType.AddToken, "+", null);
            case '-':
                _position++;
                return new Token(TokenType.SubToken, "-", null);
            case '*':
                _position++;
                return new Token(TokenType.MultToken, "*", null);
            case '/':
                _position++;
                return new Token(TokenType.DivToken, "/", null);
            case '(':
                ++_position;
                return new Token(TokenType.OpenParensToken, "(", null);
            case ')':
                ++_position;
                return new Token(TokenType.ClosedParensToken, ")", null);

            case '|':
                if (_line.charAt(_position + 1) == '|') {
                    _position += 2;
                    return new Token(TokenType.LogicalOrToken, "||", null);
                }
                break;
            case '&':
                if(_line.charAt(_position + 1) == '&') {
                    _position += 2;
                    return new Token(TokenType.LogicalAndToken, "&&", null);
                }

                break;
            case '=':
                if(_line.charAt(_position  + 1) == '=') {
                    _position += 2;
                    return new Token(TokenType.EqualityToken, "==", null);
                }
                else {
                    _position += 1;
                    return new Token(TokenType.AssignmentToken, "=", null);
                }
            case '<':
                if(_line.charAt(_position  + 1) == '=') {
                    _position += 2;
                    return new Token(TokenType.LessThanEqualToken, "<=", null);
                }
                else {
                    _position += 1;
                    return new Token(TokenType.LessThanToken, "<", null);
                }

            case '>':
                if(_line.charAt(_position  + 1) == '=') {
                    _position += 2;
                    return new Token(TokenType.GreaterThanEqualToken, ">=", null);
                }
                else {
                    _position += 1;
                    return new Token(TokenType.GreaterThanToken, ">", null);
                }
            case '!':
                if(_line.charAt(_position  + 1) == '=') {
                    _position += 2;
                    return new Token(TokenType.NotEqualsToken, ">=", null);
                }
                else {
                    ++_position;
                    return new Token(TokenType.LogicalNotToken, "!", null);
                }



        }


        // Add an error here
        _diagnostics.add("Invalid token : " + _line.charAt(_position));

        return new Token(TokenType.ErrorToken, "" + _line.charAt(_position++), null);

    }


}
