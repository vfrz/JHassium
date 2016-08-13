package lang.jhassium.runtime.standardlibrary.util;

import lang.jhassium.HassiumExecuter;
import lang.jhassium.runtime.VirtualMachine;
import lang.jhassium.runtime.standardlibrary.HassiumObject;
import lang.jhassium.runtime.standardlibrary.InternalModule;
import lang.jhassium.runtime.standardlibrary.types.HassiumFunction;
import lang.jhassium.runtime.standardlibrary.types.HassiumNull;
import lang.jhassium.runtime.standardlibrary.types.HassiumString;
import lang.jhassium.utils.HassiumLogger;

import java.util.ArrayList;

/**
 * File : HassiumUtilModule.java
 * Description : None
 * Authors : FRITZ Valentin, Jacob Misirian & zdimension
 * Website : https://github.com/vfrz/JHassium
 * Date : 13/08/2016 18:56
 */
public class HassiumUtilModule extends InternalModule {

    public HassiumUtilModule() {
        super("Util");

        try {
            Attributes.put("eval", new HassiumFunction(this.getClass().getDeclaredMethod("eval", VirtualMachine.class, HassiumObject[].class), this, 1));
        } catch (NoSuchMethodException e) {
            HassiumLogger.error("Internal error HassiumStopWatch : " + e.getMessage());
        }
        Attributes.put("StopWatch", new HassiumStopWatch());
    }

    public HassiumNull eval(VirtualMachine virtualMachine, HassiumObject[] args) {
        HassiumExecuter.fromString(HassiumString.create(args[0]).getValue(), new ArrayList<>());
        return HassiumObject.Null;
    }

}
