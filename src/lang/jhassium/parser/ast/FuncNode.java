package lang.jhassium.parser.ast;

import lang.jhassium.SourceLocation;
import lang.jhassium.lexer.TokenType;
import lang.jhassium.parser.AstNode;
import lang.jhassium.parser.IVisitor;
import lang.jhassium.parser.Parameter;
import lang.jhassium.parser.Parser;

import java.util.ArrayList;
import java.util.List;

/**
 * File : FuncNode.java
 * Description : None
 * Author : FRITZ Valentin
 * Website : https://github.com/vfrz/JHassium
 * Date : 10/08/2016 21:59
 */
public class FuncNode extends AstNode {

    private String name;
    private String returnType;
    private List<Parameter> parameters;
    private String sourceRepresentation;

    public FuncNode(String name, List<Parameter> parameters, AstNode body, String sourceRepresentation, String returnType, SourceLocation location) {
        this.name = name;
        this.returnType = returnType;
        this.parameters = parameters;
        Children.add(body);
        this.sourceRepresentation = sourceRepresentation;
        Location = location;
    }

    public String getName() {
        return name;
    }

    public String getReturnType() {
        return returnType;
    }

    public List<Parameter> getParameters() {
        return parameters;
    }

    public String getSourceRepresentation() {
        return sourceRepresentation;
    }

    public static FuncNode parse(Parser parser) {
        parser.expectToken(TokenType.Identifier, "func");
        String name = parser.expectToken(TokenType.Identifier).getValue();
        parser.expectToken(TokenType.LeftParentheses);

        List<Parameter> parameters = new ArrayList<>();
        if (!parser.acceptToken(TokenType.RightParentheses)) {
            while (!parser.acceptToken(TokenType.RightParentheses)) {
                String paramName = parser.expectToken(TokenType.Identifier).getValue();
                if (parser.acceptToken(TokenType.Colon))
                    parameters.add(new Parameter(paramName, parser.expectToken(TokenType.Identifier).getValue()));
                else
                    parameters.add(new Parameter(paramName));
                parser.acceptToken(TokenType.Comma);
            }
        }
        String returnType = parser.acceptToken(TokenType.Colon) ? parser.expectToken(TokenType.Identifier).getValue() : "";

        AstNode body = StatementNode.parse(parser);

        StringBuilder sourceRepresentation = new StringBuilder(String.format("func %1s (%2s", name, parameters.size() != 0 ? parameters.get(0).toString() : ""));
        for (int i = 1; i < parameters.size(); i++)
            sourceRepresentation.append(", " + parameters.get(i).toString());
        sourceRepresentation.append(")");
        if (returnType != "")
            sourceRepresentation.append(" : " + returnType);

        return new FuncNode(name, parameters, body, sourceRepresentation.toString(), returnType, parser.getLocation());
    }

    public void visit(IVisitor visitor) {
        visitor.accept(this);
    }

    public void visitChildren(IVisitor visitor) {
        for (AstNode child : Children)
            child.visit(visitor);
    }

}
