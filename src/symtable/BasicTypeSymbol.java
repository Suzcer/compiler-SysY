package symtable;

import TypeSys.BasicType;
import TypeSys.Type;

/**
 * 既是一个类型，也是一个符号 int double void, 但不包括 int a
 * 不使用父类的Type，只使用自己的basicType
 */
public class BasicTypeSymbol extends BaseSymbol implements Type {

    private BasicType basicType;

    public BasicTypeSymbol(String name,BasicType basicType) {
        super(name, null);
        this.basicType=basicType;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof BasicTypeSymbol basicTypeSymbol){
            return this.basicType.getSimpleType().equals(basicTypeSymbol.basicType.getSimpleType());
        }
        return false;
    }

    public Type getBasicType() {
        return basicType;
    }

    public void setBasicType(BasicType basicType) {
        this.basicType = basicType;
    }
}
