package lang.jhassium.parser.ast;

import lang.jhassium.SourceLocation;
import lang.jhassium.lexer.TokenType;
import lang.jhassium.parser.AstNode;
import lang.jhassium.parser.IVisitor;
import lang.jhassium.parser.Parser;

/**
 * File : RaiseNode.java
 * Description : None
 * Author : FRITZ Valentin
 * Website : https://github.com/vfrz/JHassium
 * Date : 10/08/2016 22:15
 */
public class RaiseNode extends AstNode {

    public RaiseNode(AstNode expression, SourceLocation location) {
        Children.add(expression);
        Location = location;
    }

    public AstNode getExpression() {
        return Children.get(0);
    }

    public static RaiseNode parse(Parser parser) {
        parser.expectToken(TokenType.Identifier, "raise");
        AstNode expression = ExpressionNode.parse(parser);
        return new RaiseNode(expression, parser.getLocation());
    }

    public void visit(IVisitor visitor) {
        visitor.accept(this);
    }

    public void visitChildren(IVisitor visitor) {
        for (AstNode child : Children)
            child.visit(visitor);
    }
}
