package symtable;

import TypeSys.Type;

public class VariableSymbol extends BaseSymbol {
    public VariableSymbol(String name, Type type) {
        super(name, type);
    }
}
