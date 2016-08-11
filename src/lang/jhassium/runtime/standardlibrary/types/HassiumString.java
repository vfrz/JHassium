package lang.jhassium.runtime.standardlibrary.types;

import lang.jhassium.runtime.HassiumTypeDefinition;
import lang.jhassium.runtime.VirtualMachine;
import lang.jhassium.runtime.standardlibrary.HassiumObject;
import lang.jhassium.utils.HassiumLogger;

import java.util.ArrayList;
import java.util.List;

/**
 * File : HassiumString.java
 * Description : None
 * Author : FRITZ Valentin
 * Website : https://github.com/vfrz/JHassium
 * Date : 10/08/2016 22:56
 */
public class HassiumString extends HassiumObject {

    public static HassiumTypeDefinition TypeDefinition = new HassiumTypeDefinition("string");

    public static HassiumString create(HassiumObject obj) {
        if (!(obj instanceof HassiumString))
            HassiumLogger.error(String.format("Cannot convert from %1s to string!", obj.type()));
        return (HassiumString) obj;
    }

    private String value;

    public String getValue() {
        return value;
    }

    public HassiumString(String value) {
        this.value = value;
        try {
            Attributes.put("contains", new HassiumFunction(this.getClass().getDeclaredMethod("contains", VirtualMachine.class, HassiumObject[].class), this, 1));
            Attributes.put("endsWith", new HassiumFunction(this.getClass().getDeclaredMethod("endsWith", VirtualMachine.class, HassiumObject[].class), this, 1));
            Attributes.put("getBytes", new HassiumFunction(this.getClass().getDeclaredMethod("getBytes", VirtualMachine.class, HassiumObject[].class), this, 0));
            Attributes.put("length", new HassiumProperty(this.getClass().getDeclaredMethod("get_Length", VirtualMachine.class, HassiumObject[].class), this));
            Attributes.put("replace", new HassiumFunction(this.getClass().getDeclaredMethod("replace", VirtualMachine.class, HassiumObject[].class), this, 2));
            Attributes.put("reverse", new HassiumFunction(this.getClass().getDeclaredMethod("reverse", VirtualMachine.class, HassiumObject[].class), this, 0));
            Attributes.put("split", new HassiumFunction(this.getClass().getDeclaredMethod("split", VirtualMachine.class, HassiumObject[].class), this, 1));
            Attributes.put("startsWith", new HassiumFunction(this.getClass().getDeclaredMethod("startsWith", VirtualMachine.class, HassiumObject[].class), this, 1));
            Attributes.put("stripChars", new HassiumFunction(this.getClass().getDeclaredMethod("stripChars", VirtualMachine.class, HassiumObject[].class), this, 1));
            Attributes.put("substring", new HassiumFunction(this.getClass().getDeclaredMethod("substring", VirtualMachine.class, HassiumObject[].class), this, new int[]{1, 2}));
            Attributes.put("toBool", new HassiumFunction(this.getClass().getDeclaredMethod("toBool", VirtualMachine.class, HassiumObject[].class), this, 0));
            Attributes.put("toChar", new HassiumFunction(this.getClass().getDeclaredMethod("toChar", VirtualMachine.class, HassiumObject[].class), this, 0));
            Attributes.put("toDouble", new HassiumFunction(this.getClass().getDeclaredMethod("toDouble", VirtualMachine.class, HassiumObject[].class), this, 0));
            Attributes.put("toInt", new HassiumFunction(this.getClass().getDeclaredMethod("toInt", VirtualMachine.class, HassiumObject[].class), this, 0));
            Attributes.put("toList", new HassiumFunction(this.getClass().getDeclaredMethod("toList", VirtualMachine.class, HassiumObject[].class), this, 0));
            Attributes.put("toLower", new HassiumFunction(this.getClass().getDeclaredMethod("toLower", VirtualMachine.class, HassiumObject[].class), this, 0));
            Attributes.put("toUpper", new HassiumFunction(this.getClass().getDeclaredMethod("toUpper", VirtualMachine.class, HassiumObject[].class), this, 0));
            Attributes.put("trim", new HassiumFunction(this.getClass().getDeclaredMethod("trim", VirtualMachine.class, HassiumObject[].class), this, 0));
            Attributes.put("trimLeft", new HassiumFunction(this.getClass().getDeclaredMethod("trimLeft", VirtualMachine.class, HassiumObject[].class), this, 0));
            Attributes.put("trimRight", new HassiumFunction(this.getClass().getDeclaredMethod("trimRight", VirtualMachine.class, HassiumObject[].class), this, 0));
            Attributes.put(HassiumObject.CONTAINS, new HassiumFunction(this.getClass().getDeclaredMethod("contains", VirtualMachine.class, HassiumObject[].class), this, 1));
            Attributes.put(HassiumObject.TOSTRING_FUNCTION, new HassiumFunction(this.getClass().getDeclaredMethod("__tostring__", VirtualMachine.class, HassiumObject[].class), this, 0));
            Attributes.put(HassiumObject.ADD_FUNCTION, new HassiumFunction(this.getClass().getDeclaredMethod("__add__", VirtualMachine.class, HassiumObject[].class), this, 1));
            Attributes.put(HassiumObject.MUL_FUNCTION, new HassiumFunction(this.getClass().getDeclaredMethod("__mul__", VirtualMachine.class, HassiumObject[].class), this, 1));
            Attributes.put(HassiumObject.EQUALS_FUNCTION, new HassiumFunction(this.getClass().getDeclaredMethod("__equals__", VirtualMachine.class, HassiumObject[].class), this, 1));
            Attributes.put(HassiumObject.NOT_EQUAL_FUNCTION, new HassiumFunction(this.getClass().getDeclaredMethod("__notequal__", VirtualMachine.class, HassiumObject[].class), this, 1));
            Attributes.put(HassiumObject.INDEX_FUNCTION, new HassiumFunction(this.getClass().getDeclaredMethod("__index__", VirtualMachine.class, HassiumObject[].class), this, 1));
            Attributes.put(HassiumObject.ITER_FUNCTION, new HassiumFunction(this.getClass().getDeclaredMethod("__iter__", VirtualMachine.class, HassiumObject[].class), this, 0));
        } catch (NoSuchMethodException e) {
            HassiumLogger.error("Internal error HassiumString : " + e.getMessage());
        }
        addType(HassiumString.TypeDefinition);
    }

