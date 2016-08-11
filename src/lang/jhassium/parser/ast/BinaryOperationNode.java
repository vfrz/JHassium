package lang.jhassium.parser.ast;

import lang.jhassium.SourceLocation;
import lang.jhassium.parser.AstNode;
import lang.jhassium.parser.BinaryOperation;
import lang.jhassium.parser.IVisitor;

/**
 * File : BinaryOperationNode.java
 * Description : None
 * Authors : FRITZ Valentin, Jacob Misirian & zdimension
 * Website : https://github.com/vfrz/JHassium
 * Date : 10/08/2016 21:18
 */
public class BinaryOperationNode extends AstNode {

    private BinaryOperation binaryOperation;

    public BinaryOperationNode(BinaryOperation binaryOperation, AstNode left, AstNode right, SourceLocation location) {
        this.binaryOperation = binaryOperation;
        Children.add(left);
        Children.add(right);
        Location = location;
    }

    public BinaryOperation getBinaryOperation() {
        return binaryOperation;
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
