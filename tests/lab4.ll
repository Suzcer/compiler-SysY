; ModuleID = 'moudle'
source_filename = "moudle"

define i32 @main() {
mainEntry:
  %a = alloca i32, align 4
  store i32 4, i32* %a, align 4
  br label %whileCond

whileCond:                                        ; preds = %IfOut, %mainEntry
  %a1 = load i32, i32* %a, align 4
  %icmp = icmp ne i32 %a1, 0
  br i1 %icmp, label %whileBody, label %whileExit

whileBody:                                        ; preds = %whileCond
  %a2 = load i32, i32* %a, align 4
  %EQ = icmp eq i32 %a2, 2
  %eq = zext i1 %EQ to i32
  %icmp3 = icmp ne i32 %eq, 0
  br i1 %icmp3, label %IfBody, label %Else

whileExit:                                        ; preds = %whileCond
  %a7 = load i32, i32* %a, align 4
  ret i32 %a7

IfBody:                                           ; preds = %whileBody
  %a4 = load i32, i32* %a, align 4
  %sub = sub i32 %a4, 1
  store i32 %sub, i32* %a, align 4
  br label %IfOut

Else:                                             ; preds = %whileBody
  br label %IfOut

IfOut:                                            ; preds = %Else, %IfBody
  %a5 = load i32, i32* %a, align 4
  %sub6 = sub i32 %a5, 1
  store i32 %sub6, i32* %a, align 4
  br label %whileCond
}
