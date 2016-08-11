package lang.jhassium.runtime.standardlibrary.types;

import lang.jhassium.runtime.HassiumTypeDefinition;
import lang.jhassium.runtime.VirtualMachine;
import lang.jhassium.runtime.standardlibrary.HassiumObject;
import lang.jhassium.utils.HassiumLogger;

/**
 * File : HassiumKeyValuePair.java
 * Description : None
 * Author : FRITZ Valentin
 * Website : https://github.com/vfrz/JHassium
 * Date : 11/08/2016 14:23
 */
public class HassiumKeyValuePair extends HassiumObject {

    public static HassiumTypeDefinition TypeDefinition = new HassiumTypeDefinition("KeyValuePair");

    public static HassiumKeyValuePair create(HassiumObject obj) {
        if (!(obj instanceof HassiumKeyValuePair))
            HassiumLogger.error(String.format("Cannot convert from %1s to KeyValuePair", obj.type()));
        return (HassiumKeyValuePair) obj;
    }

    public HassiumObject Key;

    private HassiumObject value;

    public HassiumObject getValue() {
        return value;
    }

    public HassiumKeyValuePair(HassiumObject key, HassiumObject value) {
        Key = key;
        this.value = value;
        try {
            Attributes.put("key", new HassiumProperty(this.getClass().getDeclaredMethod("get_Key"), this.getClass().getDeclaredMethod("set_Key"), this));
            Attributes.put("value", new HassiumProperty(this.getClass().getDeclaredMethod("get_Value"), this.getClass().getDeclaredMethod("set_Value"), this));
            Attributes.put(HassiumObject.TOSTRING_FUNCTION, new HassiumFunction(this.getClass().getDeclaredMethod("toString"), this, 0));
        } catch (NoSuchMethodException e) {
            HassiumLogger.error("Internal error HassiumBool : " + e.getMessage());
        }
        addType(HassiumKeyValuePair.TypeDefinition);
    }

    private HassiumObject get_Key(VirtualMachine vm, HassiumObject[] args) {
        return Key;
    }

    private HassiumNull set_Key(VirtualMachine vm, HassiumObject[] args) {
        Key = args[0];
        return HassiumObject.Null;
    }

    private HassiumObject get_Value(VirtualMachine vm, HassiumObject[] args) {
        return value;
    }

    private HassiumNull set_Value(VirtualMachine vm, HassiumObject[] args) {
        value = args[0];
        return HassiumObject.Null;
    }

    private HassiumObject value(VirtualMachine vm, HassiumObject[] args) {
        return value;
    }

    private HassiumString toString(VirtualMachine vm, HassiumObject[] args) {
        return new HassiumString(Key.toString(vm) + " : " + value.toString(vm));
    }

}
