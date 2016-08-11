package lang.jhassium.parser.ast;

import lang.jhassium.SourceLocation;
import lang.jhassium.lexer.TokenType;
import lang.jhassium.parser.*;
import lang.jhassium.utils.HassiumLogger;
import lang.jhassium.utils.Helpers;

/**
 * File : ExpressionNode.java
 * Description : None
 * Author : FRITZ Valentin
 * Website : https://github.com/vfrz/JHassium
 * Date : 10/08/2016 21:12
 */
public class ExpressionNode extends AstNode {

    public ExpressionNode(SourceLocation location) {
        Location = location;
    }

    public static AstNode parse(Parser parser) {
        return parseAssignment(parser);
    }

    private static AstNode parseAssignment(Parser parser) {
        AstNode left = parseConditional(parser);

        if (parser.acceptToken(TokenType.Assignment, "="))
            return new BinaryOperationNode(BinaryOperation.Assignment, left, parseAssignment(parser), parser.getLocation());
        else if (parser.acceptToken(TokenType.Assignment, "+="))
            return new BinaryOperationNode(BinaryOperation.Assignment, left, new BinaryOperationNode(BinaryOperation.Addition, left, parseAssignment(parser), parser.getLocation()), parser.getLocation());
        else if (parser.acceptToken(TokenType.Assignment, "-="))
            return new BinaryOperationNode(BinaryOperation.Assignment, left, new BinaryOperationNode(BinaryOperation.Subtraction, left, parseAssignment(parser), parser.getLocation()), parser.getLocation());
        else if (parser.acceptToken(TokenType.Assignment, "*="))
            return new BinaryOperationNode(BinaryOperation.Assignment, left, new BinaryOperationNode(BinaryOperation.Multiplication, left, parseAssignment(parser), parser.getLocation()), parser.getLocation());
        else if (parser.acceptToken(TokenType.Assignment, "/="))
            return new BinaryOperationNode(BinaryOperation.Assignment, left, new BinaryOperationNode(BinaryOperation.Division, left, parseAssignment(parser), parser.getLocation()), parser.getLocation());
        else if (parser.acceptToken(TokenType.Assignment, "%="))
            return new BinaryOperationNode(BinaryOperation.Assignment, left, new BinaryOperationNode(BinaryOperation.Modulus, left, parseAssignment(parser), parser.getLocation()), parser.getLocation());
        else if (parser.acceptToken(TokenType.Assignment, "^="))
            return new BinaryOperationNode(BinaryOperation.Assignment, left, new BinaryOperationNode(BinaryOperation.XOR, left, parseAssignment(parser), parser.getLocation()), parser.getLocation());
        else if (parser.acceptToken(TokenType.Assignment, "|="))
            return new BinaryOperationNode(BinaryOperation.Assignment, left, new BinaryOperationNode(BinaryOperation.OR, left, parseAssignment(parser), parser.getLocation()), parser.getLocation());
        else if (parser.acceptToken(TokenType.Assignment, "&="))
            return new BinaryOperationNode(BinaryOperation.Assignment, left, new BinaryOperationNode(BinaryOperation.XAnd, left, parseAssignment(parser), parser.getLocation()), parser.getLocation());
        else if (parser.acceptToken(TokenType.BinaryOperation, "<->"))
            return new BinaryOperationNode(BinaryOperation.Swap, left, parseAssignment(parser), parser.getLocation());
        else
            return left;
    }

    private static AstNode parseConditional(Parser parser) {
        AstNode left = parseLogicalOr(parser);

        while (parser.acceptToken(TokenType.Question)) {
            AstNode body = parse(parser);
            if (body instanceof BinaryOperationNode) {
                BinaryOperationNode binop = Helpers.as(body, BinaryOperationNode.class);
                if (binop.getBinaryOperation() == BinaryOperation.Slice)
                    left = new TernaryOperationNode(left, binop.getLeft(), binop.getRight(), parser.getLocation());
                else
                    HassiumLogger.error("Expected conditions after ? ! At " + parser.getLocation());
            } else
                HassiumLogger.error("Expected conditions after ? ! At " + parser.getLocation());
        }

        return left;
    }

