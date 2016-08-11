package lang.jhassium.runtime.standardlibrary.types;

import lang.jhassium.runtime.HassiumTypeDefinition;
import lang.jhassium.runtime.VirtualMachine;
import lang.jhassium.runtime.standardlibrary.HassiumObject;
import lang.jhassium.utils.HassiumLogger;

/**
 * File : HassiumEvent.java
 * Description : None
 * Author : FRITZ Valentin
 * Website : https://github.com/vfrz/JHassium
 * Date : 11/08/2016 12:58
 */
public class HassiumEvent extends HassiumObject {

    public static HassiumTypeDefinition TypeDefinition = new HassiumTypeDefinition("Event");

    public HassiumList Handlers;

    public HassiumEvent() {
        try {
            Attributes.put(HassiumObject.INVOKE_FUNCTION, new HassiumFunction(this.getClass().getDeclaredMethod("_new"), this, new int[]{0, 1}));
        } catch (NoSuchMethodException e) {
            HassiumLogger.error("Internal error HassiumChar : " + e.getMessage());
        }
    }

    private HassiumEvent _new(VirtualMachine vm, HassiumObject[] args) {
        HassiumEvent hassiumEvent = new HassiumEvent();

        hassiumEvent.Handlers = args.length == 0 ? new HassiumList(new HassiumObject[0]) : HassiumList.Create(args[0]);
        try {
            hassiumEvent.Attributes.put("add", new HassiumFunction(this.getClass().getDeclaredMethod("add"), this, -1));
            hassiumEvent.Attributes.put("handle", new HassiumFunction(this.getClass().getDeclaredMethod("handle"), this, -1));
            hassiumEvent.Attributes.put("remove", new HassiumFunction(this.getClass().getDeclaredMethod("remove"), this, -1));
        } catch (NoSuchMethodException e) {
            HassiumLogger.error("Internal error HassiumChar : " + e.getMessage());
        }
        hassiumEvent.addType(HassiumEvent.TypeDefinition);

        return hassiumEvent;
    }

    public HassiumNull add(VirtualMachine vm, HassiumObject[] args) {
        for (HassiumObject obj : args) Handlers.getValue().add(obj);
        return HassiumObject.Null;
    }

    public HassiumList handle(VirtualMachine vm, HassiumObject[] args) {
        HassiumList result = new HassiumList(new HassiumObject[0]);
        for (HassiumObject obj : Handlers.getValue()) result.getValue().add(obj.invoke(vm, args));
        return result;
    }

    public HassiumNull remove(VirtualMachine vm, HassiumObject[] args) {
        for (HassiumObject obj : args) Handlers.getValue().remove(obj);
        return HassiumObject.Null;
    }

}
