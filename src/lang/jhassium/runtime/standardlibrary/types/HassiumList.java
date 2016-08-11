package lang.jhassium.runtime.standardlibrary.types;

import lang.jhassium.runtime.HassiumTypeDefinition;
import lang.jhassium.runtime.VirtualMachine;
import lang.jhassium.runtime.standardlibrary.HassiumObject;
import lang.jhassium.utils.HassiumLogger;
import lang.jhassium.utils.Helpers;

import java.util.ArrayList;
import java.util.List;

/**
 * File : HassiumList.java
 * Description : None
 * Author : FRITZ Valentin
 * Website : https://github.com/vfrz/JHassium
 * Date : 11/08/2016 00:28
 */
public class HassiumList extends HassiumObject {

    public static HassiumTypeDefinition TypeDefinition = new HassiumTypeDefinition("list");

    private List<HassiumObject> value;

    public List<HassiumObject> getValue() {
        return value;
    }

    public static HassiumList Create(HassiumObject obj) {
        if (!(obj instanceof HassiumList))
            HassiumLogger.error(String.format("Cannot convert from %1s to list!", obj.type()));
        return (HassiumList) obj;
    }

    public HassiumList(HassiumObject[] initial) {
        this.value = new ArrayList<>();
        for (HassiumObject obj : initial) value.add(obj);

        try {
            Attributes.put("add", new HassiumFunction(this.getClass().getDeclaredMethod("_add"), this, -1));
            Attributes.put("contains", new HassiumFunction(this.getClass().getDeclaredMethod("contains"), this, -1));
            Attributes.put("copy", new HassiumFunction(this.getClass().getDeclaredMethod("copy"), this, 1));
            Attributes.put("Event", new HassiumEvent());
            Attributes.put("getString", new HassiumFunction(this.getClass().getDeclaredMethod("getString"), this, 0));
            Attributes.put("indexOf", new HassiumFunction(this.getClass().getDeclaredMethod("indexOf"), this, 1));
            Attributes.put("lastIndexOf", new HassiumFunction(this.getClass().getDeclaredMethod("lastIndexOf"), this, 1));
            Attributes.put("length", new HassiumProperty(this.getClass().getDeclaredMethod("get_Length"), this));
            Attributes.put("remove", new HassiumFunction(this.getClass().getDeclaredMethod("remove"), this, -1));
            Attributes.put("reverse", new HassiumFunction(this.getClass().getDeclaredMethod("reverse"), this, 0));
            Attributes.put("slice", new HassiumFunction(this.getClass().getDeclaredMethod("slice"), this, new int[]{1, 2}));
            //Attributes.put("Thread", new HassiumThread());
            Attributes.put(HassiumObject.CONTAINS, new HassiumFunction(this.getClass().getDeclaredMethod("contains"), this, 1));
            Attributes.put(HassiumObject.TOSTRING_FUNCTION, new HassiumFunction(this.getClass().getDeclaredMethod("__tostring__"), this, 0));
            Attributes.put(HassiumObject.EQUALS_FUNCTION, new HassiumFunction(this.getClass().getDeclaredMethod("__equals__"), this, 1));
            Attributes.put(HassiumObject.NOT_EQUAL_FUNCTION, new HassiumFunction(this.getClass().getDeclaredMethod("__notequals__"), this, 1));
            Attributes.put(HassiumObject.INDEX_FUNCTION, new HassiumFunction(this.getClass().getDeclaredMethod("__index__"), this, 1));
            Attributes.put(HassiumObject.STORE_INDEX_FUNCTION, new HassiumFunction(this.getClass().getDeclaredMethod("__storeindex__"), this, 2));
            Attributes.put(HassiumObject.ITER_FUNCTION, new HassiumFunction(this.getClass().getDeclaredMethod("__iter__"), this, 0));
            Attributes.put(HassiumObject.SLICE_FUNCTION, new HassiumFunction(this.getClass().getDeclaredMethod("slice"), this, 2));
            Attributes.put(HassiumObject.SKIP_FUNCTION, new HassiumFunction(this.getClass().getDeclaredMethod("skip"), this, 1));
            Attributes.put(HassiumObject.ADD_FUNCTION, new HassiumFunction(this.getClass().getDeclaredMethod("__add__"), this, 1));
            Attributes.put(HassiumObject.ENUMERABLE_FULL, new HassiumFunction(this.getClass().getDeclaredMethod("__enumerablefull__"), this, 0));
            Attributes.put(HassiumObject.ENUMERABLE_NEXT, new HassiumFunction(this.getClass().getDeclaredMethod("__enumerablenext__"), this, 0));
            Attributes.put(HassiumObject.ENUMERABLE_RESET, new HassiumFunction(this.getClass().getDeclaredMethod("__enumerablereset__"), this, 0));
        } catch (NoSuchMethodException e) {
            HassiumLogger.error("Internal error HassiumChar : " + e.getMessage());
        }
        addType(HassiumList.TypeDefinition);
    }

    private HassiumObject _add(VirtualMachine vm, HassiumObject[] args) {
        for (HassiumObject obj : args) value.add(obj);
        return HassiumObject.Null;
    }

    private HassiumBool contains(VirtualMachine vm, HassiumObject[] args) {
        for (HassiumObject obj : args) {
            for (HassiumObject x : value) {
                if (!x.equals(vm, obj).getValue())
                    return new HassiumBool(false);
            }
        }
        return new HassiumBool(true);
    }

