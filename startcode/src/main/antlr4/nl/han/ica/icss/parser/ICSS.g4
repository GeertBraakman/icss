grammar ICSS;

//--- LEXER: ---
// IF support:
IF: 'if';
ELSE: 'else';
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
COMMA: ',';
PLUS: '+';
MIN: '-';
MUL: '*';
ASSIGNMENT_OPERATOR: ':=';

//--- PARSER: ---

stylesheet: (variableAssignment | styleRule)+;
styleRule: selector OPEN_BRACE body+ CLOSE_BRACE;
variableAssignment: variableReference ASSIGNMENT_OPERATOR expression SEMICOLON;
variableReference: CAPITAL_IDENT;
selector: (tagSelector | idSelector | classSelector) (COMMA selector)*;
tagSelector: LOWER_IDENT;
idSelector: ID_IDENT;
classSelector: CLASS_IDENT;
ifClause: IF BOX_BRACKET_OPEN (variableReference| booleanLiteral) BOX_BRACKET_CLOSE OPEN_BRACE body+ CLOSE_BRACE elseClause?;
elseClause: ELSE OPEN_BRACE body+ CLOSE_BRACE;
body: variableAssignment | decleration | ifClause;
decleration: propertyName COLON expression SEMICOLON;
propertyName: LOWER_IDENT;

expression:   expression MUL expression  #multiplyOperation
            | expression PLUS expression #addOperation
            | expression MIN expression  #subtractOperation
            | variableReference          #varReference
            | literal                    #lit;

literal: pixelLiteral
       | colorLiteral
       | booleanLiteral
       | scalarLiteral
       | percentageLiteral;

colorLiteral: COLOR;
pixelLiteral: PIXELSIZE;
scalarLiteral: SCALAR;
percentageLiteral: PERCENTAGE;
booleanLiteral: TRUE | FALSE;
