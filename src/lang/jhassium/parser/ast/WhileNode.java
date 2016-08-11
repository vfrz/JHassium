package lang.jhassium.parser.ast;

import lang.jhassium.SourceLocation;
import lang.jhassium.lexer.TokenType;
import lang.jhassium.parser.AstNode;
import lang.jhassium.parser.IVisitor;
import lang.jhassium.parser.Parser;
import lang.jhassium.parser.UnaryOperation;

/**
 * File : WhileNode.java
 * Description : None
 * Authors : FRITZ Valentin, Jacob Misirian & zdimension
 * Website : https://github.com/vfrz/JHassium
 * Date : 10/08/2016 22:28
 */
public class WhileNode extends AstNode {

    public WhileNode(AstNode predicate, AstNode body, SourceLocation location) {
        Children.add(predicate);
        Children.add(body);
        Location = location;
    }

    public AstNode getPredicate() {
        return Children.get(0);
    }

    public AstNode getBody() {
        return Children.get(1);
    }

    public static WhileNode parse(Parser parser) {
        return parse(parser, false);
    }

    public static WhileNode parse(Parser parser, boolean until) {
        parser.expectToken(TokenType.Identifier);
        parser.expectToken(TokenType.LeftParentheses);
        AstNode predicate = until ? new UnaryOperationNode(UnaryOperation.Not, ExpressionNode.parse(parser), parser.getLocation()) : ExpressionNode.parse(parser);
        parser.expectToken(TokenType.RightParentheses);
        AstNode body = StatementNode.parse(parser);
        return new WhileNode(predicate, body, parser.getLocation());
    }

    public void visit(IVisitor visitor) {
        visitor.accept(this);
    }

    public void visitChildren(IVisitor visitor) {
        for (AstNode child : Children)
            child.visit(visitor);
    }
}
