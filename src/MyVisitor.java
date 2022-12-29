import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.Pointer;
import org.bytedeco.javacpp.PointerPointer;
import org.bytedeco.llvm.LLVM.*;
import symtable.FunctionSymbol;
import symtable.GlobalScope;
import symtable.Scope;

import java.util.List;

import static org.bytedeco.llvm.global.LLVM.*;


public class MyVisitor extends SysYParserBaseVisitor<LLVMValueRef> {

    private GlobalScope globalScope = null;

    private Scope currentScope = null;

    LLVMModuleRef module;
    LLVMBuilderRef builder;
    LLVMTypeRef i32Type;
    LLVMValueRef[] constDigit;
    String des;

    public static final BytePointer error = new BytePointer();


    public MyVisitor() {

        //1. 初始化LLVM
        LLVMInitializeCore(LLVMGetGlobalPassRegistry());
        LLVMLinkInMCJIT();
        LLVMInitializeNativeAsmPrinter();
        LLVMInitializeNativeAsmParser();
        LLVMInitializeNativeTarget();

        //2. 创建module
        module = LLVMModuleCreateWithName("moudle");

        //3. 初始化IRBuilder，后续将使用这个builder去生成LLVM IR
        builder = LLVMCreateBuilder();

        //4. 考虑到我们的语言中仅存在int一个基本类型，可以通过下面的语句为LLVM的int型重命名方便以后使用
        i32Type = LLVMInt32Type();
        initConstDigit();
    }

    private void initConstDigit() {
        constDigit = new LLVMValueRef[10];
        for (int i = 0; i <= 9; i++) {
            constDigit[i] = LLVMConstInt(i32Type, i, 0);
        }
    }

    @Override
    public LLVMValueRef visitProgram(SysYParser.ProgramContext ctx) {
        globalScope = new GlobalScope(null);
        currentScope = globalScope;
        super.visitProgram(ctx);
        if (LLVMPrintModuleToFile(module, des, error) != 0) {    // moudle是你自定义的LLVMModuleRef对象
            LLVMDisposeMessage(error);
        }
        currentScope = currentScope.getEnclosingScope();
        return null;
    }

    @Override
    public LLVMValueRef visitVarDecl(SysYParser.VarDeclContext ctx) {
//        List<SysYParser.VarDefContext> varDefCtxs = ctx.varDef();
//        if (varDefCtxs != null) {
//            for (int i = 0; i < varDefCtxs.size(); i++) {
//                this.visit(varDefCtxs.get(i));
//            }
//        }

        return super.visitVarDecl(ctx);
    }

    @Override
    public LLVMValueRef visitVarDef(SysYParser.VarDefContext ctx) {

        if (ctx.L_BRACKT().isEmpty()) {   //one
            LLVMValueRef ref = LLVMBuildAlloca(builder, i32Type, ctx.IDENT().getText());
            SysYParser.InitValContext initValCtx = ctx.initVal();
            if (initValCtx != null) {
                LLVMValueRef initValueRef = this.visit(initValCtx);
                LLVMBuildStore(builder, initValueRef, ref);// ref是左边的
            }
            currentScope.putValueRef(ctx.IDENT().getText(), ref);
        } else {          // vector
            int vecSize = Integer.parseInt(ctx.constExp().get(0).getText());

            LLVMTypeRef vectorType = LLVMVectorType(i32Type, vecSize);
            LLVMValueRef vectorPointer = LLVMBuildAlloca(builder, vectorType, ctx.IDENT().getText());

            if (ctx.initVal() != null) {
                List<SysYParser.InitValContext> initValCtxs = ctx.initVal().initVal();
                int initSize = initValCtxs.size();
                LLVMValueRef[] initVals = new LLVMValueRef[vecSize];
                for (int j = 0; j < initSize; j++) initVals[j] = this.visit(initValCtxs.get(j));
                for (int j = initSize; j < vecSize; j++) initVals[j] = constDigit[0];

                LLVMValueRef[] refs = new LLVMValueRef[2];
                refs[0] = constDigit[0];

                for (int j = 0; j < vecSize; j++) {
                    refs[1] = LLVMConstInt(i32Type, j, 0);
                    PointerPointer<Pointer> pp = new PointerPointer<>(refs);
                    LLVMValueRef pointer = LLVMBuildGEP(builder, vectorPointer, pp, 2, "pointer");
                    LLVMBuildStore(builder, initVals[j], pointer);
                }
            }
            currentScope.putValueRef(ctx.IDENT().getText(), vectorPointer);
        }
        return super.visitVarDef(ctx);
    }

