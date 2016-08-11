package lang.jhassium.runtime.standardlibrary;

import com.rits.cloning.Cloner;
import lang.jhassium.runtime.HassiumTypeDefinition;
import lang.jhassium.runtime.VirtualMachine;
import lang.jhassium.runtime.standardlibrary.types.HassiumBool;
import lang.jhassium.runtime.standardlibrary.types.HassiumNull;
import lang.jhassium.runtime.standardlibrary.types.HassiumString;
import lang.jhassium.utils.HassiumLogger;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * File : HassiumObject.java
 * Description : None
 * Authors : FRITZ Valentin, Jacob Misirian & zdimension
 * Website : https://github.com/vfrz/JHassium
 * Date : 10/08/2016 22:40
 */
public class HassiumObject implements ICloneable {

    public static final String ADD_FUNCTION = "__add__";
    public static final String SUB_FUNCTION = "__sub__";
    public static final String MUL_FUNCTION = "__mul__";
    public static final String DIV_FUNCTION = "__div__";
    public static final String MOD_FUNCTION = "__mod__";
    public static final String XOR_FUNCTION = "__xor__";
    public static final String OR_FUNCTION = "__or__";
    public static final String XAND_FUNCTION = "__xand__";
    public static final String EQUALS_FUNCTION = "__equals__";
    public static final String NOT_EQUAL_FUNCTION = "__notequal__";
    public static final String GREATER_FUNCTION = "__greater__";
    public static final String LESSER_FUNCTION = "__lesser__";
    public static final String GREATER_OR_EQUAL_FUNCTION = "__greaterorequal__";
    public static final String LESSER_OR_EQUAL_FUNCTION = "__lesserorequal__";
    public static final String INVOKE_FUNCTION = "__invoke__";
    public static final String INDEX_FUNCTION = "__index__";
    public static final String STORE_INDEX_FUNCTION = "__storeindex__";
    public static final String ITER_FUNCTION = "__iter__";
    public static final String SLICE_FUNCTION = "__slice__";
    public static final String SKIP_FUNCTION = "__skip__";
    public static final String ENUMERABLE_FULL = "__enumerablefull__";
    public static final String ENUMERABLE_NEXT = "__enumerablenext__";
    public static final String ENUMERABLE_RESET = "__enumerableReset__";
    public static final String NOT = "__not__";
    public static final String BITWISE_COMPLEMENT = "__bcompl__";
    public static final String NEGATE = "__negate__";
    public static final String BIT_SHIFT_LEFT = "__bshiftleft__";
    public static final String BIT_SHIFT_RIGHT = "__bshiftright__";
    public static final String CONTAINS = "__contains__";
    public static final String TOSTRING_FUNCTION = "toString";
    public static HassiumNull Null = new HassiumNull();
    public static HassiumTypeDefinition DefaultTypeDefinition = new HassiumTypeDefinition("object");
    public LinkedHashMap<String, HassiumObject> Attributes = new LinkedHashMap<>();
    public List<HassiumTypeDefinition> Types = new ArrayList<HassiumTypeDefinition>() {{
        add(DefaultTypeDefinition);
    }};

    public Object getValue() {
        return null;
    }

    ;

    public HassiumObject add(VirtualMachine vm, HassiumObject obj) {
        if (!Attributes.containsKey(ADD_FUNCTION))
            HassiumLogger.error("Object " + type().toString(vm) + " does not support adding!");
        return Attributes.get(ADD_FUNCTION).invoke(vm, new HassiumObject[]{obj});
    }

    public HassiumObject sub(VirtualMachine vm, HassiumObject obj) {
        if (!Attributes.containsKey(SUB_FUNCTION))
            HassiumLogger.error("Object " + type().toString(vm) + " does not support subtracting!");
        return Attributes.get(SUB_FUNCTION).invoke(vm, new HassiumObject[]{obj});
    }

    public HassiumObject mul(VirtualMachine vm, HassiumObject obj) {
        if (!Attributes.containsKey(MUL_FUNCTION))
            HassiumLogger.error("Object " + type().toString(vm) + " does not support multiplying!");
        return Attributes.get(MUL_FUNCTION).invoke(vm, new HassiumObject[]{obj});
    }