    private static AstNode parseLogicalOr(Parser parser) {
        AstNode left = parseLogicalAnd(parser);
        while (parser.matchToken(TokenType.BinaryOperation)) {
            switch (parser.getToken().getValue()) {
                case "||":
                    parser.acceptToken(TokenType.BinaryOperation);
                    left = new BinaryOperationNode(BinaryOperation.LogicalOr, left, parseLogicalAnd(parser), parser.getLocation());
                    continue;
                case "??":
                    parser.acceptToken(TokenType.BinaryOperation);
                    left = new BinaryOperationNode(BinaryOperation.NullCoalescing, left, parseLogicalAnd(parser), parser.getLocation());
                    continue;
                default:
                    break;
            }
            break;
        }
        return left;
    }

    private static AstNode parseLogicalAnd(Parser parser) {
        AstNode left = parseEquality(parser);
        while (parser.matchToken(TokenType.BinaryOperation)) {
            switch (parser.getToken().getValue()) {
                case "&&":
                    parser.acceptToken(TokenType.BinaryOperation);
                    left = new BinaryOperationNode(BinaryOperation.LogicalAnd, left, parseEquality(parser), parser.getLocation());
                    continue;
                default:
                    break;
            }
            break;
        }
        return left;
    }

    private static AstNode parseEquality(Parser parser) {
        AstNode left = parseIn(parser);
        while (parser.matchToken(TokenType.Comparison)) {
            switch (parser.getToken().getValue()) {
                case "!=":
                    parser.acceptToken(TokenType.Comparison);
                    left = new BinaryOperationNode(BinaryOperation.NotEqualTo, left, parseIn(parser), parser.getLocation());
                    continue;
                case "==":
                    parser.acceptToken(TokenType.Comparison);
                    left = new BinaryOperationNode(BinaryOperation.EqualTo, left, parseIn(parser), parser.getLocation());
                    continue;
                default:
                    break;
            }
            break;
        }
        return left;
    }

    private static AstNode parseIn(Parser parser) {
        AstNode left = parseComparison(parser);
        while (parser.acceptToken(TokenType.Identifier, "in"))
            left = new BinaryOperationNode(BinaryOperation.In, left, parseComparison(parser), parser.getLocation());
        return left;
    }

    private static AstNode parseComparison(Parser parser) {
        AstNode left = parseOr(parser);
        while (parser.matchToken(TokenType.Comparison)) {
            switch (parser.getToken().getValue()) {
                case ">":
                    parser.acceptToken(TokenType.Comparison);
                    left = new BinaryOperationNode(BinaryOperation.GreaterThan, left, parseOr(parser), parser.getLocation());
                    continue;
                case "<":
                    parser.acceptToken(TokenType.Comparison);
                    left = new BinaryOperationNode(BinaryOperation.LesserThan, left, parseOr(parser), parser.getLocation());
                    continue;
                case ">=":
                    parser.acceptToken(TokenType.Comparison);
                    left = new BinaryOperationNode(BinaryOperation.GreaterThanOrEqual, left, parseOr(parser), parser.getLocation());
                    continue;
                case "<=":
                    parser.acceptToken(TokenType.Comparison);
                    left = new BinaryOperationNode(BinaryOperation.LesserThanOrEqual, left, parseOr(parser), parser.getLocation());
                    continue;
                case "!=":
                    parser.acceptToken(TokenType.Comparison);
                    left = new BinaryOperationNode(BinaryOperation.NotEqualTo, left, parseOr(parser), parser.getLocation());
                    continue;
                case "==":
                    parser.acceptToken(TokenType.Comparison);
                    left = new BinaryOperationNode(BinaryOperation.EqualTo, left, parseOr(parser), parser.getLocation());
                    continue;
                default:
                    break;
            }
            break;
        }
        return left;
    }

    private static AstNode parseOr(Parser parser) {
        AstNode left = parseXor(parser);
        while (parser.acceptToken(TokenType.BinaryOperation, "|"))
            left = new BinaryOperationNode(BinaryOperation.OR, left, parseXor(parser), parser.getLocation());
        return left;
    }

    private static AstNode parseXor(Parser parser) {
        AstNode left = parseAnd(parser);
        while (parser.acceptToken(TokenType.BinaryOperation, "^"))
            left = new BinaryOperationNode(BinaryOperation.XOR, left, parseAnd(parser), parser.getLocation());
        return left;
    }

    private static AstNode parseAnd(Parser parser) {
        AstNode left = parseBitShift(parser);
        while (parser.acceptToken(TokenType.BinaryOperation, "&"))
            left = new BinaryOperationNode(BinaryOperation.XAnd, left, parseBitShift(parser), parser.getLocation());
        return left;
    }

