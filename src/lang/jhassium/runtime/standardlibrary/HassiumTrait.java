package lang.jhassium.runtime.standardlibrary;

import lang.jhassium.parser.Trait;
import lang.jhassium.runtime.HassiumTypeDefinition;
import lang.jhassium.runtime.VirtualMachine;
import lang.jhassium.utils.Helpers;

import java.util.List;

/**
 * File : HassiumTrait.java
 * Description : None
 * Author : FRITZ Valentin
 * Website : https://github.com/vfrz/JHassium
 * Date : 11/08/2016 17:15
 */
public class HassiumTrait extends HassiumObject {

    public HassiumTypeDefinition TypeDefinition;

    private List<Trait> traits;

    public List<Trait> getTraits() {
        return traits;
    }

    public HassiumTrait(List<Trait> traits) {
        this.traits = traits;
    }

    public boolean MatchesTrait(VirtualMachine vm, HassiumObject obj) {
        for (Trait trait : traits) {
            HassiumTypeDefinition type = Helpers.as(vm.getGlobals().get(trait.getType()), HassiumTypeDefinition.class);
            if (!obj.Attributes.containsKey(trait.getName()))
                return false;
            if (!(obj.Attributes.get(trait.getName()).type() == type))
                return false;
        }
        return true;
    }
}
