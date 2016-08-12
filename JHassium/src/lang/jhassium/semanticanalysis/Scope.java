package lang.jhassium.semanticanalysis;

import java.util.LinkedHashMap;

/**
 * File : Scope.java
 * Description : None
 * Authors : FRITZ Valentin, Jacob Misirian & zdimension
 * Website : https://github.com/vfrz/JHassium
 * Date : 10/08/2016 22:34
 */
public class Scope {

    private LinkedHashMap<String, Integer> symbols = new LinkedHashMap<>();

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
