package lang.jhassium.parser.ast;

import lang.jhassium.SourceLocation;
import lang.jhassium.lexer.TokenType;
import lang.jhassium.parser.AstNode;
import lang.jhassium.parser.IVisitor;
import lang.jhassium.parser.Parser;

/**
 * File : TupleNode.java
 * Description : None
 * Authors : FRITZ Valentin, Jacob Misirian & zdimension
 * Website : https://github.com/vfrz/JHassium
 * Date : 10/08/2016 22:26
 */
public class TupleNode extends AstNode {

    public TupleNode(SourceLocation location) {
        Location = location;
    }

    public static TupleNode parse(Parser parser, AstNode initialExpression) {
        TupleNode tuple = new TupleNode(parser.getLocation());
        tuple.Children.add(initialExpression);
        while (!parser.matchToken(TokenType.RightParentheses)) {
            tuple.Children.add(ExpressionNode.parse(parser));
            if (!parser.acceptToken(TokenType.Comma))
                break;
        }
        parser.expectToken(TokenType.RightParentheses);
        return tuple;
    }

    public void visit(IVisitor visitor) {
        visitor.accept(this);
    }

    public void visitChildren(IVisitor visitor) {
        for (AstNode child : Children)
            child.visit(visitor);
    }
}
