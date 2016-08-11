package lang.jhassium.runtime.standardlibrary.types;

import lang.jhassium.runtime.HassiumTypeDefinition;
import lang.jhassium.runtime.VirtualMachine;
import lang.jhassium.runtime.standardlibrary.HassiumObject;
import lang.jhassium.utils.HassiumLogger;

import java.lang.reflect.Method;

/**
 * File : HassiumProperty.java
 * Description : None
 * Author : FRITZ Valentin
 * Website : https://github.com/vfrz/JHassium
 * Date : 11/08/2016 00:43
 */
public class HassiumProperty extends HassiumObject {

    public static HassiumTypeDefinition TypeDefinition = new HassiumTypeDefinition("property");

    private Method getValue;
    private Method setValue;

    private Object object;

    public Method getGetValue() {
        return getValue;
    }

    public Method getSetValue() {
        return setValue;
    }

    public Object getObject() {
        return object;
    }

    public HassiumProperty(Method getValue, Object object) {
        this(getValue, null, object);
    }

    public HassiumProperty(Method getValue, Method setValue, Object object) {
        this.getValue = getValue;
        this.setValue = setValue;
        this.object = object;
        addType(TypeDefinition);
    }

    public HassiumObject invoke(VirtualMachine vm, HassiumObject[] args) {
        try {
            return (HassiumObject) getValue.invoke(object, vm, args);
        } catch (Exception e) {
            HassiumLogger.error("Error while invoking HassiumProperty with target name : " + getValue.getName());
        }
        return null; //Not reachable but for compilation
    }

    public HassiumObject set(VirtualMachine vm, HassiumObject[] args) {
        try {
            return (HassiumObject) setValue.invoke(object, vm, args);
        } catch (Exception e) {
            HassiumLogger.error("Error while invoking HassiumProperty with target name : " + setValue.getName());
        }
        return null; //Not reachable but for compilation
    }
}
