package lang.jhassium.parser.ast;

import lang.jhassium.SourceLocation;
import lang.jhassium.parser.AstNode;
import lang.jhassium.parser.IVisitor;

/**
 * File : TernaryOperationNode.java
 * Description : None
 * Authors : FRITZ Valentin, Jacob Misirian & zdimension
 * Website : https://github.com/vfrz/JHassium
 * Date : 10/08/2016 21:55
 */
public class TernaryOperationNode extends AstNode {

    public TernaryOperationNode(AstNode predicate, AstNode trueBody, AstNode elseBody, SourceLocation location) {
        Children.add(predicate);
        Children.add(trueBody);
        Children.add(elseBody);
        Location = location;
    }

    public AstNode getPredicate() {
        return Children.get(0);
    }

    public AstNode getTrueBody() {
        return Children.get(1);
    }

    public AstNode getElseBody() {
        return Children.get(2);
    }

    public void visit(IVisitor visitor) {
        visitor.accept(this);
    }

    public void visitChildren(IVisitor visitor) {
        for (AstNode child : Children)
            child.visit(visitor);
    }
}
