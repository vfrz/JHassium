package lang.jhassium.parser.ast;

import lang.jhassium.SourceLocation;
import lang.jhassium.lexer.TokenType;
import lang.jhassium.parser.AstNode;
import lang.jhassium.parser.IVisitor;
import lang.jhassium.parser.Parser;

/**
 * File : PropertyNode.java
 * Description : None
 * Authors : FRITZ Valentin, Jacob Misirian & zdimension
 * Website : https://github.com/vfrz/JHassium
 * Date : 10/08/2016 22:12
 */
public class PropertyNode extends AstNode {

    private String identifier;

    public PropertyNode(String identifier, AstNode getBody, SourceLocation location) {
        this(identifier, getBody, location, null);
    }

    public PropertyNode(String identifier, AstNode getBody, SourceLocation location, AstNode setBody) {
        this.identifier = identifier;
        Children.add(getBody);
        if (setBody != null)
            Children.add(setBody);
        Location = location;
    }

    public AstNode getGetBody() {
        return Children.get(0);
    }

    public AstNode getSetBody() {
        return Children.get(1);
    }

    public String getIdentifier() {
        return identifier;
    }

    public static PropertyNode parse(Parser parser) {
        String identifier = parser.expectToken(TokenType.Identifier).getValue();
        parser.expectToken(TokenType.LeftBrace);
        parser.expectToken(TokenType.Identifier, "get");
        parser.expectToken(TokenType.LeftBrace);
        AstNode getBody = StatementNode.parse(parser);
        parser.acceptToken(TokenType.Semicolon);
        parser.expectToken(TokenType.RightBrace);
        if (!parser.acceptToken(TokenType.Identifier, "set"))
            return new PropertyNode(identifier, getBody, parser.getLocation());
        parser.expectToken(TokenType.LeftBrace);
        AstNode setBody = StatementNode.parse(parser);
        parser.acceptToken(TokenType.Semicolon);
        parser.expectToken(TokenType.RightBrace);
        parser.expectToken(TokenType.RightBrace);
        return new PropertyNode(identifier, getBody, parser.getLocation(), setBody);
    }

    public void visit(IVisitor visitor) {
        visitor.accept(this);
    }

    public void visitChildren(IVisitor visitor) {
        for (AstNode child : Children)
            child.visit(visitor);
    }
}
