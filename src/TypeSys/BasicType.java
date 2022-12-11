package TypeSys;

/**
 *   如果是int，就是一个BasicTypeSymbol，其name和其type一一对应。
 *   如果是int a，就是一个 VariableSymbol，其name和其type不对应。
 */
public class BasicType implements Type{

    private SimpleType simpleType;

    public SimpleType getSimpleType() {
        return simpleType;
    }

    public void setSimpleType(SimpleType simpleType) {
        this.simpleType = simpleType;
    }

    public BasicType(SimpleType simpleType){
        this.simpleType=simpleType;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof BasicType objType){
            return objType.getSimpleType().equals(this.getSimpleType());
        }
        return false;
    }
}
