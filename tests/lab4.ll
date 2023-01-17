; ModuleID = 'moudle'
source_filename = "moudle"

define i32 @main() {
mainEntry:
  %a = alloca i32, align 4
  store i32 1, i32* %a, align 4
  %a1 = load i32, i32* %a, align 4
  %EQ = icmp eq i32 %a1, 1
  %eq = zext i1 %EQ to i32
  %icmp = icmp ne i32 %eq, 0
  br i1 %icmp, label %IfBody, label %Else

IfBody:                                           ; preds = %mainEntry
  ret i32 1
  br label %IfOut

Else:                                             ; preds = %mainEntry
  ret i32 2
  br label %IfOut

IfOut:                                            ; preds = %Else, %IfBody
}
