package lang.jhassium.parser.ast;

import lang.jhassium.SourceLocation;
import lang.jhassium.lexer.TokenType;
import lang.jhassium.parser.AstNode;
import lang.jhassium.parser.IVisitor;
import lang.jhassium.parser.Parser;

/**
 * File : ForNode.java
 * Description : None
 * Authors : FRITZ Valentin, Jacob Misirian & zdimension
 * Website : https://github.com/vfrz/JHassium
 * Date : 10/08/2016 21:57
 */
public class ForNode extends AstNode {

    public ForNode(AstNode singleStatement, AstNode predicate, AstNode repeatStatement, AstNode body, SourceLocation location) {
        Children.add(singleStatement);
        Children.add(predicate);
        Children.add(repeatStatement);
        Children.add(body);
        Location = location;
    }

    public static ForNode parse(Parser parser) {
        parser.expectToken(TokenType.Identifier, "for");
        parser.expectToken(TokenType.LeftParentheses);
        AstNode singleStatement = ExpressionStatementNode.parse(parser);
        parser.acceptToken(TokenType.Semicolon);
        AstNode predicate = ExpressionNode.parse(parser);
        parser.acceptToken(TokenType.Semicolon);
        AstNode repeatStatement = ExpressionStatementNode.parse(parser);
        parser.expectToken(TokenType.RightParentheses);
        AstNode body = StatementNode.parse(parser);
        return new ForNode(singleStatement, predicate, repeatStatement, body, parser.getLocation());
    }

    public AstNode getSingleStatement() {
        return Children.get(0);
    }

    public AstNode getPredicate() {
        return Children.get(1);
    }

    public AstNode getRepeatStatement() {
        return Children.get(2);
    }

    public AstNode getBody() {
        return Children.get(3);
    }

    public void visit(IVisitor visitor) {
        visitor.accept(this);
    }

    public void visitChildren(IVisitor visitor) {
        for (AstNode child : Children)
            child.visit(visitor);
    }
}