    public HassiumBool contains(VirtualMachine vm, HassiumObject[] args) {
        return new HassiumBool(value.contains(HassiumString.create(args[0]).getValue()));
    }

    public HassiumBool endsWith(VirtualMachine vm, HassiumObject[] args) {
        return new HassiumBool(value.endsWith(HassiumString.create(args[0]).getValue()));
    }

    public HassiumList getBytes(VirtualMachine vm, HassiumObject[] args) {
        byte[] bytes = value.getBytes();
        HassiumChar[] elements = new HassiumChar[bytes.length];
        for (int i = 0; i < elements.length; i++)
            elements[i] = new HassiumChar((char) bytes[i]);
        return new HassiumList(elements);
    }

    public HassiumInt get_Length(VirtualMachine vm, HassiumObject[] args) {
        return new HassiumInt(value.length());
    }

    public HassiumString replace(VirtualMachine vm, HassiumObject[] args) {
        return new HassiumString(value.replace(HassiumString.create(args[0]).getValue(), HassiumString.create(args[1]).getValue()));
    }

    public HassiumString reverse(VirtualMachine vm, HassiumObject[] args) {
        StringBuilder sb = new StringBuilder();
        for (int i = value.length() - 1; i >= 0; i--)
            sb.append(value.charAt(i));
        return new HassiumString(sb.toString());
    }

    public HassiumList split(VirtualMachine vm, HassiumObject[] args) {
        HassiumChar c = HassiumChar.create(args[0]);
        String[] strings = value.split(c.getValue().toString());
        HassiumObject[] elements = new HassiumObject[strings.length];
        for (int i = 0; i < elements.length; i++)
            elements[i] = new HassiumString(strings[i]);
        return new HassiumList(elements);
    }

    public HassiumBool startsWith(VirtualMachine vm, HassiumObject[] args) {
        return new HassiumBool(value.startsWith(HassiumString.create(args[0]).getValue()));
    }

    public HassiumString stripChars(VirtualMachine vm, HassiumObject[] args) {
        StringBuilder sb = new StringBuilder();
        if (args[0] instanceof HassiumList) {
            HassiumList list = (HassiumList) args[0];
            List<Character> chars = new ArrayList<>();
            for (int i = 0; i < list.getValue().size(); i++)
                chars.add(list.getValue().get(i).toString().charAt(0));
            for (char c : value.toCharArray()) {
                if (!chars.contains(c))
                    sb.append(c);
            }
        } else {
            char ch = (char) HassiumChar.create(args[0]).getValue();
            for (char c : value.toCharArray()) {
                if (c != ch)
                    sb.append(c);
            }
        }
        return new HassiumString(sb.toString());
    }

