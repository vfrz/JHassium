package lang.jhassium.runtime.standardlibrary.types;

import lang.jhassium.runtime.HassiumTypeDefinition;
import lang.jhassium.runtime.VirtualMachine;
import lang.jhassium.runtime.standardlibrary.HassiumObject;
import lang.jhassium.utils.HassiumLogger;

/**
 * File : HassiumChar.java
 * Description : None
 * Author : FRITZ Valentin
 * Website : https://github.com/vfrz/JHassium
 * Date : 11/08/2016 00:07
 */
public class HassiumChar extends HassiumObject {

    public static HassiumTypeDefinition TypeDefinition = new HassiumTypeDefinition("char");

    private char value;

    public Character getValue() {
        return value;
    }

    public static HassiumChar create(HassiumObject obj) {
        if (!(obj instanceof HassiumChar))
            HassiumLogger.error(String.format("Cannot convert from %1s to char!", obj.type()));
        return (HassiumChar) obj;
    }

    public HassiumChar(char value) {
        this.value = value;
        try {
            Attributes.put("isDigit", new HassiumFunction(this.getClass().getDeclaredMethod("isDigit", VirtualMachine.class, HassiumObject[].class), this, 0));
            Attributes.put("isLetter", new HassiumFunction(this.getClass().getDeclaredMethod("isLetter", VirtualMachine.class, HassiumObject[].class), this, 0));
            Attributes.put("isLetterOrDigit", new HassiumFunction(this.getClass().getDeclaredMethod("isLetterOrDigit", VirtualMachine.class, HassiumObject[].class), this, 0));
            Attributes.put("isLower", new HassiumFunction(this.getClass().getDeclaredMethod("isLower", VirtualMachine.class, HassiumObject[].class), this, 0));
            Attributes.put("isUpper", new HassiumFunction(this.getClass().getDeclaredMethod("isUpper", VirtualMachine.class, HassiumObject[].class), this, 0));
            Attributes.put("isWhitespace", new HassiumFunction(this.getClass().getDeclaredMethod("isWhitespace", VirtualMachine.class, HassiumObject[].class), this, 0));
            Attributes.put("toBool", new HassiumFunction(this.getClass().getDeclaredMethod("toBool", VirtualMachine.class, HassiumObject[].class), this, 0));
            Attributes.put("toChar", new HassiumFunction(this.getClass().getDeclaredMethod("toChar", VirtualMachine.class, HassiumObject[].class), this, 0));
            Attributes.put("toDouble", new HassiumFunction(this.getClass().getDeclaredMethod("toDouble", VirtualMachine.class, HassiumObject[].class), this, 0));
            Attributes.put("toInt", new HassiumFunction(this.getClass().getDeclaredMethod("toInt", VirtualMachine.class, HassiumObject[].class), this, 0));
            Attributes.put(HassiumObject.ADD_FUNCTION, new HassiumFunction(this.getClass().getDeclaredMethod("__add__", VirtualMachine.class, HassiumObject[].class), this, 1));
            Attributes.put(HassiumObject.SUB_FUNCTION, new HassiumFunction(this.getClass().getDeclaredMethod("__sub__", VirtualMachine.class, HassiumObject[].class), this, 1));
            Attributes.put(HassiumObject.MUL_FUNCTION, new HassiumFunction(this.getClass().getDeclaredMethod("__mul__", VirtualMachine.class, HassiumObject[].class), this, 1));
            Attributes.put(HassiumObject.DIV_FUNCTION, new HassiumFunction(this.getClass().getDeclaredMethod("__div__", VirtualMachine.class, HassiumObject[].class), this, 1));
            Attributes.put(HassiumObject.MOD_FUNCTION, new HassiumFunction(this.getClass().getDeclaredMethod("__mod__", VirtualMachine.class, HassiumObject[].class), this, 1));
            Attributes.put(HassiumObject.XOR_FUNCTION, new HassiumFunction(this.getClass().getDeclaredMethod("__xor__", VirtualMachine.class, HassiumObject[].class), this, 1));
            Attributes.put(HassiumObject.OR_FUNCTION, new HassiumFunction(this.getClass().getDeclaredMethod("__or__", VirtualMachine.class, HassiumObject[].class), this, 1));
            Attributes.put(HassiumObject.XAND_FUNCTION, new HassiumFunction(this.getClass().getDeclaredMethod("__xand__", VirtualMachine.class, HassiumObject[].class), this, 1));
            Attributes.put(HassiumObject.EQUALS_FUNCTION, new HassiumFunction(this.getClass().getDeclaredMethod("__equals__", VirtualMachine.class, HassiumObject[].class), this, 1));
            Attributes.put(HassiumObject.NOT_EQUAL_FUNCTION, new HassiumFunction(this.getClass().getDeclaredMethod("__notequals__", VirtualMachine.class, HassiumObject[].class), this, 1));
            Attributes.put(HassiumObject.TOSTRING_FUNCTION, new HassiumFunction(this.getClass().getDeclaredMethod("__tostring__", VirtualMachine.class, HassiumObject[].class), this, 0));
        } catch (NoSuchMethodException e) {
            HassiumLogger.error("Internal error HassiumChar : " + e.getMessage());
        }
        addType(HassiumChar.TypeDefinition);
    }

