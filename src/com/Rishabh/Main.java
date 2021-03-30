package com.Rishabh;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class Main {

    public static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
	// write your code here
        while (true) {

            System.out.print("Krypton >> ");
            String line = scanner.nextLine();

            Parser parser = new Parser(line);
            Expression result = parser.parse();
//
            // PrettyPrint(result) in form of a tree
            // ├──
            // │
            // └──
//            result.prettyPrint("");
            System.out.println(result.evaluate());
        }
    }



}


// Make a token type enum
enum TokenType {IntToken, AddToken, EndOfLineToken, SubToken, MultToken, DivToken, ErrorToken;}

class Token
{
    TokenType _type;
    String _lexeme;
    Object _value;

    Token(TokenType type, String lexeme, Object value) {
        _type = type;
        _lexeme = lexeme;
        _value = value;
    }
}

// Parse strings and return Tokens

class Lexer {
    String _line;
    int _position;

    Lexer(String line) {
        _line = line;
        _position = 0;
    }

    Token nextToken() {
        if(_line == null || _position >= _line.length())
            return new Token(TokenType.EndOfLineToken, null, null);
        // if current char is a digit
        int start = _position;
        if(Character.isDigit(_line.charAt(_position)))
        {
            while((_position  < _line.length()) && Character.isDigit(_line.charAt(_position)))
                _position++;

            // Create a new Lexeme
            String lexeme = _line.substring(start, _position);
            return new Token(TokenType.IntToken, lexeme, Integer.parseInt(lexeme));

        }

        // Reject spaces
        if(_line.charAt(_position) == ' ') {
            while(_line.charAt(_position) == ' ') {
                _position++;
            }

            return nextToken();
        }

        if(_line.charAt(_position) == '+') {
            _position++;
            return new Token(TokenType.AddToken, "+", null);
        } else if(_line.charAt(_position) == '-') {
            _position++;
            return new Token(TokenType.SubToken, "-", null);
        } else if(_line.charAt(_position) == '*') {
            _position++;
            return new Token(TokenType.MultToken, "*", null);
        } else if(_line.charAt(_position) == '/') {
            _position++;
            return new Token(TokenType.DivToken, "/", null);
        }


        return new Token(TokenType.ErrorToken, ""+ _line.charAt(_position++), null);

    }






}

enum ExpressionType {BinaryExpression, IntExpression}

class Expression {
    ExpressionType _type;

    Expression(ExpressionType type) {
        _type = type;
    }

    ExpressionType getType()
    {
        return _type;
    }

    int evaluate() {return 0;}

    void prettyPrint(String indent) {}

}

class NumberExpression extends Expression {
    int _value;
//    ExpressionType _type;

    NumberExpression(int value)
    {
        super(ExpressionType.BinaryExpression);
        _value = value;
//        super(ExpressionType.BinaryExpression);
    }

    @Override
    void prettyPrint(String indent) {
        System.out.println(_value);
    }

    @Override
    int evaluate() {
        return _value;
    }

}

class BinaryExpression extends Expression {
    Expression _left, _right;
    TokenType _operatorToken;
//    ExpressionType _type;

    BinaryExpression(Expression left, TokenType operatorToken, Expression right) {
        super(ExpressionType.BinaryExpression);
        _left = left;
        _operatorToken = operatorToken;
        _right = right;
    }

    @Override
    void prettyPrint(String indent) {

        System.out.println(_operatorToken);
        System.out.println(indent + "|");
        System.out.print(indent + "├──"); _left.prettyPrint(indent + "    ");
        System.out.print(indent + "└──"); _right.prettyPrint(indent + "    ");

    }

    @Override
    int evaluate() {
        if(_operatorToken == TokenType.AddToken)
            return (_left.evaluate() + _right.evaluate());

        else if(_operatorToken == TokenType.SubToken) {
            return (_left.evaluate() - _right.evaluate());
        }

        else {
            return 0;
        }
    }

}

class Parser {
    private Lexer lexer;
    private List<Token> _tokens;
    private int _position;

    Parser(String text) {
        lexer = new Lexer(text);
        _tokens = new ArrayList<>();
        _position = 0;

        Token newToken = null;

        do {
            newToken = lexer.nextToken();
            System.out.println(newToken._lexeme);
            _tokens.add(newToken);
        } while(newToken._type != TokenType.EndOfLineToken);
    }

    Expression parse() {
        Expression left = parsePrimaryExp();

        if(left == null)
            return null;

        _position++;

        while(_tokens.get(_position)._type == TokenType.AddToken ||
                _tokens.get(_position)._type == TokenType.SubToken) {

            TokenType operatorToken = _tokens.get(_position)._type;
            _position++;
            Expression right = parsePrimaryExp();



            // Create a binary operation
            left = new BinaryExpression(left, operatorToken, right);

            _position++;
        }

        System.out.println("Final left value = " + left);

        return left;

    }

    private Expression parsePrimaryExp() {
        if(_tokens.get(_position)._type == TokenType.IntToken) {
            return new NumberExpression((int) _tokens.get(_position)._value);
        }
//
        // Since this is not a primary exp .. go and deal with this
        return null;
    }


    void printTokens() {
        for(Token token : _tokens) {
            System.out.println(token._type.toString() + "    " + token._lexeme);
        }
    }
}

