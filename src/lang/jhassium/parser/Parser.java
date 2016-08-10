package lang.jhassium.parser;

import lang.jhassium.SourceLocation;
import lang.jhassium.lexer.Token;
import lang.jhassium.lexer.TokenType;
import lang.jhassium.parser.ast.CodeBlockNode;
import lang.jhassium.parser.ast.StatementNode;
import lang.jhassium.utils.HassiumLogger;

import java.util.List;

/**
 * File : Parser.java
 * Description : None
 * Author : FRITZ Valentin
 * Website : https://github.com/vfrz/JHassium
 * Date : 10/08/2016 20:58
 */
public class Parser {

    public int Position;
    private List<Token> tokens;

    public AstNode Parse(List<Token> tokens)
    {
        Position = 0;
        this.tokens = tokens;
        CodeBlockNode ast = new CodeBlockNode(getLocation());
        while (Position < tokens.size())
            ast.Children.add(StatementNode.parse(this));
        return ast;
    }

    public List<Token> getTokens() {
        return tokens;
    }

    public SourceLocation getLocation() {
        return Position < tokens.size() ? tokens.get(Position).getSourceLocation() : tokens.get(Position - 1).getSourceLocation();
    }

    public boolean matchToken(TokenType tokenType)
    {
        return Position < tokens.size() && tokens.get(Position).getTokenType() == tokenType;
    }
    public boolean matchToken(TokenType tokenType, String value)
    {
        return Position < tokens.size() && tokens.get(Position).getTokenType() == tokenType && tokens.get(Position).getValue() == value;
    }

    public boolean acceptToken(TokenType tokenType)
    {
        boolean matches = matchToken(tokenType);
        if (matches)
            Position++;
        return matches;
    }
    public boolean acceptToken(TokenType tokenType, String value)
    {
        boolean matches = matchToken(tokenType, value);
        if (matches)
            Position++;
        return matches;
    }

    public Token expectToken(TokenType tokenType) {
        boolean matches = matchToken(tokenType);
        if (!matches)
            HassiumLogger.error(tokenType + " was expected in parser! Instead got " + getToken().getTokenType() + " with value " + getToken().getValue() + " at" + getLocation());
        return tokens.get(Position++);
    }

    public Token expectToken(TokenType tokenType, String value) {
        boolean matches = matchToken(tokenType);
        if (!matches)
            HassiumLogger.error(tokenType + " of value " + value + " was expected in parser! At " + getLocation());
        return tokens.get(Position++);
    }

    public Token getToken() {
        return getToken(0);
    }

    public Token getToken(int n)
    {
        if (Position < tokens.size())
            return tokens.get(Position + n);
        else
            return tokens.get(Position + n - 1);
    }
}
