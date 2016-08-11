package lang.jhassium.parser.ast;

import lang.jhassium.SourceLocation;
import lang.jhassium.parser.AstNode;
import lang.jhassium.parser.IVisitor;

/**
 * File : FunctionCallNode.java
 * Description : None
 * Authors : FRITZ Valentin, Jacob Misirian & zdimension
 * Website : https://github.com/vfrz/JHassium
 * Date : 10/08/2016 22:06
 */
public class FunctionCallNode extends AstNode {

    public boolean IsConstructorCall;

    public FunctionCallNode(AstNode target, ArgListNode arguments, SourceLocation location) {
        Children.add(target);
        Children.add(arguments);
        Location = location;
    }

    public AstNode getTarget() {
        return Children.get(0);
    }

    public AstNode getArguments() {
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
