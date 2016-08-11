package lang.jhassium.codegen;

import lang.jhassium.HassiumExecuter;
import lang.jhassium.parser.AstNode;
import lang.jhassium.parser.IVisitor;
import lang.jhassium.parser.Parameter;
import lang.jhassium.parser.ast.*;
import lang.jhassium.runtime.HassiumTypeDefinition;
import lang.jhassium.runtime.standardlibrary.*;
import lang.jhassium.runtime.standardlibrary.types.*;
import lang.jhassium.semanticanalysis.SymbolTable;
import lang.jhassium.utils.Helpers;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import static lang.jhassium.parser.BinaryOperation.*;
import static lang.jhassium.parser.UnaryOperation.*;

/**
 * File : HassiumCompiler.java
 * Description : None
 * Authors : FRITZ Valentin, Jacob Misirian & zdimension
 * Website : https://github.com/vfrz/JHassium
 * Date : 11/08/2016 17:18
 */
public class HassiumCompiler implements IVisitor {

    private SymbolTable table;
    private HassiumModule module;
    private MethodBuilder currentMethod;

    private boolean popExpressionStatementsFromStack = true;

    public HassiumModule Compile(AstNode ast, SymbolTable table, String name) {
        this.table = table;
        module = new HassiumModule(name);

        for (AstNode child : ast.Children) {
            if (child instanceof ExpressionStatementNode) {
                if (child.Children.get(0) instanceof BinaryOperationNode) {
                    BinaryOperationNode operation = Helpers.as(child.Children.get(0), BinaryOperationNode.class);
                    if (operation.getBinaryOperation() == Assignment) {
                        String ident = ((IdentifierNode) operation.getLeft()).getIdentifier();
                        if (!table.findGlobalSymbol(ident))
                            table.addGlobalSymbol(ident);
                        MethodBuilder previousMethod = currentMethod;
                        currentMethod = new MethodBuilder();
                        currentMethod.setName("__assign__");
                        popExpressionStatementsFromStack = true;
                        operation.getRight().visit(this);
                        popExpressionStatementsFromStack = true;
                        currentMethod.emit(operation.Location, InstructionType.Return);
                        module.Globals.put(table.getGlobalIndex(ident), currentMethod);
                        currentMethod = previousMethod;
                    }
                }
            }
        }

        for (AstNode child : ast.Children) {
            if (child instanceof FuncNode) {
                child.visit(this);
                if (module.Attributes.containsKey(currentMethod.getName())) {
                    HassiumObject method = module.Attributes.get(currentMethod.getName());
                    if (method instanceof MethodBuilder)
                        module.Attributes.put(currentMethod.getName(), new HassiumMultiFunc(new ArrayList<MethodBuilder>() {{
                            add((MethodBuilder) method);
                            add(currentMethod);
                        }}));
                    else if (method instanceof HassiumMultiFunc)
                        ((HassiumMultiFunc) method).getLambdas().add(currentMethod);
                } else
                    module.Attributes.put(currentMethod.getName(), currentMethod);
            } else if (child instanceof ClassNode)
                child.visit(this);
            else if (child instanceof ExtendNode)
                child.visit(this);
            else if (child instanceof EnumNode) {
                HassiumObject enumerator = new HassiumObject();
                for (int i = 0; i < child.Children.size(); i++)
                    enumerator.Attributes.put(((IdentifierNode) child.Children.get(i)).getIdentifier(), new HassiumInt(i));
                module.Attributes.put(((EnumNode) child).getName(), enumerator);
            } else if (child instanceof TraitNode) {
                String traitName = ((TraitNode) child).getName();
                module.Attributes.put(traitName, compileTrait(Helpers.as(child, TraitNode.class)));
            } else if (child instanceof UseNode) {
                UseNode use = Helpers.as(child, UseNode.class);
                if (use.getTarget() instanceof IdentifierNode) {
                    String identifier = ((IdentifierNode) use.getTarget()).getIdentifier();
                    for (InternalModule internalModule : InternalModule.InternalModules) {
                        if (internalModule.getName().toLowerCase().equals(identifier.toLowerCase()))
                            for (Map.Entry<String, HassiumObject> attribute : internalModule.Attributes.entrySet()) {
                                module.Attributes.put(attribute.getKey(), attribute.getValue());
                            }
                    }
                    module.Imports.add(identifier);
                } else if (use.getTarget() instanceof StringNode) {
                    String path = ((StringNode) use.getTarget()).getString();
                    if (path.endsWith(".dll")) {
                        //TODO Load jar instead of dll
                        /*
                        foreach (InternalModule internalModule in LoadModulesFromDLL(path))
                        {
                            foreach (KeyValuePair<string, HassiumObject> attribute in internalModule.Attributes)
                            module.Attributes.put(attribute.Key, attribute.Value);
                        }*/
                    } else {
                        HassiumModule compiledModule = HassiumExecuter.fromFilePath(path, null, false);
                        compiledModule.Attributes.entrySet().stream().filter(attribute -> !module.Attributes.containsKey(attribute.getKey())).forEach(attribute -> module.Attributes.put(attribute.getKey(), attribute.getValue()));
                        compiledModule.ConstantPool.stream().filter(constant -> !module.ConstantPool.contains(constant)).forEach(constant -> module.ConstantPool.add(constant));
                    }
                    module.Imports.add(path);
                }
            }
        }
        for (AstNode child : ast.Children) {
            if (child instanceof ClassNode) {
                ClassNode clazz = Helpers.as(child, ClassNode.class);
                for (String inherit : clazz.getInherits()) {
                    LinkedHashMap<String, HassiumObject> inheritedAttributes = MethodBuilder.cloneDictionary(module.Attributes.get(inherit).Attributes);
                    inheritedAttributes.entrySet().stream().filter(attribute -> !module.Attributes.get(clazz.getName()).Attributes.containsKey(attribute.getKey())).forEach(attribute -> {
                        if (attribute.getValue() instanceof MethodBuilder) {
                            MethodBuilder newMethod = Helpers.as(attribute.getValue(), MethodBuilder.class);
                            newMethod.Parent = Helpers.as(module.Attributes.get(clazz.getName()), HassiumClass.class);
                            module.Attributes.get(clazz.getName()).Attributes.put(attribute.getKey(), newMethod);
                        }
                        if (attribute.getValue() instanceof UserDefinedProperty) {
                            UserDefinedProperty property = Helpers.as(attribute.getValue(), UserDefinedProperty.class);
                            property.GetMethod.Parent = Helpers.as(module.Attributes.get(clazz.getName()), HassiumClass.class);
                            if (property.SetMethod != null)
                                property.SetMethod.Parent = Helpers.as(module.Attributes.get(clazz.getName()), HassiumClass.class);
                            module.Attributes.get(clazz.getName()).Attributes.put(attribute.getKey(), property);
                        }
                    });
                }
            }
        }
        return module;
    }

