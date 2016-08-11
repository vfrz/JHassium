package lang.jhassium.codegen;

import lang.jhassium.runtime.standardlibrary.HassiumObject;

/**
 * File : UserDefinedProperty.java
 * Description : None
 * Authors : FRITZ Valentin, Jacob Misirian & zdimension
 * Website : https://github.com/vfrz/JHassium
 * Date : 11/08/2016 17:13
 */
public class UserDefinedProperty extends HassiumObject {

    public MethodBuilder GetMethod;
    public MethodBuilder SetMethod;
    private String name;

    public UserDefinedProperty(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
