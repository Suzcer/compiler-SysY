; ModuleID = 'moudle'
source_filename = "moudle"

@a = global <3 x i32> <i32 1, i32 2, i32 0>

define i32 @main() {
mainEntry:
  %b = alloca i32, align 4
  store i32 1, i32* %b, align 4
  %b1 = load i32, i32* %b, align 4
  %a = load i32, i32* getelementptr (<3 x i32>, <3 x i32>* @a, i32 0, i32 541749448), align 4
  store i32 %a, i32* %b, align 4
  %a2 = load i32, i32* getelementptr (<3 x i32>, <3 x i32>* @a, i32 0, i32 0), align 4
  ret i32 %a2
}
