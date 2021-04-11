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

        // if isDigit .. go with IntToken
        if (Character.isDigit(currentChar()))
            return getIntToken(start);
            
        // if isLetter.. go for word
        if(Character.isLetter(currentChar()))
            return getWordToken(start);


        if(currentChar() == '"') {
            // This is going to be a Krypton String
            _position++;

            while(currentChar() != '"') {
                _position++;
            }

            String inputStr = _line.substring(start + 1, _position);
            _position++;

            return new Token(TokenType.StringConstToken, inputStr, inputStr);
        }


        // Reject spaces
        // GIving bugs... fix this error now
        if (currentChar() == ' ') {
//            System.out.println("Found space");
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

            case '#': {
                while(_line.charAt(_position  + 1) != '#')
                    _position += 1;
                _position += 2;
                return nextToken();
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
        _diagnostics.add("Invalid token : " + _line.charAt(_position) + " at location " + _position);

        return new Token(TokenType.ErrorToken, "" + _line.charAt(_position++), null);

    }

    private char currentChar() {
        return _line.charAt(_position);
    }

    private Token getWordToken(int start) {
        Condition isLetterCondition = (Character::isLetter);
        String lexeme = getToken(start, isLetterCondition);

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


// Multiple statements ---> if the statement is not complete, We are not waiting for a second prompt
// (calcParens... if Parens are not balanced... go for another prompt
// Else submit