    public void accept(ArgListNode node) {
        node.visitChildren(this);
    }

    public void accept(ArrayAccessNode node) {
        if (node.getExpression() instanceof BinaryOperationNode) {
            BinaryOperationNode binop = Helpers.as(node.getExpression(), BinaryOperationNode.class);
            if (binop.getBinaryOperation() == Slice) {
                node.getTarget().visit(this);
                binop.visitChildren(this);
                currentMethod.emit(node.Location, InstructionType.Slice);
                return;
            }
        } else if (node.getExpression() instanceof UnaryOperationNode) {
            UnaryOperationNode unop = Helpers.as(node.getExpression(), UnaryOperationNode.class);
            if (unop.getUnaryOperation() == Skip) {
                node.getTarget().visit(this);
                unop.getBody().visit(this);
                currentMethod.emit(node.Location, InstructionType.Skip);
                return;
            }
        }
        node.visitChildren(this);
        currentMethod.emit(node.Location, InstructionType.Load_List_Element);
    }

    public void accept(ArrayDeclarationNode node) {
        node.visitChildren(this);
        currentMethod.emit(node.Location, InstructionType.Create_List, node.Children.size());
    }

    public void accept(AttributeAccessNode node) {
        node.getLeft().visit(this);
        if (!containsStringConstant(node.getRight()))
            module.ConstantPool.add(new HassiumString(node.getRight()));
        currentMethod.emit(node.Location, InstructionType.Load_Attribute, findStringIndex(node.getRight()));
    }

