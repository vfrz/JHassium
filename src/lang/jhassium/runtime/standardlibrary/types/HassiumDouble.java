package lang.jhassium.runtime.standardlibrary.types;

import lang.jhassium.runtime.HassiumTypeDefinition;
import lang.jhassium.runtime.VirtualMachine;
import lang.jhassium.runtime.standardlibrary.HassiumObject;
import lang.jhassium.utils.HassiumLogger;

/**
 * File : HassiumDouble.java
 * Description : None
 * Author : FRITZ Valentin
 * Website : https://github.com/vfrz/JHassium
 * Date : 11/08/2016 00:04
 */
public class HassiumDouble extends HassiumObject {

    public static HassiumTypeDefinition TypeDefinition = new HassiumTypeDefinition("double");

    private double value;

    public Double getValue() {
        return value;
    }

    public int getValueInt() {
        return (int) value;
    }

    public static HassiumDouble create(HassiumObject obj) {
        if (!(obj instanceof HassiumDouble))
            HassiumLogger.error(String.format("Cannot convert from %1s to double!", obj.type()));
        return (HassiumDouble) obj;
    }

    public HassiumDouble(double value) {
        this.value = value;
        try {
            Attributes.put("toBool", new HassiumFunction(this.getClass().getDeclaredMethod("toBool"), this, 0));
            Attributes.put("toChar", new HassiumFunction(this.getClass().getDeclaredMethod("toChar"), this, 0));
            Attributes.put("toDouble", new HassiumFunction(this.getClass().getDeclaredMethod("toDouble"), this, 0));
            Attributes.put("toInt", new HassiumFunction(this.getClass().getDeclaredMethod("toInt"), this, 0));
            Attributes.put(HassiumObject.ADD_FUNCTION, new HassiumFunction(this.getClass().getDeclaredMethod("__add__"), this, 1));
            Attributes.put(HassiumObject.SUB_FUNCTION, new HassiumFunction(this.getClass().getDeclaredMethod("__sub__"), this, 1));
            Attributes.put(HassiumObject.MUL_FUNCTION, new HassiumFunction(this.getClass().getDeclaredMethod("__mul__"), this, 1));
            Attributes.put(HassiumObject.DIV_FUNCTION, new HassiumFunction(this.getClass().getDeclaredMethod("__div__"), this, 1));
            Attributes.put(HassiumObject.MOD_FUNCTION, new HassiumFunction(this.getClass().getDeclaredMethod("__mod__"), this, 1));
            Attributes.put(HassiumObject.EQUALS_FUNCTION, new HassiumFunction(this.getClass().getDeclaredMethod("__equals__"), this, 1));
            Attributes.put(HassiumObject.NOT_EQUAL_FUNCTION, new HassiumFunction(this.getClass().getDeclaredMethod("__notequal__"), this, 1));
            Attributes.put(HassiumObject.GREATER_FUNCTION, new HassiumFunction(this.getClass().getDeclaredMethod("__greater__"), this, 1));
            Attributes.put(HassiumObject.GREATER_OR_EQUAL_FUNCTION, new HassiumFunction(this.getClass().getDeclaredMethod("__greaterorequal__"), this, 1));
            Attributes.put(HassiumObject.LESSER_FUNCTION, new HassiumFunction(this.getClass().getDeclaredMethod("__lesser__"), this, 1));
            Attributes.put(HassiumObject.LESSER_OR_EQUAL_FUNCTION, new HassiumFunction(this.getClass().getDeclaredMethod("__lesserorequal__"), this, 1));
            Attributes.put(HassiumObject.ENUMERABLE_FULL, new HassiumFunction(this.getClass().getDeclaredMethod("__enumerablefull__"), this, 0));
            Attributes.put(HassiumObject.ENUMERABLE_NEXT, new HassiumFunction(this.getClass().getDeclaredMethod("__enumerablenext__"), this, 0));
            Attributes.put(HassiumObject.ENUMERABLE_RESET, new HassiumFunction(this.getClass().getDeclaredMethod("__enumerablereset__"), this, 0));
            Attributes.put(HassiumObject.TOSTRING_FUNCTION, new HassiumFunction(this.getClass().getDeclaredMethod("__tostring__"), this, 0));
            Attributes.put(NEGATE, new HassiumFunction(this.getClass().getDeclaredMethod("__negate__"), this, 0));
        } catch (NoSuchMethodException e) {
            HassiumLogger.error("Internal error HassiumDouble : " + e.getMessage());
        }
        addType(HassiumDouble.TypeDefinition);
    }

    private HassiumBool toBool(VirtualMachine vm, HassiumObject[] args) {
        return new HassiumBool(value == 1);
    }

    private HassiumChar toChar(VirtualMachine vm, HassiumObject[] args) {
        return new HassiumChar((char) value);
    }

    private HassiumDouble toDouble(VirtualMachine vm, HassiumObject[] args) {
        return this;
    }

    private HassiumInt toInt(VirtualMachine vm, HassiumObject[] args) {
        return new HassiumInt(getValueInt());
    }

    private HassiumObject __add__(VirtualMachine vm, HassiumObject[] args) {
        if (args[0] instanceof HassiumDouble)
            return new HassiumDouble(value + ((HassiumDouble) args[0]).getValue());
        else if (args[0] instanceof HassiumInt)
            return new HassiumDouble(value + ((HassiumInt) args[0]).getValue());
        else if (args[0] instanceof HassiumString)
            return new HassiumString(value + args[0].toString(vm));
        HassiumLogger.error("Cannot operate HassiumDouble on " + args[0].getClass().getName());
        return null; //Not reachable but for compilation
    }

