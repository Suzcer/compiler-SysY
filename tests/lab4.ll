; ModuleID = 'moudle'
source_filename = "moudle"

@a = global <2 x i32> <i32 1, i32 0>
@c = global i32 2

define i32 @main() {
mainEntry:
  %b = alloca i32, align 4
  store i32 1, i32* %b, align 4
  %a = load i32, i32* getelementptr (<2 x i32>, <2 x i32>* @a, i32 0, i32 2), align 4
  store i32 %a, i32* %b, align 4
  %a1 = load i32, i32* getelementptr (<2 x i32>, <2 x i32>* @a, i32 0, i32 0), align 4
  ret i32 %a1
}
