package lang.jhassium.parser.ast;

import lang.jhassium.SourceLocation;
import lang.jhassium.lexer.TokenType;
import lang.jhassium.parser.AstNode;
import lang.jhassium.parser.IVisitor;
import lang.jhassium.parser.Parser;

/**
 * File : StatementNode.java
 * Description : None
 * Author : FRITZ Valentin
 * Website : https://github.com/vfrz/JHassium
 * Date : 10/08/2016 21:08
 */
public class StatementNode extends AstNode {

    public StatementNode(SourceLocation location)
    {
        Location = location;
    }

    public static AstNode parse(Parser parser)
    {
        if (parser.matchToken(TokenType.Identifier, "func"))
            return FuncNode.parse(parser);
        else if (parser.matchToken(TokenType.Identifier, "class"))
            return ClassNode.parse(parser);
        else if (parser.matchToken(TokenType.Identifier, "extend"))
            return ExtendNode.parse(parser);
        else if (parser.matchToken(TokenType.Identifier, "trait"))
            return TraitNode.parse(parser);
        else if (parser.matchToken(TokenType.Identifier, "if"))
            return ConditionalNode.parse(parser);
        else if (parser.matchToken(TokenType.Identifier, "while"))
            return WhileNode.parse(parser);
        else if (parser.matchToken(TokenType.Identifier, "until"))
            return WhileNode.parse(parser, true);
        else if (parser.matchToken(TokenType.Identifier, "for"))
            return ForNode.parse(parser);
        else if (parser.matchToken(TokenType.Identifier, "foreach"))
            return ForeachNode.parse(parser);
        else if (parser.acceptToken(TokenType.Identifier, "break"))
            return new BreakNode(parser.getLocation());
        else if (parser.acceptToken(TokenType.Identifier, "continue"))
            return new ContinueNode(parser.getLocation());
        else if (parser.matchToken(TokenType.Identifier, "return"))
            return ReturnNode.parse(parser);
        else if (parser.matchToken(TokenType.Identifier, "use"))
            return UseNode.parse(parser);
        else if (parser.matchToken(TokenType.Identifier, "enum"))
            return EnumNode.parse(parser);
        else if (parser.matchToken(TokenType.Identifier, "switch"))
            return SwitchNode.parse(parser);
        else if (parser.matchToken(TokenType.Identifier, "try"))
            return TryCatchNode.parse(parser);
        else if (parser.matchToken(TokenType.Identifier, "raise"))
            return RaiseNode.parse(parser);
        else if (parser.acceptToken(TokenType.LeftBrace))
        {
            CodeBlockNode block = new CodeBlockNode(parser.getLocation());
            while (!parser.acceptToken(TokenType.RightBrace))
            {
                block.Children.add(StatementNode.parse(parser));
                parser.acceptToken(TokenType.Semicolon);
            }
            return block;
        }
        else if (parser.matchToken(TokenType.Identifier) && parser.getToken(1).getTokenType() == TokenType.LeftBrace)
            return PropertyNode.parse(parser);
        else
            return ExpressionStatementNode.parse(parser);
    }

    public void visit(IVisitor visitor)
    {
        visitor.accept(this);
    }

    public void visitChildren(IVisitor visitor)
    {
        for (AstNode child : Children)
            child.visit(visitor);
    }
}
