package lang.jhassium.parser.ast;

import lang.jhassium.SourceLocation;
import lang.jhassium.lexer.TokenType;
import lang.jhassium.parser.AstNode;
import lang.jhassium.parser.IVisitor;
import lang.jhassium.parser.Parser;

/**
 * File : ArgListNode.java
 * Description : None
 * Authors : FRITZ Valentin, Jacob Misirian & zdimension
 * Website : https://github.com/vfrz/JHassium
 * Date : 10/08/2016 21:11
 */
public class ArgListNode extends AstNode {

    public ArgListNode(SourceLocation location) {
        Location = location;
    }

    public static ArgListNode parse(Parser parser) {
        ArgListNode ret = new ArgListNode(parser.getLocation());
        parser.expectToken(TokenType.LeftParentheses);

        while (!parser.matchToken(TokenType.RightParentheses)) {
            ret.Children.add(ExpressionNode.parse(parser));
            if (!parser.acceptToken(TokenType.Comma))
                break;
        }
        parser.expectToken(TokenType.RightParentheses);
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
