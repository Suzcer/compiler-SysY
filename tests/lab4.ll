; ModuleID = 'moudle'
source_filename = "moudle"

define i32 @main() {
mainEntry:
  %a = alloca <2 x i32>, align 8
  %pointer = getelementptr <2 x i32>, <2 x i32>* %a, i32 0, i32 0
  store i32 0, i32* %pointer, align 4
  %pointer1 = getelementptr <2 x i32>, <2 x i32>* %a, i32 0, i32 1
  store i32 0, i32* %pointer1, align 4
  %c = alloca i32, align 4
  store i32 10, i32* %c, align 4
  %d = alloca i32, align 4
  store i32 20, i32* %d, align 4
  %c2 = load i32, i32* %c, align 4
  %ret = getelementptr <2 x i32>, <2 x i32>* %a, i32 0, i32 1
  %a3 = load i32, i32* %ret, align 4
  %0 = add i32 %c2, %a3
  store i32 %0, i32* %d, align 4
  ret i32 0
}