    @Override
    public LLVMValueRef visitConstDef(SysYParser.ConstDefContext ctx) {
        if (ctx.L_BRACKT().isEmpty()) {   //one
            LLVMValueRef ref = LLVMBuildAlloca(builder, i32Type, ctx.IDENT().getText());
            LLVMValueRef initValueRef = this.visit(ctx.constInitVal());    //一定有初始化的过程
            LLVMBuildStore(builder, initValueRef, ref);       // ref是左边的
            currentScope.putValueRef(ctx.IDENT().getText(), ref);
        } else {          // vector
            int vecSize = Integer.parseInt(ctx.constExp().get(0).getText());

            LLVMTypeRef vectorType = LLVMVectorType(i32Type, vecSize);
            LLVMValueRef vectorPointer = LLVMBuildAlloca(builder, vectorType, ctx.IDENT().getText());

            List<SysYParser.ConstInitValContext> constInitValCtxs = ctx.constInitVal().constInitVal();
            int initSize = constInitValCtxs.size();
            LLVMValueRef[] initVals = new LLVMValueRef[vecSize];
            for (int j = 0; j < initSize; j++) initVals[j] = this.visit(constInitValCtxs.get(j));
            for (int j = initSize; j < vecSize; j++) initVals[j] = constDigit[0];

            LLVMValueRef[] refs = new LLVMValueRef[2];
            refs[0] = constDigit[0];

            for (int j = 0; j < vecSize; j++) {
                refs[1] = LLVMConstInt(i32Type, j, 0);
                PointerPointer<Pointer> pp = new PointerPointer<>(refs);
                LLVMValueRef pointer = LLVMBuildGEP(builder, vectorPointer, pp, 2, "pointer");
                LLVMBuildStore(builder, initVals[j], pointer);
            }

            currentScope.putValueRef(ctx.IDENT().getText(), vectorPointer);
        }
        return super.visitConstDef(ctx);
    }

    @Override
    public LLVMValueRef visitFuncDef(SysYParser.FuncDefContext ctx) {

        SysYParser.FuncFParamsContext funcFParamsCtx = ctx.funcFParams();
        PointerPointer<Pointer> mainParamTypes;
        LLVMTypeRef mainRetType;
        if (funcFParamsCtx != null) {
            List<SysYParser.FuncFParamContext> funcFParamCtxs = funcFParamsCtx.funcFParam();
            mainParamTypes = new PointerPointer<>(funcFParamCtxs.size());
            for (int i = 0; i < funcFParamCtxs.size(); i++) {
                mainParamTypes = mainParamTypes.put(i, i32Type);  // Only int will occur
            }
            mainRetType = LLVMFunctionType(i32Type, mainParamTypes, funcFParamCtxs.size(), 0);
        } else {
            mainParamTypes = new PointerPointer<>(0);
            mainRetType = LLVMFunctionType(i32Type, mainParamTypes, 0, 0);
        }

        LLVMValueRef curFunction = LLVMAddFunction(module, ctx.IDENT().getText(), mainRetType);
        LLVMBasicBlockRef curEntry = LLVMAppendBasicBlock(curFunction, ctx.IDENT().getText() + "Entry");
        LLVMPositionBuilderAtEnd(builder, curEntry);

        globalScope.putValueRef(ctx.IDENT().getText(), curFunction);

        FunctionSymbol fun = new FunctionSymbol(ctx.IDENT().getText(), currentScope);
        currentScope = fun;

        if (funcFParamsCtx != null) {
            List<SysYParser.FuncFParamContext> funcFParamCtxs = funcFParamsCtx.funcFParam();
            for (int i = 0; i < funcFParamCtxs.size(); i++) {
                LLVMValueRef argI = LLVMGetParam(curFunction, i);
                String key = funcFParamCtxs.get(i).IDENT().getText();
                LLVMValueRef value = LLVMBuildAlloca(builder, i32Type, /*pointerName:String*/key);
                LLVMBuildStore(builder, argI, value);             //将数值存入该内存
                currentScope.putValueRef(key, value);
            }
        }
        super.visitFuncDef(ctx);

        if (ctx.funcType().getText().equals("void"))
            LLVMBuildRetVoid(builder);
        currentScope = currentScope.getEnclosingScope();

        return null;
    }

    @Override
    public LLVMValueRef visitReturnStmt(SysYParser.ReturnStmtContext ctx) {
        if (ctx.exp() != null) {
            LLVMValueRef ret = this.visit(ctx.exp());
            LLVMBuildRet(builder, ret);
            return ret;
        }
        return super.visitReturnStmt(ctx);
    }

    // 右边是左值
    @Override
    public LLVMValueRef visitLvalExp(SysYParser.LvalExpContext ctx) {
        String token = ctx.lVal().IDENT().getText();
        if (!ctx.lVal().L_BRACKT().isEmpty()) {
            int i = Integer.parseInt(ctx.lVal().exp().get(0).getText());
            LLVMValueRef[] refs = new LLVMValueRef[2];
            refs[0] = constDigit[0];
            refs[1] = LLVMConstInt(i32Type, i, 0);

            LLVMValueRef vectorPointer = currentScope.resolveValueRef(token);
            PointerPointer<Pointer> pp = new PointerPointer<>(refs);
            LLVMValueRef ptr = LLVMBuildGEP(builder, vectorPointer, pp, 2, "ret");
            LLVMValueRef ee = LLVMBuildLoad(builder, ptr, token);
            return ee;
        } else {
            return LLVMBuildLoad(builder, currentScope.resolveValueRef(token), token);
        }
//        return super.visitLvalExp(ctx);
    }


