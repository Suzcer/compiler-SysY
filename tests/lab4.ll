; ModuleID = 'moudle'
source_filename = "moudle"

@a = global <3 x i32> <i32 0, i32 1, i32 1>

define i32 @main() {
mainEntry:
  %a = load i32, i32* getelementptr (<3 x i32>, <3 x i32>* @a, i32 0, i32 0), align 4
  %0 = icmp ne i32 0, %a
  %1 = xor i1 %0, true
  %2 = zext i1 %1 to i32
  store i32 %2, i32* getelementptr (<3 x i32>, <3 x i32>* @a, i32 0, i32 0), align 4
  %a1 = load i32, i32* getelementptr (<3 x i32>, <3 x i32>* @a, i32 0, i32 1), align 4
  ret i32 %a1
}
