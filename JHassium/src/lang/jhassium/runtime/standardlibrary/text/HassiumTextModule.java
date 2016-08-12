package lang.jhassium.runtime.standardlibrary.text;

import lang.jhassium.runtime.standardlibrary.InternalModule;

/**
 * File : HassiumTextModule.java
 * Description : None
 * Authors : FRITZ Valentin, Jacob Misirian & zdimension
 * Website : https://github.com/vfrz/JHassium
 * Date : 12/08/2016 00:47
 */
public class HassiumTextModule extends InternalModule {

    public HassiumTextModule() {
        super("Text");
        Attributes.put("StringBuilder", new HassiumStringBuilder());
    }
}
