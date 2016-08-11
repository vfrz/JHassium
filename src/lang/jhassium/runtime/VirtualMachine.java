package lang.jhassium.runtime;

import lang.jhassium.SourceLocation;
import lang.jhassium.codegen.HassiumModule;
import lang.jhassium.codegen.InstructionType;
import lang.jhassium.codegen.MethodBuilder;
import lang.jhassium.codegen.UserDefinedProperty;
import lang.jhassium.runtime.standardlibrary.HassiumExtend;
import lang.jhassium.runtime.standardlibrary.HassiumObject;
import lang.jhassium.runtime.standardlibrary.HassiumTrait;
import lang.jhassium.runtime.standardlibrary.types.*;
import lang.jhassium.utils.HassiumLogger;
import lang.jhassium.utils.Helpers;

import java.util.*;

import static lang.jhassium.runtime.standardlibrary.GlobalFunctions.FunctionList;
import static lang.jhassium.runtime.standardlibrary.types.HassiumTypesModule.Instance;

/**
 * File : VirtualMachine.java
 * Description : None
 * Author : FRITZ Valentin
 * Website : https://github.com/vfrz/JHassium
 * Date : 10/08/2016 22:40
 */
public class VirtualMachine {

    public StackFrame getStackFrame() {
        return stackFrame;
    }

    public Stack<HassiumObject> getStack() {
        return stack;
    }

    public Stack<String> getCallStack() {
        return callStack;
    }

    public LinkedHashMap<String, HassiumObject> getGlobals() {
        return globals;
    }

    public Stack<HassiumExceptionHandler> getHandlers() {
        return handlers;
    }

    public SourceLocation getSourceLocation() {
        return sourceLocation;
    }

    public HassiumModule getCurrentlyExecutingModule() {
        return module;
    }

    private StackFrame stackFrame;
    private Stack<HassiumObject> stack;
    private Stack<String> callStack = new Stack<>();
    private Stack<HassiumExceptionHandler> handlers = new Stack<HassiumExceptionHandler>();

    private LinkedHashMap<String, HassiumObject> globals;
    private HassiumModule module;
    private SourceLocation sourceLocation;
    private LinkedHashMap<MethodBuilder, Integer> exceptionReturns = new LinkedHashMap<>();

    public void execute(HassiumModule module, List<String> args) {
        globals = new LinkedHashMap<>();
        stack = new Stack<>();
        stackFrame = new StackFrame();
        this.module = module;
        gatherGlobals(module.ConstantPool);
        addArgs(args);
        preformExtensions();

        callStack.push("func main ()");
        stackFrame.enterFrame();
        executeMethod((MethodBuilder) module.Attributes.get("main"));
        stackFrame.popFrame();
        callStack.pop();
    }