    public void accept(BinaryOperationNode node) {
        if (node.getBinaryOperation() != Assignment && node.getBinaryOperation() != Swap) {
            node.getLeft().visit(this);
            node.getRight().visit(this);
        }
        switch (node.getBinaryOperation()) {
            case Assignment:
                node.getRight().visit(this);
                if (node.getLeft() instanceof IdentifierNode) {
                    String identifier = ((IdentifierNode) node.getLeft()).getIdentifier();
                    if (table.findGlobalSymbol(identifier)) {
                        currentMethod.emit(node.Location, InstructionType.Store_Global_Variable, table.getGlobalIndex(identifier));
                        currentMethod.emit(node.Location, InstructionType.Load_Global_Variable, table.getGlobalIndex(identifier));
                    } else {
                        if (!table.findSymbol(identifier))
                            table.addSymbol(identifier);
                        currentMethod.emit(node.Location, InstructionType.Store_Local, table.getIndex(identifier));
                        currentMethod.emit(node.Location, InstructionType.Load_Local, table.getIndex(identifier));
                    }
                } else if (node.getLeft() instanceof AttributeAccessNode) {
                    AttributeAccessNode accessor = Helpers.as(node.getLeft(), AttributeAccessNode.class);
                    accessor.getLeft().visit(this);
                    if (!containsStringConstant(accessor.getRight()))
                        module.ConstantPool.add(new HassiumString(accessor.getRight()));
                    currentMethod.emit(node.Location, InstructionType.Store_Attribute, findStringIndex(accessor.getRight()));
                    accessor.getLeft().visit(this);
                } else if (node.getLeft() instanceof ArrayAccessNode) {
                    ArrayAccessNode access = Helpers.as(node.getLeft(), ArrayAccessNode.class);
                    access.getTarget().visit(this);
                    access.getExpression().visit(this);
                    currentMethod.emit(node.Location, InstructionType.Store_List_Element);
                }
                break;
            case Addition:
                currentMethod.emit(node.Location, InstructionType.Binary_Operation, 0);
                break;
            case Subtraction:
                currentMethod.emit(node.Location, InstructionType.Binary_Operation, 1);
                break;
            case Multiplication:
                currentMethod.emit(node.Location, InstructionType.Binary_Operation, 2);
                break;
            case Division:
                currentMethod.emit(node.Location, InstructionType.Binary_Operation, 3);
                break;
            case IntegerDivision:
                currentMethod.emit(node.Location, InstructionType.Binary_Operation, 4);
                break;
            case Modulus:
                currentMethod.emit(node.Location, InstructionType.Binary_Operation, 5);
                break;
            case XOR:
                currentMethod.emit(node.Location, InstructionType.Binary_Operation, 6);
                break;
            case OR:
                currentMethod.emit(node.Location, InstructionType.Binary_Operation, 7);
                break;
            case XAnd:
                currentMethod.emit(node.Location, InstructionType.Binary_Operation, 8);
                break;
            case EqualTo:
                currentMethod.emit(node.Location, InstructionType.Binary_Operation, 9);
                break;
            case NotEqualTo:
                currentMethod.emit(node.Location, InstructionType.Binary_Operation, 10);
                break;
            case GreaterThan:
                currentMethod.emit(node.Location, InstructionType.Binary_Operation, 11);
                break;
            case GreaterThanOrEqual:
                currentMethod.emit(node.Location, InstructionType.Binary_Operation, 12);
                break;
            case LesserThan:
                currentMethod.emit(node.Location, InstructionType.Binary_Operation, 13);
                break;
            case LesserThanOrEqual:
                currentMethod.emit(node.Location, InstructionType.Binary_Operation, 14);
                break;
            case LogicalOr:
                currentMethod.emit(node.Location, InstructionType.Binary_Operation, 15);
                break;
            case LogicalAnd:
                currentMethod.emit(node.Location, InstructionType.Binary_Operation, 16);
                break;
            case Swap:
                table.addSymbol("__swaptmp__");
                int tmp = table.getIndex("__swaptmp__");
                int left = table.getIndex(((IdentifierNode) node.getLeft()).getIdentifier());
                int right = table.getIndex(((IdentifierNode) node.getRight()).getIdentifier());
                node.getLeft().visit(this);
                currentMethod.emit(node.Location, InstructionType.Store_Local, tmp);
                node.getRight().visit(this);
                currentMethod.emit(node.Location, InstructionType.Store_Local, left);
                currentMethod.emit(node.Location, InstructionType.Load_Local, tmp);
                currentMethod.emit(node.Location, InstructionType.Store_Local, right);
                break;
            case Power:
                currentMethod.emit(node.Location, InstructionType.Binary_Operation, 17);
                break;
            case BitShiftLeft:
                currentMethod.emit(node.Location, InstructionType.Binary_Operation, 18);
                break;
            case BitShiftRight:
                currentMethod.emit(node.Location, InstructionType.Binary_Operation, 19);
                break;
            case NullCoalescing:
                currentMethod.emit(node.Location, InstructionType.Binary_Operation, 20);
                break;
            case In:
                currentMethod.emit(node.Location, InstructionType.Binary_Operation, 21);
                break;
            case Is:
                currentMethod.emit(node.Location, InstructionType.Binary_Operation, 22);
                break;
            case Range:
                currentMethod.emit(node.Location, InstructionType.Binary_Operation, 23);
                break;
        }
    }

