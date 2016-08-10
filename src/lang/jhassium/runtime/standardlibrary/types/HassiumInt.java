package lang.jhassium.runtime.standardlibrary.types;

import lang.jhassium.runtime.HassiumTypeDefinition;
import lang.jhassium.runtime.VirtualMachine;
import lang.jhassium.runtime.standardlibrary.HassiumObject;
import lang.jhassium.utils.HassiumLogger;

/**
 * File : HassiumInt.java
 * Description : None
 * Author : FRITZ Valentin
 * Website : https://github.com/vfrz/JHassium
 * Date : 11/08/2016 00:04
 */
public class HassiumInt extends HassiumObject {

    public static HassiumTypeDefinition TypeDefinition = new HassiumTypeDefinition("int");

    private long value;

    public static HassiumInt create(HassiumObject obj) {
        if (!(obj instanceof HassiumInt))
            HassiumLogger.error(String.format("Cannot convert from %1s to int!", obj.type().toString(null)));
        return (HassiumInt) obj;
    }

    public HassiumInt(long value) {
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
            Attributes.put(HassiumObject.XOR_FUNCTION, new HassiumFunction(this.getClass().getDeclaredMethod("__xor__"), this, 1));
            Attributes.put(HassiumObject.OR_FUNCTION, new HassiumFunction(this.getClass().getDeclaredMethod("__or__"), this, 1));
            Attributes.put(HassiumObject.XAND_FUNCTION, new HassiumFunction(this.getClass().getDeclaredMethod("__xand__"), this, 1));
            Attributes.put(HassiumObject.EQUALS_FUNCTION, new HassiumFunction(this.getClass().getDeclaredMethod("__equals__"), this, 1));
            Attributes.put(HassiumObject.NOT_EQUAL_FUNCTION, new HassiumFunction(this.getClass().getDeclaredMethod("__notequal__"), this, 1));
            Attributes.put(HassiumObject.GREATER_FUNCTION, new HassiumFunction(this.getClass().getDeclaredMethod("__greater__"), this, 1));
            Attributes.put(HassiumObject.GREATER_OR_EQUAL_FUNCTION, new HassiumFunction(this.getClass().getDeclaredMethod("__greaterorequal__"), this, 1));
            Attributes.put(HassiumObject.LESSER_FUNCTION, new HassiumFunction(this.getClass().getDeclaredMethod("__lesser__"), this, 1));
            Attributes.put(HassiumObject.LESSER_OR_EQUAL_FUNCTION, new HassiumFunction(this.getClass().getDeclaredMethod("__lesserorequal__"), this, 1));
            Attributes.put(HassiumObject.TOSTRING_FUNCTION, new HassiumFunction(this.getClass().getDeclaredMethod("__tostring__"), this, 0));
            Attributes.put(BITWISE_COMPLEMENT, new HassiumFunction(this.getClass().getDeclaredMethod("__bcompl__"), this, 0));
            Attributes.put(NEGATE, new HassiumFunction(this.getClass().getDeclaredMethod("__negate__"), this, 0));
            Attributes.put(BIT_SHIFT_LEFT, new HassiumFunction(this.getClass().getDeclaredMethod("__bshiftleft__"), this, 1));
            Attributes.put(BIT_SHIFT_RIGHT, new HassiumFunction(this.getClass().getDeclaredMethod("__bshiftright__"), this, 1));
        } catch (NoSuchMethodException e) {
            HassiumLogger.error("Internal error HassiumInt : " + e.getMessage());
        }
        addType(HassiumInt.TypeDefinition);
    }

    public Long getValue() {
        return value;
    }

    private HassiumBool toBool(VirtualMachine vm, HassiumObject[] args) {
        return new HassiumBool(value == 1);
    }

    private HassiumChar toChar(VirtualMachine vm, HassiumObject[] args) {
        return new HassiumChar((char) value);
    }

    private HassiumDouble toDouble(VirtualMachine vm, HassiumObject[] args) {
        return new HassiumDouble(value);
    }

    private HassiumInt toInt(VirtualMachine vm, HassiumObject[] args) {
        return this;
    }

    private HassiumObject __add__(VirtualMachine vm, HassiumObject[] args) {
        if (args[0] instanceof HassiumDouble)
            return new HassiumDouble(value + ((HassiumDouble) args[0]).getValue());
        else if (args[0] instanceof HassiumInt)
            return new HassiumInt(value + ((HassiumInt) args[0]).getValue());
        else if (args[0] instanceof HassiumString)
            return new HassiumString(value + args[0].toString(vm));
        HassiumLogger.error("Cannot operate HassiumInt on " + args[0].getClass().getName());
        return null; //Not reachable but for compilation
    }

    private HassiumObject __sub__(VirtualMachine vm, HassiumObject[] args) {
        if (args[0] instanceof HassiumDouble)
            return new HassiumDouble(value - ((HassiumDouble) args[0]).getValue());
        else if (args[0] instanceof HassiumInt)
            return new HassiumInt(value - ((HassiumInt) args[0]).getValue());
        HassiumLogger.error("Cannot operate HassiumInt on " + args[0].getClass().getName());
        return null; //Not reachable but for compilation
    }

    private HassiumObject __mul__(VirtualMachine vm, HassiumObject[] args) {
        if (args[0] instanceof HassiumDouble)
            return new HassiumDouble(value * ((HassiumDouble) args[0]).getValue());
        else if (args[0] instanceof HassiumInt)
            return new HassiumInt(value * ((HassiumInt) args[0]).getValue());
        HassiumLogger.error("Cannot operate HassiumInt on " + args[0].getClass().getName());
        return null; //Not reachable but for compilation
    }

    private HassiumObject __div__(VirtualMachine vm, HassiumObject[] args) {
        if (args[0] instanceof HassiumDouble)
            return new HassiumDouble(value / ((HassiumDouble) args[0]).getValue());
        else if (args[0] instanceof HassiumInt)
            return new HassiumInt(value / ((HassiumInt) args[0]).getValue());
        HassiumLogger.error("Cannot operate HassiumInt on " + args[0].getClass().getName());
        return null; //Not reachable but for compilation
    }

    private HassiumObject __mod__(VirtualMachine vm, HassiumObject[] args) {
        if (args[0] instanceof HassiumDouble)
            return new HassiumDouble(value % ((HassiumDouble) args[0]).getValue());
        else if (args[0] instanceof HassiumInt)
            return new HassiumInt(value % ((HassiumInt) args[0]).getValue());
        HassiumLogger.error("Cannot operate HassiumInt on " + args[0].getClass().getName());
        return null; //Not reachable but for compilation
    }

    private HassiumObject __xor__(VirtualMachine vm, HassiumObject[] args) {
        return new HassiumInt(value ^ HassiumInt.create(args[0]).getValue());
    }

    private HassiumObject __or__(VirtualMachine vm, HassiumObject[] args) {
        return new HassiumInt(value | HassiumInt.create(args[0]).getValue());
    }

    private HassiumObject __xand__(VirtualMachine vm, HassiumObject[] args) {
        return new HassiumInt(value & HassiumInt.create(args[0]).getValue());
    }

    private HassiumObject __equals__(VirtualMachine vm, HassiumObject[] args) {
        if (args[0] instanceof HassiumDouble)
            return new HassiumBool(value == ((HassiumDouble) args[0]).getValue());
        else if (args[0] instanceof HassiumInt)
            return new HassiumBool(value == ((HassiumInt) args[0]).getValue());
        HassiumLogger.error("Cannot operate HassiumInt on " + args[0].getClass().getName());
        return null; //Not reachable but for compilation
    }

    private HassiumObject __notequal__(VirtualMachine vm, HassiumObject[] args) {
        if (args[0] instanceof HassiumDouble)
            return new HassiumBool(value != ((HassiumDouble) args[0]).getValue());
        else if (args[0] instanceof HassiumInt)
            return new HassiumBool(value != ((HassiumInt) args[0]).getValue());
        HassiumLogger.error("Cannot operate HassiumInt on " + args[0].getClass().getName());
        return null; //Not reachable but for compilation
    }

    private HassiumObject __greater__(VirtualMachine vm, HassiumObject[] args) {
        if (args[0] instanceof HassiumDouble)
            return new HassiumBool(value > ((HassiumDouble) args[0]).getValue());
        else if (args[0] instanceof HassiumInt)
            return new HassiumBool(value > ((HassiumInt) args[0]).getValue());
        HassiumLogger.error("Cannot operate HassiumInt on " + args[0].getClass().getName());
        return null; //Not reachable but for compilation
    }

    private HassiumObject __greaterorequal__(VirtualMachine vm, HassiumObject[] args) {
        if (args[0] instanceof HassiumDouble)
            return new HassiumBool(value >= ((HassiumDouble) args[0]).getValue());
        else if (args[0] instanceof HassiumInt)
            return new HassiumBool(value >= ((HassiumInt) args[0]).getValue());
        HassiumLogger.error("Cannot operate HassiumInt on " + args[0].getClass().getName());
        return null; //Not reachable but for compilation
    }

    private HassiumObject __lesser__(VirtualMachine vm, HassiumObject[] args) {
        if (args[0] instanceof HassiumDouble)
            return new HassiumBool(value < ((HassiumDouble) args[0]).getValue());
        else if (args[0] instanceof HassiumInt)
            return new HassiumBool(value < ((HassiumInt) args[0]).getValue());
        HassiumLogger.error("Cannot operate HassiumInt on " + args[0].getClass().getName());
        return null; //Not reachable but for compilation
    }

    private HassiumObject __lesserorequal__(VirtualMachine vm, HassiumObject[] args) {
        if (args[0] instanceof HassiumDouble)
            return new HassiumBool(value <= ((HassiumDouble) args[0]).getValue());
        else if (args[0] instanceof HassiumInt)
            return new HassiumBool(value <= ((HassiumInt) args[0]).getValue());
        HassiumLogger.error("Cannot operate HassiumInt on " + args[0].getClass().getName());
        return null; //Not reachable but for compilation
    }

    private HassiumString __tostring__(VirtualMachine vm, HassiumObject[] args) {
        return new HassiumString(Long.toString(value));
    }

    private HassiumObject __bcompl__(VirtualMachine vm, HassiumObject[] args) {
        return new HassiumInt(~value);
    }

    private HassiumObject __negate__(VirtualMachine vm, HassiumObject[] args) {
        return new HassiumInt(-value);
    }

    private HassiumObject __bshiftleft__(VirtualMachine vm, HassiumObject[] args) {
        return new HassiumInt(value << HassiumInt.create(args[0]).getValue());
    }

    private HassiumObject __bshiftright__(VirtualMachine vm, HassiumObject[] args) {
        return new HassiumInt(value >> HassiumInt.create(args[0]).getValue());
    }

}
