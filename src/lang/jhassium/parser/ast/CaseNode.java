package lang.jhassium.parser.ast;

import lang.jhassium.SourceLocation;
import lang.jhassium.lexer.TokenType;
import lang.jhassium.parser.AstNode;
import lang.jhassium.parser.IVisitor;
import lang.jhassium.parser.Parser;

import java.util.ArrayList;
import java.util.List;

/**
 * File : CaseNode.java
 * Description : None
 * Author : FRITZ Valentin
 * Website : https://github.com/vfrz/JHassium
 * Date : 10/08/2016 21:33
 */
public class CaseNode extends AstNode {

    private AstNode body;

    public CaseNode(List<AstNode> predicates, AstNode body, SourceLocation location) {
        Children.addAll(predicates);
        this.body = body;
        Location = location;
    }

    public AstNode getBody() {
        return body;
    }

    public static CaseNode parse(Parser parser) {
        parser.expectToken(TokenType.Identifier, "case");
        List<AstNode> predicates = new ArrayList<>();
        while (!parser.matchToken(TokenType.LeftBrace)) {
            parser.acceptToken(TokenType.Comma);
            predicates.add(ExpressionNode.parse(parser));
        }
        AstNode body = StatementNode.parse(parser);

        return new CaseNode(predicates, body, parser.getLocation());
    }

    public void visit(IVisitor visitor) {
        visitor.accept(this);
    }

    public void visitChildren(IVisitor visitor) {
        for (AstNode child : Children)
            child.visit(visitor);
    }
}
