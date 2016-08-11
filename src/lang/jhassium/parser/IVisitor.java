package lang.jhassium.parser;

import lang.jhassium.parser.ast.*;

/**
 * File : IVisitor.java
 * Description : None
 * Author : FRITZ Valentin
 * Website : https://github.com/vfrz/JHassium
 * Date : 10/08/2016 20:57
 */
public interface IVisitor {
    void accept(ArgListNode node);

    void accept(ArrayAccessNode node);

    void accept(ArrayDeclarationNode node);

    void accept(AttributeAccessNode node);

    void accept(BinaryOperationNode node);

    void accept(BoolNode node);

    void accept(BreakNode node);

    void accept(CaseNode node);

    void accept(CharNode node);

    void accept(CodeBlockNode node);

    void accept(ContinueNode node);

    void accept(ClassNode node);

    void accept(ConditionalNode node);

    void accept(DoubleNode node);

    void accept(DictionaryNode node);

    void accept(EnumNode node);

    void accept(ExpressionNode node);

    void accept(ExpressionStatementNode node);

    void accept(ExtendNode node);

    void accept(ForNode node);

    void accept(ForeachNode node);

    void accept(FuncNode node);

    void accept(FunctionCallNode node);

    void accept(IdentifierNode node);

    void accept(Int64Node node);

    void accept(KeyValuePairNode node);

    void accept(LambdaNode node);

    void accept(NewNode node);

    void accept(PropertyNode node);

    void accept(RaiseNode node);

    void accept(ReturnNode node);

    void accept(StatementNode node);

    void accept(StringNode node);

    void accept(SwitchNode node);

    void accept(TernaryOperationNode node);

    void accept(ThisNode node);

    void accept(TraitNode node);

    void accept(TryCatchNode node);

    void accept(TupleNode node);

    void accept(UseNode node);

    void accept(UnaryOperationNode node);

    void accept(WhileNode node);
}
