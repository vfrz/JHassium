package lang.jhassium.runtime.standardlibrary;

import lang.jhassium.runtime.VirtualMachine;
import lang.jhassium.runtime.standardlibrary.types.*;
import lang.jhassium.utils.HassiumLogger;
import lang.jhassium.utils.Helpers;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * File : GlobalFunctions.java
 * Description : None
 * Authors : FRITZ Valentin, Jacob Misirian & zdimension
 * Website : https://github.com/vfrz/JHassium
 * Date : 11/08/2016 16:35
 */
public class GlobalFunctions {

    public static LinkedHashMap<String, HassiumObject> FunctionList = new LinkedHashMap<String, HassiumObject>() {{
        try {
            put("exit", new HassiumFunction(GlobalFunctions.class.getDeclaredMethod("exit", VirtualMachine.class, HassiumObject[].class), this, 1));
            put("fillList", new HassiumFunction(GlobalFunctions.class.getDeclaredMethod("fillList", VirtualMachine.class, HassiumObject[].class), this, new int[]{1, 2, 3}));
            put("filter", new HassiumFunction(GlobalFunctions.class.getDeclaredMethod("filter", VirtualMachine.class, HassiumObject[].class), this, 2));
            put("format", new HassiumFunction(GlobalFunctions.class.getDeclaredMethod("format", VirtualMachine.class, HassiumObject[].class), this, -1));
            put("getAttribute", new HassiumFunction(GlobalFunctions.class.getDeclaredMethod("getAttribute", VirtualMachine.class, HassiumObject[].class), this, 2));
            put("globals", new HassiumProperty(GlobalFunctions.class.getDeclaredMethod("get_Globals", VirtualMachine.class, HassiumObject[].class), this));
            put("hasAttribute", new HassiumFunction(GlobalFunctions.class.getDeclaredMethod("hasAttribute", VirtualMachine.class, HassiumObject[].class), this, 2));
            put("input", new HassiumFunction(GlobalFunctions.class.getDeclaredMethod("input", VirtualMachine.class, HassiumObject[].class), this, 0));
            put("inputChar", new HassiumFunction(GlobalFunctions.class.getDeclaredMethod("inputChar", VirtualMachine.class, HassiumObject[].class), this, 0));
            put("map", new HassiumFunction(GlobalFunctions.class.getDeclaredMethod("map", VirtualMachine.class, HassiumObject[].class), this, new int[]{2, 3}));
            put("print", new HassiumFunction(GlobalFunctions.class.getDeclaredMethod("print", VirtualMachine.class, HassiumObject[].class), this, -1));
            put("println", new HassiumFunction(GlobalFunctions.class.getDeclaredMethod("println", VirtualMachine.class, HassiumObject[].class), this, -1));
            put("range", new HassiumFunction(GlobalFunctions.class.getDeclaredMethod("range", VirtualMachine.class, HassiumObject[].class), this, 2));
            put("setAttribute", new HassiumFunction(GlobalFunctions.class.getDeclaredMethod("setAttribute", VirtualMachine.class, HassiumObject[].class), this, 3));
            put("sleep", new HassiumFunction(GlobalFunctions.class.getDeclaredMethod("sleep", VirtualMachine.class, HassiumObject[].class), this, 1));
            put("type", new HassiumFunction(GlobalFunctions.class.getDeclaredMethod("type", VirtualMachine.class, HassiumObject[].class), this, 1));
            put("types", new HassiumFunction(GlobalFunctions.class.getDeclaredMethod("types", VirtualMachine.class, HassiumObject[].class), this, -1));

        } catch (NoSuchMethodException e) {
            HassiumLogger.error("Internal error GlobalFunctions : " + e.getMessage());
        }
    }};

    public static HassiumNull exit(VirtualMachine vm, HassiumObject[] args) {
        System.exit(HassiumInt.create(args[0]).getValue());
        return HassiumObject.Null; // Not reachable but for compilation
    }

    public static HassiumList fillList(VirtualMachine vm, HassiumObject[] args) {
        HassiumList list = HassiumList.create(args[0]);
        HassiumObject filler = (args.length == 3) ? args[2] : new HassiumInt(0);
        int max = HassiumInt.create(args[1]).getValue();
        for (int i = 0; i < max; i++)
            list.getValue().add(Helpers.as(filler.clone(), HassiumObject.class));
        return list;
    }

