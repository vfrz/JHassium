package lang.jhassium.parser.ast;

import lang.jhassium.SourceLocation;
import lang.jhassium.parser.AstNode;
import lang.jhassium.parser.IVisitor;

/**
 * File : ArrayAccessNode.java
 * Description : None
 * Authors : FRITZ Valentin, Jacob Misirian & zdimension
 * Website : https://github.com/vfrz/JHassium
 * Date : 10/08/2016 21:27
 */
public class ArrayAccessNode extends AstNode {

    public ArrayAccessNode(AstNode target, AstNode expression, SourceLocation location) {
        Children.add(target);
        Children.add(expression);
        Location = location;
    }

    public AstNode getTarget() {
        return Children.get(0);
    }

    public AstNode getExpression() {
        return Children.get(1);
    }

    public void visit(IVisitor visitor) {
        visitor.accept(this);
    }

    public void visitChildren(IVisitor visitor) {
        for (AstNode child : Children)
            child.visit(visitor);
    }
}
