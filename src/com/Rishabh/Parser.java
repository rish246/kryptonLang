package com.Rishabh;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

class Parser {
    private Lexer lexer;
    private Token[] _tokens;
    private int _position;

    // Make diagnostics for parser as well
    List<String> _diagnostics = new ArrayList<String>();

    Parser(String text) {
        lexer = new Lexer(text);
        _position = 0;

        _tokens = tokenizeLine();

        System.out.println(_tokens);

        // Append lexer diagnostics with parser diagnostics
        _diagnostics.addAll(lexer._diagnostics);
    }


    void printTokens() {
        for (Token token : _tokens) {
            System.out.println(token._type.toString() + "    " + token._lexeme);
        }
    }

    private Token CurrentToken() {
        return _tokens[_position];
    }

    private Token Peek(int places) {
        int destIdx = _position + places;
        if(destIdx >= _tokens.length)
            return _tokens[_tokens.length - 1];

        return _tokens[destIdx];
    }

    private Token NextToken() {
        return _tokens[++_position];
    }


    Expression parseTerm() {
        Expression left = parseFactor(); // Why did the position change during this


        while (Peek(1)._type == TokenType.AddToken ||
                Peek(1)._type == TokenType.SubToken) {

            TokenType operatorToken = NextToken()._type; // _position == 1

            ++_position;
            Expression right = parseFactor();

            left = new BinaryExpression(left, operatorToken, right);

            // Why is it not breaking right now
            if (_position >= _tokens.length - 1)
                break;

        }


        return left;

    }

    public Expression parseFactor() {
        Expression left = parsePrimaryExp();

        // Check if left recieved was generated, if yes, then return the token
        if (CurrentToken()._type == TokenType.EndOfLineToken)
            return left;



        while (Peek(1)._type == TokenType.MultToken ||
                Peek(1)._type == TokenType.DivToken) {

            TokenType operatorToken = NextToken()._type;

            ++_position;
            Expression right = parsePrimaryExp();

            left = new BinaryExpression(left, operatorToken, right);

            // Why is it not breaking right now
            if (_position >= _tokens.length - 1)
                break;

        }


        return left;
    }

// Tomorrow refactor the whole code
    // Think why 5 + is not working ... parser should be traced

    private Token[] tokenizeLine() {
        Token newToken;

        List<Token> listTokens = new ArrayList<>();

        do {
            newToken = lexer.nextToken();
            listTokens.add(newToken);
        } while (newToken._type != TokenType.EndOfLineToken);

        Token[] result = new Token[listTokens.size()];
        result = listTokens.toArray(result);

        return result;

    }

    private Token match(TokenType type) {
        // If type matches the next token, then return the current token
        // else return newly generated token
        if (_position < _tokens.length && CurrentToken()._type == type)
            return CurrentToken();

        _diagnostics.add("Expected " + type + ", Got : " + CurrentToken()._type);

        return new Token(type, null, null);
    }



    // precedence fix is required --> yay boi

    private Expression parsePrimaryExp() {
//        System.out.println("In parsePrimearyExp method");
        if (CurrentToken()._type == TokenType.OpenParensToken) {
            // return a tree for parens
            Token left = CurrentToken(); // Next Token -->
            ++_position;
            Expression body = parseTerm();

          ++_position;// recursive decent
            Token right = match(TokenType.ClosedParensToken);

            return new ParensExpression(left, body, right);
        }

        Token currentToken = match(TokenType.IntToken); // it does

        int value = (currentToken._value == null) ? Integer.MIN_VALUE : (int) currentToken._value;

        return new NumberExpression(value); // creating nullPoint Exception
    }



}

// replace get with a[i]
