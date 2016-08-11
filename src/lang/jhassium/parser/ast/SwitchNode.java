package lang.jhassium.parser.ast;

import lang.jhassium.SourceLocation;
import lang.jhassium.lexer.TokenType;
import lang.jhassium.parser.AstNode;
import lang.jhassium.parser.IVisitor;
import lang.jhassium.parser.Parser;

import java.util.ArrayList;
import java.util.List;

/**
 * File : SwitchNode.java
 * Description : None
 * Authors : FRITZ Valentin, Jacob Misirian & zdimension
 * Website : https://github.com/vfrz/JHassium
 * Date : 10/08/2016 22:19
 */
public class SwitchNode extends AstNode {

    private AstNode predicate;
    private AstNode defaultCase;

    public SwitchNode(AstNode predicate, List<CaseNode> cases, AstNode defaultCase, SourceLocation location) {
        this.predicate = predicate;
        Children.addAll(cases);
        this.defaultCase = defaultCase;
        Location = location;
    }

    public AstNode getPredicate() {
        return predicate;
    }

    public AstNode getDefaultCase() {
        return defaultCase;
    }

    public static SwitchNode parse(Parser parser) {
        parser.expectToken(TokenType.Identifier, "switch");
        parser.expectToken(TokenType.LeftParentheses);
        AstNode predicate = ExpressionNode.parse(parser);
        parser.expectToken(TokenType.RightParentheses);
        List<CaseNode> cases = new ArrayList<>();
        parser.expectToken(TokenType.LeftBrace);
        while (parser.matchToken(TokenType.Identifier, "case"))
            cases.add(CaseNode.parse(parser));
        AstNode defaultCase = null;
        if (parser.acceptToken(TokenType.Identifier, "default"))
            defaultCase = StatementNode.parse(parser);
        parser.expectToken(TokenType.RightBrace);
        return new SwitchNode(predicate, cases, defaultCase, parser.getLocation());
    }

    public void visit(IVisitor visitor) {
        visitor.accept(this);
    }

    public void visitChildren(IVisitor visitor) {
        for (AstNode child : Children)
            child.visit(visitor);
    }
}
