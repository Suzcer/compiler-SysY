package TypeSys;

/**
 * int a[10]; -> dimension=1,subType=BasicType,count=10;
 *
 * double b[2][3]; -> dimension=2,subType=ArrayType,count=2
 *                    dimension=1,subType=double,count=3
 */
public class ArrayType implements Type{

    private int dimension;
    private Type subType;
    private int count;

    public Type getSubType() {
        return subType;
    }

    public int getCount() {
        return count;
    }

    public int getDimension(){
        return dimension;
    }

    public ArrayType(Type subType,int count,int dimension){
        this.count=count;
        this.subType=subType;
        this.dimension=dimension;
    }

    @Override
    public String toString() {
        return "subType:"+ subType.toString()+"  ;dimension:"+dimension;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof ArrayType){
            ArrayType arrayType = (ArrayType) obj;
            return arrayType.dimension==this.dimension;
        }
        return false;
    }
}
