package TypeSys;

import java.util.ArrayList;

public class FunctionType implements Type{
    private Type retType;
    private ArrayList<Type> paramsType;

    public Type getRetType() {
        return retType;
    }

    public ArrayList<Type> getParamsType() {
        return paramsType;
    }

    public FunctionType(Type retType,ArrayList<Type> paramsType){
        this.retType=retType;
        this.paramsType=paramsType;
    }
}
