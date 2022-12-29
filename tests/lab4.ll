; ModuleID = 'moudle'
source_filename = "moudle"

define i32 @main() {
mainEntry:
  %a = alloca i32, align 4
  store i32 1, i32* %a, align 4
  %b = alloca <1 x i32>, align 4
  %pointer = getelementptr <1 x i32>, <1 x i32>* %b, i32 0, i32 0
  store i32 1, i32* %pointer, align 4
  %ret = getelementptr <1 x i32>, <1 x i32>* %b, i32 0, i32 0
  %b1 = load i32, i32* %ret, align 4
  ret i32 %b1
}

define i32 @f() {
fEntry:
  ret void
}
