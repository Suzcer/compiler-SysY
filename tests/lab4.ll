; ModuleID = 'moudle'
source_filename = "moudle"

@a = global <3 x i32> <i32 0, i32 1, i32 1>
@b = global i32 1
@c = global i32 2

define i32 @main() {
mainEntry:
  %a = load i32, i32* getelementptr (<3 x i32>, <3 x i32>* @a, i32 0, i32 0), align 4
  %ExpCond = icmp ne i32 %a, 0
  br i1 %ExpCond, label %If_true, label %Else

If_true:                                          ; preds = %mainEntry
  br label %Out

Else:                                             ; preds = %mainEntry
  br label %Out

Out:                                              ; preds = %Else, %If_true
  %a1 = load i32, i32* getelementptr (<3 x i32>, <3 x i32>* @a, i32 0, i32 1), align 4
  ret i32 %a1
}
