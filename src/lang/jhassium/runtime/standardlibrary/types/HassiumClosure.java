package lang.jhassium.runtime.standardlibrary.types;

import lang.jhassium.codegen.MethodBuilder;
import lang.jhassium.runtime.Frame;
import lang.jhassium.runtime.HassiumTypeDefinition;
import lang.jhassium.runtime.VirtualMachine;
import lang.jhassium.runtime.standardlibrary.HassiumObject;
import lang.jhassium.utils.HassiumLogger;

/**
 * File : HassiumClosure.java
 * Description : None
 * Authors : FRITZ Valentin, Jacob Misirian & zdimension
 * Website : https://github.com/vfrz/JHassium
 * Date : 11/08/2016 13:24
 */
public class HassiumClosure extends HassiumObject {

    public static HassiumTypeDefinition TypeDefinition = new HassiumTypeDefinition("lambda");

    private MethodBuilder method;
    private Frame frame;

    public MethodBuilder getMethod() {
        return method;
    }

    public Frame getFrame() {
        return frame;
    }

    public HassiumClosure(MethodBuilder method, Frame frame) {
        this.method = method;
        this.frame = frame;
        try {
            Attributes.put(HassiumObject.INVOKE_FUNCTION, new HassiumFunction(this.getClass().getDeclaredMethod("__invoke__", VirtualMachine.class, HassiumObject[].class), this, -1));
        } catch (NoSuchMethodException e) {
            HassiumLogger.error("Internal error HassiumClosure : " + e.getMessage());
        }
        addType(HassiumClosure.TypeDefinition);
    }

    public HassiumObject __invoke__(VirtualMachine vm, HassiumObject[] args) {
        vm.getStackFrame().Frames.push(frame);
        HassiumObject ret = method.invoke(vm, args);
        vm.getStackFrame().popFrame();

        return ret;
    }

}
