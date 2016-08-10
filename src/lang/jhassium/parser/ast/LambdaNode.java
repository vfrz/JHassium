package lang.jhassium.parser.ast;

import lang.jhassium.SourceLocation;
import lang.jhassium.lexer.TokenType;
import lang.jhassium.parser.AstNode;
import lang.jhassium.parser.IVisitor;
import lang.jhassium.parser.Parser;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * File : LambdaNode.java
 * Description : None
 * Author : FRITZ Valentin
 * Website : https://github.com/vfrz/JHassium
 * Date : 10/08/2016 22:08
 */
public class LambdaNode extends AstNode {

    private List<String> parameters;

    public LambdaNode(List<String> parameters, AstNode body, SourceLocation location) {
        this.parameters = parameters;
        Children.add(body);
        Location = location;
    }

    public List<String> getParameters() {
        return parameters;
    }

    public AstNode getBody() {
        return Children.get(0);
    }

    public static LambdaNode parse(Parser parser) {
        parser.expectToken(TokenType.Identifier, "lambda");
        List<String> parameters = new ArrayList<>();
        ArgListNode args = ArgListNode.parse(parser);
        parameters.addAll(args.Children.stream().map(child -> ((IdentifierNode) child).getIdentifier()).collect(Collectors.toList()));
        AstNode body = StatementNode.parse(parser);
        return new LambdaNode(parameters, body, parser.getLocation());
    }

    public void visit(IVisitor visitor) {
        visitor.accept(this);
    }

    public void visitChildren(IVisitor visitor) {
        for (AstNode child : Children)
            child.visit(visitor);
    }
}
