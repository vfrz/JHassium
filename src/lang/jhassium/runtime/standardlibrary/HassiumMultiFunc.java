package lang.jhassium.runtime.standardlibrary;

import lang.jhassium.codegen.MethodBuilder;
import lang.jhassium.runtime.HassiumTypeDefinition;
import lang.jhassium.runtime.VirtualMachine;
import lang.jhassium.runtime.standardlibrary.types.HassiumFunction;
import lang.jhassium.runtime.standardlibrary.types.HassiumNull;
import lang.jhassium.utils.HassiumLogger;
import lang.jhassium.utils.Helpers;

import java.util.List;

/**
 * File : HassiumMultiFunc.java
 * Description : None
 * Authors : FRITZ Valentin, Jacob Misirian & zdimension
 * Website : https://github.com/vfrz/JHassium
 * Date : 11/08/2016 17:29
 */
public class HassiumMultiFunc extends HassiumObject {

    public static HassiumTypeDefinition TypeDefinition = new HassiumTypeDefinition("multifunc");

    private List<MethodBuilder> lambdas;

    public List<MethodBuilder> getLambdas() {
        return lambdas;
    }

    public HassiumMultiFunc(List<MethodBuilder> methods) {
        this.lambdas = methods;
        try {
            Attributes.put("add", new HassiumFunction(this.getClass().getDeclaredMethod("add"), this, -1));
            Attributes.put(HassiumObject.ADD_FUNCTION, new HassiumFunction(this.getClass().getDeclaredMethod("__add__"), this, 1));
            Attributes.put(HassiumObject.INVOKE_FUNCTION, new HassiumFunction(this.getClass().getDeclaredMethod("__invoke__"), this, -1));
        } catch (NoSuchMethodException e) {
            HassiumLogger.error("Internal error HassiumBool : " + e.getMessage());
        }
    }

    public HassiumNull add(VirtualMachine vm, HassiumObject[] args) {
        for (HassiumObject obj : args) {
            lambdas.add((MethodBuilder) obj);
        }
        return HassiumObject.Null;
    }

    public HassiumMultiFunc __add__(VirtualMachine vm, HassiumObject[] args) {
        HassiumMultiFunc multiFunc = Helpers.as(this.clone(), HassiumMultiFunc.class);
        multiFunc.getLambdas().add((MethodBuilder) args[0]);
        return multiFunc;
    }

    public HassiumObject __invoke__(VirtualMachine vm, HassiumObject[] args) {
        for (MethodBuilder method : lambdas) {
            if (method.Parameters.size() == args.length)
                return method.invoke(vm, args);
        }
        HassiumLogger.error("Unknown parameter length " + args.length);
        return null; //Not reachable but for compilation
    }

}
