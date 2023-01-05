; ModuleID = 'moudle'
source_filename = "moudle"

@a = global <3 x i32> <i32 0, i32 1, i32 1>

define i32 @main() {
mainEntry:
  %a = load i32, i32* getelementptr (<3 x i32>, <3 x i32>* @a, i32 0, i32 2), align 4
  %ExpCond = icmp ne i32 %a, 0
  br i1 %ExpCond, label %If_true, label %If_false

If_true:                                          ; preds = %mainEntry
  %a1 = load i32, i32* getelementptr (<3 x i32>, <3 x i32>* @a, i32 0, i32 1), align 4
  %ExpCond2 = icmp ne i32 %a1, 0
  br i1 %ExpCond2, label %If_true3, label %If_false4        a[1]

If_false:                                         ; preds = %mainEntry
  br label %Out

Out:                                              ; preds = %If_false, %Out5
  %a6 = load i32, i32* getelementptr (<3 x i32>, <3 x i32>* @a, i32 0, i32 1), align 4
  ret i32 %a6

If_true3:                                         ; preds = %If_true
  store i32 0, i32* getelementptr (<3 x i32>, <3 x i32>* @a, i32 0, i32 0), align 4
  br label %Out5

If_false4:                                        ; preds = %If_true
  store i32 1, i32* getelementptr (<3 x i32>, <3 x i32>* @a, i32 0, i32 0), align 4
  br label %Out5

Out5:                                             ; preds = %If_false4, %If_true3
  br label %Out
}
