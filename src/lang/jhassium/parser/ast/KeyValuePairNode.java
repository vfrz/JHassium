package lang.jhassium.parser.ast;

import lang.jhassium.SourceLocation;
import lang.jhassium.parser.AstNode;
import lang.jhassium.parser.IVisitor;

/**
 * File : KeyValuePairNode.java
 * Description : None
 * Author : FRITZ Valentin
 * Website : https://github.com/vfrz/JHassium
 * Date : 10/08/2016 21:46
 */
public class KeyValuePairNode extends AstNode {

    public KeyValuePairNode(AstNode left, AstNode right, SourceLocation location)
    {
        Children.add(left);
        Children.add(right);
        Location = location;
    }

    public AstNode getLeft() {
        return Children.get(0);
    }

    public AstNode getRight() {
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
