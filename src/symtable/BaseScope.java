package symtable;


import TypeSys.Type;

import java.util.LinkedHashMap;
import java.util.Map;

public class BaseScope implements Scope {
    private final Scope enclosingScope;
    private final Map<String, Symbol> symbols = new LinkedHashMap<>();
    private String name;

    private final Map<String,Scope> subScope=new LinkedHashMap<>();

    public BaseScope(String name, Scope enclosingScope) {
        this.name = name;
        this.enclosingScope = enclosingScope;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void putScope(String name,Scope scope) {
        this.subScope.put(name, scope);
    }

    @Override
    public Scope getSubScope(String name) {
        return subScope.get(name);
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Scope getEnclosingScope() {
        return this.enclosingScope;
    }

    public Map<String, Symbol> getSymbols() {
        return this.symbols;
    }

    @Override
    public void define(Symbol symbol) {
        symbols.put(symbol.getName(), symbol);
//        System.out.println("+" + symbol.getName());
    }

    @Override
    public Symbol resolve(String name) {
        Symbol symbol = symbols.get(name);
        if (symbol != null) {
//            System.out.println("*" + name);
            return symbol;
        }

        if (enclosingScope != null) {
            return enclosingScope.resolve(name);
        }

        return null;
    }

    @Override
    public Type resolveType(String name) {
        Symbol symbol = resolve(name);
        if(symbol instanceof BaseSymbol){
            return ((BaseSymbol) symbol).getType();
        }else{
            return (Type) symbol;
        }
    }


    @Override
    public Scope findScope(String name) {
        Symbol symbol = symbols.get(name);
        if (symbol != null) {
            return this;
        }
        if (enclosingScope != null) {
            return enclosingScope.findScope(name);
        }
        if(this instanceof GlobalScope)
            return this;
        return null;
    }
}