    public void accept(BoolNode node) {
        currentMethod.emit(node.Location, InstructionType.Push_Bool, node.getValue() ? 1 : 0);
    }

    public void accept(BreakNode node) {
        currentMethod.emit(node.Location, InstructionType.Jump, currentMethod.BreakLabels.pop());
    }

    public void accept(CaseNode node) {
    }

    public void accept(CharNode node) {
        if (!containsCharConstant(node.getChar()))
            module.ConstantPool.add(new HassiumChar(node.getChar()));
        currentMethod.emit(node.Location, InstructionType.Push_Object, findCharIndex(node.getChar()));
    }

    public void accept(ClassNode node) {
        module.Attributes.put(node.getName(), compileClass(node));
    }

    private HassiumClass compileClass(ClassNode node) {
        if (!containsStringConstant(node.getName()))
            module.ConstantPool.add(new HassiumString(node.getName()));
        HassiumClass clazz = new HassiumClass();
        clazz.Name = node.getName();
        clazz.TypeDefinition = new HassiumTypeDefinition(clazz.Name);
        clazz.addType(clazz.TypeDefinition);

        for (AstNode child : node.getBody().Children) {
            if (child instanceof FuncNode) {
                child.visit(this);
                currentMethod.Parent = clazz;
                if (clazz.Attributes.containsKey(currentMethod.getName())) {
                    HassiumObject method = clazz.Attributes.get(currentMethod.getName());
                    if (method instanceof MethodBuilder)
                        clazz.Attributes.put(currentMethod.getName(), new HassiumMultiFunc(new ArrayList<MethodBuilder>() {{
                            add((MethodBuilder) method);
                            add(currentMethod);
                        }}));
                    else if (method instanceof HassiumMultiFunc)
                        ((HassiumMultiFunc) method).getLambdas().add(currentMethod);
                } else
                    clazz.Attributes.put(currentMethod.getName(), currentMethod);
            } else if (child instanceof ClassNode)
                clazz.Attributes.put(((ClassNode) child).getName(), compileClass(Helpers.as(child, ClassNode.class)));
            else if (child instanceof ExtendNode)
                clazz.Attributes.put("__extend__" + generateSymbol(), compileExtend(Helpers.as(child, ExtendNode.class)));
            else if (child instanceof TraitNode)
                clazz.Attributes.put(((TraitNode) child).getName(), compileTrait(Helpers.as(child, TraitNode.class)));
            else if (child instanceof PropertyNode) {
                child.visit(this);
                PropertyNode propNode = Helpers.as(child, PropertyNode.class);
                if (!containsStringConstant(propNode.getIdentifier()))
                    module.ConstantPool.add(new HassiumString(propNode.getIdentifier()));
                UserDefinedProperty property = new UserDefinedProperty(propNode.getIdentifier());
                currentMethod = new MethodBuilder();
                currentMethod.setName("__get__" + propNode.getClass());
                currentMethod.Parent = clazz;
                table.enterScope();
                propNode.getGetBody().visit(this);
                table.popScope();
                property.GetMethod = currentMethod;

                if (node.Children.size() > 2) {
                    currentMethod = new MethodBuilder();
                    currentMethod.setName("__set__" + propNode.getIdentifier());
                    currentMethod.Parent = clazz;
                    table.enterScope();
                    if (!table.findSymbol("value"))
                        table.addSymbol("value");
                    currentMethod.Parameters.put(new Parameter("value"), table.getIndex("value"));
                    propNode.getSetBody().visit(this);
                    table.popScope();
                    property.SetMethod = currentMethod;
                }
                clazz.Attributes.put(propNode.getIdentifier(), property);
            }
        }
        return clazz;
    }

