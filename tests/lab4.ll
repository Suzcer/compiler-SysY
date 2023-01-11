; ModuleID = 'moudle'
source_filename = "moudle"

define i32 @main() {
mainEntry:
  %a = alloca i32, align 4
  store i32 1, i32* %a, align 4
  %b = alloca i32, align 4
  store i32 -1, i32* %b, align 4
  %a1 = load i32, i32* %a, align 4
  %b2 = load i32, i32* %b, align 4
  %NEQ = icmp ne i32 %a1, %b2
  %eq = zext i1 %NEQ to i32
  %icmp = icmp ne i32 %eq, 0
  br i1 %icmp, label %IfBody, label %Else

IfBody:                                           ; preds = %mainEntry
  br label %IfOut

Else:                                             ; preds = %mainEntry
  br label %IfOut

IfOut:                                            ; preds = %Else, %IfBody
  ret i32 0
}