    public HassiumBool isDigit(VirtualMachine vm, HassiumObject[] args) {
        return new HassiumBool(Character.isDigit(value));
    }

    public HassiumBool isLetter(VirtualMachine vm, HassiumObject[] args) {
        return new HassiumBool(Character.isLetter(value));
    }

    public HassiumBool isLower(VirtualMachine vm, HassiumObject[] args) {
        return new HassiumBool(Character.isLowerCase(value));
    }

    public HassiumBool isLetterOrDigit(VirtualMachine vm, HassiumObject[] args) {
        return new HassiumBool(Character.isLetterOrDigit(value));
    }

    public HassiumBool isUpper(VirtualMachine vm, HassiumObject[] args) {
        return new HassiumBool(Character.isUpperCase(value));
    }

    public HassiumBool isWhitespace(VirtualMachine vm, HassiumObject[] args) {
        return new HassiumBool(Character.isWhitespace(value));
    }

    public HassiumBool toBool(VirtualMachine vm, HassiumObject[] args) {
        switch ((int) value) {
            case 0:
                return new HassiumBool(false);
            case 1:
                return new HassiumBool(true);
            default:
                HassiumLogger.error("Cannot convert char to boolean!");
        }
        return null; //Not reachable but for compilation
    }

    public HassiumChar toChar(VirtualMachine vm, HassiumObject[] args) {
        return this;
    }

    public HassiumDouble toDouble(VirtualMachine vm, HassiumObject[] args) {
        return new HassiumDouble(value);
    }

    public HassiumInt toInt(VirtualMachine vm, HassiumObject[] args) {
        return new HassiumInt(value);
    }

    public HassiumObject __add__(VirtualMachine vm, HassiumObject[] args) {
        HassiumObject obj = args[0];
        if (obj instanceof HassiumChar)
            return new HassiumChar((char) (this.getValue() + (char) ((HassiumChar) obj).getValue()));
        else if (obj instanceof HassiumInt)
            return new HassiumChar((char) (this.getValue() + ((HassiumInt) obj).getValue()));
        HassiumLogger.error("Cannot operate char on " + obj.getClass().getName());
        return null; //Not reachable but for compilation
    }

    public HassiumObject __sub__(VirtualMachine vm, HassiumObject[] args) {
        HassiumObject obj = args[0];
        if (obj instanceof HassiumChar)
            return new HassiumChar((char) (this.getValue() - (char) ((HassiumChar) obj).getValue()));
        else if (obj instanceof HassiumInt)
            return new HassiumChar((char) (this.getValue() - ((HassiumInt) obj).getValue()));
        HassiumLogger.error("Cannot operate char on " + obj.getClass().getName());
        return null; //Not reachable but for compilation
    }

    public HassiumObject __mul__(VirtualMachine vm, HassiumObject[] args) {
        HassiumObject obj = args[0];
        if (obj instanceof HassiumChar)
            return new HassiumChar((char) (this.getValue() * (char) ((HassiumChar) obj).getValue()));
        else if (obj instanceof HassiumInt)
            return new HassiumChar((char) (this.getValue() * ((HassiumInt) obj).getValue()));
        HassiumLogger.error("Cannot operate char on " + obj.getClass().getName());
        return null; //Not reachable but for compilation
    }

    public HassiumObject __div__(VirtualMachine vm, HassiumObject[] args) {
        HassiumObject obj = args[0];
        if (obj instanceof HassiumChar)
            return new HassiumChar((char) (this.getValue() / (char) ((HassiumChar) obj).getValue()));
        else if (obj instanceof HassiumInt)
            return new HassiumChar((char) (this.getValue() / ((HassiumInt) obj).getValue()));
        HassiumLogger.error("Cannot operate char on " + obj.getClass().getName());
        return null; //Not reachable but for compilation
    }

    public HassiumObject __mod__(VirtualMachine vm, HassiumObject[] args) {
        HassiumObject obj = args[0];
        if (obj instanceof HassiumChar)
            return new HassiumChar((char) (this.getValue() % (char) ((HassiumChar) obj).getValue()));
        else if (obj instanceof HassiumInt)
            return new HassiumChar((char) (this.getValue() % ((HassiumInt) obj).getValue()));
        HassiumLogger.error("Cannot operate char on " + obj.getClass().getName());
        return null; //Not reachable but for compilation
    }