    public void accept(CodeBlockNode node) {
        table.enterScope();
        node.visitChildren(this);
        table.popScope();
    }

    public void accept(ConditionalNode node) {
        double elseLabel = generateSymbol();
        double endLabel = generateSymbol();
        node.getPredicate().visit(this);
        currentMethod.emit(node.Location, InstructionType.Jump_If_False, elseLabel);
        node.getBody().visit(this);
        currentMethod.emit(node.Location, InstructionType.Jump, endLabel);
        currentMethod.emit(node.Location, InstructionType.Label, elseLabel);
        if (node.Children.size() > 2)
            node.getElseBody().visit(this);
        currentMethod.emit(node.Location, InstructionType.Label, endLabel);
    }

    public void accept(ContinueNode node) {
        currentMethod.emit(node.Location, InstructionType.Jump, currentMethod.ContinueLabels.pop());
    }

    public void accept(DoubleNode node) {
        currentMethod.emit(node.Location, InstructionType.Push, node.getNumber());
    }

    public void accept(DictionaryNode node) {
        node.visitChildren(this);
        currentMethod.emit(node.Location, InstructionType.Create_Dict, node.Children.size());
    }

    public void accept(EnumNode node) {
    }

    public void accept(ExpressionNode node) {
        node.visitChildren(this);
    }

    public void accept(ExpressionStatementNode node) {
        node.visitChildren(this);
        if (popExpressionStatementsFromStack)
            currentMethod.emit(node.Location, InstructionType.Pop);
    }

    public void accept(ExtendNode node) {
        module.Attributes.put("__extend__" + generateSymbol(), compileExtend(node));
    }

    private HassiumExtend compileExtend(ExtendNode node) {
        MethodBuilder previousMethod = currentMethod;
        currentMethod = new MethodBuilder();
        currentMethod.setName("__extend__");
        node.getTarget().visit(this);
        currentMethod.emit(node.Location, InstructionType.Return);
        HassiumExtend extend = new HassiumExtend(currentMethod, compileClass(node.getClazz()));
        currentMethod = previousMethod;

        return extend;
    }

    public void accept(ForNode node) {
        double forLabel = generateSymbol();
        double endLabel = generateSymbol();
        currentMethod.ContinueLabels.push(forLabel);
        currentMethod.BreakLabels.push(endLabel);
        node.getSingleStatement().visit(this);
        currentMethod.emit(node.Location, InstructionType.Label, forLabel);
        node.getPredicate().visit(this);
        currentMethod.emit(node.Location, InstructionType.Jump_If_False, endLabel);
        node.getBody().visit(this);
        node.getRepeatStatement().visit(this);
        currentMethod.emit(node.Location, InstructionType.Jump, forLabel);
        currentMethod.emit(node.Location, InstructionType.Label, endLabel);
    }

    public void accept(ForeachNode node) {
        double foreachLabel = generateSymbol();
        double endLabel = generateSymbol();
        currentMethod.ContinueLabels.push(foreachLabel);
        currentMethod.BreakLabels.push(endLabel);
        table.enterScope();
        table.addSymbol(node.getIdentifier());
        table.addSymbol("__tmp__");
        int tmp = table.getIndex("__tmp__");
        node.getExpression().visit(this);
        currentMethod.emit(node.Location, InstructionType.Iter);
        currentMethod.emit(node.Location, InstructionType.Store_Local, tmp);
        currentMethod.emit(node.Location, InstructionType.Label, foreachLabel);
        currentMethod.emit(node.Location, InstructionType.Load_Local, tmp);
        currentMethod.emit(node.Location, InstructionType.Enumerable_Full);
        currentMethod.emit(node.Location, InstructionType.Jump_If_True, endLabel);
        currentMethod.emit(node.Location, InstructionType.Load_Local, tmp);
        currentMethod.emit(node.Location, InstructionType.Enumerable_Next);
        currentMethod.emit(node.Location, InstructionType.Store_Local, table.getIndex(node.getIdentifier()));
        node.getBody().visit(this);
        currentMethod.emit(node.Location, InstructionType.Jump, foreachLabel);
        currentMethod.emit(node.Location, InstructionType.Label, endLabel);
        table.popScope();
    }

