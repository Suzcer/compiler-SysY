import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.Pointer;
import org.bytedeco.javacpp.PointerPointer;
import org.bytedeco.llvm.LLVM.*;

import java.util.List;

import static org.bytedeco.llvm.global.LLVM.*;


public class MyVisitor extends SysYParserBaseVisitor<LLVMValueRef> {

    LLVMModuleRef module;
    LLVMBuilderRef builder;
    LLVMTypeRef i32Type;
    LLVMValueRef zero;
    LLVMValueRef nine;

    PointerPointer<Pointer> mainParamTypes;

    LLVMTypeRef mainRetType;

    LLVMValueRef mainFunction;

    LLVMBasicBlockRef mainEntry;

    LLVMValueRef alloca1;
    LLVMValueRef alloca4;

    LLVMValueRef load4;

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
        zero = LLVMConstInt(i32Type, 0, 0);
        nine = LLVMConstInt(i32Type, 9, 0);

        mainParamTypes = new PointerPointer<>(0);

        mainRetType = LLVMFunctionType(i32Type, mainParamTypes, 0, 0);

        mainFunction = LLVMAddFunction(module, "main", mainRetType);

        mainEntry = LLVMAppendBasicBlock(mainFunction, "mainEntry");
        LLVMPositionBuilderAtEnd(builder, mainEntry);

//        alloca4 = LLVMBuildAlloca(builder, i32Type, "");
//        load4 = LLVMBuildLoad(builder, alloca4, "");
//        LLVMBuildRet(builder, load4);

    }

    @Override
    public LLVMValueRef visitProgram(SysYParser.ProgramContext ctx) {
        this.visitCompUnit(ctx.compUnit());

        if (LLVMPrintModuleToFile(module, des, error) != 0) {    // moudle是你自定义的LLVMModuleRef对象
            LLVMDisposeMessage(error);
        }
        return null;
    }

    @Override
    public LLVMValueRef visitReturnStmt(SysYParser.ReturnStmtContext ctx) {
        LLVMValueRef ret = this.visit(ctx.exp());
        LLVMBuildRet(builder, ret);
        return ret;
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
            ret = LLVMBuildSDiv(builder, lhs, rhs, "");//TODO choose which
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

        } else if (ctx.unaryOp().MINUS() != null) {
            ret = LLVMBuildNeg(builder,val,"");
        } else {        //PLUS

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