    private static AstNode parseBitShift(Parser parser) {
        AstNode left = parseAdditive(parser);
        while (parser.matchToken(TokenType.BinaryOperation)) {
            switch (parser.getToken().getValue()) {
                case "<<":
                    parser.acceptToken(TokenType.BinaryOperation);
                    left = new BinaryOperationNode(BinaryOperation.BitShiftLeft, left, parseAdditive(parser), parser.getLocation());
                    continue;
                case ">>":
                    parser.acceptToken(TokenType.BinaryOperation);
                    left = new BinaryOperationNode(BinaryOperation.BitShiftRight, left, parseAdditive(parser), parser.getLocation());
                    continue;
                default:
                    break;
            }
            break;
        }
        return left;
    }

    private static AstNode parseAdditive(Parser parser) {
        AstNode left = parseMultiplicative(parser);
        while (parser.matchToken(TokenType.BinaryOperation)) {
            switch (parser.getToken().getValue()) {
                case "+":
                    parser.acceptToken(TokenType.BinaryOperation);
                    left = new BinaryOperationNode(BinaryOperation.Addition, left, parseMultiplicative(parser), parser.getLocation());
                    continue;
                case "-":
                    parser.acceptToken(TokenType.BinaryOperation);
                    left = new BinaryOperationNode(BinaryOperation.Subtraction, left, parseMultiplicative(parser), parser.getLocation());
                    continue;
                default:
                    break;
            }
            break;
        }
        return left;
    }

    private static AstNode parseMultiplicative(Parser parser) {
        AstNode left = parseUnary(parser);
        while (parser.matchToken(TokenType.BinaryOperation) || parser.matchToken(TokenType.Colon)) {
            switch (parser.getToken().getValue()) {
                case "is":
                    parser.acceptToken(TokenType.BinaryOperation);
                    left = new BinaryOperationNode(BinaryOperation.Is, left, parseMultiplicative(parser), parser.getLocation());
                    continue;
                case "*":
                    parser.acceptToken(TokenType.BinaryOperation);
                    left = new BinaryOperationNode(BinaryOperation.Multiplication, left, parseMultiplicative(parser), parser.getLocation());
                    continue;
                case "**":
                    parser.acceptToken(TokenType.BinaryOperation);
                    left = new BinaryOperationNode(BinaryOperation.Power, left, parseMultiplicative(parser), parser.getLocation());
                    continue;
                case "..":
                    parser.acceptToken(TokenType.BinaryOperation);
                    left = new BinaryOperationNode(BinaryOperation.Range, left, parseMultiplicative(parser), parser.getLocation());
                    continue;
                case "/":
                    parser.acceptToken(TokenType.BinaryOperation);
                    left = new BinaryOperationNode(BinaryOperation.Division, left, parseMultiplicative(parser), parser.getLocation());
                    continue;
                case "//":
                    parser.acceptToken(TokenType.BinaryOperation);
                    left = new BinaryOperationNode(BinaryOperation.IntegerDivision, left, parseMultiplicative(parser), parser.getLocation());
                    continue;
                case "%":
                    parser.acceptToken(TokenType.BinaryOperation);
                    left = new BinaryOperationNode(BinaryOperation.Modulus, left, parseMultiplicative(parser), parser.getLocation());
                    continue;
                case ":":
                    parser.acceptToken(TokenType.Colon);
                    left = new BinaryOperationNode(BinaryOperation.Slice, left, parseMultiplicative(parser), parser.getLocation());
                    continue;
                default:
                    break;
            }
            break;
        }
        return left;
    }

    private static AstNode parseUnary(Parser parser) {
        if (parser.matchToken(TokenType.UnaryOperation)) {
            switch (parser.getToken().getValue()) {
                case "!":
                    parser.expectToken(TokenType.UnaryOperation);
                    return new UnaryOperationNode(UnaryOperation.Not, parseUnary(parser), parser.getLocation());
                case "++":
                    parser.expectToken(TokenType.UnaryOperation);
                    return new UnaryOperationNode(UnaryOperation.PreIncrement, parseUnary(parser), parser.getLocation());
                case "--":
                    parser.expectToken(TokenType.UnaryOperation);
                    return new UnaryOperationNode(UnaryOperation.PreDecrement, parseUnary(parser), parser.getLocation());
                case "~":
                    parser.expectToken(TokenType.UnaryOperation);
                    return new UnaryOperationNode(UnaryOperation.BitwiseComplement, parseUnary(parser), parser.getLocation());
                case "::":
                    parser.expectToken(TokenType.UnaryOperation);
                    return new UnaryOperationNode(UnaryOperation.Skip, parseUnary(parser), parser.getLocation());
            }
        } else if (parser.matchToken(TokenType.BinaryOperation)) {
            switch (parser.getToken().getValue()) {
                case "-":
                    parser.expectToken(TokenType.BinaryOperation);
                    return new UnaryOperationNode(UnaryOperation.Negate, parseUnary(parser), parser.getLocation());
            }
        }
        return parseAccess(parser);
    }

