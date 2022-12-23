//
// Created by hengxin on 12/14/22.
//

// command: clang -S -emit-llvm factorial1.c -o f0-opt0.ll
int factorial(int val);

int main(int argc, char **argv) {
  return factorial(2) * 7 == 42;
}

// precondition: val is non-negative
int factorial(int val) {
  if (val == 0) {   //entry1
    return 1;
  }
                    //entry2
  return val * factorial(val - 1);
}

// java result:
//; ModuleID = 'llvm.factorial1'
//source_filename = "llvm.factorial1"
//
//define i32 @main(i32 %0, i8** %1) {
//  %3 = alloca i32, align 4
//  %4 = alloca i32, align 4
//  %5 = alloca i8**, align 8
//  store i32 0, i32* %3, align 4
//  store i32 %0, i32* %4, align 4
//  store i8** %1, i8*** %5, align 8
//  %6 = call i32 @factorial(i32 2)
//  %7 = mul i32 %6, 7
//  %8 = icmp eq i32 %7, 42
//  %9 = zext i1 %8 to i32
//  ret i32 %9
//}
//
//define i32 @factorial(i32 %0) {
//  %2 = alloca i32, align 4
//  %3 = alloca i32, align 4
//  store i32 %0, i32* %3, align 4
//  %4 = load i32, i32* %3, align 4
//  %5 = icmp eq i32 %4, 0
//  br i1 %5, label %6, label %7
//
//6:                                                ; preds = %1
//  store i32 1, i32* %2, align 4
//  br label %13
//
//7:                                                ; preds = %1
//  %8 = load i32, i32* %3, align 4
//  %9 = load i32, i32* %3, align 4
//  %10 = sub i32 %9, 1
//  %11 = call i32 @factorial(i32 %10)
//  %12 = mul i32 %8, %11
//  store i32 %12, i32* %2, align 4
//  br label %13
//
//13:                                               ; preds = %7, %6
//  %14 = load i32, i32* %2, align 4
//  ret i32 %14
//}

