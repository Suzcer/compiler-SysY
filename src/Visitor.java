import TypeSys.*;
import org.antlr.v4.runtime.Vocabulary;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNode;
import symtable.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Visitor<T> extends SysYParserBaseVisitor<T> {


    private GlobalScope globalScope = null;

    private Scope currentScope = null;

    private int localScopeCounter = 0;

    private Type currentRetType;

    private boolean hasError = false;

    /**
     * 以下属性均与重命名相关
     **/
    private int lineNo;

    private int column;

    private String rename;

    private boolean second = false;

    /**
     * 区分函数紧跟的block和其他的block
     **/
    private boolean next = false;

    private void report(int errType, int lineNo) {
//        hasError = true;
//        if (!second)
//            System.err.println("Error type " + errType + " at Line " + lineNo + ":");
    }

    public void setSecond(boolean second) {
        this.second = second;
    }

    public boolean hasError() {
        return hasError;
    }

    private String[] ruleNames;

    private Vocabulary vocabulary;

    private HashMap<String, String> mp;

    private String renameRecord;

    public void setRulesName(String[] ruleNames) {
        this.ruleNames = ruleNames;
    }

    public void setVocabulary(Vocabulary vocabulary) {
        this.vocabulary = vocabulary;
    }

    public void setMp(HashMap<String, String> mp) {
        this.mp = mp;
    }

    public void setArgs(int lineNo, int column, String rename) {
        this.lineNo = lineNo;
        this.column = column;
        this.rename = rename;
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
    @Override
    public T visitChildren(RuleNode node) {
        if (second) {
            String ruleName = ruleNames[node.getRuleContext().getRuleIndex()];
            for (int i = 0; i + 1 < node.getRuleContext().depth(); i++) System.err.print("  ");
            System.err.println(captureName(ruleName));
        }
        return super.visitChildren(node);
    }

    //打印终结符及终结符的高亮
    @Override
    public T visitTerminal(TerminalNode node) {

        if (second) {
            String type = vocabulary.getSymbolicName(node.getSymbol().getType());
            if (mp.containsKey(type)) {
                RuleNode parent;
                if (node.getParent() instanceof RuleNode) {
                    parent = (RuleNode) node.getParent();
                    for (int i = 0; i < parent.getRuleContext().depth(); i++)
                        System.err.print("  ");
                }

                String oriName = baseTrans(node.getText());
                if (type.equals("IDENT")) {
                    String str = currentScope.findScope(node.getText()) + "." + oriName;
//                    System.err.print("yyy: " + currentScope.getName() + "   ");
//                    System.err.print("xxx: " + str + "   ");
                    if (str.equals(renameRecord)) {
                        System.err.print(rename + " ");
                    }else System.err.print(oriName + " ");
                } else {
                    System.err.print(oriName + " ");
                }
                System.err.print(type);
                System.err.println("[" + mp.get(type) + "]");
            }
        }
        return super.visitTerminal(node);
    }

    @Override
    public T visitProgram(SysYParser.ProgramContext ctx) {
        localScopeCounter = 0;                            // 保证两次命名名称相同
        globalScope = new GlobalScope(null);
        currentScope = globalScope;
        super.visitProgram(ctx);
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
//        if (second) return super.visitVarDecl(ctx);               // TODO 如果不注释掉会出现问题，不太清楚是什么问题
        String typeName = ctx.bType().getText();
        Type type = globalScope.resolveType(typeName);              // TODO 涉及到 globalScope，需要考虑是否能够解析出来
        List<SysYParser.VarDefContext> varDefContexts = ctx.varDef();
        for (SysYParser.VarDefContext varDefCtx : varDefContexts) {
            String varName = varDefCtx.IDENT().getText();
            Symbol tmp = currentScope.getSymbols().get(varName);    // 如果和当前作用域的重名了，才需要进行错误报告
            if (tmp != null) report(3, varDefCtx.IDENT().getSymbol().getLine());
            else {
                int dimensions = varDefCtx.L_BRACKT().size();       // int a[1][2];
                if (dimensions == 0) {   //VariableSymbol
                    VariableSymbol symbol = new VariableSymbol(varName, type);
                    currentScope.define(symbol);
                } else {
                    BasicType mostInner = (BasicType) type;
                    ArrayType ptr = new ArrayType(mostInner, 1, 1);   // lab3的count不予考虑
                    for (int cnt = 2; cnt <= dimensions; cnt++) {
                        ptr = new ArrayType(ptr, 1, cnt);
                    }
                    ArraySymbol symbol = new ArraySymbol(varName, ptr);
                    currentScope.define(symbol);
                }
                // 保证初始化是正确的，因此这里不予处理
            }
            // renameRecord记录
            if (varDefCtx.IDENT().getSymbol().getLine() == lineNo
                    && varDefCtx.IDENT().getSymbol().getCharPositionInLine() == column) {
                renameRecord = currentScope.findScope(varName) + "." + baseTrans(varDefCtx.IDENT().getSymbol().getText());
            }
        }
        return super.visitVarDecl(ctx);
    }

    @Override
    public T visitConstDecl(SysYParser.ConstDeclContext ctx) {
//        if (second) return super.visitConstDecl(ctx);
        String typeName = ctx.bType().getText();
        Type type = globalScope.resolveType(typeName);
        List<SysYParser.ConstDefContext> constDefContexts = ctx.constDef();
        for (SysYParser.ConstDefContext constCtx : constDefContexts) {
            // renameRecord记录
            if (constCtx.IDENT().getSymbol().getLine() == lineNo
                    && constCtx.IDENT().getSymbol().getCharPositionInLine() == column)
                renameRecord = currentScope.findScope(constCtx.IDENT().getText()) + "." + baseTrans(constCtx.IDENT().getSymbol().getText());

            String varName = constCtx.IDENT().getText();
            Symbol tmp = currentScope.getSymbols().get(varName);
            if (tmp != null) report(3, constCtx.IDENT().getSymbol().getLine());
            else {
                VariableSymbol symbol = new VariableSymbol(varName, type);
                currentScope.define(symbol);
            }
        }
        return super.visitConstDecl(ctx);
    }

    @Override
    public T visitVarDef(SysYParser.VarDefContext ctx) {
        return super.visitVarDef(ctx);
    }

    @Override
    public T visitConstExp(SysYParser.ConstExpContext ctx) {
        return super.visitConstExp(ctx);
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
//        if (second) return super.visitLVal(ctx);

        String varName = ctx.IDENT().getText();
        Symbol symbol = currentScope.resolve(varName);
        List<SysYParser.ExpContext> expCtxs = ctx.exp();

        // renameRecord记录
        if (ctx.IDENT().getSymbol().getLine() == lineNo
                && ctx.IDENT().getSymbol().getCharPositionInLine() == column) {
            renameRecord = currentScope.findScope(varName) + "." + baseTrans(ctx.IDENT().getSymbol().getText());
        }

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
                    if (ptr instanceof ArrayType)
                        ptr = ((ArrayType) ptr).getSubType();
                    else if (ptr instanceof BasicType) {    //此时没有subType，ptr不能是BasicType
                        report(9, ctx.IDENT().getSymbol().getLine());
                    }
                }
                return (T) ptr;
            }
        }
        if (symbol != null) return (T) symbol.getType();
        return null; //already 遍历
    }

    @Override
    public T visitNumberExp(SysYParser.NumberExpContext ctx) {
        super.visitNumberExp(ctx);
        return (T) new BasicType(SimpleType.INT);
    }

    @Override
    public T visitCallFuncExp(SysYParser.CallFuncExpContext ctx) {
//        if (second) return super.visitCallFuncExp(ctx);

        String varName = ctx.IDENT().getText();
        Symbol symbol = currentScope.resolve(varName);

        // renameRecord记录
        if (ctx.IDENT().getSymbol().getLine() == lineNo
                && ctx.IDENT().getSymbol().getCharPositionInLine() == column)
            renameRecord = currentScope.findScope(ctx.IDENT().getText()) + "." + baseTrans(ctx.IDENT().getSymbol().getText());

        if (symbol == null)
            report(2, ctx.IDENT().getSymbol().getLine());
        else if (!(symbol instanceof FunctionSymbol)) { //检查是否为变量的symbol而不是函数的symbol
            report(10, ctx.IDENT().getSymbol().getLine());
        } else if(!second){          // 检查参数传递是否正确
            FunctionType functionType = (FunctionType) symbol.getType();
            ArrayList<Type> paramsType = functionType.getParamsType();  //定义的时候

            SysYParser.FuncRParamsContext funcRParamsCtx = ctx.funcRParams();
            if (funcRParamsCtx != null) {
                List<SysYParser.ParamContext> paramCtxs = funcRParamsCtx.param();
                if (!paramCtxs.isEmpty()) {
                    boolean isQualified = true;
                    if (paramsType.size() != paramCtxs.size()) isQualified = false;
                    else {
                        int minSize = Math.min(paramCtxs.size(), paramsType.size());
                        for (int index = 0; index < minSize; index++) {
                            Type type = (Type) visitParam(paramCtxs.get(index)); //TODO 会导致少遍历一些ctx
                            if (type == null || !type.equals(paramsType.get(index)))
                                isQualified = false;
                        }
                    }
                    if (!isQualified)
                        report(8, ctx.IDENT().getSymbol().getLine());
                } else if (!paramsType.isEmpty()) {             // 参数列表不为空但实际没给参数
                    report(8, ctx.IDENT().getSymbol().getLine());
                }
            } else if (!paramsType.isEmpty()) {     // 参数列表不为空但实际没给参数
                report(8, ctx.IDENT().getSymbol().getLine());
            }
            return (T) functionType.getRetType();
        }

        return super.visitCallFuncExp(ctx);        //TODO , 可能需要修改，保证不会重复输出错误
    }

    @Override
    public T visitParam(SysYParser.ParamContext ctx) {
        return super.visitParam(ctx);
    }

    @Override
    public T visitUnaryOpExp(SysYParser.UnaryOpExpContext ctx) {
        if (second) return super.visitUnaryOpExp(ctx);

        Type type = (Type) this.visit(ctx.exp());
        int lineNum = 0;
        if (ctx.unaryOp().PLUS() != null) lineNum = ctx.unaryOp().PLUS().getSymbol().getLine();
        if (ctx.unaryOp().MINUS() != null) lineNum = ctx.unaryOp().MINUS().getSymbol().getLine();
        if (ctx.unaryOp().NOT() != null) lineNum = ctx.unaryOp().NOT().getSymbol().getLine();
        if (type instanceof BasicType) {
            BasicType basicType = (BasicType) type;
            if (basicType.getSimpleType() != SimpleType.INT) {
                report(6, lineNum);
            }
        } else {
            report(6, lineNum);
            return (T) new BasicType(SimpleType.ERROR);
        }
        return super.visitUnaryOpExp(ctx);
    }

    @Override
    public T visitMulExp(SysYParser.MulExpContext ctx) {
        if (second) return super.visitMulExp(ctx);

        Type lType = (Type) this.visit(ctx.exp().get(0));
        Type rType = (Type) this.visit(ctx.exp().get(1));
        int lineNum = 0;
        if (ctx.MOD() != null) lineNum = ctx.MOD().getSymbol().getLine();
        if (ctx.MUL() != null) lineNum = ctx.MUL().getSymbol().getLine();
        if (ctx.DIV() != null) lineNum = ctx.DIV().getSymbol().getLine();
        if (lType instanceof BasicType && rType instanceof BasicType) {
            BasicType lt = (BasicType) lType;
            BasicType rt = (BasicType) rType;
            if (rt.getSimpleType() != SimpleType.INT || lt.getSimpleType() != SimpleType.INT) {
                report(6, lineNum);
                return (T) new BasicType(SimpleType.ERROR);
            }
        } else {
            report(6, lineNum);
            return (T) new BasicType(SimpleType.ERROR);
        }
        return (T) lType;
    }

    @Override
    public T visitPlusExp(SysYParser.PlusExpContext ctx) {
        if (second) return super.visitPlusExp(ctx);

        Type lType = (Type) this.visit(ctx.exp().get(0));
        Type rType = (Type) this.visit(ctx.exp().get(1));
        int lineNum = 0;
        if (ctx.PLUS() != null) lineNum = ctx.PLUS().getSymbol().getLine();
        if (ctx.MINUS() != null) lineNum = ctx.MINUS().getSymbol().getLine();
        if (lType instanceof BasicType && rType instanceof BasicType) {
            BasicType lt = (BasicType) lType;
            BasicType rt = (BasicType) rType;
            if (rt.getSimpleType() != SimpleType.INT || lt.getSimpleType() != SimpleType.INT) {
                report(6, lineNum);
                return (T) new BasicType(SimpleType.ERROR);
            }
        } else {
            report(6, lineNum);
            return (T) new BasicType(SimpleType.ERROR);
        }
        return (T) lType;
    }

    @Override
    public T visitFuncDef(SysYParser.FuncDefContext ctx) {
//        if(second) return super.visitFuncDef(ctx);        需要修改Scope

        //1. 构建functionType, 获取 retType 和 paramsType
        String typeName = ctx.funcType().getText();
        Type retType = globalScope.resolveType(typeName);//解析类型名，要去全局的作用域解析
        SysYParser.FuncFParamsContext funcFParamsCtx = ctx.funcFParams();

        ArrayList<Type> paramsType = new ArrayList<>();
        ArrayList<Symbol> defineList = new ArrayList<>();
        if (funcFParamsCtx != null) {       // 如果有参数
            List<SysYParser.FuncFParamContext> funcFParamCtx = funcFParamsCtx.funcFParam();
            for (SysYParser.FuncFParamContext paramCtx : funcFParamCtx) {
                String paramTypeName = paramCtx.bType().getText();
                Type paramType = globalScope.resolveType(paramTypeName);
                // 以下逻辑处理函数参数如 int a[][2]
                if (paramCtx.L_BRACKT().isEmpty()) {
                    paramsType.add(paramType);
                    defineList.add(new VariableSymbol(paramCtx.IDENT().getText(), paramType));
                } else {
                    int dimensions = paramCtx.L_BRACKT().size();
                    BasicType mostInner = (BasicType) paramType;
                    ArrayType ptr = new ArrayType(mostInner, 1, 1);
                    for (int cnt = 2; cnt <= dimensions; cnt++) {
                        ptr = new ArrayType(ptr, 1, cnt);
                    }
                    paramsType.add(ptr);
                    defineList.add(new ArraySymbol(paramCtx.IDENT().getText(), ptr));
                }
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
            currentScope.putScope(funName, fun);
            currentScope = fun;

            //4. 进入函数作用域后，对其形参进行define
            if (!defineList.isEmpty()) {
                for (Symbol symbol : defineList)
                    currentScope.define(symbol);
            }

            //5. 记录当前的函数，与返回值类型对比,注意应当在 retType之前记录curFun，否则returnStmt无法得知curFun
            currentRetType = retType;

            //6. modify next
            next = true;

            //7. 使用父类的遍历
            super.visitFuncDef(ctx);

            //4.重命名
            if (funcFParamsCtx != null) {
                List<SysYParser.FuncFParamContext> funcFParamCtx = funcFParamsCtx.funcFParam();
                for (SysYParser.FuncFParamContext paramCtx : funcFParamCtx) {
                    if (paramCtx.IDENT().getSymbol().getLine() == lineNo
                            && paramCtx.IDENT().getSymbol().getCharPositionInLine() == column)
                        renameRecord = currentScope.findScope(paramCtx.IDENT().getText()) + "." + baseTrans(paramCtx.IDENT().getSymbol().getText());
                }
            }

            //8. 退出后修改作用域
            currentScope = currentScope.getEnclosingScope();
        }
        return null;// already 遍历
    }

    @Override
    public T visitFuncType(SysYParser.FuncTypeContext ctx) {
        return super.visitFuncType(ctx);
    }

    @Override
    public T visitFuncFParams(SysYParser.FuncFParamsContext ctx) {
        return super.visitFuncFParams(ctx);
    }

    /**
     * 在funcDef中接管了
     **/
    @Override
    public T visitFuncFParam(SysYParser.FuncFParamContext ctx) {
        return super.visitFuncFParam(ctx);
    }

    @Override
    public T visitBlock(SysYParser.BlockContext ctx) {
//        if(second) return super.visitBlock(ctx);      //block需要修改currentScope

        //0.control next, 需要该值，防止next修改之后退不出去
        boolean blockNext = next;

        //1. enterBlock
        if (!next) {
            LocalScope localScope = new LocalScope(currentScope);
            String localScopeName = localScope.getName() + localScopeCounter;
            localScope.setName(localScopeName);
            localScopeCounter++;
            currentScope.putScope(localScopeName, localScope);
            currentScope = localScope;
        } else {
            next = false;
        }

        //2. super
        super.visitBlock(ctx);

        //3. exitBlock, next为false表示不是紧跟函数的block，需要退出
        if (!blockNext)
            currentScope = currentScope.getEnclosingScope();
        return null;    //already 遍历
    }

    @Override
    public T visitBlockItem(SysYParser.BlockItemContext ctx) {
        return super.visitBlockItem(ctx);
    }

    @Override
    public T visitAssignStmt(SysYParser.AssignStmtContext ctx) {
//        if (second) return super.visitAssignStmt(ctx);

        SysYParser.LValContext lValCtx = ctx.lVal();
        String varName = lValCtx.IDENT().getText();
        Symbol symbol = currentScope.resolve(varName);
        if (symbol instanceof FunctionSymbol) {
            report(11, lValCtx.IDENT().getSymbol().getLine());
            return null;
        }
        if(!second){
            SysYParser.ExpContext expCtx = ctx.exp();
            Type lType = (Type) visitLVal(lValCtx);
            Type rType = (Type) this.visit(expCtx);
            if (lType != null)                     //TODO 删除此行则出现空指针异常
                if (!lType.equals(rType) && !rType.equals(new BasicType(SimpleType.ERROR)))
                    report(5, lValCtx.IDENT().getSymbol().getLine());

        }

        return super.visitAssignStmt(ctx); //already 遍历
    }

    @Override
    public T visitExpStmt(SysYParser.ExpStmtContext ctx) {
        return super.visitExpStmt(ctx);
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

    /**
     * 对于类型7的实现有bug，比如int a(){}，并没有写返回值，当然就不会触发7
     */
    @Override
    public T visitReturnStmt(SysYParser.ReturnStmtContext ctx) {
//        if (second) return super.visitReturnStmt(ctx);

        if (ctx.exp() == null) {
            if (!(currentRetType instanceof BasicType))
                report(7, ctx.RETURN().getSymbol().getLine());
            else {
                BasicType basicType = (BasicType) currentRetType;
                if (basicType.getSimpleType() != SimpleType.VOID)
                    report(7, ctx.RETURN().getSymbol().getLine());
            }
        } else {
            if(!second){
                Type type = (Type) this.visit(ctx.exp());
                if (type != null)                                  //TODO
                    if (!type.equals(currentRetType))
                        report(7, ctx.RETURN().getSymbol().getLine());
            }
        }

        return super.visitReturnStmt(ctx);            //already 遍历
    }
}

/**
 * Interesting Cases:
 * 1.   void a(){}
 * int main(){
 * int c[2][3];
 * int b = a()*5*c[2];
 * }
 * <p>
 * 2.   void a(){}
 * int main(){
 * int b=-a;
 * }
 */