    private HassiumObject __sub__(VirtualMachine vm, HassiumObject[] args) {
        if (args[0] instanceof HassiumDouble)
            return new HassiumDouble(value - ((HassiumDouble) args[0]).getValue());
        else if (args[0] instanceof HassiumInt)
            return new HassiumDouble(value - ((HassiumInt) args[0]).getValue());
        HassiumLogger.error("Cannot operate HassiumDouble on " + args[0].getClass().getName());
        return null; //Not reachable but for compilation
    }

    private HassiumObject __mul__(VirtualMachine vm, HassiumObject[] args) {
        if (args[0] instanceof HassiumDouble)
            return new HassiumDouble(value * ((HassiumDouble) args[0]).getValue());
        else if (args[0] instanceof HassiumInt)
            return new HassiumDouble(value * ((HassiumInt) args[0]).getValue());
        HassiumLogger.error("Cannot operate HassiumDouble on " + args[0].getClass().getName());
        return null; //Not reachable but for compilation
    }

    private HassiumObject __div__(VirtualMachine vm, HassiumObject[] args) {
        if (args[0] instanceof HassiumDouble)
            return new HassiumDouble(value / ((HassiumDouble) args[0]).getValue());
        else if (args[0] instanceof HassiumInt)
            return new HassiumDouble(value / ((HassiumInt) args[0]).getValue());
        HassiumLogger.error("Cannot operate HassiumDouble on " + args[0].getClass().getName());
        return null; //Not reachable but for compilation
    }

    private HassiumObject __mod__(VirtualMachine vm, HassiumObject[] args) {
        if (args[0] instanceof HassiumDouble)
            return new HassiumDouble(value % ((HassiumDouble) args[0]).getValue());
        else if (args[0] instanceof HassiumInt)
            return new HassiumDouble(value % ((HassiumInt) args[0]).getValue());
        HassiumLogger.error("Cannot operate HassiumDouble on " + args[0].getClass().getName());
        return null; //Not reachable but for compilation
    }

    private HassiumObject __equals__(VirtualMachine vm, HassiumObject[] args) {
        if (args[0] instanceof HassiumDouble)
            return new HassiumBool(value == ((HassiumDouble) args[0]).getValueInt());
        else if (args[0] instanceof HassiumInt)
            return new HassiumBool(value == ((HassiumInt) args[0]).getValue());
        HassiumLogger.error("Cannot operate HassiumDouble on " + args[0].getClass().getName());
        return null; //Not reachable but for compilation
    }

    private HassiumObject __notequal__(VirtualMachine vm, HassiumObject[] args) {
        if (args[0] instanceof HassiumDouble)
            return new HassiumBool(value != ((HassiumDouble) args[0]).getValueInt());
        else if (args[0] instanceof HassiumInt)
            return new HassiumBool(value != ((HassiumInt) args[0]).getValue());
        HassiumLogger.error("Cannot operate HassiumDouble on " + args[0].getClass().getName());
        return null; //Not reachable but for compilation
    }

    private HassiumObject __greater__(VirtualMachine vm, HassiumObject[] args) {
        if (args[0] instanceof HassiumDouble)
            return new HassiumBool(value > ((HassiumDouble) args[0]).getValue());
        else if (args[0] instanceof HassiumInt)
            return new HassiumBool(value > ((HassiumInt) args[0]).getValue());
        HassiumLogger.error("Cannot operate HassiumDouble on " + args[0].getClass().getName());
        return null; //Not reachable but for compilation
    }

    private HassiumObject __greaterorequal__(VirtualMachine vm, HassiumObject[] args) {
        if (args[0] instanceof HassiumDouble)
            return new HassiumBool(value >= ((HassiumDouble) args[0]).getValue());
        else if (args[0] instanceof HassiumInt)
            return new HassiumBool(value >= ((HassiumInt) args[0]).getValue());
        HassiumLogger.error("Cannot operate HassiumDouble on " + args[0].getClass().getName());
        return null; //Not reachable but for compilation
    }

    private HassiumObject __lesser__(VirtualMachine vm, HassiumObject[] args) {
        if (args[0] instanceof HassiumDouble)
            return new HassiumBool(value < ((HassiumDouble) args[0]).getValue());
        else if (args[0] instanceof HassiumInt)
            return new HassiumBool(value < ((HassiumInt) args[0]).getValue());
        HassiumLogger.error("Cannot operate HassiumDouble on " + args[0].getClass().getName());
        return null; //Not reachable but for compilation
    }

    private HassiumObject __lesserorequal__(VirtualMachine vm, HassiumObject[] args) {
        if (args[0] instanceof HassiumDouble)
            return new HassiumBool(value <= ((HassiumDouble) args[0]).getValue());
        else if (args[0] instanceof HassiumInt)
            return new HassiumBool(value <= ((HassiumInt) args[0]).getValue());
        HassiumLogger.error("Cannot operate HassiumDouble on " + args[0].getClass().getName());
        return null; //Not reachable but for compilation
    }

    private int enumerableIndex = 0;

    private HassiumBool __enumerablefull__(VirtualMachine vm, HassiumObject[] args) {
        return new HassiumBool(enumerableIndex >= value);
    }

    private HassiumDouble __enumerablenext__(VirtualMachine vm, HassiumObject[] args) {
        return new HassiumDouble(enumerableIndex++);
    }

    private HassiumNull __enumerablereset__(VirtualMachine vm, HassiumObject[] args) {
        enumerableIndex = 0;
        return HassiumObject.Null;
    }

    private HassiumString __tostring__(VirtualMachine vm, HassiumObject[] args) {
        return new HassiumString(Double.toString(value));
    }

    private HassiumObject __negate__(VirtualMachine vm, HassiumObject[] args) {
        return new HassiumDouble(-value);
    }

    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    public int hashCode() {
        return super.hashCode();
    }

    public String toString() {
        return Double.toString(value);
    }
}
