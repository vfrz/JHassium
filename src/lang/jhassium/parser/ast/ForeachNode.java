package lang.jhassium.parser.ast;

import lang.jhassium.SourceLocation;
import lang.jhassium.lexer.TokenType;
import lang.jhassium.parser.AstNode;
import lang.jhassium.parser.IVisitor;
import lang.jhassium.parser.Parser;

/**
 * File : ForeachNode.java
 * Description : None
 * Author : FRITZ Valentin
 * Website : https://github.com/vfrz/JHassium
 * Date : 10/08/2016 21:53
 */
public class ForeachNode extends AstNode {

    private String identifier;

    public ForeachNode(String identifier, AstNode expression, AstNode body, SourceLocation location) {
        this.identifier = identifier;
        Children.add(expression);
        Children.add(body);
        Location = location;
    }

    public String getIdentifier() {
        return identifier;
    }

    public AstNode getExpression() {
        return Children.get(0);
    }

    public AstNode getBody() {
        return Children.get(1);
    }

    public static ForeachNode parse(Parser parser) {
        parser.expectToken(TokenType.Identifier, "foreach");
        parser.expectToken(TokenType.LeftParentheses);
        String identifier = parser.expectToken(TokenType.Identifier).getValue();
        parser.expectToken(TokenType.Identifier, "in");
        AstNode expression = ExpressionNode.parse(parser);
        parser.expectToken(TokenType.RightParentheses);
        AstNode body = StatementNode.parse(parser);
        return new ForeachNode(identifier, expression, body, parser.getLocation());
    }

    public void visit(IVisitor visitor) {
        visitor.accept(this);
    }

    public void visitChildren(IVisitor visitor) {
        for (AstNode child : Children)
            child.visit(visitor);
    }
}
