; ModuleID = 'moudle'
source_filename = "moudle"

@a = global <3 x i32> <i32 0, i32 1, i32 1>

define i32 @main() {
mainEntry:
  %a = load i32, i32* getelementptr (<3 x i32>, <3 x i32>* @a, i32 0, i32 2), align 4
  %ExpCond = icmp ne i32 %a, 0
  br i1 %ExpCond, label %If_true, label %Else

If_true:                                          ; preds = %mainEntry
  %a1 = load i32, i32* getelementptr (<3 x i32>, <3 x i32>* @a, i32 0, i32 1), align 4
  %0 = srem i32 %a1, 3
  %ExpCond2 = icmp ne i32 %0, 0
  br i1 %ExpCond2, label %If_true3, label %Else4

Else:                                             ; preds = %mainEntry
  br label %Out

Out:                                              ; preds = %Else, %Out5
  %a6 = load i32, i32* getelementptr (<3 x i32>, <3 x i32>* @a, i32 0, i32 1), align 4
  ret i32 %a6

If_true3:                                         ; preds = %If_true
  store i32 0, i32* getelementptr (<3 x i32>, <3 x i32>* @a, i32 0, i32 0), align 4
  br label %Out5

Else4:                                            ; preds = %If_true
  br label %Out5

Out5:                                             ; preds = %Else4, %If_true3
  br label %Out
}
