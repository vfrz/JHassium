package lang.jhassium.runtime;

import lang.jhassium.runtime.standardlibrary.HassiumObject;
import lang.jhassium.runtime.standardlibrary.types.HassiumFunction;
import lang.jhassium.runtime.standardlibrary.types.HassiumString;
import lang.jhassium.utils.HassiumLogger;

/**
 * File : HassiumTypeDefinition.java
 * Description : None
 * Authors : FRITZ Valentin, Jacob Misirian & zdimension
 * Website : https://github.com/vfrz/JHassium
 * Date : 10/08/2016 22:43
 */
public class HassiumTypeDefinition extends HassiumObject {

    public static HassiumTypeDefinition TypeDefinition = new HassiumTypeDefinition("TypeDefinition");

    public static HassiumTypeDefinition create(HassiumObject obj) {
        if (!(obj instanceof HassiumTypeDefinition))
            HassiumLogger.error(String.format("Cannot convert from %1s to TypeDefinition!", obj.type()));
        return (HassiumTypeDefinition) obj;
    }

    private String typeString;

    public HassiumTypeDefinition(String type) {
        typeString = type;
        try {
            Attributes.put(HassiumObject.TOSTRING_FUNCTION, new HassiumFunction(this.getClass().getDeclaredMethod("toString", VirtualMachine.class, HassiumObject[].class), this, 0));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        addType(HassiumTypeDefinition.TypeDefinition);
    }

    public String getTypeString() {
        return typeString;
    }

    public HassiumTypeDefinition type() {
        return this;
    }

    public HassiumString toString(VirtualMachine vm, HassiumObject[] args) {
        return new HassiumString(typeString);
    }

}
