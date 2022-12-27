package symtable;

import TypeSys.Type;

public class FunctionSymbol extends BaseScope implements Symbol {

//    private FunctionType funcType;

    @Override
    public Type getType() {
        return null;
    }

    public FunctionSymbol(String name, Scope enclosingScope) {
        super(name, enclosingScope);
    }
}