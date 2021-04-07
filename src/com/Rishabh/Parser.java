package com.Rishabh;

import com.Rishabh.Expression.*;

import java.util.ArrayList;
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
        Token currentToken = CurrentToken();
        _position++;
        return currentToken;
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
            return NextToken();


        _diagnostics.add("Expected " + type + ", Got : " + CurrentToken()._type);

        return new Token(type, null, null);
    }


    public int getBinaryOperatorPrecedence(TokenType binOperator) {
        switch(binOperator) {
            case MultToken:
            case DivToken:
                return 6;

            case AddToken:
            case SubToken:
                return 5;

            case LessThanEqualToken:
            case LessThanToken:
            case GreaterThanEqualToken:
            case GreaterThanToken:
                return 4;

            case EqualityToken:
            case NotEqualsToken:
                return 3;

            case LogicalAndToken:
                return 2;

            case LogicalOrToken:
                return 1;

                default:
                    return 0;
        }

    }

    public int getUnaryOperatorPrecedence(TokenType unaryOperator) {
        switch(unaryOperator) {
            case AddToken:
            case SubToken:
            case LogicalNotToken:
                return 7;


            default:
                return 0;
        }
    }

    public Expression parse() {
        // ParseAssignment Expression
        // check if currentToken == Identifier
        // next is assignment
        if(CurrentToken()._type == TokenType.IdentifierToken
            && Peek(1)._type == TokenType.AssignmentToken) {
            // This is an assignment exp
            Token left = NextToken();
            TokenType operatorToken = NextToken()._type;
            Expression right = parse();

            // Add an entry in the environment and get the result
            return new AssignmentExpression(left, operatorToken, right);

        }
        return parseBinaryExpression(0);
    }


    private Expression parseBinaryExpression(int parentPrecedence) {
        Expression left;

        int unaryOperatorPrec = getUnaryOperatorPrecedence(CurrentToken()._type);
        if (unaryOperatorPrec != 0 && unaryOperatorPrec >= parentPrecedence)
            left = new UnaryExpression(NextToken()._type, parseBinaryExpression(unaryOperatorPrec));
        else
            left = parsePrimaryExp();


        while(CurrentToken()._type != TokenType.EndOfLineToken && left != null) {
            int curPrecedence = getBinaryOperatorPrecedence(CurrentToken()._type);

            if (curPrecedence <= parentPrecedence || curPrecedence == 0)
                break;


            TokenType binOperator = NextToken()._type;

            Expression right = parseBinaryExpression(curPrecedence);

            left = new BinaryExpression(left, binOperator, right);

        }


        return left;

//        return null;
    }

    // precedence fix is required --> yay boi

    private Expression parsePrimaryExp() {
//        System.out.println("In parsePrimearyExp method");
        switch (CurrentToken()._type) {
            case OpenParensToken:
                // return a tree for parens
                Token left = NextToken();

                Expression body = parseBinaryExpression(0);

                Token right = match(TokenType.ClosedParensToken);

                return new ParensExpression(left, body, right);
            case IntToken: {
                Token currentToken = match(TokenType.IntToken); // it does

                int value = (currentToken._value == null) ? Integer.MIN_VALUE : (int) currentToken._value;

                return new NumberExpression(value); // creating nullPoint Exception

            }
            case BoolTokenKeyword: {
                Token currentToken = match(TokenType.BoolTokenKeyword); // it does

                boolean value = (boolean) currentToken._value;

                return new BoolExperssion(value);
            }

            case IdentifierToken: {
                Token currentToken = match(TokenType.IdentifierToken);

                // Get value and type from the env

                // For now just make a value of 1
                return new IdentifierExpression(currentToken._lexeme,1, "int"); // just value of 1
            }




            default:
                _diagnostics.add("Unexpected primary expression " + CurrentToken()._lexeme);



        }


        return null;
    }



}

// replace get with a[i]
