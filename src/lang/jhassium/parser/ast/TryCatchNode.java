package lang.jhassium.parser.ast;

import lang.jhassium.SourceLocation;
import lang.jhassium.lexer.TokenType;
import lang.jhassium.parser.AstNode;
import lang.jhassium.parser.IVisitor;
import lang.jhassium.parser.Parser;

/**
 * File : TryCatchNode.java
 * Description : None
 * Authors : FRITZ Valentin, Jacob Misirian & zdimension
 * Website : https://github.com/vfrz/JHassium
 * Date : 10/08/2016 22:25
 */
public class TryCatchNode extends AstNode {

    public TryCatchNode(AstNode tryBody, AstNode catchBody, SourceLocation location) {
        Children.add(tryBody);
        Children.add(catchBody);
        Location = location;
    }

    public static TryCatchNode parse(Parser parser) {
        parser.expectToken(TokenType.Identifier, "try");
        AstNode tryBody = StatementNode.parse(parser);
        parser.expectToken(TokenType.Identifier, "catch");
        AstNode catchBody = StatementNode.parse(parser);
        return new TryCatchNode(tryBody, catchBody, parser.getLocation());
    }

    public AstNode getTryBody() {
        return Children.get(0);
    }

    public AstNode getCatchBody() {
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
