package lang.jhassium.runtime.standardlibrary.types;

import lang.jhassium.runtime.HassiumTypeDefinition;
import lang.jhassium.runtime.VirtualMachine;
import lang.jhassium.runtime.standardlibrary.HassiumObject;
import lang.jhassium.utils.HassiumLogger;

import java.lang.reflect.Method;

/**
 * File : HassiumFunction.java
 * Description : None
 * Author : FRITZ Valentin
 * Website : https://github.com/vfrz/JHassium
 * Date : 10/08/2016 23:08
 */
public class HassiumFunction extends HassiumObject {

    public static HassiumTypeDefinition TypeDefinition = new HassiumTypeDefinition("func");

    private Method target;

    private Object object;

    private int[] paramLengths;

    public HassiumFunction(Method target, Object object, int paramLength) {
        this.target = target;
        this.object = object;
        this.paramLengths = new int[]{paramLength};
        addType(HassiumFunction.TypeDefinition);
    }

    public HassiumFunction(Method target, Object object, int[] paramLengths) {
        this.target = target;
        this.object = object;
        this.paramLengths = paramLengths;
        addType(HassiumFunction.TypeDefinition);
    }

    public int[] getParamLengths() {
        return paramLengths;
    }

    public HassiumObject invoke(VirtualMachine vm, HassiumObject[] args) {
        if (vm != null)
            vm.getCallStack().push(String.format("func %1s (%2s)", target.getName(), paramLengths[paramLengths.length - 1]));
        if (paramLengths[0] != -1) {
            for (int i : paramLengths) {
                if (i == args.length) {
                    if (vm != null)
                        vm.getCallStack().pop();
                    try {
                        return (HassiumObject) target.invoke(object, vm, args);
                    } catch (Exception e) {
                        HassiumLogger.error("Error while invoking HassiumFunction with target name : " + target.getName());
                    }
                }
            }
            HassiumLogger.error(String.format("Expected argument length of %1s, got %2s", paramLengths[0], args.length));
        } else {
            if (vm != null)
                vm.getCallStack().pop();
            try {
                return (HassiumObject) target.invoke(object, vm, args);
            } catch (Exception e) {
                HassiumLogger.error("Error while invoking HassiumFunction with target name : " + target.getName());
            }
        }
        return null; //Not reachable but for compilation
    }

    private HassiumString __tostring__(HassiumObject[] args) {
        return new HassiumString(target.getName());
    }

}
