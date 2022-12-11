package symtable;

import TypeSys.Type;

/**
 * 既是一个类型，也是一个符号 int double void, 但不包括 int a
 * 不使用父类的Type，只使用自己的basicType
 */
public class BasicTypeSymbol extends BaseSymbol implements Type {


    public BasicTypeSymbol(String name, Type basicType) {
        super(name, basicType);
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof BasicTypeSymbol) {
            BasicTypeSymbol sym = (BasicTypeSymbol) obj;
            return sym.getType().equals(this.getType());
        }
        return false;
    }

}
