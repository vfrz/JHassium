package lang.jhassium.parser.ast;

import lang.jhassium.SourceLocation;
import lang.jhassium.parser.AstNode;
import lang.jhassium.parser.IVisitor;

/**
 * File : IdentifierNode.java
 * Description : None
 * Author : FRITZ Valentin
 * Website : https://github.com/vfrz/JHassium
 * Date : 10/08/2016 21:50
 */
public class IdentifierNode extends AstNode {

    private String identifier;

    public IdentifierNode(String identifier, SourceLocation location) {
        this.identifier = identifier;
        Location = location;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void visit(IVisitor visitor) {
        visitor.accept(this);
    }

    public void visitChildren(IVisitor visitor) {
        for (AstNode child : Children)
            child.visit(visitor);
    }
}
