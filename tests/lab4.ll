; ModuleID = 'moudle'
source_filename = "moudle"

define i32 @main() {
mainEntry:
  %b = alloca i32, align 4
  store i32 3, i32* %b, align 4
  %a = alloca i32, align 4
  %b1 = load i32, i32* %b, align 4
  store i32 %b1, i32* %a, align 4
  %b2 = load i32, i32* %b, align 4
  ret i32 1
}