    public HassiumObject executeMethod(MethodBuilder method) {
        gatherLabels(method);
        for (int position = 0; position < method.Instructions.size(); position++) {
            if (exceptionReturns.containsKey(method)) {
                position = exceptionReturns.get(method);
                exceptionReturns.remove(method);
            }

            HassiumObject left, right, value, list, index, location;
            double argument = method.Instructions.get(position).getArgument();
            int argumentInt = (int) argument;
            sourceLocation = method.Instructions.get(position).getSourceLocation();
            String attribute;
            try {
                switch (method.Instructions.get(position).getInstructionType()) {
                    case Binary_Operation:
                        right = stack.pop();
                        left = stack.pop();
                        executeBinaryOperation(left, right, argumentInt);
                        break;
                    case Build_Closure:
                        stack.push(new HassiumClosure((MethodBuilder) stack.pop(), stackFrame.Frames.peek()));
                        break;
                    case Call:
                        HassiumObject target = stack.pop();
                        HassiumObject[] args = new HassiumObject[argumentInt];
                        for (int i = 0; i < args.length; i++)
                            args[i] = stack.pop();
                        stack.push(target.invoke(this, args));
                        break;
                    case Create_Dict:
                        HassiumKeyValuePair[] dictElements = new HassiumKeyValuePair[argumentInt];
                        for (int i = argumentInt - 1; i >= 0; i--)
                            dictElements[i] = Helpers.as(stack.pop(), HassiumKeyValuePair.class);
                        stack.push(new HassiumDictionary(dictElements));
                        break;
                    case Create_List:
                        HassiumObject[] listElements = new HassiumObject[argumentInt];
                        for (int i = argumentInt - 1; i >= 0; i--)
                            listElements[i] = stack.pop();
                        stack.push(new HassiumList(listElements));
                        break;
                    case Create_Tuple:
                        HassiumObject[] tupleElements = new HassiumObject[argumentInt];
                        for (int i = argumentInt - 1; i >= 0; i--)
                            tupleElements[i] = stack.pop();
                        stack.push(new HassiumTuple(tupleElements));
                        break;
                    case Dup:
                        stack.push(stack.peek());
                        break;
                    case Iter:
                        stack.push(stack.pop().iter(this));
                        break;
                    case Enumerable_Full:
                        stack.push(stack.pop().enumerableFull(this));
                        break;
                    case Enumerable_Next:
                        stack.push(stack.pop().enumerableNext(this));
                        break;
                    case Enumerable_Reset:
                        stack.pop().enumerableReset(this);
                        break;
                    case Jump:
                        position = method.Labels.get(argument);
                        break;
                    case Jump_If_True:
                        if (((HassiumBool) stack.pop()).getValue())
                            position = method.Labels.get(argument);
                        break;
                    case Jump_If_False:
                        if (!((HassiumBool) stack.pop()).getValue())
                            position = method.Labels.get(argument);
                        break;
                    case Key_Value_Pair:
                        HassiumObject value_ = stack.pop();
                        HassiumObject key = stack.pop();
                        stack.push(new HassiumKeyValuePair(key, value_));
                        break;
                    case Load_Attribute:
                        attribute = module.ConstantPool.get(argumentInt).toString(this);
                        location = stack.pop();
                        HassiumObject attrib = null;
                        try {
                            attrib = location.Attributes.get(attribute);
                        } catch (Exception ex) {
                            HassiumLogger.error(location.type().toString(this) + " does not contain a definition for " + attribute);
                        }
                        if (attrib instanceof HassiumProperty)
                            stack.push((attrib).invoke(this, new HassiumObject[0]));
                        else if (attrib instanceof UserDefinedProperty)
                            stack.push(executeMethod(((UserDefinedProperty) attrib).GetMethod));
                        else
                            stack.push(attrib);
                        break;
                    case Load_Global:
                        String global_ = module.ConstantPool.get(argumentInt).toString(this);
                        if (globals.containsKey(global_))
                            stack.push(globals.get(global_));
                        else if (method.Parent.Attributes.containsKey(global_))
                            stack.push(method.Parent.Attributes.get(global_));
                        else
                            HassiumLogger.error(String.format("Cannot find global identifier %1s!", global_));
                        break;
                    case Load_Global_Variable:
                        stack.push(module.Globals.get(argumentInt));
                        break;
                    case Load_List_Element:
                        index = stack.pop();
                        list = stack.pop();
                        stack.push(list.index(this, index));
                        break;
                    case Load_Local:
                        stack.push(stackFrame.GetVariable(argumentInt));
                        break;
                    case Pop:
                        stack.pop();
                        break;
                    case Pop_Handler:
                        handlers.pop();
                        break;
                    case Push:
                        stack.push(new HassiumDouble(argument));
                        break;
                    case Push_Bool:
                        stack.push(new HassiumBool(argument == 1));
                        break;
                    case Push_Handler:
                        HassiumExceptionHandler handler = (HassiumExceptionHandler) module.ConstantPool.get(argumentInt);
                        handler.Frame = stackFrame.Frames.peek();
                        handlers.push(handler);
                        break;
                    case Push_Object:
                        stack.push(module.ConstantPool.get(argumentInt));
                        break;
                    case Raise:
                        raiseException(stack.pop(), method);
                        break;
                    case Return:
                        return stack.pop();
                    case Self_Reference:
                        stack.push(method.Parent);
                        break;
                    case Skip:
                        right = stack.pop();
                        stack.push(stack.pop().iter(this).skip(this, right));
                        break;
                    case Slice:
                        right = stack.pop();
                        left = stack.pop();
                        stack.push(stack.pop().iter(this).slice(this, left, right));
                        break;
                    case Store_Attribute:
                        location = stack.pop();
                        attribute = module.ConstantPool.get(argumentInt).toString(this);
                        if (location instanceof HassiumProperty) {
                            HassiumProperty builtinProp = Helpers.as(location, HassiumProperty.class);
                            builtinProp.invoke(this, new HassiumObject[]{stack.pop()});
                        } else if (location instanceof UserDefinedProperty) {
                            UserDefinedProperty userProp = Helpers.as(location, UserDefinedProperty.class);
                            userProp.SetMethod.invoke(this, new HassiumObject[]{stack.pop()});
                        } else
                            try {
                                location.Attributes.put(attribute, stack.pop());
                            } catch (Exception ex) {
                                HassiumLogger.error(location + " does not contain a definition for " + attribute);
                            }
                        break;
                    case Store_Global_Variable:
                        module.Globals.put(argumentInt, stack.pop());
                        break;
                    case Store_List_Element:
                        index = stack.pop();
                        list = stack.pop();
                        value = stack.pop();
                        stack.push(list.storeIndex(this, index, value));
                        break;
                    case Store_Local:
                        value = stack.pop();
                        if (stackFrame.contains(argumentInt))
                            stackFrame.modify(argumentInt, value);
                        else
                            stackFrame.add(argumentInt, value);
                        break;
                    case Unary_Operation:
                        executeUnaryOperation(stack.pop(), argumentInt);
                        break;
                }
            } catch (Exception ex) {
                raiseException(new HassiumString(ex.getMessage()), method);
            }
        }
        return HassiumObject.Null;
    }

