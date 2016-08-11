package lang.jhassium.runtime;

import lang.jhassium.SourceLocation;
import lang.jhassium.codegen.HassiumModule;
import lang.jhassium.codegen.InstructionType;
import lang.jhassium.codegen.MethodBuilder;
import lang.jhassium.runtime.standardlibrary.HassiumObject;
import lang.jhassium.runtime.standardlibrary.types.*;
import lang.jhassium.utils.HassiumLogger;
import lang.jhassium.utils.Helpers;

import java.util.HashMap;
import java.util.List;
import java.util.Stack;

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

    public HashMap<String, HassiumObject> getGlobals() {
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

    private HashMap<String, HassiumObject> globals;
    private HassiumModule module;
    private SourceLocation sourceLocation;
    private HashMap<MethodBuilder, Integer> exceptionReturns = new HashMap<>();

    public void execute(HassiumModule module, List<String> args)
    {
        globals = new HashMap<>();
        stack = new Stack<>();
        stackFrame = new StackFrame();
        this.module = module;
        gatherGlobals(module.ConstantPool);
        addArgs(args);
        preformExtensions();

        callStack.push("func main ()");
        stackFrame.enterFrame();
        executeMethod((MethodBuilder)module.Attributes.get("main"));
        stackFrame.popFrame();
        callStack.pop();
    }

    public HassiumObject executeMethod(MethodBuilder method)
    {
        gatherLabels(method);
        for (int position = 0; position < method.Instructions.size(); position++)
        {
            if (exceptionReturns.containsKey(method))
            {
                position = exceptionReturns.get(method);
                exceptionReturns.remove(method);
            }

            HassiumObject left, right, value, list, index, location;
            double argument = method.Instructions.get(position).getArgument();
            int argumentInt = (int)argument;
            sourceLocation = method.Instructions.get(position).getSourceLocation();
            String attribute;
            try
            {
                switch (method.Instructions.get(position).getInstructionType())
                {
                    case Binary_Operation:
                        right = stack.pop();
                        left = stack.pop();
                        executeBinaryOperation(left, right, argumentInt);
                        break;
                    case Build_Closure:
                        stack.push(new HassiumClosure((MethodBuilder)stack.pop(), stackFrame.Frames.peek()));
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
                        if (((HassiumBool)stack.pop()).getValue())
                            position = method.Labels.get(argument);
                        break;
                    case Jump_If_False:
                        if (!((HassiumBool)stack.pop()).getValue())
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
                        try
                        {
                            attrib = location.Attributes.get(attribute);
                        }
                        catch (Exception ex)
                        {
                            HassiumLogger.error(location.type().toString(this) + " does not contain a definition for " + attribute);
                        }
                        if (attrib instanceof HassiumProperty)
                            stack.push((attrib).invoke(this, new HassiumObject[0]));
                        else if (attrib instanceof UserDefinedProperty)
                            stack.push(executeMethod(((UserDefinedProperty)attrib).GetMethod));
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
                            throw new InternalException(string.Format("Cannot find global identifier {0}!", global_));
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
                        HassiumExceptionHandler handler = (HassiumExceptionHandler)module.ConstantPool[argumentInt];
                        handler.Frame = stackFrame.Frames.peek();
                        handlers.push(handler);
                        break;
                    case Push_Object:
                        stack.push(module.ConstantPool[argumentInt]);
                        break;
                    case Raise:
                        raiseException(stack.pop(), method, ref position);
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
                        attribute = module.ConstantPool[argumentInt].ToString(this);
                        if (location is HassiumProperty)
                    {
                        HassiumProperty builtinProp = location as HassiumProperty;
                        builtinProp.Invoke(this, new HassiumObject[] { stack.pop() });
                    }
                            else if (location is UserDefinedProperty)
                    {
                        UserDefinedProperty userProp = location as UserDefinedProperty;
                        userProp.SetMethod.Invoke(this, new HassiumObject[] { stack.pop() });
                    }
                            else
                    try
                    {
                        location.Attributes[attribute] = stack.pop();
                    }
                    catch (KeyNotFoundException)
                    {
                        throw new InternalException(location + " does not contain a definition for " + attribute);
                    }
                    break;
                    case Store_Global_Variable:
                        module.Globals[argumentInt] = stack.pop();
                        break;
                    case Store_List_Element:
                        index = stack.pop();
                        list = stack.pop();
                        value = stack.pop();
                        stack.push(list.StoreIndex(this, index, value));
                        break;
                    case Store_Local:
                        value = stack.pop();
                        if (stackFrame.Contains(argumentInt))
                            stackFrame.Modify(argumentInt, value);
                        else
                            stackFrame.Add(argumentInt, value);
                        break;
                    case Unary_Operation:
                        executeUnaryOperation(stack.pop(), argumentInt);
                        break;
                }
            }
            catch (InternalException ex)
            {
                RaiseException(new HassiumString(ex.Message), method, ref position);
            }
            catch (DivideByZeroException)
            {
                RaiseException(new HassiumString("Divide by zero!"), method, ref position);
            }
        }
        return HassiumObject.Null;
    }

    private void executeBinaryOperation(HassiumObject left, HassiumObject right, int argument)
    {
        switch (argument)
        {
            case 0:
                stack.push(left.Add(this, right));
                break;
            case 1:
                stack.push(left.Sub(this, right));
                break;
            case 2:
                stack.push(left.Mul(this, right));
                break;
            case 3:
                stack.push(left.Div(this, right));
                break;
            case 4:
                stack.push(new HassiumInt((long)Math.Floor((double)left.Div(this, right).Value)));
                break;
            case 5:
                stack.push(left.Mod(this, right));
                break;
            case 6:
                stack.push(left.XOR(this, right));
                break;
            case 7:
                stack.push(left.OR(this, right));
                break;
            case 8:
                stack.push(left.Xand(this, right));
                break;
            case 9:
                stack.push(left.Equals(this, right));
                break;
            case 10:
                stack.push(left.NotEquals(this, right));
                break;
            case 11:
                stack.push(left.GreaterThan(this, right));
                break;
            case 12:
                stack.push(left.GreaterThanOrEqual(this, right));
                break;
            case 13:
                stack.push(left.LesserThan(this, right));
                break;
            case 14:
                stack.push(left.LesserThanOrEqual(this, right));
                break;
            case 15:
                stack.push(new HassiumBool(HassiumBool.Create(left).Value || HassiumBool.Create(right).Value));
                break;
            case 16:
                stack.push(new HassiumBool(HassiumBool.Create(left).Value && HassiumBool.Create(right).Value));
                break;
            case 17:
                stack.push(new HassiumDouble(Math.Pow(HassiumDouble.Create(left).Value, HassiumDouble.Create(right).Value)));
                break;
            case 18:
                stack.push(left.BitShiftLeft(this, right));
                break;
            case 19:
                stack.push(left.BitShiftRight(this, right));
                break;
            case 20:
                stack.push(left is HassiumNull ? right : left);
                break;
            case 21:
                stack.push(left.Contains(this, right));
                break;
            case 22:
                if (right is HassiumTrait)
                stack.push(new HassiumBool(((HassiumTrait)right).MatchesTrait(this, left)));
                    else
                stack.push(new HassiumBool(left.Types.Contains(right.Type())));
                break;
            case 23:
                stack.push(GlobalFunctions.FunctionList["range"].Invoke(this, new HassiumObject[] { left, right }));
                break;
        }
    }

    public void raiseException(HassiumObject message, MethodBuilder method, ref int pos)
    {
        if (handlers.Count == 0)
            throw new RuntimeException(message.ToString(this), sourceLocation);
        else
        {
            HassiumExceptionHandler handler = handlers.Peek() as HassiumExceptionHandler;
            handler.Invoke(this, new HassiumObject[] { message });
            exceptionReturns.Add(handler.SourceMethod, handler.SourceMethod.Labels[handler.Label]);
        }
    }

    private void executeUnaryOperation(HassiumObject target, int argument)
    {
        switch (argument)
        {
            case 0:
                stack.push(target.Not(this));
                break;
            case 1:
                stack.push(target.BitwiseComplement(this));
                break;
            case 2:
                stack.push(target.Negate(this));
                break;
        }
    }

    private void gatherLabels(MethodBuilder method)
    {
        method.Labels = new Dictionary<double, int>();
        for (int i = 0; i < method.Instructions.Count; i++)
        {
            //          Console.WriteLine(method.Instructions[i].InstructionType + "\t" + method.Instructions[i].Argument);
            if (method.Instructions[i].InstructionType == InstructionType.Label)
                method.Labels.Add(method.Instructions[i].Argument, i);
        }
    }

    private void gatherGlobals(List<HassiumObject> constantPool)
    {
        for (int i = 0; i < constantPool.Count; i++)
        {
            if (GlobalFunctions.FunctionList.ContainsKey(constantPool[i].ToString(this)))
                globals.Add(constantPool[i].ToString(this), GlobalFunctions.FunctionList[constantPool[i].ToString(this)]);
            else if (module.Attributes.ContainsKey(constantPool[i].ToString(this)))
                globals.Add(constantPool[i].ToString(this), module.Attributes[constantPool[i].ToString(this)]);
        }

        // Import default types
        foreach (var type in HassiumTypesModule.Instance.Attributes)
        if (!globals.ContainsKey(type.Key))
            globals.Add(type.Key, type.Value);
        List<int> keys = new List<int>(module.Globals.Keys);
        foreach (int key in keys)
        module.Globals[key] = module.Globals[key].Invoke(this, new HassiumObject[0]);
    }

    private void preformExtensions()
    {
        foreach (string key in module.Attributes.Keys)
        {
            if (key.StartsWith("__extend__"))
            {
                HassiumExtend extend = module.Attributes[key] as HassiumExtend;
                HassiumObject target = extend.Target.Invoke(this, new HassiumObject[0]);
                foreach (var attrib in extend.Additions.Attributes)
                {
                    if (target.Attributes.ContainsKey(attrib.Key))
                        target.Attributes.Remove(attrib.Key);
                    target.Attributes.Add(attrib.Key, attrib.Value);
                }
                if (globals.ContainsKey(key))
                    globals.Remove(key);
                globals.Add(key, target);
            }
        }
    }

    private void addArgs(List<String> args)
    {
        HassiumList list = new HassiumList(new HassiumObject[0]);
        foreach (string arg in args)
        list.Value.Add(new HassiumString(arg));
        globals.Add("args", list);
    }
}
