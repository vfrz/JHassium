package lang.jhassium.runtime.standardlibrary.types;

import lang.jhassium.codegen.MethodBuilder;
import lang.jhassium.runtime.Frame;
import lang.jhassium.runtime.HassiumTypeDefinition;
import lang.jhassium.runtime.VirtualMachine;
import lang.jhassium.runtime.standardlibrary.HassiumObject;
import lang.jhassium.utils.HassiumLogger;

/**
 * File : HassiumExceptionHandler.java
 * Description : None
 * Author : FRITZ Valentin
 * Website : https://github.com/vfrz/JHassium
 * Date : 11/08/2016 14:13
 */
public class HassiumExceptionHandler extends HassiumObject {

    public static HassiumTypeDefinition TypeDefinition = new HassiumTypeDefinition("ExceptionHandler");

    private MethodBuilder sourceMethod;
    private MethodBuilder handlerMethod;
    private double label;

    public Frame Frame;

    public MethodBuilder getSourceMethod() {
        return sourceMethod;
    }

    public MethodBuilder getHandlerMethod() {
        return handlerMethod;
    }

    public double getLabel() {
        return label;
    }

    public HassiumExceptionHandler(MethodBuilder sourceMethod, MethodBuilder handlerMethod, double label) {
        this.sourceMethod = sourceMethod;
        this.handlerMethod = handlerMethod;
        this.label = label;
        try {
            Attributes.put(HassiumObject.INVOKE_FUNCTION, new HassiumFunction(this.getClass().getDeclaredMethod("__invoke__", VirtualMachine.class, HassiumObject[].class), this, -1));
        } catch (NoSuchMethodException e) {
            HassiumLogger.error("Internal error HassiumExceptionHandler : " + e.getMessage());
        }
        addType(HassiumExceptionHandler.TypeDefinition);
    }

    public HassiumObject __invoke__(VirtualMachine vm, HassiumObject[] args) {
        vm.getStackFrame().Frames.push(Frame);
        HassiumObject ret = handlerMethod.invoke(vm, args);
        vm.getStackFrame().popFrame();
        return ret;
    }
}
