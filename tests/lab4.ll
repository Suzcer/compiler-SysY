; ModuleID = 'moudle'
source_filename = "moudle"

define i32 @main() {
mainEntry:
  %b = alloca i32, align 4
  store i32 2, i32* %b, align 4
  %a = alloca <2 x i32>, align 8
  %pointer = getelementptr <2 x i32>, <2 x i32>* %a, i32 0, i32 0
  store i32 1, i32* %pointer, align 4
  %pointer1 = getelementptr <2 x i32>, <2 x i32>* %a, i32 0, i32 1
  store i32 2, i32* %pointer1, align 4
  %ret = getelementptr <2 x i32>, <2 x i32>* %a, i32 0, i32 0
  %a2 = load i32, i32* %ret, align 4
  ret i32 %a2
}
