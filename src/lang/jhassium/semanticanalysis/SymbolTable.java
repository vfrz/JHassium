package lang.jhassium.semanticanalysis;

import java.util.Stack;

/**
 * File : SymbolTable.java
 * Description : None
 * Author : FRITZ Valentin
 * Website : https://github.com/vfrz/JHassium
 * Date : 10/08/2016 22:35
 */
public class SymbolTable {

    private Stack<Scope> scopes = new Stack<>();
    private Scope globalScope = new Scope();

    public int NextIndex = 0;
    private int nextGlobalIndex = 0;

    public SymbolTable() {
        scopes.push(globalScope);
    }

    public boolean inGlobalScope() {
        return scopes.peek() == globalScope;
    }

    public void enterScope() {
        scopes.push(new Scope());
    }

    public void popScope() {
        scopes.pop();
        if (scopes.size() == 2)
            NextIndex = 0;
    }

    public int getIndex(String name) {
        for (Scope scope : scopes) {
            if (scope.findSymbol(name))
                return scope.getSymbol(name);
        }
        return -1;
    }

    public int getGlobalIndex(String name) {
        return globalScope.getSymbol(name);
    }

    public boolean findSymbol(String name) {
        for (Scope scope : scopes) {
            if (scope.findSymbol(name))
                return true;
        }
        return false;
    }

    public boolean findGlobalSymbol(String name) {
        return globalScope.findSymbol(name);
    }

    public int addSymbol(String name) {
        scopes.peek().addSymbol(name, NextIndex);
        return NextIndex++;
    }

    public int addGlobalSymbol(String name) {
        globalScope.addSymbol(name, nextGlobalIndex);
        return nextGlobalIndex++;
    }
}
