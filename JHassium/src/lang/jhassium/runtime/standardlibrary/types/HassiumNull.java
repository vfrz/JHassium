package lang.jhassium.runtime.standardlibrary.types;

import lang.jhassium.runtime.HassiumTypeDefinition;
import lang.jhassium.runtime.standardlibrary.HassiumObject;

/**
 * File : HassiumNull.java
 * Description : None
 * Authors : FRITZ Valentin, Jacob Misirian & zdimension
 * Website : https://github.com/vfrz/JHassium
 * Date : 11/08/2016 00:03
 */
public class HassiumNull extends HassiumObject {

    public HassiumNull() {
        Types.add(new HassiumTypeDefinition("null"));
    }
}
