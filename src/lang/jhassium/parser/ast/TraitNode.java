package lang.jhassium.parser.ast;

import lang.jhassium.SourceLocation;
import lang.jhassium.lexer.TokenType;
import lang.jhassium.parser.AstNode;
import lang.jhassium.parser.IVisitor;
import lang.jhassium.parser.Parser;
import lang.jhassium.parser.Trait;

import java.util.ArrayList;
import java.util.List;

/**
 * File : TraitNode.java
 * Description : None
 * Author : FRITZ Valentin
 * Website : https://github.com/vfrz/JHassium
 * Date : 10/08/2016 22:23
 */
public class TraitNode extends AstNode {

    private String name;
    private List<Trait> traits;

    public TraitNode(List<Trait> traits, String name, SourceLocation location) {
        this.traits = traits;
        this.name = name;
        Location = location;
    }

    public String getName() {
        return name;
    }

    public List<Trait> getTraits() {
        return traits;
    }

    public static TraitNode parse(Parser parser) {
        parser.expectToken(TokenType.Identifier, "trait");
        String name = parser.expectToken(TokenType.Identifier).getValue();
        parser.expectToken(TokenType.LeftBrace);
        List<Trait> traits = new ArrayList<>();
        while (!parser.acceptToken(TokenType.RightBrace)) {
            String type = parser.expectToken(TokenType.Identifier).getValue();
            parser.expectToken(TokenType.Colon);
            String attributeName = parser.expectToken(TokenType.Identifier).getValue();
            traits.add(new Trait(type, attributeName));
            parser.acceptToken(TokenType.Semicolon);
        }
        return new TraitNode(traits, name, parser.getLocation());
    }

    public void visit(IVisitor visitor) {
        visitor.accept(this);
    }

    public void visitChildren(IVisitor visitor) {
        for (AstNode child : Children)
            child.visit(visitor);
    }
}
