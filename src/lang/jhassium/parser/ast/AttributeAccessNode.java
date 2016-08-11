package lang.jhassium.parser.ast;

import lang.jhassium.SourceLocation;
import lang.jhassium.parser.AstNode;
import lang.jhassium.parser.IVisitor;

/**
 * File : AttributeAccessNode.java
 * Description : None
 * Authors : FRITZ Valentin, Jacob Misirian & zdimension
 * Website : https://github.com/vfrz/JHassium
 * Date : 10/08/2016 21:29
 */
public class AttributeAccessNode extends AstNode {

    String right;

    public AttributeAccessNode(AstNode left, String right, SourceLocation location) {
        Children.add(left);
        this.right = right;
        Location = location;
    }

    public AstNode getLeft() {
        return Children.get(0);
    }

    public String getRight() {
        return right;
    }

    public void visit(IVisitor visitor) {
        visitor.accept(this);
    }

    public void visitChildren(IVisitor visitor) {
        for (AstNode child : Children)
            child.visit(visitor);
    }

}