    @Override
    public LLVMValueRef visitCallFuncExp(SysYParser.CallFuncExpContext ctx) {
        LLVMValueRef funcValueRef = globalScope.resolveValueRef(ctx.IDENT().getText());
        //实参
        SysYParser.FuncRParamsContext funcRParamsCtx = ctx.funcRParams();
        PointerPointer<Pointer> arguments;
        LLVMValueRef retValueRef;
        if (funcRParamsCtx != null) {
            List<SysYParser.ParamContext> paramCtxs = funcRParamsCtx.param();       // 有可能是 empty()
            arguments = new PointerPointer<>(paramCtxs.size());

            for (int i = 0; i < paramCtxs.size(); i++) {
                LLVMValueRef visit = this.visit(paramCtxs.get(i));
                arguments = arguments.put(visit);
            }

            retValueRef = LLVMBuildCall(builder, funcValueRef, arguments, paramCtxs.size(), "returnValue");
        } else {
            arguments = new PointerPointer<>(0);
            retValueRef = LLVMBuildCall(builder, funcValueRef, arguments, 0, "returnValue");
        }

//        return super.visitCallFuncExp(ctx);
        return retValueRef;
    }

    @Override
    public LLVMValueRef visitAssignStmt(SysYParser.AssignStmtContext ctx) {
        LLVMValueRef rval = this.visit(ctx.exp());
        LLVMValueRef lval;
        String token = ctx.lVal().IDENT().getText();
        if (!ctx.lVal().L_BRACKT().isEmpty()) {
            int i = Integer.parseInt(ctx.lVal().exp().get(0).getText());
            LLVMValueRef[] refs = new LLVMValueRef[2];
            refs[0] = constDigit[0];
            refs[1] = LLVMConstInt(i32Type, i, 0);
            LLVMValueRef vectorPointer = currentScope.resolveValueRef(token);
            PointerPointer<Pointer> pp = new PointerPointer<>(refs);
            LLVMValueRef ptr = LLVMBuildGEP(builder, vectorPointer, pp, 2, "ret");
//            LLVMValueRef ee = LLVMBuildLoad(builder, ptr, token);
            lval = ptr;
        } else {
            lval = currentScope.resolveValueRef(token);
        }
        if (lval != null)
            LLVMBuildStore(builder, rval, lval);          // TODO bug here
        return null;
    }

    @Override
    public LLVMValueRef visitMulExp(SysYParser.MulExpContext ctx) {
        List<SysYParser.ExpContext> exps = ctx.exp();
        LLVMValueRef lhs = this.visit(exps.get(0));
        LLVMValueRef rhs = this.visit(exps.get(1));
        LLVMValueRef ret;
        if (ctx.MUL() != null)
            ret = LLVMBuildMul(builder, lhs, rhs, "");
        else if (ctx.DIV() != null)
            ret = LLVMBuildSDiv(builder, lhs, rhs, "");
        else
            ret = LLVMBuildSRem(builder, lhs, rhs, "");
        // srem指令: has sign
        // urem指令: no sign
        return ret;
    }

    @Override
    public LLVMValueRef visitPlusExp(SysYParser.PlusExpContext ctx) {
        List<SysYParser.ExpContext> exps = ctx.exp();
        LLVMValueRef lhs = this.visit(exps.get(0));
        LLVMValueRef rhs = this.visit(exps.get(1));
        LLVMValueRef ret;
        if (ctx.PLUS() != null)
            ret = LLVMBuildAdd(builder, lhs, rhs, "");
        else
            ret = LLVMBuildSub(builder, lhs, rhs, "");
        return ret;
    }

    @Override
    public LLVMValueRef visitUnaryOpExp(SysYParser.UnaryOpExpContext ctx) {
        LLVMValueRef val = this.visit(ctx.exp());
        LLVMValueRef ret = val;
        if (ctx.unaryOp().NOT() != null) {
            ret = LLVMBuildICmp(builder, LLVMIntNE, LLVMConstInt(i32Type, 0, 0), ret, "");
            ret = LLVMBuildXor(builder, ret, LLVMConstInt(LLVMInt1Type(), 1, 0), "");
            ret = LLVMBuildZExt(builder, ret, i32Type, "");
        } else if (ctx.unaryOp().MINUS() != null) {
            ret = LLVMBuildNeg(builder, val, "");
        } else {        //PLUS, no action

        }
        return ret;
    }

    @Override
    public LLVMValueRef visitNumber(SysYParser.NumberContext ctx) {
        int num = baseTrans(ctx.getText());
        LLVMValueRef numRef = LLVMConstInt(i32Type, num, 0);
        return numRef;
    }

    private int baseTrans(String text) {
        if (text.charAt(0) == '0' && text.length() >= 2) {
            int i;
            if (text.charAt(1) == 'x' || text.charAt(1) == 'X') {
                i = Integer.parseInt(text.substring(2), 16);
            } else {
                i = Integer.parseInt(text, 8);
            }
            return i;
        } else {
            return Integer.parseInt(text);
        }
    }
}
