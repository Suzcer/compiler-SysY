; ModuleID = 'moudle'
source_filename = "moudle"

define i32 @main() {
mainEntry:
  %a = alloca i32, align 4
  store i32 0, i32* %a, align 4
  %a1 = load i32, i32* %a, align 4
  %0 = srem i32 %a1, 2
  ret i32 %0
}
