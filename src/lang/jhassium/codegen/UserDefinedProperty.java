package lang.jhassium.codegen;

import lang.jhassium.runtime.standardlibrary.HassiumObject;

/**
 * File : UserDefinedProperty.java
 * Description : None
 * Author : FRITZ Valentin
 * Website : https://github.com/vfrz/JHassium
 * Date : 11/08/2016 17:13
 */
public class UserDefinedProperty extends HassiumObject {

    private String name;

    public MethodBuilder GetMethod;
    public MethodBuilder SetMethod;

    public String getName() {
        return name;
    }

    public UserDefinedProperty(String name) {
        this.name = name;
    }
}
