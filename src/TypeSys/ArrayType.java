package TypeSys;

public class ArrayType implements Type{
    private Type subType;
    private int count;

    public Type getSubType() {
        return subType;
    }

    public int getCount() {
        return count;
    }

    public ArrayType(Type subType,int count){
        this.count=count;
        this.subType=subType;
    }


}
