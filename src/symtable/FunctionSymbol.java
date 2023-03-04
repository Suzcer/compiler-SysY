package symtable;

import TypeSys.FunctionType;
import TypeSys.Type;

public class FunctionSymbol extends BaseScope implements Symbol {

    private FunctionType funcType;

//    private String name;      //name 在BasicScope 中存在


    public void setFuncType(FunctionType funcType) {
        this.funcType = funcType;
    }

    @Override
    public Type getType() {
        return funcType;
    }

    public FunctionSymbol(String name, Scope enclosingScope) {
        super(name, enclosingScope);
    }
}