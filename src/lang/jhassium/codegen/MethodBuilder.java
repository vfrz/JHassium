package lang.jhassium.codegen;

import lang.jhassium.SourceLocation;
import lang.jhassium.parser.Parameter;
import lang.jhassium.runtime.HassiumTypeDefinition;
import lang.jhassium.runtime.VirtualMachine;
import lang.jhassium.runtime.standardlibrary.HassiumClass;
import lang.jhassium.runtime.standardlibrary.HassiumObject;
import lang.jhassium.runtime.standardlibrary.ICloneable;
import lang.jhassium.runtime.standardlibrary.types.HassiumFunction;
import lang.jhassium.utils.HassiumLogger;

import java.util.*;

/**
 * File : MethodBuilder.java
 * Description : None
 * Author : FRITZ Valentin
 * Website : https://github.com/vfrz/JHassium
 * Date : 11/08/2016 13:33
 */
public class MethodBuilder extends HassiumObject {

    public HassiumClass Parent;

    private String Name;

    public String getName() {
        return Name;
    }

    public void setName(String value) {
        Name = value;
        addType(HassiumFunction.TypeDefinition);
    }

    private String name = "";
    public String ReturnType;

    public HashMap<Parameter, Integer> Parameters = new HashMap<>();
    public List<Instruction> Instructions = new ArrayList<>();

    public HashMap<Double, Integer> Labels = new HashMap<>();
    public Stack<Double> BreakLabels = new Stack<>();
    public Stack<Double> ContinueLabels = new Stack<>();

    public String SourceRepresentation;

    public boolean isConstructor() {
        return Name.equals("new");
    }

    public MethodBuilder() {
        ReturnType = "";
    }

    public HassiumObject invoke(VirtualMachine vm, HassiumObject[] args) {
        if (name != "__lambda__" && name != "__catch__")
            vm.getStackFrame().enterFrame();

        vm.getCallStack().push(SourceRepresentation != null ? SourceRepresentation : Name);
        int counter = 0;
        for (Map.Entry<Parameter, Integer> param : Parameters.entrySet()) {
            HassiumObject argument = args[counter++];
            if (param.getKey().isEnforced()) {
                if (!argument.Types.contains((HassiumTypeDefinition) vm.getGlobals().get(param.getKey().getType())))
                    HassiumLogger.error(String.format("Expected type %1s to %2s, instead got %3s!", param.getKey().getType(), param.getKey().getName(), argument.type().toString(vm)));
            }

            vm.getStackFrame().add(param.getValue(), argument);
        }
        HassiumObject returnValue = vm.executeMethod(this);
        if (ReturnType != "") {
            if (returnValue.type() != vm.getGlobals().get(ReturnType))
                HassiumLogger.error(String.format("Expected return type of %1s. Instead got %2s!", ReturnType, returnValue.type().toString(vm)));
        }
        if (name != "__lambda__" && name != "__catch__")
            vm.getStackFrame().popFrame();

        if (isConstructor()) {
            HassiumClass ret = new HassiumClass();
            ret.Attributes = cloneDictionary(Parent.Attributes);
            ret.addType(Parent.TypeDefinition);
            ret.Attributes.values().stream().filter(obj -> obj instanceof MethodBuilder).forEach(obj -> ((MethodBuilder) obj).Parent = ret);
            return ret;
        }
        vm.getCallStack().pop();
        return returnValue;
    }

    public static <TKey, TValue extends ICloneable> HashMap<TKey, TValue> cloneDictionary(HashMap<TKey, TValue> original) {
        HashMap<TKey, TValue> ret = new HashMap<>(original.size());
        for (Map.Entry<TKey, TValue> entry : original.entrySet()) {
            ret.put(entry.getKey(), (TValue) entry.getValue().clone());
        }
        return ret;
    }

    public void emit(SourceLocation location, InstructionType instructionType) {
        emit(location, instructionType, 0);
    }

    public void emit(SourceLocation location, InstructionType instructionType, double value) {
        Instructions.add(new Instruction(instructionType, value, location));
    }
}
