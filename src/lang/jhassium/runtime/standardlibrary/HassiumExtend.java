package lang.jhassium.runtime.standardlibrary;

import lang.jhassium.codegen.MethodBuilder;

/**
 * File : HassiumExtend.java
 * Description : None
 * Author : FRITZ Valentin
 * Website : https://github.com/vfrz/JHassium
 * Date : 11/08/2016 17:11
 */
public class HassiumExtend extends HassiumObject {

    private MethodBuilder target;
    private HassiumClass additions;

    public MethodBuilder getTarget() {
        return target;
    }

    public HassiumClass getAdditions() {
        return additions;
    }

    public HassiumExtend(MethodBuilder target, HassiumClass additions) {
        this.target = target;
        this.additions = additions;
    }
}