    private void executeBinaryOperation(HassiumObject left, HassiumObject right, int argument) {
        switch (argument) {
            case 0:
                stack.push(left.add(this, right));
                break;
            case 1:
                stack.push(left.sub(this, right));
                break;
            case 2:
                stack.push(left.mul(this, right));
                break;
            case 3:
                stack.push(left.div(this, right));
                break;
            case 4:
                stack.push(new HassiumInt((long) Math.floor((double) left.div(this, right).getValue())));
                break;
            case 5:
                stack.push(left.mod(this, right));
                break;
            case 6:
                stack.push(left.xor(this, right));
                break;
            case 7:
                stack.push(left.or(this, right));
                break;
            case 8:
                stack.push(left.xand(this, right));
                break;
            case 9:
                stack.push(left.equals(this, right));
                break;
            case 10:
                stack.push(left.notEquals(this, right));
                break;
            case 11:
                stack.push(left.greaterThan(this, right));
                break;
            case 12:
                stack.push(left.greaterThanOrEqual(this, right));
                break;
            case 13:
                stack.push(left.lesserThan(this, right));
                break;
            case 14:
                stack.push(left.lesserThanOrEqual(this, right));
                break;
            case 15:
                stack.push(new HassiumBool(HassiumBool.create(left).getValue() || HassiumBool.create(right).getValue()));
                break;
            case 16:
                stack.push(new HassiumBool(HassiumBool.create(left).getValue() && HassiumBool.create(right).getValue()));
                break;
            case 17:
                stack.push(new HassiumDouble(Math.pow(HassiumDouble.create(left).getValue(), HassiumDouble.create(right).getValue())));
                break;
            case 18:
                stack.push(left.bitShiftLeft(this, right));
                break;
            case 19:
                stack.push(left.bitShiftRight(this, right));
                break;
            case 20:
                stack.push(left instanceof HassiumNull ? right : left);
                break;
            case 21:
                stack.push(left.contains(this, right));
                break;
            case 22:
                if (right instanceof HassiumTrait)
                    stack.push(new HassiumBool(((HassiumTrait) right).MatchesTrait(this, left)));
                else
                    stack.push(new HassiumBool(left.Types.contains(right.type())));
                break;
            case 23:
                stack.push(FunctionList.get("range").invoke(this, new HassiumObject[]{left, right}));
                break;
        }
    }

