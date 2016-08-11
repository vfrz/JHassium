package lang.jhassium.runtime;

import lang.jhassium.runtime.standardlibrary.HassiumObject;

import java.util.HashMap;

/**
 * File : Frame.java
 * Description : None
 * Author : FRITZ Valentin
 * Website : https://github.com/vfrz/JHassium
 * Date : 11/08/2016 13:29
 */
public class Frame {

    public HashMap<Integer, HassiumObject> variables = new HashMap<>();

    public void add(int index, HassiumObject value) {
        variables.put(index, value);
    }

    public boolean containsVariable(int index) {
        return variables.containsKey(index);
    }

    public void modify(int index, HassiumObject value) {
        variables.put(index, value);
    }

    public HassiumObject getVariable(int index) {
        return variables.get(index);
    }
}