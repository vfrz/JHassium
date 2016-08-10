package lang.jhassium.parser.ast;

import lang.jhassium.SourceLocation;
import lang.jhassium.lexer.TokenType;
import lang.jhassium.parser.AstNode;
import lang.jhassium.parser.IVisitor;
import lang.jhassium.parser.Parser;

/**
 * File : ArrayDeclarationNode.java
 * Description : None
 * Author : FRITZ Valentin
 * Website : https://github.com/vfrz/JHassium
 * Date : 10/08/2016 21:28
 */
public class ArrayDeclarationNode extends AstNode {

    public ArrayDeclarationNode(SourceLocation location) {
        Location = location;
    }

    public static ArrayDeclarationNode parse(Parser parser) {
        ArrayDeclarationNode ret = new ArrayDeclarationNode(parser.getLocation());
        parser.expectToken(TokenType.LeftSquare);

        while (!parser.acceptToken(TokenType.RightSquare)) {
            ret.Children.add(ExpressionNode.parse(parser));
            parser.acceptToken(TokenType.Comma);
        }
        return ret;
    }

    public void visit(IVisitor visitor) {
        visitor.accept(this);
    }

    public void visitChildren(IVisitor visitor) {
        for (AstNode child : Children)
            child.visit(visitor);
    }
}