    public void accept(FuncNode node) {
        if (!containsStringConstant(node.getName()))
            module.ConstantPool.add(new HassiumString(node.getName()));

        currentMethod = new MethodBuilder();
        currentMethod.Parent = new HassiumClass();
        currentMethod.setName(node.getName());
        currentMethod.ReturnType = node.getReturnType();
        currentMethod.SourceRepresentation = node.getSourceRepresentation();

        table.enterScope();

        for (int i = 0; i < node.getParameters().size(); i++) {
            table.addSymbol(node.getParameters().get(i).getName());
            currentMethod.Parameters.put(node.getParameters().get(i), table.getIndex(node.getParameters().get(i).getName()));
        }

        node.Children.get(0).visitChildren(this);
        table.popScope();
    }

    public void accept(FunctionCallNode node) {
        for (int i = node.getArguments().Children.size() - 1; i >= 0; i--)
            node.getArguments().Children.get(i).visit(this);
        node.getTarget().visit(this);
        currentMethod.emit(node.Location, InstructionType.Call, node.getArguments().Children.size());
    }

    public void accept(IdentifierNode node) {
        if (table.findGlobalSymbol(node.getIdentifier()))
            currentMethod.emit(node.Location, InstructionType.Load_Global_Variable, table.getGlobalIndex(node.getIdentifier()));
        else if (!table.findSymbol(node.getIdentifier())) {
            if (!containsStringConstant(node.getIdentifier()))
                module.ConstantPool.add(new HassiumString(node.getIdentifier()));
            currentMethod.emit(node.Location, InstructionType.Load_Global, findStringIndex(node.getIdentifier()));
        } else
            currentMethod.emit(node.Location, InstructionType.Load_Local, table.getIndex(node.getIdentifier()));
    }

    public void accept(Int64Node node) {
        if (!containsInt64Constant(node.getNumber()))
            module.ConstantPool.add(new HassiumInt(node.getNumber()));
        currentMethod.emit(node.Location, InstructionType.Push_Object, findInt64Index(node.getNumber()));
    }

    public void accept(KeyValuePairNode node) {
        node.getLeft().visit(this);
        node.getRight().visit(this);
        currentMethod.emit(node.Location, InstructionType.Key_Value_Pair);
    }

    public void accept(LambdaNode node) {
        MethodBuilder previousMethod = currentMethod;
        // Swap from the lambda method to the current method
        MethodBuilder lambda = compileLambda(node);
        currentMethod = previousMethod;
        module.ConstantPool.add(lambda);

        currentMethod.emit(node.Location, InstructionType.Push_Object, findIndex(lambda));
        currentMethod.emit(node.Location, InstructionType.Build_Closure);
    }

    private MethodBuilder compileLambda(LambdaNode node) {
        currentMethod = new MethodBuilder();
        currentMethod.setName("__lambda__");
        table.enterScope();

        for (int i = 0; i < node.getParameters().size(); i++) {
            table.addSymbol(node.getParameters().get(i));
            currentMethod.Parameters.put(new Parameter(node.getParameters().get(i)), table.getIndex(node.getParameters().get(i)));
        }

        node.Children.get(0).visitChildren(this);
        table.popScope();
        return currentMethod;
    }

    public void accept(NewNode node) {
        node.getCall().IsConstructorCall = true;
        node.getCall().visit(this);
    }

    public void accept(PropertyNode node) {
    }

    public void accept(RaiseNode node) {
        node.getExpression().visit(this);
        currentMethod.emit(node.Location, InstructionType.Raise);
    }

    public void accept(ReturnNode node) {
        node.visitChildren(this);
        currentMethod.emit(node.Location, InstructionType.Return);
    }

    public void accept(StatementNode node) {
    }

    public void accept(StringNode node) {
        if (!containsStringConstant(node.getString()))
            module.ConstantPool.add(new HassiumString(node.getString()));
        currentMethod.emit(node.Location, InstructionType.Push_Object, findStringIndex(node.getString()));
    }

