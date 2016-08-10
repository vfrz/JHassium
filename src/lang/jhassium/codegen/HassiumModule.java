package lang.jhassium.codegen;

import lang.jhassium.runtime.standardlibrary.HassiumObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * File : HassiumModule.java
 * Description : None
 * Author : FRITZ Valentin
 * Website : https://github.com/vfrz/JHassium
 * Date : 10/08/2016 22:39
 */
public class HassiumModule extends HassiumObject {

    private String name;
    public List<HassiumObject> ConstantPool;
    public HashMap<Integer, HassiumObject> Globals;
    public List<String> Imports;

    public HassiumModule(String name) {
        this.name = name;
        ConstantPool = new ArrayList<>();
        Globals = new HashMap<>();
        Imports = new ArrayList<>();
    }

    public String getName() {
        return name;
    }
}
