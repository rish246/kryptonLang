package com.Rishabh;

import com.Rishabh.Expression.*;
import com.Rishabh.Expression.PrimaryExpressions.*;
import com.Rishabh.Expression.Statements.BlockExpression;
import com.Rishabh.Expression.Statements.IfExpression;
import com.Rishabh.Expression.Statements.PrintExpression;
import com.Rishabh.Expression.Statements.WhileExpression;
import com.Rishabh.Expression.Statements.ForExpression;
import com.Rishabh.Expression.Statements.ReturnExpression;

import com.Rishabh.Expression.Values.BoolExperssion;
import com.Rishabh.Expression.Values.ListExpression;
import com.Rishabh.Expression.Values.NumberExpression;
import com.Rishabh.Expression.Values.StringExpression;

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

    private void next() {
        _position++;
    }

    public Token CurrentToken() {
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
        next();
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

    public Token match(TokenType type) {
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
            case ModuloToken:
                return 7;

            case AddToken:
            case SubToken:
                return 6;

            case LessThanEqualToken:
            case LessThanToken:
            case GreaterThanEqualToken:
            case GreaterThanToken:
                return 5;

            case EqualityToken:
            case NotEqualsToken:
                return 4;

            case LogicalAndToken:
                return 3;

            case LogicalOrToken:
                return 2;

            case AssignmentToken:
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
                return 8;


            default:
                return 0;
        }
    }

    public Expression parse() {

        // if(CurrentToken()._type == TokenType.IdentifierToken
        //     && Peek(1)._type == TokenType.AssignmentToken) {
        //     // This is an assignment exp
        //     Token left = NextToken();
        //     TokenType operatorToken = NextToken()._type;
        //     Expression right = parse();

        //     // Add an entry in the environment and get the result
        //     return new AssignmentExpression(left, operatorToken, right);

        // }
        return parseBinaryExpression(0);
    }

    private boolean isStatementInitialization(Token tk) {
        return tk._type == TokenType.IfKeywordToken || tk._type == TokenType.OpenBracketToken || tk._type == TokenType.WhileKeywordToken || tk._type == TokenType.ForKeywordToken
                    || tk._type == TokenType.PrintExpToken || tk._type == TokenType.FunctionDefineToken || tk._type == TokenType.ReturnToken;
    }


    private boolean isLeftAssociative(Token tk) {
        return tk._type == TokenType.AssignmentToken;
    }

   


    private Expression parseBinaryExpression(int parentPrecedence) {
        Expression left;

        int unaryOperatorPrec = getUnaryOperatorPrecedence(CurrentToken()._type);
        if (unaryOperatorPrec != 0 && unaryOperatorPrec >= parentPrecedence)
            left = new UnaryExpression(NextToken()._type, parseBinaryExpression(unaryOperatorPrec));
        else if(isStatementInitialization(CurrentToken())) {
            left = parseStatement();
            return left;
        } else {
            left = parsePrimaryExp();
        }


        while(_position < _tokens.length
        && CurrentToken()._type != TokenType.EndOfLineToken) {

            // This is probably causing the arguement
            if(CurrentToken()._type == TokenType.SemiColonToken)
                break;

            int curPrecedence = getBinaryOperatorPrecedence(CurrentToken()._type);


            if (curPrecedence < parentPrecedence || curPrecedence == 0)
                break;

            if(curPrecedence == parentPrecedence && !isLeftAssociative(CurrentToken()))
                break;

            TokenType binOperator = NextToken()._type;

            Expression right = parseBinaryExpression(curPrecedence);

            if(binOperator == TokenType.AssignmentToken) {
                // Make an assignment expression
                return new AssignmentExpression(left, binOperator, right);
            }

            left = new BinaryExpression(left, binOperator, right);

        }

        return left;

    }

    private Expression parseStatement() {
        switch(CurrentToken()._type) {
            case IfKeywordToken: {
                match(TokenType.IfKeywordToken);
                match(TokenType.OpenParensToken);
                Expression condBranch = parse();
                // The conditional must be a primary expression
                if(!condBranch.isExpressionPrimary())
                    _diagnostics.add("Expression in if condition must be a primary expression, Got " + condBranch._type);

                match(TokenType.ClosedParensToken);
                Expression thenBranch = parse();

                // Parse Only primary expression
                Expression elseBranch = null;

                if(_tokens.length > _position && CurrentToken()._type == TokenType.ElseKeywordToken) {
                    match(TokenType.ElseKeywordToken);
                    elseBranch = parse();
                    System.out.println(CurrentToken()._type);
                }
                return new IfExpression(condBranch, thenBranch, elseBranch);
            }

            case OpenBracketToken: {
                match(TokenType.OpenBracketToken);
                List<Expression> parsedExpressions = new ArrayList<>();

                // While !match with }
                int blockDepth = 1;
                while(blockDepth > 0) { // BlockDepth is not being maintained .. why is that
                    if(CurrentToken()._type == TokenType.ClosedBracket) {
                        blockDepth--;
                        continue;
                    }
                    Expression nextExpression = parse();

                    // Same expression as yesterday .. it is giving null and hence can't do anything about it;

                    parsedExpressions.add(nextExpression);
                    if(nextExpression.getType() == ExpressionType.IfExpression
                    || nextExpression.getType() == ExpressionType.BlockExpression
                    || nextExpression.getType() == ExpressionType.WhileExpression
                        || nextExpression.getType() == ExpressionType.ForLoopExpression
                        || nextExpression.getType() == ExpressionType.FuncExpression) { // True
                        continue;
                    }

                    match(TokenType.SemiColonToken);

                }
                match(TokenType.ClosedBracket);

                return new BlockExpression(parsedExpressions);
            }

            case WhileKeywordToken: {
                match(TokenType.WhileKeywordToken);
                match(TokenType.OpenParensToken);
                Expression condition = parse();

                if(!condition.isExpressionPrimary())
                    _diagnostics.add("Expression in if condition must be a primary expression, Got " + condition._type);

                match(TokenType.ClosedParensToken);
                Expression whileBody = parse();

                return new WhileExpression(condition, whileBody);
            }

            case ForKeywordToken: {
                match(TokenType.ForKeywordToken);
                match(TokenType.OpenParensToken);
                // parse the conditions
                Expression initCondition = parse();
                // check if the init condition is a primary exp
                if(!initCondition.isExpressionPrimary())
                    _diagnostics.add("Expected a primary expression in for loop condition, Got " + initCondition._type);
                
                match(TokenType.SemiColonToken);

                Expression haltingCondtion = parse();
                if(!haltingCondtion.isExpressionPrimary())
                    _diagnostics.add("Expected a primary expression in for loop condition, Got " + haltingCondtion._type);


                match(TokenType.SemiColonToken);

                Expression progressExp = parse();
                if(!progressExp.isExpressionPrimary())
                    _diagnostics.add("Expected a primary expression in for loop condition, Got " + progressExp._type);
                
                match(TokenType.ClosedParensToken);
                Expression forBody = parse();

                return new ForExpression(initCondition, haltingCondtion, progressExp, forBody);
            }

            case FunctionDefineToken: {
                match(TokenType.FunctionDefineToken);
                String funcName = match(TokenType.IdentifierToken)._lexeme;
                match(TokenType.OpenParensToken);
                // ParseFormalArgs
                List<IdentifierExpression> formalArgs = new ArrayList<>();

                if(CurrentToken()._type != TokenType.ClosedParensToken) {
                    // Create a formal here
                    Token firstArg = match(TokenType.IdentifierToken);
                    if(_diagnostics.size() > 0)
                        return null;

                    formalArgs.add(new IdentifierExpression(firstArg._lexeme));

                }

                while(CurrentToken()._type != TokenType.ClosedParensToken) {
                    match(TokenType.CommaSeparatorToken);

                    Token nextIdentifier = match(TokenType.IdentifierToken);
                    if(_diagnostics.size() > 0)
                        return null;

                    formalArgs.add(new IdentifierExpression(nextIdentifier._lexeme));

                }


                match(TokenType.ClosedParensToken);
                Expression funcBody = parse();

                return new FunctionExpression(funcName, funcBody, formalArgs);
            }

            case ReturnToken: {
                match(TokenType.ReturnToken);

                Expression returnBody = parse();

                if(returnBody == null) {
                    _diagnostics.add("Empty return statements are not allowed");
                    return null;
                }

                if(!returnBody.isExpressionPrimary())
                    _diagnostics.add("Expected a primary expression in the return statement, Got " + returnBody._type);

                return new ReturnExpression(returnBody);


            }
            
            case PrintExpToken: {
                match(TokenType.PrintExpToken);
                match(TokenType.OpenParensToken);
                Expression printExpBody = parse();

                if(!printExpBody.isExpressionPrimary())
                    _diagnostics.add("Expected a primary expression in the print statement, Got " + printExpBody._type);



                match(TokenType.ClosedParensToken);
                return new PrintExpression(printExpBody);
            }

            default:
                _diagnostics.add("Unexpected token : " + CurrentToken()._lexeme + ", expected a statement initialization");
                return null;
        }
        
    }


    private Expression parsePrimaryExp() {
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
            case StringConstToken: {
                return new StringExpression((String) NextToken()._value);
            }

            case BoolTokenKeyword: {
                Token currentToken = match(TokenType.BoolTokenKeyword); // it does

                boolean value = (boolean) currentToken._value;

                return new BoolExperssion(value);
            }

            case IdentifierToken: {
                Token currentToken = match(TokenType.IdentifierToken);
                // check if it is a function call
                if(CurrentToken()._type == TokenType.OpenParensToken) {
                    // Match openParens
                    match(TokenType.OpenParensToken);
                    // Make a list of actual args
                    List<Expression> actualArgs = new ArrayList<>();

                    // getFirstExp
                    if(CurrentToken()._type != TokenType.ClosedParensToken) {
                        // parse an actual arg
                        Expression nextExpression = parse();

                        if(!nextExpression.isExpressionPrimary())
                            _diagnostics.add("Expected a primary expression in function call, Got "+ nextExpression._type);
                        
                        actualArgs.add(nextExpression);
                    }   



                    while(CurrentToken()._type != TokenType.ClosedParensToken) {
                        match(TokenType.CommaSeparatorToken);
                        Expression nextExpression = parse();

                        if(!nextExpression.isExpressionPrimary())
                            _diagnostics.add("Expected a primary expression in function call, Got "+ nextExpression._type);
                        
                        actualArgs.add(nextExpression);
                    }

                    match(TokenType.ClosedParensToken);


                    return new FunctionCallExpression(currentToken._lexeme, actualArgs);




                }

                // ArraySuperScriptable expression
                else if(CurrentToken()._type == TokenType.OpenSquareBracketToken) {
                    match(TokenType.OpenSquareBracketToken);
                    // Array superScripting is going on here
                    Expression index = parse();

                    if(!index.isExpressionPrimary())
                        _diagnostics.add("Expected a primary expression in subscript operator, Got "+ index._type);
                        
                    match(TokenType.ClosedSquareBracketToken);
                    return new ArrayAccessExpression(currentToken, index);


                }

                return new IdentifierExpression(currentToken._lexeme); // just value of 1
            }

            case OpenSquareBracketToken: {
                // Make a new List
                match(TokenType.OpenSquareBracketToken);


                List<Expression> listElements = new ArrayList<>();

                if(CurrentToken()._type != TokenType.ClosedSquareBracketToken) {
                    Expression firstElement = parse();
                    if(_diagnostics.size() > 0)
                        return null;

                    listElements.add(firstElement);
                }

                while(CurrentToken()._type != TokenType.ClosedSquareBracketToken) {
                    match(TokenType.CommaSeparatorToken);

                    Expression nextElement = parse();
                    if(_diagnostics.size() > 0)
                        return null;

                    listElements.add(nextElement);
                }

                match(TokenType.ClosedSquareBracketToken);

                return new ListExpression(listElements);
            }


            case LambdaExpressionToken: {
                match(TokenType.LambdaExpressionToken);
                match(TokenType.OpenParensToken);
                List<IdentifierExpression> formalArgs = new ArrayList<>();


                // Similar problem as the block exp -->
                //
                if(CurrentToken()._type != TokenType.ClosedParensToken) {
                    // Create a formal here
                    Token firstArg = match(TokenType.IdentifierToken);
                    if(_diagnostics.size() > 0)
                        return null;

                    formalArgs.add(new IdentifierExpression(firstArg._lexeme));

                }

                while(CurrentToken()._type != TokenType.ClosedParensToken) {
                    match(TokenType.CommaSeparatorToken);

                    Token nextIdentifier = match(TokenType.IdentifierToken);
                    if(_diagnostics.size() > 0)
                        return null;

                    formalArgs.add(new IdentifierExpression(nextIdentifier._lexeme));

                }


                match(TokenType.ClosedParensToken);
                Expression funcBody = parse();

                return new FunctionExpression(null, funcBody, formalArgs);



            }

            default:
                _diagnostics.add("Unexpected primary expression, Instead got : " + CurrentToken()._lexeme);

        }


        return null;
    }



}

// check for if expressions, they are posing great problems
//  make primary and non primary expressions separate
// My parse Primary expression is parsing non primary expressions as well
// We need to separate the two functions