    public HassiumString substring(VirtualMachine vm, HassiumObject[] args) {
        switch (args.length) {
            case 1:
                return new HassiumString(value.substring(HassiumDouble.create(args[0]).getValueInt()));
            case 2:
                return new HassiumString(value.substring(HassiumDouble.create(args[0]).getValueInt(), HassiumDouble.create(args[1]).getValueInt()));
        }
        return null;
    }

    public HassiumBool toBool(VirtualMachine vm, HassiumObject[] args) {
        switch (value.toLowerCase()) {
            case "false":
                return new HassiumBool(false);
            case "true":
                return new HassiumBool(true);
            default:
                HassiumLogger.error("Cannot convert string to bool : " + value.toLowerCase());
        }
        return null; // Not reachable but for compilation
    }

    public HassiumChar toChar(VirtualMachine vm, HassiumObject[] args) {
        return new HassiumChar(value.charAt(0));
    }

    public HassiumDouble toDouble(VirtualMachine vm, HassiumObject[] args) {
        return new HassiumDouble(Double.parseDouble(value));
    }

    public HassiumInt toInt(VirtualMachine vm, HassiumObject[] args) {
        return new HassiumInt(Long.parseLong(value));
    }

    public HassiumList toList(VirtualMachine vm, HassiumObject[] args) {
        HassiumObject[] items = new HassiumObject[value.length()];
        for (int i = 0; i < items.length; i++)
            items[i] = new HassiumChar(value.charAt(i));
        return new HassiumList(items);
    }

    public HassiumString toLower(VirtualMachine vm, HassiumObject[] args) {
        return new HassiumString(value.toLowerCase());
    }

    public HassiumString toUpper(VirtualMachine vm, HassiumObject[] args) {
        return new HassiumString(value.toUpperCase());
    }

    public HassiumString trim(VirtualMachine vm, HassiumObject[] args) {
        return trimRight(vm, new HassiumObject[]{trimLeft(vm, args)});
    }

    public HassiumString trimLeft(VirtualMachine vm, HassiumObject[] args) {
        return new HassiumString(value.replaceAll("^\\s+", ""));
    }

    public HassiumString trimRight(VirtualMachine vm, HassiumObject[] args) {
        return new HassiumString(value.replaceAll("\\s+$", ""));
    }

    public HassiumString __tostring__(VirtualMachine vm, HassiumObject[] args) {
        return new HassiumString(value);
    }

    public HassiumObject __add__(VirtualMachine vm, HassiumObject[] args) {
        return new HassiumString(value + new HassiumString(args[0].toString(vm)).getValue());
    }

    public HassiumObject __mul__(VirtualMachine vm, HassiumObject[] args) {
        StringBuilder sb = new StringBuilder();
        HassiumInt right = (HassiumInt) args[0];
        for (int i = 0; i < right.getValue(); i++)
            sb.append(value);
        return new HassiumString(sb.toString());
    }

    public HassiumObject __equals__(VirtualMachine vm, HassiumObject[] args) {
        HassiumObject obj = args[0];
        if (obj instanceof HassiumString)
            return new HassiumBool(value.equals(obj.getValue()));
        HassiumLogger.error("Cannot compare string to " + obj);
        return null; //Not reachable but for compilation
    }

    public HassiumObject __notequal__(VirtualMachine vm, HassiumObject[] args) {
        HassiumObject obj = args[0];
        if (obj instanceof HassiumString)
            return new HassiumBool(!value.equals(obj.getValue()));
        HassiumLogger.error("Cannot compare string to " + obj);
        return null; //Not reachable but for compilation
    }

    public HassiumObject __index__(VirtualMachine vm, HassiumObject[] args) {
        HassiumObject obj = args[0];
        if (obj instanceof HassiumDouble)
            return new HassiumChar(value.charAt(((HassiumDouble) obj).getValueInt()));
        else if (obj instanceof HassiumInt)
            return new HassiumChar(value.charAt(((HassiumInt) obj).getValue().intValue()));
        HassiumLogger.error("Cannot index string with " + obj);
        return null; //Not reachable but for compilation
    }

    public HassiumObject __iter__(VirtualMachine vm, HassiumObject[] args) {
        return toList(vm, args);
    }

    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    public int hashCode() {
        return super.hashCode();
    }

    public String toString() {
        return value;
    }
}
