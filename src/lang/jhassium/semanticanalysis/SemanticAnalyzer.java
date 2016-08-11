package lang.jhassium.semanticanalysis;

import lang.jhassium.parser.AstNode;
import lang.jhassium.parser.IVisitor;
import lang.jhassium.parser.ast.*;

/**
 * File : SemanticAnalyzer.java
 * Description : None
 * Authors : FRITZ Valentin, Jacob Misirian & zdimension
 * Website : https://github.com/vfrz/JHassium
 * Date : 10/08/2016 22:33
 */
public class SemanticAnalyzer implements IVisitor {

    private AstNode code;
    private SymbolTable result;

    public SymbolTable Analyze(AstNode ast) {
        code = ast;
        result = new SymbolTable();
        result.enterScope();
        code.visitChildren(this);
        return result;
    }

    public void accept(ArgListNode node) {
        node.visitChildren(this);
    }

    public void accept(ArrayAccessNode node) {
    }

    public void accept(ArrayDeclarationNode node) {
    }

    public void accept(AttributeAccessNode node) {
    }

    public void accept(BinaryOperationNode node) {
        node.visitChildren(this);
    }

    public void accept(BoolNode node) {
    }

    public void accept(BreakNode node) {
    }

    public void accept(CaseNode node) {
        node.visitChildren(this);
    }

    public void accept(ClassNode node) {
        node.visitChildren(this);
    }

    public void accept(CodeBlockNode node) {
        result.enterScope();
        node.visitChildren(this);
        result.popScope();
    }

    public void accept(ContinueNode node) {
    }

    public void accept(DoubleNode node) {
    }

    public void accept(DictionaryNode node) {
    }

    public void accept(ExtendNode node) {
    }

    public void accept(FuncNode node) {
        node.visitChildren(this);
    }

    public void accept(UnaryOperationNode node) {
    }

    public void accept(IdentifierNode node) {
    }

    public void accept(Int64Node node) {
    }

    public void accept(NewNode node) {
    }

    public void accept(RaiseNode node) {
    }

    public void accept(CharNode node) {
    }

    public void accept(ConditionalNode node) {
        node.visitChildren(this);
    }

    public void accept(ForNode node) {
        node.visitChildren(this);
    }

    public void accept(ForeachNode node) {
        node.visitChildren(this);
    }

    public void accept(FunctionCallNode node) {
    }

    public void accept(ExpressionNode node) {
    }

    public void accept(ExpressionStatementNode node) {
    }

    public void accept(EnumNode node) {
        node.visitChildren(this);
    }

    public void accept(KeyValuePairNode node) {
        node.visitChildren(this);
    }

    public void accept(LambdaNode node) {
        node.visitChildren(this);
    }

    public void accept(PropertyNode node) {
    }

    public void accept(ReturnNode node) {
    }

    public void accept(StatementNode node) {
    }

    public void accept(StringNode node) {
    }

    public void accept(SwitchNode node) {
        node.visitChildren(this);
    }

    public void accept(TernaryOperationNode node) {
    }

    public void accept(ThisNode node) {
    }

    public void accept(TraitNode node) {
    }

    public void accept(TryCatchNode node) {
        node.visitChildren(this);
    }

    public void accept(TupleNode node) {
    }

    public void accept(UseNode node) {
    }

    public void accept(WhileNode node) {
        node.visitChildren(this);
    }

}