    public HassiumObject div(VirtualMachine vm, HassiumObject obj) {
        if (!Attributes.containsKey(DIV_FUNCTION))
            HassiumLogger.error("Object " + type().toString(vm) + " does not support dividing!");
        return Attributes.get(DIV_FUNCTION).invoke(vm, new HassiumObject[]{obj});
    }

    public HassiumObject mod(VirtualMachine vm, HassiumObject obj) {
        if (!Attributes.containsKey(MOD_FUNCTION))
            HassiumLogger.error("Object " + type().toString(vm) + " does not support modulus!");
        return Attributes.get(MOD_FUNCTION).invoke(vm, new HassiumObject[]{obj});
    }

    public HassiumObject xor(VirtualMachine vm, HassiumObject obj) {
        if (!Attributes.containsKey(XOR_FUNCTION))
            HassiumLogger.error("Object " + type().toString(vm) + " does not support xor!");
        return Attributes.get(XOR_FUNCTION).invoke(vm, new HassiumObject[]{obj});
    }

    public HassiumObject or(VirtualMachine vm, HassiumObject obj) {
        if (!Attributes.containsKey(OR_FUNCTION))
            HassiumLogger.error("Object " + type().toString(vm) + " does not support logical or!");
        return Attributes.get(OR_FUNCTION).invoke(vm, new HassiumObject[]{obj});
    }

    public HassiumObject xand(VirtualMachine vm, HassiumObject obj) {
        if (!Attributes.containsKey(XAND_FUNCTION))
            HassiumLogger.error("Object " + type().toString(vm) + " does not support and!");
        return Attributes.get(XAND_FUNCTION).invoke(vm, new HassiumObject[]{obj});
    }

    public HassiumBool equals(VirtualMachine vm, HassiumObject obj) {
        if (!Attributes.containsKey(EQUALS_FUNCTION))
            HassiumLogger.error("Object " + type().toString(vm) + " does not support equals!");
        return (HassiumBool) Attributes.get(EQUALS_FUNCTION).invoke(vm, new HassiumObject[]{obj});
    }

    public HassiumBool notEquals(VirtualMachine vm, HassiumObject obj) {
        if (!Attributes.containsKey(NOT_EQUAL_FUNCTION))
            HassiumLogger.error("Object " + type().toString(vm) + " does not support not equal!");
        return (HassiumBool) Attributes.get(NOT_EQUAL_FUNCTION).invoke(vm, new HassiumObject[]{obj});
    }

    public HassiumBool greaterThan(VirtualMachine vm, HassiumObject obj) {
        if (!Attributes.containsKey(GREATER_FUNCTION))
            HassiumLogger.error("Object " + type().toString(vm) + " does not support greater than!");
        return (HassiumBool) Attributes.get(GREATER_FUNCTION).invoke(vm, new HassiumObject[]{obj});
    }

    public HassiumBool greaterThanOrEqual(VirtualMachine vm, HassiumObject obj) {
        if (!Attributes.containsKey(GREATER_OR_EQUAL_FUNCTION))
            HassiumLogger.error("Object " + type().toString(vm) + " does not support greater than or equal!");
        return (HassiumBool) Attributes.get(GREATER_OR_EQUAL_FUNCTION).invoke(vm, new HassiumObject[]{obj});
    }

    public HassiumBool lesserThan(VirtualMachine vm, HassiumObject obj) {
        if (!Attributes.containsKey(LESSER_FUNCTION))
            HassiumLogger.error("Object " + type().toString(vm) + " does not support lesser than!");
        return (HassiumBool) Attributes.get(LESSER_FUNCTION).invoke(vm, new HassiumObject[]{obj});
    }

