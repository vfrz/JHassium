package lang.jhassium.parser.ast;

import lang.jhassium.SourceLocation;
import lang.jhassium.lexer.TokenType;
import lang.jhassium.parser.AstNode;
import lang.jhassium.parser.IVisitor;
import lang.jhassium.parser.Parser;

import java.util.ArrayList;

/**
 * File : ExtendNode.java
 * Description : None
 * Author : FRITZ Valentin
 * Website : https://github.com/vfrz/JHassium
 * Date : 10/08/2016 21:51
 */
public class ExtendNode extends AstNode {

    private AstNode target;

    public ExtendNode(AstNode target, ClassNode clazz, SourceLocation location) {
        this.target = target;
        Children.add(clazz);
        Location = location;
    }

    public AstNode getTarget() {
        return target;
    }

    public ClassNode getClazz() {
        return (ClassNode) Children.get(0);
    }

    public static ExtendNode parse(Parser parser) {
        parser.expectToken(TokenType.Identifier, "extend");
        AstNode target = ExpressionNode.parse(parser);
        ClassNode clazz = new ClassNode("", StatementNode.parse(parser), new ArrayList<>(), parser.getLocation());
        return new ExtendNode(target, clazz, parser.getLocation());
    }

    public void visit(IVisitor visitor) {
        visitor.accept(this);
    }

    public void visitChildren(IVisitor visitor) {
        for (AstNode child : Children)
            child.visit(visitor);
    }
}
