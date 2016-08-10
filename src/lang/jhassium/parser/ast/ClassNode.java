package lang.jhassium.parser.ast;

import lang.jhassium.SourceLocation;
import lang.jhassium.lexer.TokenType;
import lang.jhassium.parser.AstNode;
import lang.jhassium.parser.IVisitor;
import lang.jhassium.parser.Parser;

import java.util.ArrayList;
import java.util.List;

/**
 * File : ClassNode.java
 * Description : None
 * Author : FRITZ Valentin
 * Website : https://github.com/vfrz/JHassium
 * Date : 10/08/2016 21:36
 */
public class ClassNode extends AstNode {

    private String name;
    private List<String> inherits;

    public ClassNode(String name, AstNode body, List<String> inherits, SourceLocation location) {
        this.name = name;
        Children.add(body);
        this.inherits = inherits;
        Location = location;
    }

    public String getName() {
        return name;
    }

    public AstNode getBody() {
        return Children.get(0);
    }

    public List<String> getInherits() {
        return inherits;
    }

    public static ClassNode parse(Parser parser) {
        parser.expectToken(TokenType.Identifier, "class");
        String name = parser.expectToken(TokenType.Identifier).getValue();
        List<String> inherits = new ArrayList<>();
        if (parser.acceptToken(TokenType.Colon)) {
            inherits.add(parser.expectToken(TokenType.Identifier).getValue());
            while (parser.acceptToken(TokenType.Comma))
                inherits.add(parser.expectToken(TokenType.Identifier).getValue());
        }
        AstNode body = StatementNode.parse(parser);

        return new ClassNode(name, body, inherits, parser.getLocation());
    }

    public void visit(IVisitor visitor) {
        visitor.accept(this);
    }

    public void visitChildren(IVisitor visitor) {
        for (AstNode child : Children)
            child.visit(visitor);
    }
}
