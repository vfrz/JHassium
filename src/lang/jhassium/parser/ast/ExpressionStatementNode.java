package lang.jhassium.parser.ast;

import lang.jhassium.SourceLocation;
import lang.jhassium.lexer.TokenType;
import lang.jhassium.parser.*;

/**
 * File : ExpressionStatementNode.java
 * Description : None
 * Author : FRITZ Valentin
 * Website : https://github.com/vfrz/JHassium
 * Date : 10/08/2016 21:22
 */
public class ExpressionStatementNode extends AstNode {

    public ExpressionStatementNode(AstNode body, SourceLocation location) {
        Children.add(body);
        Location = location;
    }

    public AstNode getBody() {
        return Children.get(0);
    }

    public static AstNode parse(Parser parser) {
        AstNode expression = ExpressionNode.parse(parser);
        parser.acceptToken(TokenType.Semicolon);
        if (expression instanceof FunctionCallNode)
            return new ExpressionStatementNode(expression, parser.getLocation());
        else if (expression instanceof BinaryOperationNode) {
            if (((BinaryOperationNode) expression).getBinaryOperation() == BinaryOperation.Assignment)
                return new ExpressionStatementNode(expression, parser.getLocation());
        } else if (expression instanceof UnaryOperationNode) {
            if (((UnaryOperationNode) expression).getUnaryOperation() != UnaryOperation.Not)
                return new ExpressionStatementNode(expression, parser.getLocation());
        }
        return expression;
    }

    public void visit(IVisitor visitor) {
        visitor.accept(this);
    }

    public void visitChildren(IVisitor visitor) {
        for (AstNode child : Children)
            child.visit(visitor);
    }
}