    private static AstNode parseAccess(Parser parser) {
        return parseAccess(parser, parseTerm(parser));
    }

    private static AstNode parseAccess(Parser parser, AstNode left) {
        if (parser.matchToken(TokenType.LeftParentheses))
            return parseAccess(parser, new FunctionCallNode(left, ArgListNode.parse(parser), parser.getLocation()));
        else if (parser.acceptToken(TokenType.LeftSquare)) {
            AstNode expression = parse(parser);
            parser.expectToken(TokenType.RightSquare);
            return parseAccess(parser, new ArrayAccessNode(left, expression, parser.getLocation()));
        } else if (parser.acceptToken(TokenType.UnaryOperation, "++"))
            return new UnaryOperationNode(UnaryOperation.PostIncrement, left, parser.getLocation());
        else if (parser.acceptToken(TokenType.UnaryOperation, "--"))
            return new UnaryOperationNode(UnaryOperation.PostDecrement, left, parser.getLocation());
        else if (parser.acceptToken(TokenType.BinaryOperation, ".")) {
            String identifier = parser.expectToken(TokenType.Identifier).getValue();
            return parseAccess(parser, new AttributeAccessNode(left, identifier, parser.getLocation()));
        } else
            return left;
    }

    private static AstNode parseTerm(Parser parser) {
        if (parser.acceptToken(TokenType.Identifier, "new"))
            return new NewNode((FunctionCallNode) parseAccess(parser), parser.getLocation());
        else if (parser.acceptToken(TokenType.Identifier, "this"))
            return new ThisNode(parser.getLocation());
        else if (parser.matchToken(TokenType.Identifier, "true") || parser.matchToken(TokenType.Identifier, "false"))
            return new BoolNode(parser.expectToken(TokenType.Identifier).getValue(), parser.getLocation());
        else if (parser.matchToken(TokenType.Identifier, "lambda"))
            return LambdaNode.parse(parser);
        else if (parser.matchToken(TokenType.Identifier))
            return new IdentifierNode(parser.expectToken(TokenType.Identifier).getValue(), parser.getLocation());
        else if (parser.matchToken(TokenType.Double))
            return new DoubleNode(Double.parseDouble(parser.expectToken(TokenType.Double).getValue()), parser.getLocation());
        else if (parser.matchToken(TokenType.Int64))
            return new Int64Node(Long.parseLong(parser.expectToken(TokenType.Int64).getValue()), parser.getLocation());
        else if (parser.matchToken(TokenType.String))
            return new StringNode(parser.expectToken(TokenType.String).getValue(), parser.getLocation());
        else if (parser.matchToken(TokenType.Char))
            return new CharNode(parser.expectToken(TokenType.Char).getValue(), parser.getLocation());
        else if (parser.acceptToken(TokenType.LeftParentheses)) {
            AstNode expression = parse(parser);
            if (parser.acceptToken(TokenType.Comma))
                return TupleNode.parse(parser, expression);
            parser.expectToken(TokenType.RightParentheses);
            return expression;
        } else if (parser.acceptToken(TokenType.LeftBrace))
            return DictionaryNode.parse(parser);
        else if (parser.matchToken(TokenType.LeftSquare))
            return ArrayDeclarationNode.parse(parser);
        else if (parser.acceptToken(TokenType.Semicolon))
            return new StatementNode(parser.getLocation());
        else
            HassiumLogger.error(String.format("Unexpected type %1s with value %2s encountered in parser! At %3s", parser.getToken().getTokenType(), parser.getToken().getValue(), parser.getLocation()));
        return null; // Not reachable for compilation only
    }

    public void visit(IVisitor visitor) {
        visitor.accept(this);
    }

    public void visitChildren(IVisitor visitor) {
        for (AstNode child : Children)
            child.visit(visitor);
    }
}
