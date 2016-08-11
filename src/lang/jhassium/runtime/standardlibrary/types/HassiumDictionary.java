package lang.jhassium.runtime.standardlibrary.types;

import lang.jhassium.runtime.HassiumTypeDefinition;
import lang.jhassium.runtime.VirtualMachine;
import lang.jhassium.runtime.standardlibrary.HassiumObject;
import lang.jhassium.utils.HassiumLogger;
import lang.jhassium.utils.Helpers;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * File : HassiumDictionary.java
 * Description : None
 * Author : FRITZ Valentin
 * Website : https://github.com/vfrz/JHassium
 * Date : 11/08/2016 14:27
 */
public class HassiumDictionary extends HassiumObject {

    public static HassiumTypeDefinition TypeDefinition = new HassiumTypeDefinition("dictionary");

    private LinkedHashMap<HassiumObject, HassiumObject> value = new LinkedHashMap<>();

    public LinkedHashMap<HassiumObject, HassiumObject> getValue() {
        return value;
    }

    public HassiumDictionary(HassiumKeyValuePair[] pairs) {
        for (HassiumKeyValuePair pair : pairs)
            value.put(pair.Key, pair.getValue());

        try {
            Attributes.put("containsKey", new HassiumFunction(this.getClass().getDeclaredMethod("containsKey", VirtualMachine.class, HassiumObject[].class), this, 1));
            Attributes.put("containsValue", new HassiumFunction(this.getClass().getDeclaredMethod("containsValue", VirtualMachine.class, HassiumObject[].class), this, 1));
            Attributes.put("length", new HassiumProperty(this.getClass().getDeclaredMethod("get_Length", VirtualMachine.class, HassiumObject[].class), this));
            Attributes.put(HassiumObject.ADD_FUNCTION, new HassiumFunction(this.getClass().getDeclaredMethod("__add__", VirtualMachine.class, HassiumObject[].class), this, 1));
            Attributes.put(HassiumObject.INDEX_FUNCTION, new HassiumFunction(this.getClass().getDeclaredMethod("__index__", VirtualMachine.class, HassiumObject[].class), this, 1));
            Attributes.put(HassiumObject.STORE_INDEX_FUNCTION, new HassiumFunction(this.getClass().getDeclaredMethod("__storeindex__", VirtualMachine.class, HassiumObject[].class), this, 2));
        } catch (NoSuchMethodException e) {
            HassiumLogger.error("Internal error HassiumBool : " + e.getMessage());
        }
        addType(HassiumDictionary.TypeDefinition);
    }

    public HassiumBool containsKey(VirtualMachine vm, HassiumObject[] args) {
        for (Map.Entry<HassiumObject, HassiumObject> pair : value.entrySet()) {
            if ((pair.getKey()).equals(vm, args[0]).getValue())
                return new HassiumBool(true);
        }
        return new HassiumBool(false);
    }

    public HassiumBool containsValue(VirtualMachine vm, HassiumObject[] args) {
        for (Map.Entry<HassiumObject, HassiumObject> pair : value.entrySet()) {
            if ((pair.getValue()).equals(vm, args[0]).getValue())
                return new HassiumBool(true);
        }
        return new HassiumBool(false);
    }

    public HassiumInt get_Length(VirtualMachine vm, HassiumObject[] args) {
        return new HassiumInt(value.size());
    }

    public HassiumDictionary __add__(VirtualMachine vm, HassiumObject[] args) {
        HassiumDictionary dict = Helpers.as(this.clone(), HassiumDictionary.class);
        HassiumKeyValuePair pair = HassiumKeyValuePair.create(args[0]);
        dict.getValue().put(pair.Key, pair.getValue());

        return dict;
    }

    public HassiumList __iter__(VirtualMachine vm, HassiumObject[] args) {
        HassiumList list = new HassiumList(new HassiumObject[0]);

        for (Map.Entry<HassiumObject, HassiumObject> pair : value.entrySet())
            list.getValue().add(new HassiumKeyValuePair(pair.getKey(), pair.getValue()));
        return list;
    }

    public HassiumObject __index__(VirtualMachine vm, HassiumObject[] args) {
        for (Map.Entry<HassiumObject, HassiumObject> pair : value.entrySet()) {
            if (pair.getKey().equals(vm, args[0]).getValue())
                return (HassiumObject) pair.getValue();
        }
        HassiumLogger.error("Key not found!");
        return null; //Not reachable but for compilation
    }

    public HassiumObject __storeindex__(VirtualMachine vm, HassiumObject[] args) {
        for (HassiumObject key : value.keySet()) {
            if (key.equals(vm, args[0]).getValue()) {
                value.put(key, args[1]);
                return args[1];
            }
        }
        value.put(args[0], args[1]);
        return args[1];
    }
}