    private HassiumNull copy(VirtualMachine vm, HassiumObject[] args) {
        HassiumList list = HassiumList.Create(args[0]);
        for (HassiumObject obj : value) {
            list.add(vm, obj);
        }
        return HassiumObject.Null;
    }

    private HassiumString getString(VirtualMachine vm, HassiumObject[] args) {
        byte[] bytes = new byte[value.size()];
        for (int i = 0; i < bytes.length; i++)
            bytes[i] = (byte) (char) HassiumChar.create(value.get(i)).getValue();
        return new HassiumString(new String(bytes)); //HACK Verify charset
    }

    private HassiumDouble indexOf(VirtualMachine vm, HassiumObject[] args) {
        for (int i = 0; i < value.size(); i++)
            if (value.get(i).equals(vm, args[0]).getValue())
                return new HassiumDouble(i);
        return new HassiumDouble(-1);
    }

    private HassiumDouble lastIndexOf(VirtualMachine vm, HassiumObject[] args) {
        for (int i = value.size() - 1; i >= 0; i--)
            if (value.get(i).equals(vm, args[0]).getValue())
                return new HassiumDouble(i);
        return new HassiumDouble(-1);
    }

    private HassiumInt get_Length(VirtualMachine vm, HassiumObject[] args) {
        return new HassiumInt(value.size());
    }

    private HassiumNull remove(VirtualMachine vm, HassiumObject[] args) {
        for (HassiumObject obj : args) value.remove(obj);
        return HassiumObject.Null;
    }

    private HassiumList reverse(VirtualMachine vm, HassiumObject[] args) {
        HassiumObject[] elements = new HassiumObject[value.size()];
        for (int i = 0; i < elements.length; i++)
            elements[i] = value.get(value.size() - (i + 1));
        return new HassiumList(elements);
    }

    private HassiumList skip(VirtualMachine vm, HassiumObject[] args) {
        HassiumList list = new HassiumList(new HassiumObject[0]);
        int step = (int) (long) HassiumInt.create(args[0]).getValue();
        if (step == -1)
            return reverse(vm, args);
        for (int i = 0; (i + step) < value.size(); i += step)
            list.value.add(value.get(i));
        return list;
    }

    private HassiumList slice(VirtualMachine vm, HassiumObject[] args) {
        HassiumList list = new HassiumList(new HassiumObject[0]);

        int max = args.length == 2 ? (int) (long) HassiumInt.create(args[1]).getValue() : list.getValue().size();
        if (max == -1)
            max = list.getValue().size() - 2;
        for (int i = (int) (long) HassiumInt.create(args[0]).getValue(); i < max; i++)
            list.add(vm, value.get(i));
        return list;
    }

    private HassiumString __tostring__(VirtualMachine vm, HassiumObject[] args) {
        StringBuilder sb = new StringBuilder();
        for (HassiumObject obj : value) sb.append(obj.toString(vm) + " ");
        return new HassiumString(sb.toString());
    }

    private HassiumBool __equals__(VirtualMachine vm, HassiumObject[] args) {
        HassiumList list = HassiumList.Create(args[0].iter(vm));
        for (int i = 0; i < list.getValue().size(); i++) {
            if (!list.getValue().get(i).equals(vm, value.get(i)).getValue())
                return new HassiumBool(false);
        }
        return new HassiumBool(true);
    }

    private HassiumBool __notequals__(VirtualMachine vm, HassiumObject[] args) {
        return new HassiumBool(!__equals__(vm, args).getValue());
    }

    private HassiumObject __index__(VirtualMachine vm, HassiumObject[] args) {
        HassiumObject obj = args[0];
        if (obj instanceof HassiumDouble)
            return value.get(((HassiumDouble) obj).getValueInt());
        else if (obj instanceof HassiumInt)
            return value.get((int) (long) ((HassiumInt) obj).getValue());
        HassiumLogger.error("Cannot index list with " + obj.type().toString(vm));
        return null; // Not reachable but for compilation
    }

    private HassiumObject __storeindex__(VirtualMachine vm, HassiumObject[] args) {
        int index = 0;
        if (args[0] instanceof HassiumInt)
            index = (int) (long) ((HassiumInt) args[0]).getValue();
        else if (args[0] instanceof HassiumDouble)
            index = ((HassiumDouble) args[0]).getValueInt();
        else
            HassiumLogger.error("Cannot index list with " + args[0].type().toString(vm));
        value.set(index, args[1]);
        return args[1];
    }

    private HassiumList __add__(VirtualMachine vm, HassiumObject[] args) {
        HassiumList copy = Helpers.as(this.Clone(), HassiumList.class);
        copy.value.add(args[0]);
        return copy;
    }

    public int EnumerableIndex = 0;

    private HassiumObject __iter__(VirtualMachine vm, HassiumObject[] args) {
        return this;
    }

    private HassiumObject __enumerablefull__(VirtualMachine vm, HassiumObject[] args) {
        return new HassiumBool(EnumerableIndex >= value.size());
    }

    private HassiumObject __enumerablenext__(VirtualMachine vm, HassiumObject[] args) {
        return value.get(EnumerableIndex++);
    }

    private HassiumObject __enumerablereset__(VirtualMachine vm, HassiumObject[] args) {
        EnumerableIndex = 0;
        return HassiumObject.Null;
    }
}
