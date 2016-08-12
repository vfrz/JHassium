package lang.jhassium.runtime.standardlibrary.math;

import lang.jhassium.runtime.VirtualMachine;
import lang.jhassium.runtime.standardlibrary.HassiumObject;
import lang.jhassium.runtime.standardlibrary.types.HassiumDouble;
import lang.jhassium.runtime.standardlibrary.types.HassiumFunction;
import lang.jhassium.runtime.standardlibrary.types.HassiumInt;
import lang.jhassium.runtime.standardlibrary.types.HassiumProperty;
import lang.jhassium.utils.HassiumLogger;

/**
 * File : HassiumMath.java
 * Description : None
 * Authors : FRITZ Valentin, Jacob Misirian & zdimension
 * Website : https://github.com/vfrz/JHassium
 * Date : 12/08/2016 00:51
 */
public class HassiumMath extends HassiumObject {

    public HassiumMath() {
        try {
            Attributes.put("abs", new HassiumFunction(this.getClass().getDeclaredMethod("abs", VirtualMachine.class, HassiumObject[].class), this, 1));
            Attributes.put("acos", new HassiumFunction(this.getClass().getDeclaredMethod("acos", VirtualMachine.class, HassiumObject[].class), this, 1));
            Attributes.put("asin", new HassiumFunction(this.getClass().getDeclaredMethod("asin", VirtualMachine.class, HassiumObject[].class), this, 1));
            Attributes.put("atan", new HassiumFunction(this.getClass().getDeclaredMethod("atan", VirtualMachine.class, HassiumObject[].class), this, 1));
            Attributes.put("ceil", new HassiumFunction(this.getClass().getDeclaredMethod("ceil", VirtualMachine.class, HassiumObject[].class), this, 1));
            Attributes.put("cos", new HassiumFunction(this.getClass().getDeclaredMethod("cos", VirtualMachine.class, HassiumObject[].class), this, 1));
            Attributes.put("e", new HassiumProperty(this.getClass().getDeclaredMethod("get_e", VirtualMachine.class, HassiumObject[].class), this));
            Attributes.put("floor", new HassiumFunction(this.getClass().getDeclaredMethod("floor", VirtualMachine.class, HassiumObject[].class), this, 1));
            Attributes.put("log", new HassiumFunction(this.getClass().getDeclaredMethod("log", VirtualMachine.class, HassiumObject[].class), this, 2));
            Attributes.put("log10", new HassiumFunction(this.getClass().getDeclaredMethod("log10", VirtualMachine.class, HassiumObject[].class), this, 2));
            Attributes.put("max", new HassiumFunction(this.getClass().getDeclaredMethod("max", VirtualMachine.class, HassiumObject[].class), this, 2));
            Attributes.put("min", new HassiumFunction(this.getClass().getDeclaredMethod("min", VirtualMachine.class, HassiumObject[].class), this, 2));
            Attributes.put("pi", new HassiumProperty(this.getClass().getDeclaredMethod("get_Pi", VirtualMachine.class, HassiumObject[].class), this));
            Attributes.put("pow", new HassiumFunction(this.getClass().getDeclaredMethod("pow", VirtualMachine.class, HassiumObject[].class), this, 2));
            Attributes.put("round", new HassiumFunction(this.getClass().getDeclaredMethod("round", VirtualMachine.class, HassiumObject[].class), this, 1));
            Attributes.put("sin", new HassiumFunction(this.getClass().getDeclaredMethod("sin", VirtualMachine.class, HassiumObject[].class), this, 1));
            Attributes.put("sqrt", new HassiumFunction(this.getClass().getDeclaredMethod("sqrt", VirtualMachine.class, HassiumObject[].class), this, 1));
            Attributes.put("tan", new HassiumFunction(this.getClass().getDeclaredMethod("tan", VirtualMachine.class, HassiumObject[].class), this, 1));
        } catch (NoSuchMethodException e) {
            HassiumLogger.error("Internal error HassiumMath : " + e.getMessage());
        }
    }

    private HassiumObject abs(VirtualMachine vm, HassiumObject[] args) {
        if (args[0] instanceof HassiumInt)
            return new HassiumDouble(Math.abs(HassiumInt.create(args[0]).getValue()));
        else if (args[0] instanceof HassiumDouble)
            return new HassiumDouble(Math.abs(HassiumDouble.create(args[0]).getValue()));
        return HassiumObject.Null;
    }

    private HassiumObject acos(VirtualMachine vm, HassiumObject[] args) {
        if (args[0] instanceof HassiumInt)
            return new HassiumDouble(Math.acos(HassiumInt.create(args[0]).getValue()));
        else if (args[0] instanceof HassiumDouble)
            return new HassiumDouble(Math.acos(HassiumDouble.create(args[0]).getValue()));
        return HassiumObject.Null;
    }

    private HassiumObject asin(VirtualMachine vm, HassiumObject[] args) {
        if (args[0] instanceof HassiumInt)
            return new HassiumDouble(Math.asin(HassiumInt.create(args[0]).getValue()));
        else if (args[0] instanceof HassiumDouble)
            return new HassiumDouble(Math.asin(HassiumDouble.create(args[0]).getValue()));
        return HassiumObject.Null;
    }

