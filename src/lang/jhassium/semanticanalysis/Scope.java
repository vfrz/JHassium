package lang.jhassium.semanticanalysis;

import java.util.HashMap;

/**
 * File : Scope.java
 * Description : None
 * Author : FRITZ Valentin
 * Website : https://github.com/vfrz/JHassium
 * Date : 10/08/2016 22:34
 */
public class Scope {

    private HashMap<String, Integer> symbols = new HashMap<>();

    public int getSymbol(String name) {
        return symbols.get(name);
    }

    public boolean findSymbol(String name) {
        return symbols.containsKey(name);
    }

    public void addSymbol(String name, int index) {
        symbols.put(name, index);
    }
}
