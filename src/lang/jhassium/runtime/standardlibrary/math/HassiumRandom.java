package lang.jhassium.runtime.standardlibrary.math;

import lang.jhassium.runtime.HassiumTypeDefinition;
import lang.jhassium.runtime.VirtualMachine;
import lang.jhassium.runtime.standardlibrary.HassiumObject;
import lang.jhassium.runtime.standardlibrary.types.HassiumDouble;
import lang.jhassium.runtime.standardlibrary.types.HassiumFunction;
import lang.jhassium.runtime.standardlibrary.types.HassiumInt;
import lang.jhassium.utils.HassiumLogger;

import java.util.Random;

/**
 * File : HassiumRandom.java
 * Description : None
 * Authors : FRITZ Valentin, Jacob Misirian & zdimension
 * Website : https://github.com/vfrz/JHassium
 * Date : 12/08/2016 01:03
 */
public class HassiumRandom extends HassiumObject {

    public static HassiumTypeDefinition TypeDefinition = new HassiumTypeDefinition("Random");

    private Random value;

    public HassiumRandom() {
        try {
            Attributes.put(HassiumObject.INVOKE_FUNCTION, new HassiumFunction(this.getClass().getDeclaredMethod("_new", VirtualMachine.class, HassiumObject[].class), this, new int[]{0, 1}));
        } catch (NoSuchMethodException e) {
            HassiumLogger.error("Internal error HassiumRandom : " + e.getMessage());
        }
        addType(HassiumRandom.TypeDefinition);
    }

    public Random getValue() {
        return value;
    }

    public void setValue(Random value) {
        this.value = value;
    }

    public HassiumRandom _new(VirtualMachine vm, HassiumObject[] args) {
        HassiumRandom hassiumRandom = new HassiumRandom();

        hassiumRandom.setValue(args.length == 1 ? new Random(HassiumInt.create(args[0]).getValue()) : new Random());

        try {
            hassiumRandom.Attributes.put("nextDouble", new HassiumFunction(hassiumRandom.getClass().getDeclaredMethod("nextDouble", VirtualMachine.class, HassiumObject[].class), hassiumRandom, 0));
            hassiumRandom.Attributes.put("nextInt", new HassiumFunction(hassiumRandom.getClass().getDeclaredMethod("nextInt", VirtualMachine.class, HassiumObject[].class), hassiumRandom, new int[]{0, 1, 2}));
        } catch (NoSuchMethodException e) {
            HassiumLogger.error("Internal error HassiumRandom : " + e.getMessage());
        }

        return hassiumRandom;
    }

    public HassiumDouble nextDouble(VirtualMachine vm, HassiumObject[] args) {
        return new HassiumDouble(value.nextDouble());
    }

    public HassiumInt nextInt(VirtualMachine vm, HassiumObject[] args) {
        int value = 0;
        switch (args.length) {
            case 0:
                value = this.value.nextInt();
                break;
            case 1:
                value = this.value.nextInt(HassiumInt.create(args[0]).getValue());
                break;
            case 2:
                value = this.value.nextInt(HassiumInt.create(args[1]).getValue() - HassiumInt.create(args[0]).getValue()) + HassiumInt.create(args[0]).getValue();
                break;
        }

        return new HassiumInt(value);
    }

}
