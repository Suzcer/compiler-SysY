package symtable;


import org.bytedeco.llvm.LLVM.LLVMValueRef;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class BaseScope implements Scope {
    private final Scope enclosingScope;

    private Map<String, LLVMValueRef> valueRefs = new HashMap<>();
    private String name;

    private boolean isBuildRet = false;

    private final Map<String, Scope> subScope = new LinkedHashMap<>();

    public BaseScope(String name, Scope enclosingScope) {
        this.name = name;
        this.enclosingScope = enclosingScope;
    }

    @Override
    public void setIsBuildRet() {
        isBuildRet=true;
    }

    @Override
    public boolean getIsBuildRet() {
        return isBuildRet;
    }

    @Override
    public void putValueRef(String name, LLVMValueRef valueRef) {
        valueRefs.put(name, valueRef);
    }

    @Override
    public LLVMValueRef resolveValueRef(String name) {
        LLVMValueRef ret = valueRefs.get(name);
        if (ret != null)
            return ret;

        if (enclosingScope != null)
            return enclosingScope.resolveValueRef(name);

        return null;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void putScope(String name, Scope scope) {
        this.subScope.put(name, scope);
    }

    @Override
    public Scope getSubScope(String name) {
        return subScope.get(name);
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Scope getEnclosingScope() {
        return this.enclosingScope;
    }

    @Override
    public String toString() {
        return name;
    }

}
