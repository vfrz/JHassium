package lang.jhassium.runtime;

import lang.jhassium.runtime.standardlibrary.HassiumObject;
import lang.jhassium.utils.HassiumLogger;

import java.util.LinkedHashMap;
import java.util.Stack;

/**
 * File : StackFrame.java
 * Description : None
 * Authors : FRITZ Valentin, Jacob Misirian & zdimension
 * Website : https://github.com/vfrz/JHassium
 * Date : 11/08/2016 13:30
 */
public class StackFrame {

    public Stack<Frame> Frames;

    public LinkedHashMap<Integer, HassiumObject> getLocals() {
        return Frames.peek().variables;
    }

    public StackFrame() {
        Frames = new Stack<Frame>();
    }

    public void enterFrame() {
        Frames.push(new Frame());
    }

    public void popFrame() {
        Frames.pop();
    }

    public void add(int index) {
        add(index, null);
    }

    public void add(int index, HassiumObject value) {
        if (Frames.peek().containsVariable(index))
            Frames.peek().variables.remove(index);
        Frames.peek().add(index, value);
    }

    public boolean contains(int index) {
        for (Frame frame : Frames) {
            if (frame.containsVariable(index))
                return true;
        }
        return false;
    }

    public void modify(int index, HassiumObject value) {
        Frames.peek().modify(index, value);
    }

    public HassiumObject GetVariable(int index) {
        if (Frames.peek().containsVariable(index))
            return Frames.peek().getVariable(index);
        HassiumLogger.error("Variable was not found inside the stack frame! Index " + index);
        return null; //Not reachable but for compilation
    }
}
