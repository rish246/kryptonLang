package com.Rishabh;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

    public static Scanner scanner = new Scanner(System.in);
    public static final String TEXT_RED = "\u001B[31m";
    public static final String TEXT_RESET = "\u001B[0m";


    public static void main(String[] args) {


	// write your code here
        while (true) {

            System.out.print("Krypton >> ");
            String line = scanner.nextLine().trim();

            Parser parser = new Parser(line);
            Expression result = parser.parseTerm();
//
            // PrettyPrint(result) in form of a tree
            // ├──
            // │
            // └──
//            result.prettyPrint("");
//            parser.printTokens();
            try {

                if (parser._diagnostics.size() > 0) {
                    // Print the errors and continue with our loop
                    for(String diagnostic : parser._diagnostics)
                        System.out.println(TEXT_RED + diagnostic + TEXT_RESET);

                    continue;
                }
                result.prettyPrint("");

                int answer = (int) result.evaluate();
                System.out.println(answer);
            } catch (Exception e1) {
                System.out.println(e1.toString());
            }
        }
    }



}

// Adding parser diagnostics


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
        // GIving bugs... fix this error now
        if(_line.charAt(_position) == ' ') {
//            System.out.println("Found space");
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

        // Add an error here
        _diagnostics.add("Invalid token : " + _line.charAt(_position));

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

    Object evaluate() throws Exception {return 0;}

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
//        System.out.print("|");
        System.out.println(_value);
    }

    @Override
    Object evaluate() {
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
        // Add some long lines here
        System.out.println(indent + "|");

        System.out.print(indent + "└──"); _right.prettyPrint(indent + "    ");

    }

    @Override
    Object evaluate() throws Exception {
        if(_operatorToken == TokenType.AddToken)
            return ((int) _left.evaluate() + (int) _right.evaluate());

        else if(_operatorToken == TokenType.SubToken) {
            return ((int)_left.evaluate() - (int)_right.evaluate());
        }
        else if(_operatorToken == TokenType.MultToken) {
            return ((int)_left.evaluate() * (int)_right.evaluate());
        }
        else if(_operatorToken  == TokenType.DivToken) {
            return ((int)_left.evaluate() / (int)_right.evaluate());
        }
        else {
            throw new Exception("Unknown binary operator" + _operatorToken);
        }
    }

}

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
        } while(newToken._type != TokenType.EndOfLineToken);

        // Append lexer diagnostics with parser diagnostics
        _diagnostics.addAll(lexer._diagnostics);
    }

    Expression parseTerm() {
        Expression left = parseFactor();

        // position == 0


        while(_tokens.get(_position + 1)._type == TokenType.AddToken ||
                _tokens.get(_position + 1)._type == TokenType.SubToken)
        {

            _position++;
            TokenType operatorToken = _tokens.get(_position)._type; // +
            ++_position; // 2
//            System.out.println();

            Expression right = parseFactor();

            left = new BinaryExpression(left, operatorToken, right);

            // Why is it not breaking right now
            if(_position >= _tokens.size() - 1)
                break;

        }


        return left;

    }

    public Expression parseFactor() {
        Expression left = parsePrimaryExp();

// Position = 2
        // (position == 2)
        while(_tokens.get(_position + 1)._type == TokenType.MultToken ||
                _tokens.get(_position + 1)._type == TokenType.DivToken)
        {
            ++_position;
            TokenType operatorToken = _tokens.get(_position)._type;



            ++_position; // 2
            Expression right = parsePrimaryExp();

            left = new BinaryExpression(left, operatorToken, right);

            // Why is it not breaking right now
            if(_position >= _tokens.size() - 1)
                break;

        }


        return left;
    }


    private Token match(TokenType type) {
        // If type matches the next token, then return the current token
        // else return newly generated token
        if(_position < _tokens.size() && _tokens.get(_position)._type == type)
            return _tokens.get(_position);

        _diagnostics.add("Expected " + type + ", Got : " + _tokens.get(_position)._type);

        return new Token(type, null, null);
    }



    // precedence fix is required --> yay boi

    private Expression parsePrimaryExp() {
//        System.out.println("In parsePrimearyExp method");
        Token currentToken = match(TokenType.IntToken);

        int value = (currentToken._value == null) ? Integer.MIN_VALUE : (int) currentToken._value;

        return new NumberExpression(value); // creating nullPoint Exception
    }


    void printTokens() {
        for(Token token : _tokens) {
            System.out.println(token._type.toString() + "    " + token._lexeme);
        }
    }
}

