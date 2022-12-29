; ModuleID = 'moudle'
source_filename = "moudle"

define i32 @f() {
fEntry:
  ret i32 1
}

define i32 @main() {
mainEntry:
  %0 = call i32 @f()
  ret i32 0
}
