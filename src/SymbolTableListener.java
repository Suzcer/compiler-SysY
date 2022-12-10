import TypeSys.Type;
import symtable.*;

import java.util.List;

public class SymbolTableListener extends SysYParserBaseListener {

    private GlobalScope globalScope = null;
    private Scope currentScope = null;

    private int localScopeCounter = 0;

    /**
     * 1. 开启作用域
     */
    @Override
    public void enterProgram(SysYParser.ProgramContext ctx) {
        globalScope = new GlobalScope(null);
        currentScope = globalScope;
    }

    @Override
    public void enterFuncDef(SysYParser.FuncDefContext ctx) {

        String typeName = ctx.funcType().getText();
        globalScope.resolve(typeName);//解析类型名，要去全局的作用域解析

        String funName = ctx.IDENT().getText();
        FunctionSymbol fun = new FunctionSymbol(funName, currentScope);

        currentScope.define(fun);
        currentScope = fun;
    }

    // block & blockStmt 应该一致
    @Override
    public void enterBlock(SysYParser.BlockContext ctx) {
        LocalScope localScope = new LocalScope(currentScope);
        String localScopeName = localScope.getName() + localScopeCounter;
        localScope.setName(localScopeName);
        localScopeCounter++;

        currentScope = localScope;
    }

    /**
     * 2. 退出作用域
     */
    @Override
    public void exitProgram(SysYParser.ProgramContext ctx) {
        currentScope = currentScope.getEnclosingScope();
    }

    @Override
    public void exitFuncDef(SysYParser.FuncDefContext ctx) {
        currentScope = currentScope.getEnclosingScope();
    }

    @Override
    public void exitBlock(SysYParser.BlockContext ctx) {
        currentScope = currentScope.getEnclosingScope();
    }

    /**
     * 3. 定义 symbol
     */
    @Override
    public void exitVarDecl(SysYParser.VarDeclContext ctx) {
        String typeName = ctx.bType().getText();
        Type type = (Type) globalScope.resolve(typeName);
        List<SysYParser.VarDefContext> varDefContexts = ctx.varDef();
        for(SysYParser.VarDefContext var_ctx:varDefContexts){
            VariableSymbol symbol = new VariableSymbol(var_ctx.IDENT().getText(), type);
            currentScope.define(symbol);
        }
    }

    @Override
    public void exitLVal(SysYParser.LValContext ctx) {
        String varName = ctx.IDENT().getText();
        Symbol symbol = currentScope.resolve(varName);
        if(symbol==null)
            System.err.println("Error type 1 at Line "+ctx.IDENT().getSymbol().getLine()+":");
    }

    /** 函数形参 **/
    @Override
    public void exitFuncFParam(SysYParser.FuncFParamContext ctx) {
        String typeName = ctx.bType().getText();
        Type type = (Type) globalScope.resolve(typeName);

        String varName = ctx.IDENT().getText();
        VariableSymbol symbol=new VariableSymbol(varName,type);
        currentScope.define(symbol);
    }

    /**
     * 4. 解析变量
     */

    /**
     * 5. 什么时候加入节点
     */
    /**
     * 6. 什么时候加入边
     */

}