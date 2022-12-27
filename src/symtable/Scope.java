package symtable;


import org.bytedeco.llvm.LLVM.LLVMValueRef;

public interface Scope {


    public void putScope(String name,Scope scope);

    public void putValueRef(String name, LLVMValueRef valueRef);

    public LLVMValueRef getValueRef(String name);
    public Scope getSubScope(String name) ;
    public String getName();

    public void setName(String name);

    /** 获取父作用域 **/
    public Scope getEnclosingScope();



}
