package lang.jhassium.runtime.standardlibrary.types;

import lang.jhassium.runtime.HassiumTypeDefinition;
import lang.jhassium.runtime.VirtualMachine;
import lang.jhassium.runtime.standardlibrary.HassiumObject;
import lang.jhassium.utils.HassiumLogger;

/**
 * File : HassiumBool.java
 * Description : None
 * Author : FRITZ Valentin
 * Website : https://github.com/vfrz/JHassium
 * Date : 10/08/2016 23:41
 */
public class HassiumBool extends HassiumObject {

    public static HassiumTypeDefinition TypeDefinition = new HassiumTypeDefinition("bool");

    public static HassiumBool create(HassiumObject obj) {
        if (!(obj instanceof HassiumBool))
            HassiumLogger.error(String.format("Cannot convert from %1s to bool!", obj.type()));
        return (HassiumBool) obj;
    }

    private boolean value;

    public Boolean getValue() {
        return value;
    }

    public HassiumBool(boolean value) {
        this.value = value;
        try {
            Attributes.put("toBool", new HassiumFunction(this.getClass().getDeclaredMethod("toBool"), this, 0));
            Attributes.put("toChar", new HassiumFunction(this.getClass().getDeclaredMethod("toChar"), this, 0));
            Attributes.put("toDouble", new HassiumFunction(this.getClass().getDeclaredMethod("toDouble"), this, 0));
            Attributes.put("toInt", new HassiumFunction(this.getClass().getDeclaredMethod("toInt"), this, 0));
            Attributes.put(HassiumObject.EQUALS_FUNCTION, new HassiumFunction(this.getClass().getDeclaredMethod("__equals__"), this, 1));
            Attributes.put(HassiumObject.NOT_EQUAL_FUNCTION, new HassiumFunction(this.getClass().getDeclaredMethod("__notequals__"), this, 1));
            Attributes.put(HassiumObject.NOT, new HassiumFunction(this.getClass().getDeclaredMethod("__not__"), this, 0));
            Attributes.put(HassiumObject.TOSTRING_FUNCTION, new HassiumFunction(this.getClass().getDeclaredMethod("__tostring__"), this, 0));
        } catch (NoSuchMethodException e) {
            HassiumLogger.error("Internal error HassiumBool : " + e.getMessage());
        }
        addType(HassiumBool.TypeDefinition);
    }

    private HassiumBool toBool(VirtualMachine vm, HassiumObject[] args) {
        return this;
    }

    private HassiumChar toChar(VirtualMachine vm, HassiumObject[] args) {
        return new HassiumChar((char) (value ? 1 : 0));
    }

    private HassiumDouble toDouble(VirtualMachine vm, HassiumObject[] args) {
        return new HassiumDouble(value ? 1 : 0);
    }

    private HassiumInt toInt(VirtualMachine vm, HassiumObject[] args) {
        return new HassiumInt(value ? 1 : 0);
    }

    private HassiumObject __equals__(VirtualMachine vm, HassiumObject[] args) {
        return new HassiumBool(value == (Boolean) ((HassiumBool) args[0]).getValue());
    }

    private HassiumObject __notequals__(VirtualMachine vm, HassiumObject[] args) {
        return new HassiumBool(value != (Boolean) ((HassiumBool) args[0]).getValue());
    }

    private HassiumBool __not__(VirtualMachine vm, HassiumObject[] args) {
        return new HassiumBool(!value);
    }

    private HassiumString __tostring__(VirtualMachine vm, HassiumObject[] args) {
        return new HassiumString(Boolean.toString(value));
    }

    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    public int hashCode() {
        return super.hashCode();
    }

    public String toString() {
        return Boolean.toString(value);
    }
}
