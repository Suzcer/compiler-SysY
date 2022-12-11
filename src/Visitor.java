import TypeSys.*;
import org.antlr.v4.runtime.Vocabulary;
import symtable.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Visitor<T> extends SysYParserBaseVisitor<T> {

    public String[] ruleNames;

    public Vocabulary vocabulary;

    public HashMap<String, String> mp;

    private GlobalScope globalScope = null;

    private Scope currentScope = null;

    private int localScopeCounter = 0;

    private Type currentRetType;

    private void report(int errType, int lineNo) {
        System.err.println("Error type " + errType + " at Line " + lineNo + ":");
    }

    public void setRulesName(String[] ruleNames) {
        this.ruleNames = ruleNames;
    }

    public void setVocabulary(Vocabulary vocabulary) {
        this.vocabulary = vocabulary;
    }

    public void setMp(HashMap<String, String> mp) {
        this.mp = mp;
    }


    private static String captureName(String name) {
        name = name.substring(0, 1).toUpperCase() + name.substring(1);
        return name;
    }

    private String baseTrans(String text) {
        if (text.charAt(0) == '0' && text.length() >= 2) {
            int i;
            if (text.charAt(1) == 'x' || text.charAt(1) == 'X') {
                i = Integer.parseInt(text.substring(2), 16);
            } else {
                i = Integer.parseInt(text, 8);
            }
            return String.valueOf(i);
        } else {
            return text;
        }
    }

    //实现语法树的打印
//    @Override
//    public T visitChildren(RuleNode node) {
//        String ruleName = ruleNames[node.getRuleContext().getRuleIndex()];
//        for (int i = 0; i + 1 < node.getRuleContext().depth(); i++) System.err.print("  ");
//        System.err.println(captureName(ruleName));
//        return super.visitChildren(node);
//    }
//
//    //打印终结符及终结符的高亮
//    @Override
//    public T visitTerminal(TerminalNode node) {
//
//        String type = vocabulary.getSymbolicName(node.getSymbol().getType());
//        if (mp.containsKey(type)) {
//            RuleNode parent;
//            if (node.getParent() instanceof RuleNode) {
//                parent = (RuleNode) node.getParent();
//                for (int i = 0; i < parent.getRuleContext().depth(); i++)
//                    System.err.print("  ");
//            }
//
//            System.err.print(baseTrans(node.getText()) + " ");
//            System.err.print(type);
//            System.err.println("[" + mp.get(type) + "]");
//        }
//
//        return super.visitTerminal(node);
//    }

    @Override
    public T visitProgram(SysYParser.ProgramContext ctx) {
        globalScope = new GlobalScope(null);
        currentScope = globalScope;
        SysYParser.CompUnitContext compUnitCtx = ctx.compUnit();
        if (compUnitCtx != null)
            visitCompUnit(compUnitCtx);
        currentScope = currentScope.getEnclosingScope();
        return null;
    }

    @Override
    public T visitCompUnit(SysYParser.CompUnitContext ctx) {
        return super.visitCompUnit(ctx);
    }

    @Override
    public T visitDecl(SysYParser.DeclContext ctx) {
        return super.visitDecl(ctx);
    }

    @Override
    public T visitVarDecl(SysYParser.VarDeclContext ctx) {

        String typeName = ctx.bType().getText();
        Type type = globalScope.resolveType(typeName);              // TODO 涉及到 globalScope，需要考虑是否能够解析出来
        List<SysYParser.VarDefContext> varDefContexts = ctx.varDef();
        for (SysYParser.VarDefContext varDefCtx : varDefContexts) {
            visitVarDef(varDefCtx);
            String varName = varDefCtx.IDENT().getText();
            Symbol tmp = currentScope.getSymbols().get(varName);        // 如果和当前作用域的重名了，才需要进行错误报告
            if (tmp != null) report(3, varDefCtx.IDENT().getSymbol().getLine());
            else {
                // 确定是数组还是VariableSymbol
                if (varDefCtx.L_BRACKT().isEmpty()) {
                    VariableSymbol symbol = new VariableSymbol(varName, type);
                    currentScope.define(symbol);
                } else {
                    // int a[1][9];
                    int dimensions = varDefCtx.L_BRACKT().size();
                    BasicType mostInner = (BasicType) type;
                    ArrayType ptr = new ArrayType(mostInner,1,1);// lab3的count不予考虑
                    int cnt = 2;
                    while (cnt <= dimensions) {
                        ptr = new ArrayType(ptr, 1, cnt);
                        cnt++;
                    }
                    ArraySymbol symbol = new ArraySymbol(varName, ptr);
                    currentScope.define(symbol);
                }
                // 保证初始化是正确的，因此这里不予处理
            }
        }
        return null;
    }

    @Override
    public T visitConstDecl(SysYParser.ConstDeclContext ctx) {
        String typeName = ctx.bType().getText();
        Type type = globalScope.resolveType(typeName);
        List<SysYParser.ConstDefContext> constDefContexts = ctx.constDef();
        for (SysYParser.ConstDefContext const_ctx : constDefContexts) {
            String varName = const_ctx.IDENT().getText();
            Symbol tmp = currentScope.getSymbols().get(varName);
            if (tmp != null) report(3, const_ctx.IDENT().getSymbol().getLine());
            else {
                VariableSymbol symbol = new VariableSymbol(varName, type);
                currentScope.define(symbol);
            }
        }
        return super.visitConstDecl(ctx);
    }

    @Override
    public T visitVarDef(SysYParser.VarDefContext ctx) {
        super.visitVarDef(ctx);
        return null;
    }

    @Override
    public T visitConstExp(SysYParser.ConstExpContext ctx) {
        SysYParser.ExpContext expCtx = ctx.exp();
        if (expCtx != null)
            this.visit(expCtx);
        return null;
    }

    @Override
    public T visitPARENS(SysYParser.PARENSContext ctx) {
        return super.visitPARENS(ctx);
    }

    @Override
    public T visitLvalExp(SysYParser.LvalExpContext ctx) {
        return super.visitLvalExp(ctx);
    }

    @Override
    public T visitLVal(SysYParser.LValContext ctx) {
        String varName = ctx.IDENT().getText();
        Symbol symbol = currentScope.resolve(varName);
        List<SysYParser.ExpContext> expCtxs = ctx.exp();

        if (symbol == null)
            report(1, ctx.IDENT().getSymbol().getLine());
        else if ((symbol instanceof VariableSymbol || symbol instanceof FunctionSymbol) && !expCtxs.isEmpty())
            report(9, ctx.IDENT().getSymbol().getLine());
        super.visitLVal(ctx);

        if (symbol instanceof ArraySymbol) {
            int cnt = ctx.L_BRACKT().size();
            ArraySymbol arraySymbol = (ArraySymbol) symbol;
            Type ptr = arraySymbol.getType();
            if (ptr instanceof ArrayType) {
                while (cnt-- > 0) {
                    if(ptr instanceof ArrayType)
                        ptr = ((ArrayType) ptr).getSubType();
                    else if (ptr instanceof BasicType) {
                        ptr = (BasicType) ptr;      //其实如果不是 ArrayType 则无需任何操作
                    }else{
                        report(9,ctx.IDENT().getSymbol().getLine());
                    }
                }
                return (T) ptr;
            }
        }
        if(symbol!=null) return (T) symbol.getType();
        return null;
    }

    @Override
    public T visitNumberExp(SysYParser.NumberExpContext ctx) {
        super.visitNumberExp(ctx);
        return (T) new BasicType(SimpleType.INT);
    }

    @Override
    public T visitCallFuncExp(SysYParser.CallFuncExpContext ctx) {
        String varName = ctx.IDENT().getText();
        Symbol symbol = currentScope.resolve(varName);
        if (symbol == null)
            report(2, ctx.IDENT().getSymbol().getLine());
        else if(!(symbol instanceof FunctionSymbol)){ //检查是否为变量的symbol而不是函数的symbol
                report(10, ctx.IDENT().getSymbol().getLine());
        }else{          // 检查参数传递是否正确
            FunctionType functionType = (FunctionType) symbol.getType();
            ArrayList<Type> paramsType = functionType.getParamsType();  //定义的时候

            SysYParser.FuncRParamsContext funcRParamsCtx = ctx.funcRParams();
            if(funcRParamsCtx!=null){
                List<SysYParser.ParamContext> paramCtxs =funcRParamsCtx.param();
                if(!paramCtxs.isEmpty()){
                    boolean isQualified=true;
                    if(paramsType.size()!=paramCtxs.size()) isQualified=false;
                    else{
                        int minSize= Math.min(paramCtxs.size(), paramsType.size());
                        for(int index=0;index<minSize;index++){
                            Type type = (Type)visitParam(paramCtxs.get(index)); //TODO 会导致少遍历一些ctx
                            if(type==null||!type.equals(paramsType.get(index)))
                                isQualified=false;
                        }
                    }
                    if(!isQualified)
                        report(8,ctx.IDENT().getSymbol().getLine());
                }
            }
            return (T)functionType.getRetType();
        }

        return null;        //TODO , 可能需要修改，保证不会重复输出错误
    }

    @Override
    public T visitParam(SysYParser.ParamContext ctx) {
        SysYParser.ExpContext expCtx = ctx.exp();
        return this.visit(expCtx);
    }

    @Override
    public T visitUnaryOpExp(SysYParser.UnaryOpExpContext ctx) {
        return super.visitUnaryOpExp(ctx);
    }

    @Override
    public T visitMulExp(SysYParser.MulExpContext ctx) {
        Type lType = (Type) this.visit(ctx.exp().get(0));
        Type rType = (Type) this.visit(ctx.exp().get(1));
        int lineNum = 0;
        if (ctx.MOD() != null) lineNum = ctx.MOD().getSymbol().getLine();
        if (ctx.MUL() != null) lineNum = ctx.MUL().getSymbol().getLine();
        if (ctx.DIV() != null) lineNum = ctx.DIV().getSymbol().getLine();
        if (lType instanceof BasicType && rType instanceof BasicType) {
            BasicType lt = (BasicType) lType;
            BasicType rt = (BasicType) rType;
            if (rt.getSimpleType() != SimpleType.INT || lt.getSimpleType() != SimpleType.INT){
                report(6, lineNum);
                return (T)new BasicType(SimpleType.ERROR);
            }
        }else{
            report(6,lineNum);
            return (T)new BasicType(SimpleType.ERROR);
        }
        return (T) lType;
    }

    @Override
    public T visitPlusExp(SysYParser.PlusExpContext ctx) {
        Type lType = (Type) this.visit(ctx.exp().get(0));
        Type rType = (Type) this.visit(ctx.exp().get(1));
        int lineNum = 0;
        if (ctx.PLUS() != null) lineNum = ctx.PLUS().getSymbol().getLine();
        if (ctx.MINUS() != null) lineNum = ctx.MINUS().getSymbol().getLine();
        if (lType instanceof BasicType && rType instanceof BasicType) {
            BasicType lt = (BasicType) lType;
            BasicType rt = (BasicType) rType;
            if (rt.getSimpleType() != SimpleType.INT || lt.getSimpleType() != SimpleType.INT){
                report(6, lineNum);
                return (T)new BasicType(SimpleType.ERROR);
            }
        }else{
            report(6,lineNum);
            return (T)new BasicType(SimpleType.ERROR);
        }
        return (T) lType;
    }

    @Override
    public T visitFuncDef(SysYParser.FuncDefContext ctx) {
        //1. 构建functionType, 获取 retType 和 paramsType
        String typeName = ctx.funcType().getText();
        Type retType = globalScope.resolveType(typeName);//解析类型名，要去全局的作用域解析
        SysYParser.FuncFParamsContext funcFParamsCtx = ctx.funcFParams();

        ArrayList<Type> paramsType = new ArrayList<>();
        if (funcFParamsCtx != null) {       // 如果有参数
            List<SysYParser.FuncFParamContext> funcFParamCtx = funcFParamsCtx.funcFParam();
            for (SysYParser.FuncFParamContext paramCtx : funcFParamCtx) {
                String paramTypeName = paramCtx.bType().getText();
                Type paramType = globalScope.resolveType(paramTypeName);
                paramsType.add(paramType);
            }
        }
        FunctionType ft = new FunctionType(retType, paramsType);


        //2. 报告函数重定义错误
        String funName = ctx.IDENT().getText();
        Symbol tmp = currentScope.resolve(funName);
        if (tmp != null) report(4, ctx.IDENT().getSymbol().getLine());
        else {
            //3. 构建 FunctionSymbol，设置 funcType
            FunctionSymbol fun = new FunctionSymbol(funName, currentScope);
            fun.setFuncType(ft);
            currentScope.define(fun);
            currentScope = fun;

            //4. 记录当前的函数，与返回值类型对比,注意应当在 retType之前记录curFun，否则returnStmt无法得知curFun
            currentRetType = retType;

            //5. 使用父类的遍历
            super.visitFuncDef(ctx);

            //6. 退出后修改作用域
            currentScope = currentScope.getEnclosingScope();
        }
        return null;
    }

    @Override
    public T visitFuncType(SysYParser.FuncTypeContext ctx) {
        return null;
    }

    @Override
    public T visitFuncFParams(SysYParser.FuncFParamsContext ctx) {
        if (ctx != null) {
            List<SysYParser.FuncFParamContext> funcFParamContexts = ctx.funcFParam();
            for (SysYParser.FuncFParamContext each : funcFParamContexts) visitFuncFParam(each);
        }
        return null;
    }

    @Override
    public T visitFuncFParam(SysYParser.FuncFParamContext ctx) {
        List<SysYParser.ExpContext> expCtxs = ctx.exp();
        for (SysYParser.ExpContext each : expCtxs) this.visit(each);

        String typeName = ctx.bType().getText();

        Type type = globalScope.resolveType(typeName);

        String varName = ctx.IDENT().getText();
        VariableSymbol symbol = new VariableSymbol(varName, type);
        currentScope.define(symbol);

        return null;
    }

    @Override
    public T visitBlock(SysYParser.BlockContext ctx) {
        //1. enterBlock
        LocalScope localScope = new LocalScope(currentScope);
        String localScopeName = localScope.getName() + localScopeCounter;
        localScope.setName(localScopeName);
        localScopeCounter++;
        currentScope = localScope;

        //2. super
        super.visitBlock(ctx);
//        List<SysYParser.BlockItemContext> blockItemCtxs = ctx.blockItem();
//        for(SysYParser.BlockItemContext each:blockItemCtxs) visitBlockItem(each);

        //3. exitBlock
        currentScope = currentScope.getEnclosingScope();
        return null;
    }

    @Override
    public T visitBlockItem(SysYParser.BlockItemContext ctx) {
        return super.visitBlockItem(ctx);
    }

    @Override
    public T visitAssignStmt(SysYParser.AssignStmtContext ctx) {
        SysYParser.LValContext lValCtx = ctx.lVal();
        String varName = lValCtx.IDENT().getText();
        Symbol symbol = currentScope.resolve(varName);
        if (symbol instanceof FunctionSymbol){
            report(11, lValCtx.IDENT().getSymbol().getLine());
            return null;
        }

        SysYParser.ExpContext expCtx = ctx.exp();
        Type lType = (Type) visitLVal(lValCtx);
        Type rType = (Type) this.visit(expCtx);
        if (lType != null)                     //TODO 删除此行则出现空指针异常
            if (!lType.equals(rType) && !rType.equals(new BasicType(SimpleType.ERROR)))
                report(5, lValCtx.IDENT().getSymbol().getLine());
        return null;
    }

    @Override
    public T visitExpStmt(SysYParser.ExpStmtContext ctx) {
        SysYParser.ExpContext expCtx = ctx.exp();
        this.visit(expCtx);
        return null;
    }

    @Override
    public T visitBlockStmt(SysYParser.BlockStmtContext ctx) {
        return super.visitBlockStmt(ctx);
    }

    @Override
    public T visitIfStmt(SysYParser.IfStmtContext ctx) {
        return super.visitIfStmt(ctx);
    }

    @Override
    public T visitWhileStmt(SysYParser.WhileStmtContext ctx) {
        return super.visitWhileStmt(ctx);
    }

    @Override
    public T visitBreakStmt(SysYParser.BreakStmtContext ctx) {
        return super.visitBreakStmt(ctx);
    }

    @Override
    public T visitContinueStmt(SysYParser.ContinueStmtContext ctx) {
        return super.visitContinueStmt(ctx);
    }

    @Override
    public T visitReturnStmt(SysYParser.ReturnStmtContext ctx) {
        if(ctx.exp()==null){
            if(!(currentRetType instanceof BasicType))
                report(7,ctx.RETURN().getSymbol().getLine());
            else{
                BasicType basicType = (BasicType) currentRetType;
                if(basicType.getSimpleType()!=SimpleType.VOID)
                    report(7,ctx.RETURN().getSymbol().getLine());
            }
        }else{
            Type type = (Type) this.visit(ctx.exp());
            if (type != null)                                  //TODO
                if (!type.equals(currentRetType))
                    report(7, ctx.RETURN().getSymbol().getLine());
        }

        return null;
    }
}
