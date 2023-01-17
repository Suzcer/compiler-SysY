package symtable;


import org.bytedeco.llvm.LLVM.LLVMValueRef;

public interface Scope {


    public void putScope(String name,Scope scope);

    public void define(String name, LLVMValueRef valueRef);

    public void putPointer(String name,boolean flag);

    public boolean getPointer(String name);

    public boolean getArrays(String name);

    public void putConst(String name,int i);

    public int getConst(String name);

    public LLVMValueRef resolve(String name);

    public void setIsBuildRet();

    public boolean getIsBuildRet();

    public void setCurFunction(LLVMValueRef ref);

    public LLVMValueRef getCurFunction();
    public String getName();

    public void setName(String name);

    /** 获取父作用域 **/
    public Scope getEnclosingScope();



}