    public static HassiumList filter(VirtualMachine vm, HassiumObject[] args) {
        HassiumList list = Helpers.as(args[0].iter(vm), HassiumList.class);
        HassiumList result = new HassiumList(new HassiumObject[0]);

        for (HassiumObject obj : list.getValue()) {
            if (HassiumBool.create(args[1].invoke(vm, new HassiumObject[]{obj})).getValue())
                result.getValue().add(obj);
        }
        return result;
    }

    public static HassiumString format(VirtualMachine vm, HassiumObject[] args) {
        String[] objs = new String[args.length - 1];
        for (int i = 1; i < args.length; i++)
            objs[i - 1] = args[i].toString(vm);
        return new HassiumString(String.format(HassiumString.create(args[0]).toString(), objs));
    }

    public static HassiumList get_Globals(VirtualMachine vm, HassiumObject[] args) {
        HassiumList list = new HassiumList(new HassiumObject[0]);

        for (Map.Entry<String, HassiumObject> entry : vm.getGlobals().entrySet())
            list.getValue().add(new HassiumKeyValuePair(new HassiumString(entry.getKey()), entry.getValue()));

        return list;
    }

    public static HassiumObject getAttribute(VirtualMachine vm, HassiumObject[] args) {
        return args[0].Attributes.get(args[1].toString(vm));
    }

    public static HassiumBool hasAttribute(VirtualMachine vm, HassiumObject[] args) {
        return new HassiumBool(args[0].Attributes.containsKey(args[1].toString(vm)));
    }

    public static HassiumString input(VirtualMachine vm, HassiumObject[] args) {
        return new HassiumString(new Scanner(System.in).nextLine());
    }

    public static HassiumChar inputChar(VirtualMachine vm, HassiumObject[] args) {
        return new HassiumChar(new Scanner(System.in).nextLine().charAt(0));
    }

    public static HassiumList map(VirtualMachine vm, HassiumObject[] args) {
        HassiumList list = Helpers.as(args[0].iter(vm), HassiumList.class);
        HassiumList result = new HassiumList(new HassiumObject[0]);

        list.enumerableReset(vm);
        if (args.length == 2)
            while (!HassiumBool.create(list.enumerableFull(vm)).getValue())
                result.getValue().add(args[1].invoke(vm, new HassiumObject[]{list.enumerableNext(vm)}));
        else {
            while (!HassiumBool.create(list.enumerableFull(vm)).getValue()) {
                HassiumObject obj = list.enumerableNext(vm);
                if (HassiumBool.create(args[2].invoke(vm, new HassiumObject[]{obj})).getValue())
                    result.getValue().add(args[1].invoke(vm, new HassiumObject[]{obj}));
            }
        }

        list.enumerableReset(vm);

        return result;
    }

    public static HassiumObject print(VirtualMachine vm, HassiumObject[] args) {
        for (HassiumObject obj : args)
            System.out.print(obj.toString(vm));
        return HassiumObject.Null;
    }

    public static HassiumObject println(VirtualMachine vm, HassiumObject[] args) {
        for (HassiumObject obj : args)
            System.out.println(obj.toString(vm));
        return HassiumObject.Null;
    }

    public static HassiumObject range(VirtualMachine vm, HassiumObject[] args) {
        int max = HassiumInt.create(args[1]).getValue();
        HassiumList list = new HassiumList(new HassiumObject[0]);
        for (int i = HassiumInt.create(args[0]).getValue(); i < max; i++)
            list.getValue().add(new HassiumInt(i));

        return list;
    }

    public static HassiumObject setAttribute(VirtualMachine vm, HassiumObject[] args) {
        String attribute = args[1].toString(vm);
        if (!args[0].Attributes.containsKey(attribute))
            args[0].Attributes.put(attribute, args[2]);
        else
            args[0].Attributes.put(attribute, args[2]);
        return args[2];
    }

    public static HassiumNull sleep(VirtualMachine vm, HassiumObject[] args) {
        try {
            Thread.sleep(HassiumInt.create(args[0]).getValue());
        } catch (InterruptedException e) {
            HassiumLogger.error("Error while sleep : " + e.getMessage());
        }
        return HassiumObject.Null;
    }

    public static HassiumObject type(VirtualMachine vm, HassiumObject[] args) {
        return new HassiumString(args[0].type().toString(vm));
    }

    public static HassiumObject types(VirtualMachine vm, HassiumObject[] args) {
        HassiumObject[] elements = new HassiumObject[args[0].Types.size()];
        for (int i = 0; i < elements.length; i++)
            elements[i] = new HassiumString(args[0].Types.get(i).toString(vm));
        return new HassiumList(elements);
    }

}