    public void raiseException(HassiumObject message, MethodBuilder method) {
        if (handlers.size() == 0)
            HassiumLogger.error(message.toString(this) + " At " + sourceLocation);
        else {
            HassiumExceptionHandler handler = Helpers.as(handlers.peek(), HassiumExceptionHandler.class);
            handler.invoke(this, new HassiumObject[]{message});
            exceptionReturns.put(handler.getSourceMethod(), handler.getSourceMethod().Labels.get(handler.getLabel()));
        }
    }

    private void executeUnaryOperation(HassiumObject target, int argument) {
        switch (argument) {
            case 0:
                stack.push(target.not(this));
                break;
            case 1:
                stack.push(target.bitwiseComplement(this));
                break;
            case 2:
                stack.push(target.negate(this));
                break;
        }
    }

    private void gatherLabels(MethodBuilder method) {
        method.Labels = new LinkedHashMap<>();
        for (int i = 0; i < method.Instructions.size(); i++) {
            //          Console.WriteLine(method.Instructions[i].InstructionType + "\t" + method.Instructions[i].Argument);
            if (method.Instructions.get(i).getInstructionType() == InstructionType.Label)
                method.Labels.put(method.Instructions.get(i).getArgument(), i);
        }
    }

    private void gatherGlobals(List<HassiumObject> constantPool) {
        for (int i = 0; i < constantPool.size(); i++) {
            if (FunctionList.containsKey(constantPool.get(i).toString(this)))
                globals.put(constantPool.get(i).toString(this), FunctionList.get(constantPool.get(i).toString(this)));
            else if (module.Attributes.containsKey(constantPool.get(i).toString(this)))
                globals.put(constantPool.get(i).toString(this), module.Attributes.get(constantPool.get(i).toString(this)));
        }

        // Import default types
        for (Map.Entry<String, HassiumObject> type : Instance.Attributes.entrySet()) {
            if (!globals.containsKey(type.getKey()))
                globals.put(type.getKey(), type.getValue());
        }
        List<Integer> keys = new ArrayList<>(module.Globals.keySet());
        for (int key : keys) {
            module.Globals.put(key, module.Globals.get(key).invoke(this, new HassiumObject[0]));
        }
    }

    private void preformExtensions() {
        for (String key : module.Attributes.keySet()) {
            if (key.startsWith("__extend__")) {
                HassiumExtend extend = Helpers.as(module.Attributes.get(key), HassiumExtend.class);
                HassiumObject target = extend.getTarget().invoke(this, new HassiumObject[0]);
                for (Map.Entry<String, HassiumObject> attrib : extend.getAdditions().Attributes.entrySet()) {
                    if (target.Attributes.containsKey(attrib.getKey()))
                        target.Attributes.remove(attrib.getKey());
                    target.Attributes.put(attrib.getKey(), attrib.getValue());
                }
                if (globals.containsKey(key))
                    globals.remove(key);
                globals.put(key, target);
            }
        }
    }

    private void addArgs(List<String> args) {
        HassiumList list = new HassiumList(new HassiumObject[0]);
        for (String arg : args) {
            list.getValue().add(new HassiumString(arg));
        }
        globals.put("args", list);
    }
}
