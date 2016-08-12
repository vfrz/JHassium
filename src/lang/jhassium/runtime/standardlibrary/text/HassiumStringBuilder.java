package lang.jhassium.runtime.standardlibrary.text;

import lang.jhassium.runtime.HassiumTypeDefinition;
import lang.jhassium.runtime.VirtualMachine;
import lang.jhassium.runtime.standardlibrary.HassiumObject;
import lang.jhassium.runtime.standardlibrary.types.HassiumChar;
import lang.jhassium.runtime.standardlibrary.types.HassiumFunction;
import lang.jhassium.runtime.standardlibrary.types.HassiumInt;
import lang.jhassium.runtime.standardlibrary.types.HassiumString;
import lang.jhassium.utils.HassiumLogger;

/**
 * File : HassiumStringBuilder.java
 * Description : None
 * Authors : FRITZ Valentin, Jacob Misirian & zdimension
 * Website : https://github.com/vfrz/JHassium
 * Date : 12/08/2016 00:34
 */
public class HassiumStringBuilder extends HassiumObject {

    public static HassiumTypeDefinition TypeDefinition = new HassiumTypeDefinition("StringBuilder");

    public StringBuilder StringBuilder;

    public HassiumStringBuilder() {
        try {
            Attributes.put(HassiumObject.INVOKE_FUNCTION, new HassiumFunction(this.getClass().getDeclaredMethod("_new", VirtualMachine.class, HassiumObject[].class), this, new int[]{0, 1}));
        } catch (NoSuchMethodException e) {
            HassiumLogger.error("Internal error HassiumStringBuilder : " + e.getMessage());
        }

        addType(HassiumStringBuilder.TypeDefinition);
    }

    public HassiumStringBuilder _new(VirtualMachine vm, HassiumObject[] args) {
        HassiumStringBuilder hassiumStringBuilder = new HassiumStringBuilder();
        if (args.length == 0)
            hassiumStringBuilder.StringBuilder = new StringBuilder();
        else
            hassiumStringBuilder.StringBuilder = new StringBuilder(HassiumString.create(args[0]).getValue());

        try {
            hassiumStringBuilder.Attributes.put("append", new HassiumFunction(hassiumStringBuilder.getClass().getDeclaredMethod("append", VirtualMachine.class, HassiumObject[].class), hassiumStringBuilder, 1));
            hassiumStringBuilder.Attributes.put("appendLine", new HassiumFunction(hassiumStringBuilder.getClass().getDeclaredMethod("appendLine", VirtualMachine.class, HassiumObject[].class), hassiumStringBuilder, 1));
            hassiumStringBuilder.Attributes.put("insert", new HassiumFunction(hassiumStringBuilder.getClass().getDeclaredMethod("insert", VirtualMachine.class, HassiumObject[].class), hassiumStringBuilder, 2));
            hassiumStringBuilder.Attributes.put(HassiumObject.TOSTRING_FUNCTION, new HassiumFunction(hassiumStringBuilder.getClass().getDeclaredMethod("toString", VirtualMachine.class, HassiumObject[].class), hassiumStringBuilder, 0));
            hassiumStringBuilder.Attributes.put(HassiumObject.ADD_FUNCTION, new HassiumFunction(hassiumStringBuilder.getClass().getDeclaredMethod("__add__", VirtualMachine.class, HassiumObject[].class), hassiumStringBuilder, 1));
            hassiumStringBuilder.Attributes.put(HassiumObject.INDEX_FUNCTION, new HassiumFunction(hassiumStringBuilder.getClass().getDeclaredMethod("__index__", VirtualMachine.class, HassiumObject[].class), hassiumStringBuilder, 1));
            hassiumStringBuilder.Attributes.put(HassiumObject.STORE_INDEX_FUNCTION, new HassiumFunction(hassiumStringBuilder.getClass().getDeclaredMethod("__storeindex__", VirtualMachine.class, HassiumObject[].class), hassiumStringBuilder, 2));
        } catch (NoSuchMethodException e) {
            HassiumLogger.error("Internal error HassiumStringBuilder : " + e.getMessage());
        }

        return hassiumStringBuilder;
    }

    public HassiumStringBuilder append(VirtualMachine vm, HassiumObject[] args) {
        StringBuilder.append(args[0].toString(vm));
        return this;
    }

    public HassiumStringBuilder appendLine(VirtualMachine vm, HassiumObject[] args) {
        StringBuilder.append(args[0].toString(vm) + "\n");
        return this;
    }

    public HassiumStringBuilder insert(VirtualMachine vm, HassiumObject[] args) {
        StringBuilder.insert(HassiumInt.create(args[0]).getValue(), args[1].toString(vm));
        return this;
    }

    public HassiumString toString(VirtualMachine vm, HassiumObject[] args) {
        return new HassiumString(StringBuilder.toString());
    }

    public HassiumStringBuilder __add__(VirtualMachine vm, HassiumObject[] args) {
        return append(vm, args);
    }

    public HassiumChar __index__(VirtualMachine vm, HassiumObject[] args) {
        return new HassiumChar(StringBuilder.charAt(HassiumInt.create(args[0]).getValue()));
    }

    public HassiumStringBuilder __storeindex__(VirtualMachine vm, HassiumObject[] args) {
        return insert(vm, args);
    }

}
