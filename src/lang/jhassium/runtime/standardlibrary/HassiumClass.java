package lang.jhassium.runtime.standardlibrary;

import lang.jhassium.runtime.HassiumTypeDefinition;
import lang.jhassium.runtime.VirtualMachine;
import lang.jhassium.utils.HassiumLogger;

/**
 * File : HassiumClass.java
 * Description : None
 * Author : FRITZ Valentin
 * Website : https://github.com/vfrz/JHassium
 * Date : 11/08/2016 13:35
 */
public class HassiumClass extends HassiumObject {

    public HassiumTypeDefinition TypeDefinition;
    public String Name;

    public HassiumObject invoke(VirtualMachine vm, HassiumObject[] args) {
        if (!Attributes.containsKey("new"))
            HassiumLogger.error("Class has no suitible constructor!");
        return Attributes.get("new").invoke(vm, args);
    }
}
