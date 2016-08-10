package lang.jhassium.parser;

import lang.jhassium.SourceLocation;

import java.util.ArrayList;
import java.util.List;

/**
 * File : AstNode.java
 * Description : None
 * Author : FRITZ Valentin
 * Website : https://github.com/vfrz/JHassium
 * Date : 10/08/2016 20:56
 */
public abstract class AstNode {
    public List<AstNode> Children = new ArrayList<>();
    public SourceLocation Location;
    public abstract void visit(IVisitor visitor);
    public abstract void visitChildren(IVisitor visitor);
}
