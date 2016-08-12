package lang.jhassium.codegen;

import lang.jhassium.runtime.standardlibrary.HassiumObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * File : HassiumModule.java
 * Description : None
 * Authors : FRITZ Valentin, Jacob Misirian & zdimension
 * Website : https://github.com/vfrz/JHassium
 * Date : 10/08/2016 22:39
 */
public class HassiumModule extends HassiumObject {

    public List<HassiumObject> ConstantPool;
    public LinkedHashMap<Integer, HassiumObject> Globals;
    public List<String> Imports;
    private String name;

    public HassiumModule(String name) {
        this.name = name;
        ConstantPool = new ArrayList<>();
        Globals = new LinkedHashMap<>();
        Imports = new ArrayList<>();
    }

    public String getName() {
        return name;
    }
}