    public HassiumObject __xor__(VirtualMachine vm, HassiumObject[] args) {
        HassiumObject obj = args[0];
        if (obj instanceof HassiumChar)
            return new HassiumChar((char) (this.getValue() ^ (char) ((HassiumChar) obj).getValue()));
        else if (obj instanceof HassiumInt)
            return new HassiumChar((char) (this.getValue() ^ ((HassiumInt) obj).getValue()));
        HassiumLogger.error("Cannot operate char on " + obj.getClass().getName());
        return null; //Not reachable but for compilation
    }

    public HassiumObject __or__(VirtualMachine vm, HassiumObject[] args) {
        HassiumObject obj = args[0];
        if (obj instanceof HassiumChar)
            return new HassiumChar((char) (this.getValue() | (char) ((HassiumChar) obj).getValue()));
        else if (obj instanceof HassiumInt)
            return new HassiumChar((char) (this.getValue() | ((HassiumInt) obj).getValue()));
        HassiumLogger.error("Cannot operate char on " + obj.getClass().getName());
        return null; //Not reachable but for compilation
    }

    public HassiumObject __xand__(VirtualMachine vm, HassiumObject[] args) {
        HassiumObject obj = args[0];
        if (obj instanceof HassiumChar)
            return new HassiumChar((char) (this.getValue() & (char) ((HassiumChar) obj).getValue()));
        else if (obj instanceof HassiumInt)
            return new HassiumChar((char) (this.getValue() & ((HassiumInt) obj).getValue()));
        HassiumLogger.error("Cannot operate char on " + obj.getClass().getName());
        return null; //Not reachable but for compilation
    }

    public HassiumObject __equals__(VirtualMachine vm, HassiumObject[] args) {
        HassiumObject obj = args[0];
        if (obj instanceof HassiumChar)
            return new HassiumBool(this.getValue() == ((HassiumChar) obj).getValue());
        else if (obj instanceof HassiumInt)
            return new HassiumBool(value == ((HassiumInt) obj).getValue());
        HassiumLogger.error("Cannot operate char on " + obj.getClass().getName());
        return null; //Not reachable but for compilation
    }

    public HassiumObject __notequals__(VirtualMachine vm, HassiumObject[] args) {
        HassiumObject obj = args[0];
        if (obj instanceof HassiumChar)
            return new HassiumBool((char) this.getValue() != ((HassiumChar) obj).getValue());
        else if (obj instanceof HassiumInt)
            return new HassiumBool((char) this.getValue() != ((HassiumInt) obj).getValue());
        HassiumLogger.error("Cannot operate char on " + obj.getClass().getName());
        return null; //Not reachable but for compilation
    }

    public HassiumObject __greater__(VirtualMachine vm, HassiumObject[] args) {
        HassiumObject obj = args[0];
        if (obj instanceof HassiumChar)
            return new HassiumBool(this.getValue() > ((HassiumChar) obj).getValue());
        else if (obj instanceof HassiumInt)
            return new HassiumBool(this.getValue() > ((HassiumInt) obj).getValue());
        HassiumLogger.error("Cannot operate char on " + obj.getClass().getName());
        return null; //Not reachable but for compilation
    }

    public HassiumObject __lesser__(VirtualMachine vm, HassiumObject[] args) {
        HassiumObject obj = args[0];
        if (obj instanceof HassiumChar)
            return new HassiumBool(this.getValue() < ((HassiumChar) obj).getValue());
        else if (obj instanceof HassiumInt)
            return new HassiumBool(this.getValue() < ((HassiumInt) obj).getValue());
        HassiumLogger.error("Cannot operate char on " + obj.getClass().getName());
        return null; //Not reachable but for compilation
    }

    public HassiumObject __greaterorequal__(VirtualMachine vm, HassiumObject[] args) {
        HassiumObject obj = args[0];
        if (obj instanceof HassiumChar)
            return new HassiumBool(this.getValue() >= ((HassiumChar) obj).getValue());
        else if (obj instanceof HassiumInt)
            return new HassiumBool(this.getValue() >= ((HassiumInt) obj).getValue());
        HassiumLogger.error("Cannot operate char on " + obj.getClass().getName());
        return null; //Not reachable but for compilation
    }

    public HassiumObject __lesserorequal__(VirtualMachine vm, HassiumObject[] args) {
        HassiumObject obj = args[0];
        if (obj instanceof HassiumChar)
            return new HassiumBool(this.getValue() <= ((HassiumChar) obj).getValue());
        else if (obj instanceof HassiumInt)
            return new HassiumBool(this.getValue() <= ((HassiumInt) obj).getValue());
        HassiumLogger.error("Cannot operate char on " + obj.getClass().getName());
        return null; //Not reachable but for compilation
    }

    public HassiumString __tostring__(VirtualMachine vm, HassiumObject[] args) {
        return new HassiumString(Character.toString(value));
    }

    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    public int hashCode() {
        return super.hashCode();
    }

    public String toString() {
        return String.valueOf(value);
    }
}
