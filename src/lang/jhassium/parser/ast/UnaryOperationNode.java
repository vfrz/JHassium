package lang.jhassium.parser.ast;

import lang.jhassium.SourceLocation;
import lang.jhassium.parser.AstNode;
import lang.jhassium.parser.IVisitor;
import lang.jhassium.parser.UnaryOperation;

/**
 * File : UnaryOperationNode.java
 * Description : None
 * Authors : FRITZ Valentin, Jacob Misirian & zdimension
 * Website : https://github.com/vfrz/JHassium
 * Date : 10/08/2016 21:24
 */
public class UnaryOperationNode extends AstNode {

    private UnaryOperation unaryOperation;

    public UnaryOperationNode(UnaryOperation unaryOperation, AstNode body, SourceLocation location) {
        this.unaryOperation = unaryOperation;
        Children.add(body);
        Location = location;
    }

    public UnaryOperation getUnaryOperation() {
        return unaryOperation;
    }

    public AstNode getBody() {
        return Children.get(0);
    }

    public void visit(IVisitor visitor) {
        visitor.accept(this);
    }

    public void visitChildren(IVisitor visitor) {
        for (AstNode child : Children)
            child.visit(visitor);
    }
}
