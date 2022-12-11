package symtable;

import TypeSys.BasicType;
import TypeSys.SimpleType;

public class GlobalScope extends BaseScope {
    public GlobalScope(Scope enclosingScope) {
        super("GlobalScope", enclosingScope);
        define(new BasicTypeSymbol("int", new BasicType(SimpleType.INT)));
        define(new BasicTypeSymbol("double",new BasicType(SimpleType.DOUBLE)));
        define(new BasicTypeSymbol("void",new BasicType(SimpleType.VOID)));
    }


}