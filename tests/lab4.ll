; ModuleID = 'moudle'
source_filename = "moudle"

define i32 @main() {
mainEntry:
  %a = alloca <3 x i32>, align 16
  %pointer = getelementptr <3 x i32>, <3 x i32>* %a, i32 0, i32 0
  store i32 1, i32* %pointer, align 4
  %pointer1 = getelementptr <3 x i32>, <3 x i32>* %a, i32 0, i32 1
  store i32 2, i32* %pointer1, align 4
  %pointer2 = getelementptr <3 x i32>, <3 x i32>* %a, i32 0, i32 2
  store i32 3, i32* %pointer2, align 4
  %ret = getelementptr <3 x i32>, <3 x i32>* %a, i32 0, i32 1
  %a3 = load i32, i32* %ret, align 4
  ret i32 %a3
}
