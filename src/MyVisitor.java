//import org.bytedeco.javacpp.Pointer;
//import org.bytedeco.javacpp.PointerPointer;
//import org.bytedeco.llvm.LLVM.*;
//
//import static org.bytedeco.llvm.global.LLVM.*;
//
//
//public class MyVisitor extends SysYParserBaseVisitor<LLVMValueRef>{
//
//    LLVMModuleRef module;
//    LLVMBuilderRef builder;
//    LLVMTypeRef i32Type;
//    LLVMValueRef zero;
//
//    public MyVisitor(){
//
//        //1. 初始化LLVM
//        LLVMInitializeCore(LLVMGetGlobalPassRegistry());
//        LLVMLinkInMCJIT();
//        LLVMInitializeNativeAsmPrinter();
//        LLVMInitializeNativeAsmParser();
//        LLVMInitializeNativeTarget();
//
//        //2. 创建module
//        module = LLVMModuleCreateWithName("module");
//
//        //3. 初始化IRBuilder，后续将使用这个builder去生成LLVM IR
//        builder = LLVMCreateBuilder();
//
//        //4. 考虑到我们的语言中仅存在int一个基本类型，可以通过下面的语句为LLVM的int型重命名方便以后使用
//        i32Type = LLVMInt32Type();
//        zero = LLVMConstInt(i32Type, 0, 0);
//
//        PointerPointer<Pointer> mainParamTypes = new PointerPointer<>(0);
//
//        LLVMTypeRef mainRetType = LLVMFunctionType(i32Type, mainParamTypes, 0, 0);
//
//        LLVMValueRef mainFunction = LLVMAddFunction(module, "main", mainRetType);
//
//        LLVMBasicBlockRef mainEntry = LLVMAppendBasicBlock(mainFunction, "");
//        LLVMPositionBuilderAtEnd(builder, mainEntry);
//
//        LLVMValueRef alloca1 = LLVMBuildAlloca(builder, i32Type, "");
//
//        LLVMBuildStore(builder, zero, alloca1);
//    }
//
//    @Override
//    public LLVMValueRef visitProgram(SysYParser.ProgramContext ctx) {
//
//        return super.visitProgram(ctx);
//    }
//
//
//
//}
