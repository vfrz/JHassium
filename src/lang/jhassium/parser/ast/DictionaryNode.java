package lang.jhassium.parser.ast;

import lang.jhassium.SourceLocation;
import lang.jhassium.lexer.TokenType;
import lang.jhassium.parser.AstNode;
import lang.jhassium.parser.BinaryOperation;
import lang.jhassium.parser.IVisitor;
import lang.jhassium.parser.Parser;
import lang.jhassium.utils.HassiumLogger;
import lang.jhassium.utils.Helpers;

/**
 * File : DictionaryNode.java
 * Description : None
 * Author : FRITZ Valentin
 * Website : https://github.com/vfrz/JHassium
 * Date : 10/08/2016 21:44
 */
public class DictionaryNode extends AstNode {

    public DictionaryNode(SourceLocation location)
    {
        Location = location;
    }

    public static DictionaryNode parse(Parser parser)
    {
        DictionaryNode dict = new DictionaryNode(parser.getLocation());
        while (!parser.acceptToken(TokenType.RightBrace))
        {
            BinaryOperationNode binop = Helpers.as(ExpressionNode.parse(parser), BinaryOperationNode.class);
            if (binop.getBinaryOperation() == BinaryOperation.Slice)
                dict.Children.add(new KeyValuePairNode(binop.getLeft(), binop.getRight(), binop.Location));
            else
                HassiumLogger.error("Unknown node encountered in dictionary " + binop.getBinaryOperation() + " at " + parser.getLocation());
            parser.acceptToken(TokenType.Comma);
        }

        return dict;
    }

    public void visit(IVisitor visitor) {
        visitor.accept(this);
    }

    public void visitChildren(IVisitor visitor) {
        for (AstNode child : Children)
            child.visit(visitor);
    }
}