    public void accept(SwitchNode node) {
        double endSwitchLabel = generateSymbol();
        node.getPredicate().visit(this);
        table.enterScope();
        table.addSymbol("__tmp__");
        int tmp = table.getIndex("__tmp__");
        currentMethod.emit(node.Location, InstructionType.Store_Local, tmp);
        for (AstNode child : node.Children) {
            CaseNode cas = Helpers.as(child, CaseNode.class);
            double caseLabel = generateSymbol();
            double endCase = generateSymbol();
            currentMethod.BreakLabels.push(endCase);
            for (AstNode predicate : cas.Children) {
                currentMethod.emit(node.Location, InstructionType.Load_Local, tmp);
                predicate.visit(this);
                currentMethod.emit(node.Location, InstructionType.Binary_Operation, 8); // TODO WHY ?
                currentMethod.emit(node.Location, InstructionType.Jump_If_True, caseLabel);
            }
            currentMethod.emit(node.Location, InstructionType.Jump, endCase);
            currentMethod.emit(node.Location, InstructionType.Label, caseLabel);
            cas.getBody().visit(this);
            currentMethod.emit(node.Location, InstructionType.Jump, endSwitchLabel);
            currentMethod.emit(node.Location, InstructionType.Label, endCase);
        }
        if (node.getDefaultCase() != null)
            node.getDefaultCase().visit(this);
        currentMethod.emit(node.Location, InstructionType.Label, endSwitchLabel);
    }

    public void accept(TernaryOperationNode node) {
        double falseLabel = generateSymbol();
        double endLabel = generateSymbol();
        node.getPredicate().visit(this);
        currentMethod.emit(node.Location, InstructionType.Jump_If_False, falseLabel);
        node.getTrueBody().visit(this);
        currentMethod.emit(node.Location, InstructionType.Jump, endLabel);
        currentMethod.emit(node.Location, InstructionType.Label, falseLabel);
        node.getElseBody().visit(this);
        currentMethod.emit(node.Location, InstructionType.Label, endLabel);
    }

    public void accept(ThisNode node) {
        currentMethod.emit(node.Location, InstructionType.Self_Reference, findIndex(currentMethod));
    }

    public void accept(TraitNode node) {
    }

    public void accept(TryCatchNode node) {
        double endLabel = generateSymbol();
        MethodBuilder previousMethod = currentMethod;
        currentMethod = new MethodBuilder();
        currentMethod.setName("__catch__");
        table.enterScope();
        if (!table.findSymbol("value"))
            table.addSymbol("value");
        currentMethod.Parameters.put(new Parameter("value"), table.getIndex("value"));
        node.getCatchBody().visitChildren(this);
        HassiumExceptionHandler handler = new HassiumExceptionHandler(previousMethod, currentMethod, endLabel);
        module.ConstantPool.add(handler);
        int catchIndex = findIndex(handler);
        currentMethod = previousMethod;
        currentMethod.emit(node.Location, InstructionType.Push_Handler, catchIndex);
        node.getTryBody().visit(this);
        currentMethod.emit(node.Location, InstructionType.Pop_Handler);
        currentMethod.emit(node.Location, InstructionType.Label, endLabel);
    }

    public void accept(TupleNode node) {
        node.visitChildren(this);
        currentMethod.emit(node.Location, InstructionType.Create_Tuple, node.Children.size());
    }

    public void accept(UnaryOperationNode node) {
        switch (node.getUnaryOperation()) {
            case BitwiseComplement:
                node.getBody().visit(this);
                currentMethod.emit(node.Location, InstructionType.Unary_Operation, 1);
                break;
            case Negate:
                node.getBody().visit(this);
                currentMethod.emit(node.Location, InstructionType.Unary_Operation, 2);
                break;
            case Not:
                node.getBody().visit(this);
                currentMethod.emit(node.Location, InstructionType.Unary_Operation, 0);
                break;
            case PostDecrement:
            case PostIncrement:
            case PreDecrement:
            case PreIncrement:
                if (node.getBody() instanceof IdentifierNode) {
                    String identifier = ((IdentifierNode) node.getBody()).getIdentifier();
                    Instruction loadInstruction, storeInstruction;
                    if (table.findGlobalSymbol(identifier)) {
                        loadInstruction = new Instruction(InstructionType.Load_Global_Variable, table.getGlobalIndex(identifier), node.Location);
                        storeInstruction = new Instruction(InstructionType.Store_Global_Variable, table.getGlobalIndex(identifier), node.Location);
                    } else {
                        loadInstruction = new Instruction(InstructionType.Load_Local, table.getIndex(identifier), node.Location);
                        storeInstruction = new Instruction(InstructionType.Store_Local, table.getIndex(identifier), node.Location);
                    }
                    currentMethod.Instructions.add(loadInstruction);
                    if (node.getUnaryOperation() == PostDecrement || node.getUnaryOperation() == PostIncrement)
                        currentMethod.emit(node.Location, InstructionType.Dup);
                    currentMethod.emit(node.Location, InstructionType.Push, 1);
                    currentMethod.emit(node.Location, InstructionType.Binary_Operation,
                            node.getUnaryOperation() == PostIncrement || node.getUnaryOperation() == PreIncrement ? 0 : 1);
                    currentMethod.Instructions.add(storeInstruction);
                    if (node.getUnaryOperation() == PreDecrement || node.getUnaryOperation() == PreIncrement)
                        currentMethod.Instructions.add(loadInstruction);
                }
                break;
        }
    }

