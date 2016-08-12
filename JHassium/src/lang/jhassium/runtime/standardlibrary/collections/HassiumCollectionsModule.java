package lang.jhassium.runtime.standardlibrary.collections;

import lang.jhassium.runtime.standardlibrary.InternalModule;

/**
 * File : HassiumCollectionsModule.java
 * Description : None
 * Authors : FRITZ Valentin, Jacob Misirian & zdimension
 * Website : https://github.com/vfrz/JHassium
 * Date : 12/08/2016 11:57
 */
public class HassiumCollectionsModule extends InternalModule {

    public HassiumCollectionsModule() {
        super("Collections");
        Attributes.put("Stack", new HassiumStack());
    }
}
