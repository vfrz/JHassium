package lang.jhassium.lexer;

import lang.jhassium.SourceLocation;

/**
 * File : Token.java
 * Description : None
 * Authors : FRITZ Valentin, Jacob Misirian & zdimension
 * Website : https://github.com/vfrz/JHassium
 * Date : 10/08/2016 18:51
 */
public class Token {

    private TokenType tokenType;
    private String value;
    private SourceLocation sourceLocation;

    public Token(TokenType tokenType, String value, SourceLocation location) {
        this.tokenType = tokenType;
        this.value = value;
        this.sourceLocation = location;
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    public String getValue() {
        return value;
    }

    public SourceLocation getSourceLocation() {
        return sourceLocation;
    }
}
