grammar ICSS;

//--- LEXER: ---
// IF support:
IF: 'if';
BOX_BRACKET_OPEN: '[';
BOX_BRACKET_CLOSE: ']';


//Literals
TRUE: 'TRUE';
FALSE: 'FALSE';
PIXELSIZE: [0-9]+ 'px';
PERCENTAGE: [0-9]+ '%';
SCALAR: [0-9]+;

//Color value takes precedence over id idents
COLOR: '#' [0-9a-f] [0-9a-f] [0-9a-f] [0-9a-f] [0-9a-f] [0-9a-f];

//Specific identifiers for id's and css classes
ID_IDENT: '#' [a-z0-9\-]+;
CLASS_IDENT: '.' [a-z0-9\-]+;

//General identifiers
LOWER_IDENT: [a-z] [a-z0-9\-]*;
CAPITAL_IDENT: [A-Z] [A-Za-z0-9_]*;

//All whitespace is skipped
WS: [ \t\r\n]+ -> skip;

//
OPEN_BRACE: '{';
CLOSE_BRACE: '}';
SEMICOLON: ';';
COLON: ':';
PLUS: '+';
MIN: '-';
MUL: '*';
ASSIGNMENT_OPERATOR: ':=';

//--- PARSER: ---

stylesheet: styleRule+;
styleRule: variableAssignment | selector OPEN_BRACE (decleration | ifClause)+ CLOSE_BRACE;
variableAssignment: variableReference ASSIGNMENT_OPERATOR expression SEMICOLON;
variableReference: CAPITAL_IDENT;
selector: tagSelector | idSelector | classSelector;
tagSelector: LOWER_IDENT;
idSelector: ID_IDENT;
classSelector: CLASS_IDENT;
ifClause: IF BOX_BRACKET_OPEN (variableReference| booleanLiteral) BOX_BRACKET_CLOSE OPEN_BRACE (decleration | ifClause)+ CLOSE_BRACE;
decleration: LOWER_IDENT COLON expression SEMICOLON;
expression: literal | variableReference | operation;



operation: (literal | variableReference) ((addOperation | multiplyOperation | subtractOperation) operation)?;
literal: pixelLiteral | colorLiteral | booleanLiteral | scalarLiteral | percentageLiteral;
addOperation: PLUS;
subtractOperation: MIN;
multiplyOperation: MUL;
colorLiteral: COLOR;
pixelLiteral: PIXELSIZE;
scalarLiteral: SCALAR;
percentageLiteral: PERCENTAGE;
booleanLiteral: TRUE | FALSE;

//operation: '(' operation ')'|
//    operation '^' operation|
//    operation ('*' operation|
//    operation '/' operation|
//    operation ('+' | '-') operation|
//    (PIXELSIZE|PERCENTAGE|SCALAR|CAPITAL_IDENT));
