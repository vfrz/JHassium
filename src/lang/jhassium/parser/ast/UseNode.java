package lang.jhassium.parser.ast;

import lang.jhassium.SourceLocation;
import lang.jhassium.lexer.TokenType;
import lang.jhassium.parser.AstNode;
import lang.jhassium.parser.IVisitor;
import lang.jhassium.parser.Parser;

/**
 * File : UseNode.java
 * Description : None
 * Author : FRITZ Valentin
 * Website : https://github.com/vfrz/JHassium
 * Date : 10/08/2016 22:27
 */
public class UseNode extends AstNode {

    public UseNode(AstNode target, SourceLocation location) {
        Children.add(target);
    }

    public AstNode getTarget() {
        return Children.get(0);
    }

    public static UseNode parse(Parser parser) {
        parser.expectToken(TokenType.Identifier, "use");
        AstNode target = ExpressionNode.parse(parser);
        return new UseNode(target, parser.getLocation());
    }

    public void visit(IVisitor visitor) {
        visitor.accept(this);
    }

    public void visitChildren(IVisitor visitor) {
        for (AstNode child : Children)
            child.visit(visitor);
    }
}