    public void accept(UseNode node) {
    }

    public void accept(WhileNode node) {
        double whileLabel = generateSymbol();
        double endLabel = generateSymbol();
        currentMethod.ContinueLabels.push(whileLabel);
        currentMethod.BreakLabels.push(endLabel);
        currentMethod.emit(node.Location, InstructionType.Label, whileLabel);
        node.getPredicate().visit(this);
        currentMethod.emit(node.Location, InstructionType.Jump_If_False, endLabel);
        node.getBody().visit(this);
        currentMethod.emit(node.Location, InstructionType.Jump, whileLabel);
        currentMethod.emit(node.Location, InstructionType.Label, endLabel);
    }

    private int findIndex(HassiumObject constant) {
        for (int i = 0; i < module.ConstantPool.size(); i++)
            if (module.ConstantPool.get(i) == constant)
                return i;
        return -1;
    }

    private int findStringIndex(String constant) {
        for (int i = 0; i < module.ConstantPool.size(); i++)
            if (module.ConstantPool.get(i) instanceof HassiumString)
                if (module.ConstantPool.get(i).toString(null).equals(constant))
                    return i;
        return -1;
    }

    private int findInt64Index(long constant) {
        for (int i = 0; i < module.ConstantPool.size(); i++)
            if (module.ConstantPool.get(i) instanceof HassiumInt)
                if (((HassiumInt) module.ConstantPool.get(i)).getValue() == constant)
                    return i;
        return -1;
    }

    private int findCharIndex(char constant) {
        for (int i = 0; i < module.ConstantPool.size(); i++)
            if (module.ConstantPool.get(i) instanceof HassiumChar)
                if (((HassiumChar) module.ConstantPool.get(i)).getValue() == constant)
                    return i;
        return -1;
    }

    private boolean containsStringConstant(String constant) {
        for (HassiumObject obj : module.ConstantPool) {
            if (obj instanceof HassiumString)
                if (((HassiumString) obj).getValue().equals(constant))
                    return true;
        }
        return false;
    }

    private boolean containsInt64Constant(long constant) {
        for (HassiumObject obj : module.ConstantPool) {
            if (obj instanceof HassiumInt)
                if (((HassiumInt) obj).getValue() == constant)
                    return true;
        }
        return false;
    }

    private boolean containsCharConstant(char constant) {
        for (HassiumObject obj : module.ConstantPool) {
            if (obj instanceof HassiumChar)
                if (((HassiumChar) obj).getValue() == constant)
                    return true;
        }
        return false;
    }

    private boolean containsDoubleConstant(double constant) {
        for (HassiumObject obj : module.ConstantPool) {
            if (obj instanceof HassiumDouble)
                if (((HassiumDouble) obj).getValue() == constant)
                    return true;
        }
        return false;
    }

    private HassiumTrait compileTrait(TraitNode trait) {
        HassiumTrait hassiumTrait = new HassiumTrait(trait.getTraits());
        hassiumTrait.TypeDefinition = new HassiumTypeDefinition(trait.getName());
        hassiumTrait.addType(hassiumTrait.TypeDefinition);

        if (!containsStringConstant(trait.getName()))
            module.ConstantPool.add(new HassiumString(trait.getName()));
        return hassiumTrait;
    }

    private double nextSymbol = 0;

    private double generateSymbol() {
        return ++nextSymbol;
    }

    //TODO Load jar instead of dll
    /*
    public static InternalModule[] LoadModulesFromDLL(string path)
    {
        List<InternalModule> modules = new List<InternalModule>();
        Assembly ass = Assembly.LoadFrom(path);
        foreach (var type in ass.GetTypes())
        {
            if (type.IsSubclassOf(typeof(InternalModule)))
                modules.Add((InternalModule)Activator.CreateInstance(type));
        }
        return modules.ToArray();
    }*/
}
