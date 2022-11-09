//grammar SysY;
lexer grammar SysYLexer;

//prog : stat* EOF;	//语法需要以;结尾
//
//stat : expr ';'
//     | ID '=' expr ';'
//  	 | 'if' expr ';'
//     ;
//
////递归的描述表达式
////|:or
////():subrule
//expr : expr('*'|'/') expr //一个表达式由两个表达式通过*或/连接
//		 | expr('+'|'-') expr	//+或-连接但优先级低于*和/
//  	 | ID
//  	 | INT
//  	 ;

//第一个字符为字母或下划线，之后为任意多个的字母数字下划线
//ID : (LETTER|'_')(LETTER|DIGIT|'_')* ;
//
//INT : '0'|([1-9]DIGIT*) ;
//
//WS : [ \t\r\n]+ ->skip ;
//
////单行注释
////.通配符
//SL_COMMENT : '//' .*? '\n';
//
////fragment:当识别到单独的字母或数字时不是token
//fragment LETTER : [a-zA-Z] ;
//fragment DIGIT : [0-9] ;

//
CONST : 'const';

INT : 'int';

VOID : 'void';

IF : 'if';

ELSE : 'else';

WHILE : 'while';

BREAK : 'break';

CONTINUE : 'continue';

RETURN : 'return';

PLUS : '+';

MINUS : '-';

MUL : '*';

DIV : '/';

MOD : '%';

ASSIGN : '=';

EQ : '==';

NEQ : '!=';

LT : '<';

GT : '>';

LE : '<=';

GE : '>=';

NOT : '!';

AND : '&&';

OR : '||';

L_PAREN : '(';

R_PAREN : ')';

L_BRACE : '{';

R_BRACE : '}';

L_BRACKT : '[';

R_BRACKT : ']';

COMMA : ',';

SEMICOLON : ';';

IDENT : ('_'|LETTER)('_'|LETTER|DIGIT)*;
//以下划线或字母开头，仅包含下划线、英文字母大小写、阿拉伯数字

INTEGR_CONST :DECIMAL | OCTAL |HEXADECIMAL
//数字常量，包含十进制数，0开头的八进制数，0x或0X开头的十六进制数
   ;

WS: [ \r\n\t]+;

LINE_COMMENT: '//' .*? '\n';

MULTILINE_COMMENT:'/*' .*? '*/';

fragment LETTER: [a-zA-Z];

fragment DECIMAL : '0'|([1-9]DIGIT*) ;

fragment OCTAL: '0'[0-7]+;

fragment HEXADECIMAL: '0'('x'|'X')[0-9a-fA-F]+;

fragment DIGIT:     [0-9];