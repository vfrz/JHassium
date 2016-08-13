package lang.jhassium.runtime.standardlibrary.util;

import lang.jhassium.runtime.HassiumTypeDefinition;
import lang.jhassium.runtime.VirtualMachine;
import lang.jhassium.runtime.standardlibrary.HassiumObject;
import lang.jhassium.runtime.standardlibrary.types.*;
import lang.jhassium.utils.HassiumLogger;
import org.apache.commons.lang3.time.StopWatch;

import java.util.concurrent.TimeUnit;

/**
 * File : HassiumStopWatch.java
 * Description : None
 * Authors : FRITZ Valentin, Jacob Misirian & zdimension
 * Website : https://github.com/vfrz/JHassium
 * Date : 13/08/2016 18:43
 */
public class HassiumStopWatch extends HassiumObject {

    //TODO Use HassiumLong instead of HassiumInt

    public static HassiumTypeDefinition TypeDefinition = new HassiumTypeDefinition("StopWatch");

    private StopWatch value;

    public StopWatch getValue() {
        return value;
    }

    public void setValue(StopWatch value) {
        this.value = value;
    }

    public HassiumStopWatch() {
        try {
            Attributes.put(HassiumObject.INVOKE_FUNCTION, new HassiumFunction(this.getClass().getDeclaredMethod("_new", VirtualMachine.class, HassiumObject[].class), this, 0));
        } catch (NoSuchMethodException e) {
            HassiumLogger.error("Internal error HassiumStopWatch : " + e.getMessage());
        }
        addType(HassiumStopWatch.TypeDefinition);
    }

    public HassiumStopWatch _new(VirtualMachine vm, HassiumObject[] args) {
        HassiumStopWatch hassiumStopWatch = new HassiumStopWatch();

        hassiumStopWatch.setValue(new StopWatch());
        try {
            hassiumStopWatch.Attributes.put("hours", new HassiumProperty(hassiumStopWatch.getClass().getDeclaredMethod("get_Hours", VirtualMachine.class, HassiumObject[].class), hassiumStopWatch));
            hassiumStopWatch.Attributes.put("milliseconds", new HassiumProperty(hassiumStopWatch.getClass().getDeclaredMethod("get_Milliseconds", VirtualMachine.class, HassiumObject[].class), hassiumStopWatch));
            hassiumStopWatch.Attributes.put("minutes", new HassiumProperty(hassiumStopWatch.getClass().getDeclaredMethod("get_Minutes", VirtualMachine.class, HassiumObject[].class), hassiumStopWatch));
            hassiumStopWatch.Attributes.put("reset", new HassiumFunction(hassiumStopWatch.getClass().getDeclaredMethod("reset", VirtualMachine.class, HassiumObject[].class), hassiumStopWatch, 0));
            hassiumStopWatch.Attributes.put("restart", new HassiumFunction(hassiumStopWatch.getClass().getDeclaredMethod("restart", VirtualMachine.class, HassiumObject[].class), hassiumStopWatch, 0));
            hassiumStopWatch.Attributes.put("running", new HassiumProperty(hassiumStopWatch.getClass().getDeclaredMethod("get_Running", VirtualMachine.class, HassiumObject[].class), hassiumStopWatch));
            hassiumStopWatch.Attributes.put("seconds", new HassiumProperty(hassiumStopWatch.getClass().getDeclaredMethod("get_Seconds", VirtualMachine.class, HassiumObject[].class), hassiumStopWatch));
            hassiumStopWatch.Attributes.put("start", new HassiumFunction(hassiumStopWatch.getClass().getDeclaredMethod("start", VirtualMachine.class, HassiumObject[].class), hassiumStopWatch, 0));
            hassiumStopWatch.Attributes.put("stop", new HassiumFunction(hassiumStopWatch.getClass().getDeclaredMethod("stop", VirtualMachine.class, HassiumObject[].class), hassiumStopWatch, 0));
            hassiumStopWatch.Attributes.put("ticks", new HassiumProperty(hassiumStopWatch.getClass().getDeclaredMethod("get_Ticks", VirtualMachine.class, HassiumObject[].class), hassiumStopWatch));
        } catch (NoSuchMethodException e) {
            HassiumLogger.error("Internal error HassiumStopWatch : " + e.getMessage());
        }

        return hassiumStopWatch;
    }

    public HassiumInt get_Hours(VirtualMachine vm, HassiumObject[] args) {
        return new HassiumInt((int) TimeUnit.MILLISECONDS.toHours(value.getTime()));
    }

    public HassiumInt get_Milliseconds(VirtualMachine vm, HassiumObject[] args) {
        return new HassiumInt((int) value.getTime());
    }

    public HassiumInt get_Minutes(VirtualMachine vm, HassiumObject[] args) {
        return new HassiumInt((int) TimeUnit.MILLISECONDS.toMinutes(value.getTime()));
    }

    public HassiumNull reset(VirtualMachine vm, HassiumObject[] args) {
        value.reset();
        return HassiumObject.Null;
    }

    public HassiumNull restart(VirtualMachine mv, HassiumObject[] args) {
        if (value.isStarted())
            value.stop();
        value.reset();
        value.start();
        return HassiumObject.Null;
    }

    public HassiumBool get_Running(VirtualMachine vm, HassiumObject[] args) {
        return new HassiumBool(value.isStarted());
    }

    public HassiumInt get_Seconds(VirtualMachine vm, HassiumObject[] args) {
        return new HassiumInt((int) TimeUnit.MILLISECONDS.toSeconds(value.getTime()));
    }

    public HassiumNull start(VirtualMachine vm, HassiumObject[] args) {
        value.start();
        return HassiumObject.Null;
    }

    public HassiumNull stop(VirtualMachine vm, HassiumObject[] args) {
        value.stop();
        return HassiumObject.Null;
    }

    public HassiumInt get_Ticks(VirtualMachine vm, HassiumObject[] args) {
        return new HassiumInt((int) (621355968000000000L + value.getTime() * 10000));
    }

}