    private HassiumObject atan(VirtualMachine vm, HassiumObject[] args) {
        if (args[0] instanceof HassiumInt)
            return new HassiumDouble(Math.atan(HassiumInt.create(args[0]).getValue()));
        else if (args[0] instanceof HassiumDouble)
            return new HassiumDouble(Math.atan(HassiumDouble.create(args[0]).getValue()));
        return HassiumObject.Null;
    }

    private HassiumObject ceil(VirtualMachine vm, HassiumObject[] args) {
        return new HassiumDouble(Math.ceil(HassiumDouble.create(args[0]).getValue()));
    }

    private HassiumObject cos(VirtualMachine vm, HassiumObject[] args) {
        if (args[0] instanceof HassiumInt)
            return new HassiumDouble(Math.cos(HassiumInt.create(args[0]).getValue()));
        else if (args[0] instanceof HassiumDouble)
            return new HassiumDouble(Math.cos(HassiumDouble.create(args[0]).getValue()));
        return HassiumObject.Null;
    }

    private HassiumDouble get_e(VirtualMachine vm, HassiumObject[] args) {
        return new HassiumDouble(Math.E);
    }

    private HassiumDouble floor(VirtualMachine vm, HassiumObject[] args) {
        return new HassiumDouble(Math.floor(HassiumDouble.create(args[0]).getValue()));
    }

    private HassiumObject log(VirtualMachine vm, HassiumObject[] args) {
        if (args[0] instanceof HassiumInt)
            return new HassiumDouble(Math.log(HassiumInt.create(args[0]).getValue()) / Math.log(HassiumInt.create(args[1]).getValue()));
        else if (args[0] instanceof HassiumDouble)
            return new HassiumDouble(Math.log(HassiumDouble.create(args[0]).getValue()) / Math.log(HassiumDouble.create(args[1]).getValue()));
        return HassiumObject.Null;
    }

    private HassiumObject log10(VirtualMachine vm, HassiumObject[] args) {
        if (args[0] instanceof HassiumInt)
            return new HassiumDouble(Math.log10(HassiumInt.create(args[0]).getValue()));
        else if (args[0] instanceof HassiumDouble)
            return new HassiumDouble(Math.log10(HassiumDouble.create(args[0]).getValue()));
        return HassiumObject.Null;
    }

    private HassiumObject max(VirtualMachine vm, HassiumObject[] args) {
        if (args[0] instanceof HassiumInt)
            return new HassiumDouble(Math.max(HassiumInt.create(args[0]).getValue(), HassiumInt.create(args[1]).getValue()));
        else if (args[0] instanceof HassiumDouble)
            return new HassiumDouble(Math.max(HassiumDouble.create(args[0]).getValue(), HassiumDouble.create(args[1]).getValue()));
        return HassiumObject.Null;
    }

    private HassiumObject min(VirtualMachine vm, HassiumObject[] args) {
        if (args[0] instanceof HassiumInt)
            return new HassiumDouble(Math.min(HassiumInt.create(args[0]).getValue(), HassiumInt.create(args[1]).getValue()));
        else if (args[0] instanceof HassiumDouble)
            return new HassiumDouble(Math.min(HassiumDouble.create(args[0]).getValue(), HassiumDouble.create(args[1]).getValue()));
        return HassiumObject.Null;
    }

    private HassiumDouble get_Pi(VirtualMachine vm, HassiumObject[] args) {
        return new HassiumDouble(Math.PI);
    }

    private HassiumObject pow(VirtualMachine vm, HassiumObject[] args) {
        if (args[0] instanceof HassiumInt)
            return new HassiumDouble(Math.pow(HassiumInt.create(args[0]).getValue(), HassiumInt.create(args[1]).getValue()));
        else if (args[0] instanceof HassiumDouble)
            return new HassiumDouble(Math.pow(HassiumDouble.create(args[0]).getValue(), HassiumDouble.create(args[1]).getValue()));
        return HassiumObject.Null;
    }

    private HassiumObject round(VirtualMachine vm, HassiumObject[] args) {
        return new HassiumDouble(Math.round(HassiumDouble.create(args[0]).getValue()));
    }

    private HassiumObject sin(VirtualMachine vm, HassiumObject[] args) {
        if (args[0] instanceof HassiumInt)
            return new HassiumDouble(Math.sin(HassiumInt.create(args[0]).getValue()));
        else if (args[0] instanceof HassiumDouble)
            return new HassiumDouble(Math.sin(HassiumDouble.create(args[0]).getValue()));
        return HassiumObject.Null;
    }

    private HassiumObject sqrt(VirtualMachine vm, HassiumObject[] args) {
        if (args[0] instanceof HassiumInt)
            return new HassiumDouble(Math.sqrt(HassiumInt.create(args[0]).getValue()));
        else if (args[0] instanceof HassiumDouble)
            return new HassiumDouble(Math.sqrt(HassiumDouble.create(args[0]).getValue()));
        return HassiumObject.Null;
    }

    private HassiumObject tan(VirtualMachine vm, HassiumObject[] args) {
        if (args[0] instanceof HassiumInt)
            return new HassiumDouble(Math.tan(HassiumInt.create(args[0]).getValue()));
        else if (args[0] instanceof HassiumDouble)
            return new HassiumDouble(Math.tan(HassiumDouble.create(args[0]).getValue()));
        return HassiumObject.Null;
    }

}
