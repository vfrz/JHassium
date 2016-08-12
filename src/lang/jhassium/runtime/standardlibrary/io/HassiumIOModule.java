package lang.jhassium.runtime.standardlibrary.io;

import lang.jhassium.runtime.standardlibrary.InternalModule;

/**
 * File : HassiumIOModule.java
 * Description : None
 * Authors : FRITZ Valentin, Jacob Misirian & zdimension
 * Website : https://github.com/vfrz/JHassium
 * Date : 12/08/2016 13:06
 */
public class HassiumIOModule extends InternalModule {

    public HassiumIOModule() {
        super("IO");
        Attributes.put("File", new HassiumFile());
    }

}
