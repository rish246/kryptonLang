package com.Rishabh;

import java.util.ArrayList;
import java.util.List;

interface Condition {
    public boolean isOk(char curChar);
}

class Lexer {
    // Care about lexer errors
    String _line;
    int _position;
    int _lineNumber;

    List<String> _diagnostics;

    Lexer(String line) {
        _line = line;
        _position = 0;
        _diagnostics = new ArrayList<String>();
        _lineNumber = 1;
    }

    Token nextToken() {
        if (_line == null || _position >= _line.length())
            return new Token(TokenType.EndOfLineToken, null, null);
        // if current char is a digit
        int start = _position;

        if(currentChar() == '\n') {
            _lineNumber++;
            _position++;
            return nextToken();
        }

        // check if it is a comment
        if(currentChar() == '#') {
            while(_position < _line.length() && currentChar() != '\n') {
                _position++;
            }
            return nextToken();
            // return nextToken();
        }


        if(currentChar() == '"') {
            // This is going to be a Krypton String
            _position++;

            while(_position < _line.length() && currentChar() != '"') {
                _position++;
            }

            // If the position is equal to length of string
            // voila beros
            String inputStr = "";
            if(_position == _line.length()) {
                _diagnostics.add("Got EOL while scanning string literal");
            }
            else {
                inputStr = _line.substring(start + 1, _position);
                _position++;

            }

            return new Token(TokenType.StringConstToken, inputStr, inputStr);
        }

        // if isDigit .. go with IntToken
        if (Character.isDigit(currentChar()))
            return getIntToken(start);
            
        // if isLetter.. go for word
        if(Character.isLetter(currentChar()))
            return getWordToken(start);


        if (currentChar() == ' ') {
            while (currentChar() == ' ') {
                _position++;
            }
            return nextToken();
        }

        // Tokens
        switch (currentChar()) {
            case ',':
                _position++;
                return new Token(TokenType.CommaSeparatorToken, ",", null);
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
            case '%':
                _position++;
                return new Token(TokenType.ModuloToken, "%", null);
            case '(':
                ++_position;
                return new Token(TokenType.OpenParensToken, "(", null);
            case ')':
                ++_position;
                return new Token(TokenType.ClosedParensToken, ")", null);
            case '[':
                ++_position;
                return new Token(TokenType.OpenSquareBracketToken, "[", null);
            case ']':
                ++_position;
                return new Token(TokenType.ClosedSquareBracketToken, "]", null);
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
            case ':':
                _position += 1;
                return new Token(TokenType.ColonOperator, ":", null);
            case '!':
                if(_line.charAt(_position  + 1) == '=') {
                    _position += 2;
                    return new Token(TokenType.NotEqualsToken, ">=", null);
                }
                else {
                    ++_position;
                    return new Token(TokenType.LogicalNotToken, "!", null);
                }

            case '{':
                ++_position;
                return new Token(TokenType.OpenBracketToken, "{", null);

            case '}':
                ++_position;

                return new Token(TokenType.ClosedBracket, "}", null);

            case ';':
                ++_position;

                return new Token(TokenType.SemiColonToken, ";", null);

        }

        // Add an error here
        _diagnostics.add("Invalid token : " + _line.charAt(_position) + " at line number "  + _lineNumber);

        return new Token(TokenType.ErrorToken, "" + _line.charAt(_position++), null);

    }

    private char currentChar() {
        return _line.charAt(_position);
    }

    private Token getWordToken(int start) {
        Condition isWordCondition = (letter -> Character.isLetterOrDigit(letter) || letter == '_');
        String lexeme = getToken(start, isWordCondition);

        // If keyword
        switch (lexeme) {
            case "true":
            case "false":
                return new Token(TokenType.BoolTokenKeyword, lexeme, Boolean.parseBoolean(lexeme));
            case "type":
                return new Token(TokenType.TypeToken, lexeme, null);
            case "if":
                return new Token(TokenType.IfKeywordToken, "if", null);
            case "else":
                return new Token(TokenType.ElseKeywordToken, "else", null);
            case "while":
                return new Token(TokenType.WhileKeywordToken, "while", null);
            case "print":
                return new Token(TokenType.PrintExpToken, "print", null);
            case "for":
                return new Token(TokenType.ForKeywordToken, "for", null);
            case "def":
                return new Token(TokenType.FunctionDefineToken, "def", null);
            case "return":
                return new Token(TokenType.ReturnToken, "return", null);
            case "lambda":
                return new Token(TokenType.LambdaExpressionToken, "lambda", null);
            case "null":
                return new Token(TokenType.NullValueToken, "null", null);
            case "len":
                return new Token(TokenType.LengthToken, "len", null);
            default:
                return new Token(TokenType.IdentifierToken, lexeme, lexeme);
        }
    }

    private Token getIntToken (int start) {
        Condition isDigitCondition = (Character::isDigit);
        String lexeme = getToken(start, isDigitCondition);
        return new Token(TokenType.IntToken, lexeme, Integer.parseInt(lexeme));
    }

    private String getToken(int start, Condition cond) {
        while ((_position < _line.length()) && cond.isOk(_line.charAt(_position )))
            _position++;

        // Create a new Lexeme
        return _line.substring(start, _position);
    }


}
