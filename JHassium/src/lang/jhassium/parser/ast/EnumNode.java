package lang.jhassium.parser.ast;

import lang.jhassium.SourceLocation;
import lang.jhassium.lexer.TokenType;
import lang.jhassium.parser.AstNode;
import lang.jhassium.parser.IVisitor;
import lang.jhassium.parser.Parser;

import java.util.ArrayList;
import java.util.List;

/**
 * File : EnumNode.java
 * Description : None
 * Authors : FRITZ Valentin, Jacob Misirian & zdimension
 * Website : https://github.com/vfrz/JHassium
 * Date : 10/08/2016 21:48
 */
public class EnumNode extends AstNode {

    private String name;

    public EnumNode(String name, List<IdentifierNode> members, SourceLocation location) {
        this.name = name;
        for (IdentifierNode child : members)
            Children.add(child);
        Location = location;
    }

    public static EnumNode parse(Parser parser) {
        parser.expectToken(TokenType.Identifier, "enum");
        String name = parser.expectToken(TokenType.Identifier).getValue();
        parser.expectToken(TokenType.LeftBrace);
        List<IdentifierNode> members = new ArrayList<>();
        while (parser.matchToken(TokenType.Identifier)) {
            members.add(new IdentifierNode(parser.expectToken(TokenType.Identifier).getValue(), parser.getLocation()));
            if (!parser.acceptToken(TokenType.Comma))
                break;
        }
        parser.expectToken(TokenType.RightBrace);

        return new EnumNode(name, members, parser.getLocation());
    }

    public String getName() {
        return name;
    }

    public void visit(IVisitor visitor) {
        visitor.accept(this);
    }

    public void visitChildren(IVisitor visitor) {
        for (AstNode child : Children)
            child.visit(visitor);
    }
}
