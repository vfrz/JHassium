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
            Attributes.put("contains", new HassiumFunction(this.getClass().getDeclaredMethod("contains"), this, 1));
            Attributes.put("endsWith", new HassiumFunction(this.getClass().getDeclaredMethod("endsWith"), this, 1));
            Attributes.put("getBytes", new HassiumFunction(this.getClass().getDeclaredMethod("getBytes"), this, 0));
            Attributes.put("length", new HassiumProperty(this.getClass().getDeclaredMethod("get_Length"), this));
            Attributes.put("replace", new HassiumFunction(this.getClass().getDeclaredMethod("replace"), this, 2));
            Attributes.put("reverse", new HassiumFunction(this.getClass().getDeclaredMethod("reverse"), this, 0));
            Attributes.put("split", new HassiumFunction(this.getClass().getDeclaredMethod("split"), this, 1));
            Attributes.put("startsWith", new HassiumFunction(this.getClass().getDeclaredMethod("starsWith"), this, 1));
            Attributes.put("stripChars", new HassiumFunction(this.getClass().getDeclaredMethod("stripChars"), this, 1));
            Attributes.put("substring", new HassiumFunction(this.getClass().getDeclaredMethod("substring"), this, new int[]{1, 2}));
            Attributes.put("toBool", new HassiumFunction(this.getClass().getDeclaredMethod("toBool"), this, 0));
            Attributes.put("toChar", new HassiumFunction(this.getClass().getDeclaredMethod("toChar"), this, 0));
            Attributes.put("toDouble", new HassiumFunction(this.getClass().getDeclaredMethod("toDouble"), this, 0));
            Attributes.put("toInt", new HassiumFunction(this.getClass().getDeclaredMethod("toInt"), this, 0));
            Attributes.put("toList", new HassiumFunction(this.getClass().getDeclaredMethod("toList"), this, 0));
            Attributes.put("toLower", new HassiumFunction(this.getClass().getDeclaredMethod("toLower"), this, 0));
            Attributes.put("toUpper", new HassiumFunction(this.getClass().getDeclaredMethod("toUpper"), this, 0));
            Attributes.put("trim", new HassiumFunction(this.getClass().getDeclaredMethod("trim"), this, 0));
            Attributes.put("trimLeft", new HassiumFunction(this.getClass().getDeclaredMethod("trimLeft"), this, 0));
            Attributes.put("trimRight", new HassiumFunction(this.getClass().getDeclaredMethod("trimRight"), this, 0));
            Attributes.put(HassiumObject.CONTAINS, new HassiumFunction(this.getClass().getDeclaredMethod("contains"), this, 1));
            Attributes.put(HassiumObject.TOSTRING_FUNCTION, new HassiumFunction(this.getClass().getDeclaredMethod("__tostring__"), this, 0));
            Attributes.put(HassiumObject.ADD_FUNCTION, new HassiumFunction(this.getClass().getDeclaredMethod("__add__"), this, 1));
            Attributes.put(HassiumObject.MUL_FUNCTION, new HassiumFunction(this.getClass().getDeclaredMethod("__mul__"), this, 1));
            Attributes.put(HassiumObject.EQUALS_FUNCTION, new HassiumFunction(this.getClass().getDeclaredMethod("__equals__"), this, 1));
            Attributes.put(HassiumObject.NOT_EQUAL_FUNCTION, new HassiumFunction(this.getClass().getDeclaredMethod("__notequal__"), this, 1));
            Attributes.put(HassiumObject.INDEX_FUNCTION, new HassiumFunction(this.getClass().getDeclaredMethod("__index__"), this, 1));
            Attributes.put(HassiumObject.ITER_FUNCTION, new HassiumFunction(this.getClass().getDeclaredMethod("__iter__"), this, 0));
        } catch (NoSuchMethodException e) {
            HassiumLogger.error("Internal error HassiumString : " + e.getMessage());
        }
        addType(HassiumString.TypeDefinition);
    }

    private HassiumBool contains(VirtualMachine vm, HassiumObject[] args) {
        return new HassiumBool(value.contains(HassiumString.create(args[0]).getValue()));
    }

    private HassiumBool endsWith(VirtualMachine vm, HassiumObject[] args) {
        return new HassiumBool(value.endsWith(HassiumString.create(args[0]).getValue()));
    }

    private HassiumList getBytes(VirtualMachine vm, HassiumObject[] args) {
        byte[] bytes = value.getBytes();
        HassiumChar[] elements = new HassiumChar[bytes.length];
        for (int i = 0; i < elements.length; i++)
            elements[i] = new HassiumChar((char) bytes[i]);
        return new HassiumList(elements);
    }

    private HassiumInt get_Length(VirtualMachine vm, HassiumObject[] args) {
        return new HassiumInt(value.length());
    }

    private HassiumString replace(VirtualMachine vm, HassiumObject[] args) {
        return new HassiumString(value.replace(HassiumString.create(args[0]).getValue(), HassiumString.create(args[1]).getValue()));
    }

    private HassiumString reverse(VirtualMachine vm, HassiumObject[] args) {
        StringBuilder sb = new StringBuilder();
        for (int i = value.length() - 1; i >= 0; i--)
            sb.append(value.charAt(i));
        return new HassiumString(sb.toString());
    }

    private HassiumList split(VirtualMachine vm, HassiumObject[] args) {
        HassiumChar c = HassiumChar.create(args[0]);
        String[] strings = value.split(c.getValue().toString());
        HassiumObject[] elements = new HassiumObject[strings.length];
        for (int i = 0; i < elements.length; i++)
            elements[i] = new HassiumString(strings[i]);
        return new HassiumList(elements);
    }

    private HassiumBool startsWith(VirtualMachine vm, HassiumObject[] args) {
        return new HassiumBool(value.startsWith(HassiumString.create(args[0]).getValue()));
    }

    private HassiumString stripChars(VirtualMachine vm, HassiumObject[] args) {
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

    private HassiumString substring(VirtualMachine vm, HassiumObject[] args) {
        switch (args.length) {
            case 1:
                return new HassiumString(value.substring(HassiumDouble.create(args[0]).getValueInt()));
            case 2:
                return new HassiumString(value.substring(HassiumDouble.create(args[0]).getValueInt(), HassiumDouble.create(args[1]).getValueInt()));
        }
        return null;
    }

    private HassiumBool toBool(VirtualMachine vm, HassiumObject[] args) {
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

    private HassiumChar toChar(VirtualMachine vm, HassiumObject[] args) {
        return new HassiumChar(value.charAt(0));
    }

    private HassiumDouble toDouble(VirtualMachine vm, HassiumObject[] args) {
        return new HassiumDouble(Double.parseDouble(value));
    }

    private HassiumInt toInt(VirtualMachine vm, HassiumObject[] args) {
        return new HassiumInt(Long.parseLong(value));
    }

    private HassiumList toList(VirtualMachine vm, HassiumObject[] args) {
        HassiumObject[] items = new HassiumObject[value.length()];
        for (int i = 0; i < items.length; i++)
            items[i] = new HassiumChar(value.charAt(i));
        return new HassiumList(items);
    }

    private HassiumString toLower(VirtualMachine vm, HassiumObject[] args) {
        return new HassiumString(value.toLowerCase());
    }

    private HassiumString toUpper(VirtualMachine vm, HassiumObject[] args) {
        return new HassiumString(value.toUpperCase());
    }

    private HassiumString trim(VirtualMachine vm, HassiumObject[] args) {
        return trimRight(vm, new HassiumObject[]{trimLeft(vm, args)});
    }

    private HassiumString trimLeft(VirtualMachine vm, HassiumObject[] args) {
        return new HassiumString(value.replaceAll("^\\s+", ""));
    }

    private HassiumString trimRight(VirtualMachine vm, HassiumObject[] args) {
        return new HassiumString(value.replaceAll("\\s+$", ""));
    }

    private HassiumString __tostring__(VirtualMachine vm, HassiumObject[] args) {
        return this;
    }

    private HassiumObject __add__(VirtualMachine vm, HassiumObject[] args) {
        return new HassiumString(value + new HassiumString(args[0].toString(vm)).getValue());
    }

    private HassiumObject __mul__(VirtualMachine vm, HassiumObject[] args) {
        StringBuilder sb = new StringBuilder();
        HassiumInt right = (HassiumInt) args[0];
        for (int i = 0; i < right.getValue(); i++)
            sb.append(value);
        return new HassiumString(sb.toString());
    }

    private HassiumObject __equals__(VirtualMachine vm, HassiumObject[] args) {
        HassiumObject obj = args[0];
        if (obj instanceof HassiumString)
            return new HassiumBool(value.equals((HassiumString) obj.getValue()));
        HassiumLogger.error("Cannot compare string to " + obj);
        return null; //Not reachable but for compilation
    }

    private HassiumObject __notequal__(VirtualMachine vm, HassiumObject[] args) {
        HassiumObject obj = args[0];
        if (obj instanceof HassiumString)
            return new HassiumBool(!value.equals((HassiumString) obj.getValue()));
        HassiumLogger.error("Cannot compare string to " + obj);
        return null; //Not reachable but for compilation
    }

    private HassiumObject __index__(VirtualMachine vm, HassiumObject[] args) {
        HassiumObject obj = args[0];
        if (obj instanceof HassiumDouble)
            return new HassiumChar(value.charAt(((HassiumDouble) obj).getValueInt()));
        else if (obj instanceof HassiumInt)
            return new HassiumChar(value.charAt(((HassiumInt) obj).getValue().intValue()));
        HassiumLogger.error("Cannot index string with " + obj);
        return null; //Not reachable but for compilation
    }

    private HassiumObject __iter__(VirtualMachine vm, HassiumObject[] args) {
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
