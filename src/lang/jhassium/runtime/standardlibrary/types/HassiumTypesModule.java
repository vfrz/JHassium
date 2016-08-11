package lang.jhassium.runtime.standardlibrary.types;

import lang.jhassium.runtime.standardlibrary.HassiumObject;
import lang.jhassium.runtime.standardlibrary.InternalModule;

/**
 * File : HassiumTypesModule.java
 * Description : None
 * Author : FRITZ Valentin
 * Website : https://github.com/vfrz/JHassium
 * Date : 11/08/2016 17:04
 */
public class HassiumTypesModule extends InternalModule {

    public static HassiumTypesModule Instance = new HassiumTypesModule();

    public HassiumTypesModule() {
        super("types");
        Attributes.put("bool", HassiumBool.TypeDefinition);
        Attributes.put("char", HassiumChar.TypeDefinition);
        Attributes.put("lambda", HassiumClosure.TypeDefinition);
        Attributes.put("double", HassiumDouble.TypeDefinition);
        Attributes.put("dictionary", HassiumDictionary.TypeDefinition);
        Attributes.put("event", HassiumEvent.TypeDefinition);
        Attributes.put("ExceptionHandler", HassiumExceptionHandler.TypeDefinition);
        Attributes.put("func", HassiumFunction.TypeDefinition);
        Attributes.put("int", HassiumInt.TypeDefinition);
        Attributes.put("KeyValuePair", HassiumKeyValuePair.TypeDefinition);
        Attributes.put("list", HassiumList.TypeDefinition);
        Attributes.put("null", HassiumObject.Null);
        Attributes.put("object", HassiumObject.DefaultTypeDefinition);
        Attributes.put("property", HassiumProperty.TypeDefinition);
        Attributes.put("string", HassiumString.TypeDefinition);
        Attributes.put("tuple", HassiumTuple.TypeDefinition);
    }
}
