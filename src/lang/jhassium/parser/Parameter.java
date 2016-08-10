package lang.jhassium.parser;

/**
 * File : Parameter.java
 * Description : None
 * Author : FRITZ Valentin
 * Website : https://github.com/vfrz/JHassium
 * Date : 10/08/2016 22:02
 */
public class Parameter {
    private boolean isEnforced;

    private String name;
    private String type;

    public Parameter(String name) {
        this(name, null);
    }

    public Parameter(String name, String type) {
        isEnforced = true;
        this.name = name;
        this.type = type;
    }

    public boolean isEnforced() {
        return isEnforced;
    }

    public String toString() {
        return this.type == null ? this.name : String.format("%1s : %2s", this.name, this.type);
    }
}