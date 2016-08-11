package lang.jhassium.runtime.standardlibrary.math;

import lang.jhassium.runtime.standardlibrary.InternalModule;

/**
 * File : HassiumMathModule.java
 * Description : None
 * Authors : FRITZ Valentin, Jacob Misirian & zdimension
 * Website : https://github.com/vfrz/JHassium
 * Date : 12/08/2016 01:02
 */
public class HassiumMathModule extends InternalModule {

    public HassiumMathModule() {
        super("Math");
        Attributes.put("Math", new HassiumMath());
        Attributes.put("Random", new HassiumRandom());
    }
}
