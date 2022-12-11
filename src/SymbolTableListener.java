import TypeSys.FunctionType;
import TypeSys.Type;
import symtable.*;

import java.util.ArrayList;
import java.util.List;

public class SymbolTableListener extends SysYParserBaseListener {

    private GlobalScope globalScope = null;
    private Scope currentScope = null;

    private int localScopeCounter = 0;

    private void report(int errType, int lineNo) {
        System.err.println("Error type " + errType + " at Line " + lineNo + ":");
    }

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

        //1. 构建functionType, 获取 retType 和 paramsType
        String typeName = ctx.funcType().getText();
        Type retType = (Type) globalScope.resolve(typeName);//解析类型名，要去全局的作用域解析
        SysYParser.FuncFParamsContext funcFParamsCtx = ctx.funcFParams();
        ArrayList<Type> paramsType = new ArrayList<>();
        if (funcFParamsCtx != null) {       // 如果有参数
            List<SysYParser.FuncFParamContext> funcFParamCtx = funcFParamsCtx.funcFParam();
            for (SysYParser.FuncFParamContext paramCtx : funcFParamCtx) {
                String paramTypeName = paramCtx.bType().getText();
                Type paramType = (Type) globalScope.resolve(paramTypeName);     // 假设参数只有基本类型
                paramsType.add(paramType);
            }
        }
        FunctionType ft = new FunctionType(retType, paramsType);

        //2. 报告函数重定义错误
        String funName = ctx.IDENT().getText();
        Symbol tmp = currentScope.resolve(funName);
        if (tmp != null) report(4, ctx.IDENT().getSymbol().getLine());

        //3. 构建 FunctionSymbol，设置 funcType
        FunctionSymbol fun = new FunctionSymbol(funName, currentScope);
        fun.setFuncType(ft);
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
        for (SysYParser.VarDefContext var_ctx : varDefContexts) {
            String varName = var_ctx.IDENT().getText();
            Symbol tmp = currentScope.getSymbols().get(varName);
            if (tmp != null) report(3, var_ctx.IDENT().getSymbol().getLine());
            else {
                VariableSymbol symbol = new VariableSymbol(varName, type);
                currentScope.define(symbol);
            }
        }
    }

    @Override
    public void enterConstDecl(SysYParser.ConstDeclContext ctx) {
        String typeName = ctx.bType().getText();
        Type type = (Type) globalScope.resolve(typeName);
        List<SysYParser.ConstDefContext> constDefContexts = ctx.constDef();
        for (SysYParser.ConstDefContext const_ctx : constDefContexts) {
            String varName = const_ctx.IDENT().getText();
            Symbol tmp = currentScope.resolve(varName);
            if (tmp != null) report(3, const_ctx.IDENT().getSymbol().getLine());
            else {
                VariableSymbol symbol = new VariableSymbol(varName, type);
                currentScope.define(symbol);
            }
        }
    }

    @Override
    public void enterLVal(SysYParser.LValContext ctx) {
        String varName = ctx.IDENT().getText();
        Symbol symbol = currentScope.resolve(varName);
        if (symbol == null)
            report(1, ctx.IDENT().getSymbol().getLine());
        else if(symbol instanceof FunctionSymbol)
            report(11,ctx.IDENT().getSymbol().getLine());
    }


    @Override
    public void enterCallFuncExp(SysYParser.CallFuncExpContext ctx) {
        String varName = ctx.IDENT().getText();
        Symbol symbol = currentScope.resolve(varName);
        if (symbol == null)
            report(2, ctx.IDENT().getSymbol().getLine());
            //else 检查是否为变量的symbol而不是函数的symbol
        else {
            if (!(symbol instanceof FunctionSymbol))
                report(10, ctx.IDENT().getSymbol().getLine());
        }
    }

    /**
     * 函数形参
     **/
    @Override
    public void exitFuncFParam(SysYParser.FuncFParamContext ctx) {
        String typeName = ctx.bType().getText();
        Type type = (Type) globalScope.resolve(typeName);

        String varName = ctx.IDENT().getText();
        VariableSymbol symbol = new VariableSymbol(varName, type);
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