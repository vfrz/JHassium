package lang.jhassium.runtime.standardlibrary.types;

import lang.jhassium.runtime.HassiumTypeDefinition;
import lang.jhassium.runtime.VirtualMachine;
import lang.jhassium.runtime.standardlibrary.HassiumObject;
import lang.jhassium.utils.HassiumLogger;

/**
 * File : HassiumTuple.java
 * Description : None
 * Authors : FRITZ Valentin, Jacob Misirian & zdimension
 * Website : https://github.com/vfrz/JHassium
 * Date : 11/08/2016 16:29
 */
public class HassiumTuple extends HassiumObject {

    public static HassiumTypeDefinition TypeDefinition = new HassiumTypeDefinition("tuple");

    private HassiumObject[] value;

    public HassiumObject[] getValue() {
        return value;
    }

    public HassiumTuple(HassiumObject[] elements) {
        value = elements;

        try {
            Attributes.put("length", new HassiumProperty(this.getClass().getDeclaredMethod("get_Length", VirtualMachine.class, HassiumObject[].class), this));
            Attributes.put("split", new HassiumFunction(this.getClass().getDeclaredMethod("split", VirtualMachine.class, HassiumObject[].class), this, 2));
            Attributes.put(HassiumObject.EQUALS_FUNCTION, new HassiumFunction(this.getClass().getDeclaredMethod("__equals__", VirtualMachine.class, HassiumObject[].class), this, 1));
            Attributes.put(HassiumObject.NOT_EQUAL_FUNCTION, new HassiumFunction(this.getClass().getDeclaredMethod("__notequals__", VirtualMachine.class, HassiumObject[].class), this, 1));
            Attributes.put(HassiumObject.INDEX_FUNCTION, new HassiumFunction(this.getClass().getDeclaredMethod("__index__", VirtualMachine.class, HassiumObject[].class), this, 1));
            Attributes.put(HassiumObject.ITER_FUNCTION, new HassiumFunction(this.getClass().getDeclaredMethod("__iter__", VirtualMachine.class, HassiumObject[].class), this, 0));
            Attributes.put(HassiumObject.TOSTRING_FUNCTION, new HassiumFunction(this.getClass().getDeclaredMethod("__tostring__", VirtualMachine.class, HassiumObject[].class), this, 0));
        } catch (NoSuchMethodException e) {
            HassiumLogger.error("Internal error HassiumBool : " + e.getMessage());
        }

        addType(HassiumTuple.TypeDefinition);
    }

    public HassiumInt get_Length(VirtualMachine vm, HassiumObject[] args) {
        return new HassiumInt(value.length);
    }

    public HassiumTuple split(VirtualMachine vm, HassiumObject[] args) {
        int min = HassiumInt.create(args[0]).getValue().intValue();
        int max = HassiumInt.create(args[1]).getValue().intValue();

        HassiumObject[] elements = new HassiumObject[max - min + 1];

        for (int i = 0; i <= max - min; i++)
            elements[i] = value[i + min];

        return new HassiumTuple(elements);
    }

    public HassiumBool __equals__(VirtualMachine vm, HassiumObject[] args) {
        HassiumList list = HassiumList.Create(args[0].iter(vm));
        for (int i = 0; i < list.getValue().size(); i++)
            if (!list.getValue().get(i).equals(vm, value[i]).getValue())
                return new HassiumBool(false);
        return new HassiumBool(true);
    }

    public HassiumBool __notequals__(VirtualMachine vm, HassiumObject[] args) {
        return new HassiumBool(!__equals__(vm, args).getValue());
    }

    public HassiumObject __index__(VirtualMachine vm, HassiumObject[] args) {
        HassiumObject obj = args[0];
        if (obj instanceof HassiumDouble)
            return value[((HassiumDouble) obj).getValueInt()];
        else if (obj instanceof HassiumInt)
            return value[((HassiumInt) obj).getValue().intValue()];
        HassiumLogger.error("Cannot index list with " + obj.getClass().getName());
        return null; //Not reachable but for compilation
    }

    public HassiumString __tostring__(VirtualMachine vm, HassiumObject[] args) {
        StringBuilder sb = new StringBuilder();
        for (HassiumObject obj : value)
            sb.append(obj.toString(vm));

        return new HassiumString(sb.toString());
    }

    public HassiumList __iter__(VirtualMachine vm, HassiumObject[] args) {
        return new HassiumList(value);
    }

}
