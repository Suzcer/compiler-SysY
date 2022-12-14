package symtable;

import TypeSys.Type;

import java.util.Map;

public interface Scope {


    public void putScope(String name,Scope scope);
    public Scope getSubScope(String name) ;
    public String getName();

    public void setName(String name);

    /** 获取父作用域 **/
    public Scope getEnclosingScope();

    public Map<String, Symbol> getSymbols();

    /** 把新碰到的变量放进符号表  **/
    public void define(Symbol symbol);

    /** 比如a=b；这条语句，要找b具体指的是哪个b，即解析 **/
    public Symbol resolve(String name);

    /** 全局函数和局部变量之间可能重名，导致无法解析全局函数 **/
    public Symbol resolveGlobalFun(String name);

    /** 解决冲突问题  **/
    public Type resolveType(String name);

    /** 找到最根本的作用域 **/
    public Scope findScope(String name);
}
