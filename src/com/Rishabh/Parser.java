package com.Rishabh;

import com.Rishabh.Expression.Statements.ForEachStatement;
import com.Rishabh.Expression.Statements.ForStatement;
import com.Rishabh.Expression.Values.ObjectExpression;
import com.Rishabh.Syntax.Expression;
import com.Rishabh.Syntax.LambdaExpression;
import com.Rishabh.Syntax.PrimaryExpressions.*;
import com.Rishabh.Syntax.Statement;
import com.Rishabh.Syntax.Statements.*;
import com.Rishabh.Syntax.Values.BoolExperssion;
import com.Rishabh.Syntax.Values.NullExpression;
import com.Rishabh.Syntax.Values.NumberExpression;
import com.Rishabh.Syntax.Values.StringExpression;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


class Parser {
    private Lexer lexer;
    private Token[] _tokens;
    private Integer[] _lineNumbers;
    private int _position;

    // Make diagnostics for parser as well
    List<String> _diagnostics = new ArrayList<String>();

    Parser(String text) {
        lexer = new Lexer(text);
        _position = 0;

        tokenizeLine();

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

    private void tokenizeLine() {
        Token newToken;

        List<Token> listTokens = new ArrayList<>();
        List<Integer> lineNums = new ArrayList<>();

        do {
            newToken = lexer.nextToken();
            listTokens.add(newToken);
            lineNums.add(lexer._lineNumber);
        } while (newToken._type != TokenType.EndOfLineToken);

        Token[] result = new Token[listTokens.size()];
        _tokens = listTokens.toArray(result);

        Integer[] lines = new Integer[lineNums.size()];
        _lineNumbers = lineNums.toArray(lines);
    }

    public Token match(TokenType type) {
        if (_position < _tokens.length && CurrentToken()._type == type)
            return NextToken();


        _diagnostics.add("Expected " + type + ", Got : " + CurrentToken()._type + " at line number " + _lineNumbers[_position]);

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

    public SyntaxTree parse() {
        // If next statement is statement declaration... then parseStatement and return the statement
        // else parseBinaryExpression
        if(isStatementInitialization(CurrentToken())) {
            return parseStatement(); 
        } 
    
        return parseExpression(0);
    }

    private boolean isStatementInitialization(Token tk) {
        return tk._type == TokenType.IfKeywordToken || tk._type == TokenType.OpenBracketToken || tk._type == TokenType.WhileKeywordToken || tk._type == TokenType.ForKeywordToken
                    || tk._type == TokenType.PrintExpToken || tk._type == TokenType.FunctionDefineToken || tk._type == TokenType.ReturnToken;
    }


    private boolean isLeftAssociative(Token tk) {
        return tk._type == TokenType.AssignmentToken;
    }


    private Expression parseExpression(int parentPrecedence) {
        Expression left;

        int unaryOperatorPrec = getUnaryOperatorPrecedence(CurrentToken()._type);
        if (unaryOperatorPrec != 0 && unaryOperatorPrec >= parentPrecedence)
            left = new UnaryExpression(NextToken()._type, parseExpression(unaryOperatorPrec), CurrentLineNumber());
        else {
            left = parsePrimaryExp();
        }


        while(_position < _tokens.length
        && CurrentToken()._type != TokenType.EndOfLineToken) {

            if(CurrentToken()._type == TokenType.SemiColonToken)
                break;

            int curPrecedence = getBinaryOperatorPrecedence(CurrentToken()._type);


            if (curPrecedence < parentPrecedence || curPrecedence == 0)
                break;

            if(curPrecedence == parentPrecedence && !isLeftAssociative(CurrentToken()))
                break;

            TokenType binOperator = NextToken()._type;


            Expression right = parseExpression(curPrecedence);


            if(binOperator == TokenType.AssignmentToken)
            {
                return new AssignmentExpression(left, binOperator, right, CurrentLineNumber());
            }

            left = new BinaryExpression(left, binOperator, right, CurrentLineNumber());

        }

        return left;

    }

    // First change statements
    private Statement parseStatement() {
        switch(CurrentToken()._type) {
            case IfKeywordToken: {
                match(TokenType.IfKeywordToken);
                match(TokenType.OpenParensToken);

                Expression condBranch = parseExpression(0);
 
                match(TokenType.ClosedParensToken);
                SyntaxTree thenBranch = parse();

                // Parse Only primary expression
                SyntaxTree elseBranch = null;

                if(_tokens.length > _position && CurrentToken()._type == TokenType.ElseKeywordToken) {
                    match(TokenType.ElseKeywordToken);
                    elseBranch = parse();
                }
                return new IfStatement(condBranch, thenBranch, elseBranch, CurrentLineNumber());
            }

            case OpenBracketToken: {
                match(TokenType.OpenBracketToken);
                List<SyntaxTree> parsedExpressions = new ArrayList<>();

                // While !match with }
                int blockDepth = 1;
                while(blockDepth > 0) { // BlockDepth is not being maintained .. why is that
                    if(CurrentToken()._type == TokenType.ClosedBracket) {
                        blockDepth--;
                        continue;
                    }
                    SyntaxTree nextExpression = parse();

                    // Same expression as yesterday .. it is giving null and hence can't do anything about it;

                    parsedExpressions.add(nextExpression);
                    if(nextExpression == null) {
                        next();
                        continue;
                    }

                    // I should have never reached there... why did i start parsing the function.. it should have given an error right away


                    if(nextExpression.getType() == ExpressionType.IfExpression
                    || nextExpression.getType() == ExpressionType.BlockExpression
                    || nextExpression.getType() == ExpressionType.WhileExpression
                        || nextExpression.getType() == ExpressionType.ForLoopExpression
                        || nextExpression.getType() == ExpressionType.FuncExpression) { 
                        continue;
                    }

                    match(TokenType.SemiColonToken);

                }
                match(TokenType.ClosedBracket);

                return new BlockStatement(parsedExpressions, CurrentLineNumber());
            }

            case WhileKeywordToken: {
                match(TokenType.WhileKeywordToken);
                match(TokenType.OpenParensToken);
                Expression condition = parseExpression(0);

                match(TokenType.ClosedParensToken);
                SyntaxTree whileBody = parse();

                return new WhileStatement(condition, whileBody, CurrentLineNumber());
            }

            case ForKeywordToken: {
                match(TokenType.ForKeywordToken);
                match(TokenType.OpenParensToken);
                // Peek 1 if it is in keyword.. make a foreach expression

                Expression initCondition = parseExpression(0);

                if(CurrentToken()._type == TokenType.InKeyword) {
                    return parseForeachStatement(initCondition);
                }

                match(TokenType.SemiColonToken);

                Expression haltingCondtion = parseExpression(0);
              
                match(TokenType.SemiColonToken);

                Expression progressExp = parseExpression(0);
                
                match(TokenType.ClosedParensToken);
                SyntaxTree forBody = parse();

                return new ForStatement(initCondition, haltingCondtion, progressExp, forBody, CurrentLineNumber());
            }

            case FunctionDefineToken: {
                match(TokenType.FunctionDefineToken);
                String funcName = match(TokenType.IdentifierToken)._lexeme;
                match(TokenType.OpenParensToken);
                // ParseFormalArgs
                List<Expression> formalArgs = parseCommaSeparatedExpressions(TokenType.ClosedParensToken);

                match(TokenType.ClosedParensToken);

                Statement.EnclosingStatements++;
                SyntaxTree funcBody = parse();
                Statement.EnclosingStatements--;

                return new FunctionStatement(funcName, funcBody, formalArgs, CurrentLineNumber());
            }

            case ReturnToken: {
                match(TokenType.ReturnToken);

                // Only work if there is an enclosing function statement else throw a parse error
                if(Statement.EnclosingStatements == 0) {
                    _diagnostics.add("Return statement should be enclosed in a lambda or a function definition.. at line number " + _lineNumbers[_position]);
                }

                Expression returnBody = parseExpression(0);

                if(returnBody == null) {
                    _diagnostics.add("Empty return statements are not allowed");
                }

                return new com.Rishabh.Expression.Statements.ReturnStatement(returnBody, CurrentLineNumber());


            }
            
            case PrintExpToken: {
                match(TokenType.PrintExpToken);
                match(TokenType.OpenParensToken);
                Expression printExpBody = parseExpression(0);

                match(TokenType.ClosedParensToken);
                return new PrintStatement(printExpBody, CurrentLineNumber());
            }

            default:
                _diagnostics.add("Unexpected token : " + CurrentToken()._lexeme + ", expected a statement initialization at line number " + _lineNumbers[_position]);
                return null;
        }
        
    }

    private ForEachStatement parseForeachStatement(Expression iterator) {
        match(TokenType.InKeyword);
        Expression iterable = parsePrimaryExp(); // This will do it
        match(TokenType.ClosedParensToken);
        SyntaxTree foreachBody = parse();

        return new ForEachStatement(iterator, iterable, foreachBody, CurrentLineNumber());
    }

    private int CurrentLineNumber() {
        return _lineNumbers[_position];
    }

    private List<Expression> parseCommaSeparatedExpressions(TokenType delimiterType) {
        List<Expression> listElements = new ArrayList<>();

        while(CurrentToken()._type != delimiterType) {
            // parse
            Expression nextElement = parseExpression(0);
            listElements.add(nextElement);

            if(CurrentToken()._type == delimiterType)
                break;

            match(TokenType.CommaSeparatorToken);
        }

        return listElements;
    }


    private Expression parsePrimaryExp() {

        switch (CurrentToken()._type)  {
            case OpenParensToken:
                // return a tree for parens
                Token left = NextToken();

                Expression body = parseExpression(0);

                Token right = match(TokenType.ClosedParensToken);

                return new ParensExpression(left, body, right, CurrentLineNumber());
            case IntToken: {
                Token currentToken = match(TokenType.IntToken); // it does

                int value = (currentToken._value == null) ? Integer.MIN_VALUE : (int) currentToken._value;

                return new NumberExpression(value, CurrentLineNumber()); // creating nullPoint Exception

            }

            case LengthToken: {
                match(TokenType.LengthToken);
                match(TokenType.OpenParensToken);
                Expression lenExpBody = parseExpression(0);
                match(TokenType.ClosedParensToken);

                return new LengthExpression(lenExpBody, CurrentLineNumber());
            }

            case NullValueToken: {
                match(TokenType.NullValueToken);
                return new NullExpression(CurrentLineNumber());
            }

            case StringConstToken: {
                return new StringExpression((String) NextToken()._value, CurrentLineNumber());
            }

            case BoolTokenKeyword: {
                Token currentToken = match(TokenType.BoolTokenKeyword); // it does

                boolean value = (boolean) currentToken._value;

                return new BoolExperssion(value, CurrentLineNumber());
            }

            case IdentifierToken: {
                Token currentToken = match(TokenType.IdentifierToken);

                if(CurrentToken()._type == TokenType.OpenSquareBracketToken || CurrentToken()._type == TokenType.OpenParensToken) {
                    List<Expression> indices = new ArrayList<>();
                    while(CurrentToken()._type == TokenType.OpenSquareBracketToken || CurrentToken()._type == TokenType.OpenParensToken) {
                        Expression Index = parseChainedExpression();
                        indices.add(Index);
                    }

                    if(indices.size() == 0) {
                        _diagnostics.add("Subscript operator cannot be empty");
                        return null;
                    }


                    return new com.Rishabh.Expression.PrimaryExpressions.ArrayAccessExpression(currentToken, indices, CurrentLineNumber());

                }

                return new IdentifierExpression(currentToken._lexeme, CurrentLineNumber()); // just value of 1
            }

            case OpenSquareBracketToken: {
                // Make a new List
                match(TokenType.OpenSquareBracketToken);

                List<Expression> listElements = parseCommaSeparatedExpressions(TokenType.ClosedSquareBracketToken);

                if(listElements == null) {
                    return null;
                }


                match(TokenType.ClosedSquareBracketToken);

                return new com.Rishabh.Expression.Values.ListExpression(listElements, CurrentLineNumber());
            }

            case OpenBracketToken : {
                match(TokenType.OpenBracketToken);
                Map<Expression, Expression> bindings = new HashMap<>();
                while(CurrentToken()._type != TokenType.ClosedBracket) {
                    Expression key = parseExpression(0);
                    match(TokenType.ColonOperator);
                    Expression value = parseExpression(0);
                    bindings.put(key, value);

                    if(CurrentToken()._type == TokenType.ClosedBracket)
                        continue;
                    match(TokenType.CommaSeparatorToken);
                }
                match(TokenType.ClosedBracket);
                return new ObjectExpression(bindings, CurrentLineNumber());
            }


            case LambdaExpressionToken: {
                match(TokenType.LambdaExpressionToken);
                match(TokenType.OpenParensToken);
                List<Expression> formalArgs = parseCommaSeparatedExpressions(TokenType.ClosedParensToken);


                match(TokenType.ClosedParensToken);
                // Set Statement.isEnclosing = true as this lambda is enclosing the statement
                Statement.EnclosingStatements++;
                SyntaxTree funcBody = parse();
                Statement.EnclosingStatements++;

                return new LambdaExpression(funcBody, formalArgs, CurrentLineNumber());


            }

            case InputKeyword: {
                match(TokenType.InputKeyword);
                match(TokenType.OpenParensToken);
                String dataType = ((IdentifierExpression) parsePrimaryExp())._lexeme;
                match(TokenType.CommaSeparatorToken);
                String prompt = ((StringExpression) parsePrimaryExp())._value;
                match(TokenType.ClosedParensToken);

                return new ReadInputExpression(dataType, prompt, CurrentLineNumber());

            }

            default:
                _diagnostics.add("Unexpected primary expression, Instead got : " + CurrentToken()._lexeme + ", at line number " + _lineNumbers[_position]);

        }


        return null;
    }

    private Expression parseChainedExpression() {
        Expression result = null;

        switch(CurrentToken()._type) {
            case OpenSquareBracketToken: {
                match(TokenType.OpenSquareBracketToken);
                result = parseExpression(0);
                match(TokenType.ClosedSquareBracketToken);

                return result;
            }

            case OpenParensToken: {
                Token left = match(TokenType.OpenParensToken);
                List<Expression> actualArgs = parseCommaSeparatedExpressions(TokenType.ClosedParensToken);
                com.Rishabh.Expression.Values.ListExpression actualArgsList = new com.Rishabh.Expression.Values.ListExpression(actualArgs, CurrentLineNumber());
                Token right = match(TokenType.ClosedParensToken);
                return new ParensExpression(left, actualArgsList, right, CurrentLineNumber());
            }
        }


        return result;
    }

}