    public HassiumBool lesserThanOrEqual(VirtualMachine vm, HassiumObject obj) {
        if (!Attributes.containsKey(LESSER_OR_EQUAL_FUNCTION))
            HassiumLogger.error("Object " + type().toString(vm) + " does not support lesser than or equal!");
        return (HassiumBool) Attributes.get(LESSER_OR_EQUAL_FUNCTION).invoke(vm, new HassiumObject[]{obj});
    }

    public HassiumObject invoke(VirtualMachine vm, HassiumObject[] args) {
        if (Attributes.containsKey(INVOKE_FUNCTION))
            return Attributes.get(INVOKE_FUNCTION).invoke(vm, args);
        HassiumLogger.error("Object does not support invoking!");
        return null; // Not reachable but for compilation
    }

    public HassiumObject index(VirtualMachine vm, HassiumObject obj) {
        return Attributes.get(INDEX_FUNCTION).invoke(vm, new HassiumObject[]{obj});
    }

    public HassiumObject storeIndex(VirtualMachine vm, HassiumObject index, HassiumObject value) {
        return Attributes.get(STORE_INDEX_FUNCTION).invoke(vm, new HassiumObject[]{index, value});
    }

    public HassiumObject iter(VirtualMachine vm) {
        return Attributes.get(ITER_FUNCTION).invoke(vm, new HassiumObject[0]);
    }

    public HassiumObject slice(VirtualMachine vm, HassiumObject left, HassiumObject right) {
        return Attributes.get(SLICE_FUNCTION).invoke(vm, new HassiumObject[]{left, right});
    }

    public HassiumObject skip(VirtualMachine vm, HassiumObject skip) {
        return Attributes.get(SKIP_FUNCTION).invoke(vm, new HassiumObject[]{skip});
    }

    public HassiumObject enumerableFull(VirtualMachine vm) {
        return Attributes.get(ENUMERABLE_FULL).invoke(vm, new HassiumObject[0]);
    }

    public HassiumObject enumerableNext(VirtualMachine vm) {
        return Attributes.get(ENUMERABLE_NEXT).invoke(vm, new HassiumObject[0]);
    }

    public HassiumObject enumerableReset(VirtualMachine vm) {
        return Attributes.get(ENUMERABLE_RESET).invoke(vm, new HassiumObject[0]);
    }

    public HassiumObject not(VirtualMachine vm) {
        return Attributes.get(NOT).invoke(vm, new HassiumObject[0]);
    }

    public HassiumObject bitwiseComplement(VirtualMachine vm) {
        return Attributes.get(BITWISE_COMPLEMENT).invoke(vm, new HassiumObject[0]);
    }

    public HassiumObject negate(VirtualMachine vm) {
        return Attributes.get(NEGATE).invoke(vm, new HassiumObject[0]);
    }

    public HassiumObject bitShiftLeft(VirtualMachine vm, HassiumObject obj) {
        if (!Attributes.containsKey(BIT_SHIFT_LEFT))
            HassiumLogger.error("Object " + type().toString(vm) + " does not support bitshift left!");
        return Attributes.get(BIT_SHIFT_LEFT).invoke(vm, new HassiumObject[]{obj});
    }

    public HassiumObject bitShiftRight(VirtualMachine vm, HassiumObject obj) {
        if (!Attributes.containsKey(BIT_SHIFT_RIGHT))
            HassiumLogger.error("Object " + type().toString(vm) + " does not support bitshift right!");
        return Attributes.get(BIT_SHIFT_RIGHT).invoke(vm, new HassiumObject[]{obj});
    }

    public HassiumBool contains(VirtualMachine vm, HassiumObject obj) {
        return HassiumBool.create(Attributes.get(CONTAINS).invoke(vm, new HassiumObject[]{obj}));
    }

    public void addType(HassiumTypeDefinition type) {
        Types.add(type);
    }

    public HassiumTypeDefinition type() {
        return Types.get(Types.size() - 1);
    }

    public String toString(VirtualMachine vm) {
        if (Attributes.containsKey("toString"))
            return ((HassiumString) Attributes.get("toString").invoke(vm, new HassiumObject[0])).getValue();
        return type().toString(vm);
    }

    public Object clone() {
        try {
            return new Cloner().shallowClone(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
