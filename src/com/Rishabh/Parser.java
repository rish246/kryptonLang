package com.Rishabh;

import java.util.ArrayList;
import java.util.List;

class Parser {
    private Lexer lexer;
    private List<Token> _tokens;
    private int _position;

    // Make diagnostics for parser as well
    List<String> _diagnostics = new ArrayList<String>();

    Parser(String text) {
        lexer = new Lexer(text);
        _tokens = new ArrayList<>();
        _position = 0;

        Token newToken = null;

        do {
            newToken = lexer.nextToken();
            _tokens.add(newToken);
        } while (newToken._type != TokenType.EndOfLineToken);

        // Append lexer diagnostics with parser diagnostics
        _diagnostics.addAll(lexer._diagnostics);
    }

    Expression parseTerm() {
        Expression left = parseFactor();

        // position == 0


        while (_tokens.get(_position + 1)._type == TokenType.AddToken ||
                _tokens.get(_position + 1)._type == TokenType.SubToken) {

            // pos = 1
            _position++;
            TokenType operatorToken = _tokens.get(_position)._type; // +
            ++_position; // 2
//            System.out.println();

            Expression right = parseFactor();

            left = new BinaryExpression(left, operatorToken, right);

            // Why is it not breaking right now
            if (_position >= _tokens.size() - 1)
                break;

        }


        return left;

    }

    public Expression parseFactor() {
        Expression left = parsePrimaryExp();

        // Check if left recieved was generated, if yes, then return the token

        if (_tokens.get(_position)._type == TokenType.EndOfLineToken)
            return left;

        while (_tokens.get(_position + 1)._type == TokenType.MultToken ||
                _tokens.get(_position + 1)._type == TokenType.DivToken) {
            ++_position;
            TokenType operatorToken = _tokens.get(_position)._type;


            ++_position; // 2
            Expression right = parsePrimaryExp();

            left = new BinaryExpression(left, operatorToken, right);

            // Why is it not breaking right now
            if (_position >= _tokens.size() - 1)
                break;

        }


        return left;
    }

// Tomorrow refactor the whole code
    // Think why 5 + is not working ... parser should be traced

    private Token match(TokenType type) {
        // If type matches the next token, then return the current token
        // else return newly generated token
        if (_position < _tokens.size() && _tokens.get(_position)._type == type)
            return _tokens.get(_position);

        _diagnostics.add("Expected " + type + ", Got : " + _tokens.get(_position)._type);

        return new Token(type, null, null);
    }


    // precedence fix is required --> yay boi

    private Expression parsePrimaryExp() {
//        System.out.println("In parsePrimearyExp method");
        if (_tokens.get(_position)._type == TokenType.OpenParensToken) {
            // return a tree for parens
            Token left = _tokens.get(_position);
            ++_position;
            Expression body = parseTerm();

            ++_position;// recursive decent
            Token right = match(TokenType.ClosedParensToken);


            System.out.println("Reached here");
            return new ParensExpression(left, body, right);
        }

        Token currentToken = match(TokenType.IntToken);

        int value = (currentToken._value == null) ? Integer.MIN_VALUE : (int) currentToken._value;

        return new NumberExpression(value); // creating nullPoint Exception
    }


    void printTokens() {
        for (Token token : _tokens) {
            System.out.println(token._type.toString() + "    " + token._lexeme);
        }
    }
}
