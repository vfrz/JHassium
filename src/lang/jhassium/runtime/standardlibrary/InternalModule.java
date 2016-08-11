package lang.jhassium.runtime.standardlibrary;

import java.util.ArrayList;
import java.util.List;

/**
 * File : InternalModule.java
 * Description : None
 * Authors : FRITZ Valentin, Jacob Misirian & zdimension
 * Website : https://github.com/vfrz/JHassium
 * Date : 11/08/2016 17:03
 */
public class InternalModule extends HassiumObject {

    public static List<InternalModule> InternalModules = new ArrayList<InternalModule>() {{
        //TODO Add internal modules
    }};
    private String name;

    public InternalModule(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
