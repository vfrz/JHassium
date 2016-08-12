package lang.jhassium.runtime.standardlibrary.collections;

import lang.jhassium.runtime.HassiumTypeDefinition;
import lang.jhassium.runtime.VirtualMachine;
import lang.jhassium.runtime.standardlibrary.HassiumObject;
import lang.jhassium.runtime.standardlibrary.types.*;
import lang.jhassium.utils.HassiumLogger;

import java.util.ArrayList;
import java.util.List;

/**
 * File : HassiumStack.java
 * Description : None
 * Authors : FRITZ Valentin, Jacob Misirian & zdimension
 * Website : https://github.com/vfrz/JHassium
 * Date : 12/08/2016 11:47
 */
public class HassiumStack extends HassiumObject {

    public static HassiumTypeDefinition TypeDefinition = new HassiumTypeDefinition("Stack");

    public List<HassiumObject> Stack;
    public int EnumerableIndex = 0;

    public HassiumStack() {
        try {
            Attributes.put(HassiumObject.INVOKE_FUNCTION, new HassiumFunction(this.getClass().getDeclaredMethod("_new", VirtualMachine.class, HassiumObject[].class), this, 0));
        } catch (NoSuchMethodException e) {
            HassiumLogger.error("Internal error HassiumStack : " + e.getMessage());
        }
        addType(HassiumStack.TypeDefinition);
    }

    public HassiumStack _new(VirtualMachine vm, HassiumObject[] args) {
        HassiumStack hassiumStack = new HassiumStack();

        hassiumStack.Stack = new ArrayList<>();
        try {
            hassiumStack.Attributes.put("peek", new HassiumFunction(hassiumStack.getClass().getDeclaredMethod("peek", VirtualMachine.class, HassiumObject[].class), hassiumStack, 0));
            hassiumStack.Attributes.put("pop", new HassiumFunction(hassiumStack.getClass().getDeclaredMethod("pop", VirtualMachine.class, HassiumObject[].class), hassiumStack, 0));
            hassiumStack.Attributes.put("push", new HassiumFunction(hassiumStack.getClass().getDeclaredMethod("push", VirtualMachine.class, HassiumObject[].class), hassiumStack, -1));
            hassiumStack.Attributes.put("toList", new HassiumFunction(hassiumStack.getClass().getDeclaredMethod("toList", VirtualMachine.class, HassiumObject[].class), hassiumStack, 0));
            hassiumStack.Attributes.put(HassiumObject.TOSTRING_FUNCTION, new HassiumFunction(hassiumStack.getClass().getDeclaredMethod("tostring", VirtualMachine.class, HassiumObject[].class), hassiumStack, 0));
            hassiumStack.Attributes.put(HassiumObject.INDEX_FUNCTION, new HassiumFunction(hassiumStack.getClass().getDeclaredMethod("__index__", VirtualMachine.class, HassiumObject[].class), hassiumStack, 1));
            hassiumStack.Attributes.put(HassiumObject.STORE_INDEX_FUNCTION, new HassiumFunction(hassiumStack.getClass().getDeclaredMethod("__storeindex__", VirtualMachine.class, HassiumObject[].class), hassiumStack, 2));
            hassiumStack.Attributes.put(HassiumObject.ENUMERABLE_FULL, new HassiumFunction(hassiumStack.getClass().getDeclaredMethod("__enumerablefull__", VirtualMachine.class, HassiumObject[].class), hassiumStack, 0));
            hassiumStack.Attributes.put(HassiumObject.ENUMERABLE_NEXT, new HassiumFunction(hassiumStack.getClass().getDeclaredMethod("__enumerablenext__", VirtualMachine.class, HassiumObject[].class), hassiumStack, 0));
            hassiumStack.Attributes.put(HassiumObject.ENUMERABLE_RESET, new HassiumFunction(hassiumStack.getClass().getDeclaredMethod("__enumerablereset__", VirtualMachine.class, HassiumObject[].class), hassiumStack, 0));
        } catch (NoSuchMethodException e) {
            HassiumLogger.error("Internal error HassiumStack : " + e.getMessage());
        }
        return hassiumStack;
    }

    public HassiumObject peek(VirtualMachine vm, HassiumObject[] args) {
        return Stack.get(Stack.size() - 1);
    }

    public HassiumObject pop(VirtualMachine vm, HassiumObject[] args) {
        HassiumObject ret = Stack.get(Stack.size() - 1);
        Stack.remove(Stack.get(Stack.size() - 1));
        return ret;
    }

    public HassiumNull push(VirtualMachine vm, HassiumObject[] args) {
        for (HassiumObject obj : args)
            Stack.add(obj);
        return HassiumObject.Null;
    }

    public HassiumList toList(VirtualMachine vm, HassiumObject[] args) {
        return new HassiumList((HassiumObject[]) Stack.toArray());
    }

    public HassiumString tostring(VirtualMachine vm, HassiumObject[] args) {
        StringBuilder sb = new StringBuilder();
        sb.append("{ ");
        for (int i = 0; i < Stack.size() - 1; i++)
            sb.append(Stack.get(i).toString(vm) + " , ");
        sb.append(Stack.get(Stack.size() - 1).toString(vm));
        sb.append(" }");
        return new HassiumString(sb.toString());
    }

    public HassiumObject __index__(VirtualMachine vm, HassiumObject[] args) {
        HassiumObject obj = args[0];
        if (obj instanceof HassiumDouble)
            return Stack.get(((HassiumDouble) obj).getValueInt());
        else if (obj instanceof HassiumInt)
            return Stack.get(((HassiumInt) obj).getValue());
        HassiumLogger.error("Cannot index stack with " + obj);
        return null; //Not reachable but for compilation
    }

    public HassiumObject __storeindex__(VirtualMachine vm, HassiumObject[] args) {
        HassiumObject index = args[0];
        if (index instanceof HassiumDouble)
            Stack.set(((HassiumDouble) index).getValueInt(), args[1]);
        else if (index instanceof HassiumInt)
            Stack.set(((HassiumInt) index).getValue(), args[1]);
        else
            HassiumLogger.error("Cannot index stack with " + index);
        return args[1];
    }

    public HassiumObject __enumerablefull__(VirtualMachine vm, HassiumObject[] args) {
        return new HassiumBool(EnumerableIndex >= Stack.size());
    }

    public HassiumObject __enumerablenext__(VirtualMachine vm, HassiumObject[] args) {
        return Stack.get(EnumerableIndex++);
    }

    public HassiumObject __enumerablereset__(VirtualMachine vm, HassiumObject[] args) {
        EnumerableIndex = 0;
        return HassiumObject.Null;
    }

}
