; ModuleID = 'moudle'
source_filename = "moudle"

define i32 @q(i32 %0, i32 %1, i32 %2) {
qEntry:
  %a = alloca i32, align 4
  store i32 %0, i32* %a, align 4
  %b = alloca i32, align 4
  store i32 %1, i32* %b, align 4
  %c = alloca i32, align 4
  store i32 %2, i32* %c, align 4
  ret i32 1
}

define i32 @main() {
mainEntry:
  %d = alloca i32, align 4
  %0 = call i32 @q(i32 1, i32 2, i32 3)
  store i32 %0, i32* %d, align 4
  %1 = call i32 @q(i32 1, i32 2, i32 3)
  ret i32 0
}
