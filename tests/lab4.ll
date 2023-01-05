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
  %a1 = load i32, i32* getelementptr (<3 x i32>, <3 x i32>* @a, i32 0, i32 0), align 4
  %ExpCond = icmp ne i32 %a1, 0
  %a2 = load i32, i32* getelementptr (<3 x i32>, <3 x i32>* @a, i32 0, i32 1), align 4
  %ExpCond3 = icmp ne i32 %a2, 0
  %GT = icmp sgt i1 %ExpCond, %ExpCond3
  %a4 = load i32, i32* getelementptr (<3 x i32>, <3 x i32>* @a, i32 0, i32 2), align 4
  %ExpCond5 = icmp ne i32 %a4, 0
  %GT6 = icmp sgt i1 %GT, %ExpCond5
  br i1 %GT6, label %If_true, label %If_false

If_true:                                          ; preds = %mainEntry
  store i32 2, i32* getelementptr (<3 x i32>, <3 x i32>* @a, i32 0, i32 1), align 4
  br label %Out

If_false:                                         ; preds = %mainEntry
  store i32 5, i32* getelementptr (<3 x i32>, <3 x i32>* @a, i32 0, i32 2), align 4
  br label %Out

Out:                                              ; preds = %If_false, %If_true
  %a7 = load i32, i32* getelementptr (<3 x i32>, <3 x i32>* @a, i32 0, i32 0), align 4
  %ExpCond8 = icmp ne i32 %a7, 0
  %a9 = load i32, i32* getelementptr (<3 x i32>, <3 x i32>* @a, i32 0, i32 1), align 4
  %ExpCond10 = icmp ne i32 %a9, 0
  %GT11 = icmp sgt i1 %ExpCond8, %ExpCond10
  %a12 = load i32, i32* getelementptr (<3 x i32>, <3 x i32>* @a, i32 0, i32 2), align 4
  %ExpCond13 = icmp ne i32 %a12, 0
  %GT14 = icmp sgt i1 %GT11, %ExpCond13
  store i32 2, i32* getelementptr (<3 x i32>, <3 x i32>* @a, i32 0, i32 1), align 4
  store i32 5, i32* getelementptr (<3 x i32>, <3 x i32>* @a, i32 0, i32 2), align 4
  %a15 = load i32, i32* getelementptr (<3 x i32>, <3 x i32>* @a, i32 0, i32 1), align 4
  ret i32 %a15
}
