package lang.jhassium.parser.ast;

import lang.jhassium.SourceLocation;
import lang.jhassium.lexer.TokenType;
import lang.jhassium.parser.AstNode;
import lang.jhassium.parser.IVisitor;
import lang.jhassium.parser.Parser;

/**
 * File : ConditionalNode.java
 * Description : None
 * Authors : FRITZ Valentin, Jacob Misirian & zdimension
 * Website : https://github.com/vfrz/JHassium
 * Date : 10/08/2016 21:39
 */
public class ConditionalNode extends AstNode {

    public ConditionalNode(AstNode predicate, AstNode body, SourceLocation location) {
        this(predicate, body, location, null);
    }

    public ConditionalNode(AstNode predicate, AstNode body, SourceLocation location, AstNode elseBody) {
        Children.add(predicate);
        Children.add(body);
        if (elseBody != null)
            Children.add(elseBody);
        Location = location;
    }

    public static ConditionalNode parse(Parser parser) {
        parser.expectToken(TokenType.Identifier, "if");
        parser.expectToken(TokenType.LeftParentheses);
        AstNode predicate = ExpressionNode.parse(parser);
        parser.expectToken(TokenType.RightParentheses);
        AstNode body = StatementNode.parse(parser);
        AstNode elseBody = null;
        if (parser.acceptToken(TokenType.Identifier, "else"))
            elseBody = StatementNode.parse(parser);

        return new ConditionalNode(predicate, body, parser.getLocation(), elseBody);
    }

    public AstNode getPredicate() {
        return Children.get(0);
    }

    public AstNode getBody() {
        return Children.get(1);
    }

    public AstNode getElseBody() {
        return Children.get(2);
    }

    public void visit(IVisitor visitor) {
        visitor.accept(this);
    }

    public void visitChildren(IVisitor visitor) {
        for (AstNode child : Children)
            child.visit(visitor);
    }
}
