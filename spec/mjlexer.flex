package rs.ac.bg.etf.pp1;

import java_cup.runtime.Symbol;

%%

%{

	// ukljucivanje informacije o poziciji tokena
	private Symbol new_symbol(int type) {
		return new Symbol(type, yyline+1, yycolumn);
	}
	
	// ukljucivanje informacije o poziciji tokena
	private Symbol new_symbol(int type, Object value) {
		return new Symbol(type, yyline+1, yycolumn, value);
	}

%}

%cup
%line
%column

%xstate COMMENT

%eofval{
	return new_symbol(sym.EOF);
%eofval}

%%

"\r\n" 	{ }
"\n" 	{ }
" " 	{ }
"\b" 	{ }
"\t" 	{ }
"\n" 	{ }
"\f" 	{ }


"program"       { return new_symbol(sym.PROG, yytext()); }
"return" 	{ return new_symbol(sym.RETURN, yytext()); }
"print" 	{ return new_symbol(sym.PRINT, yytext()); }
"const"   	{ return new_symbol(sym.CONST, yytext()); }
"read" 	        { return new_symbol(sym.READ, yytext()); }
"void"   	{ return new_symbol(sym.VOID, yytext()); }
"new" 	        { return new_symbol(sym.NEW, yytext()); }
"++" 		{ return new_symbol(sym.INCR, yytext()); }
"--" 		{ return new_symbol(sym.DECR, yytext()); }
"+" 		{ return new_symbol(sym.PLUS, yytext()); }
"-" 		{ return new_symbol(sym.MINUS, yytext()); }
"=" 		{ return new_symbol(sym.EQUAL, yytext()); }
";" 		{ return new_symbol(sym.SEMIC, yytext()); }
"," 		{ return new_symbol(sym.COMMA, yytext()); }
"(" 		{ return new_symbol(sym.LPAREN, yytext()); }
")" 		{ return new_symbol(sym.RPAREN, yytext()); }
"{" 		{ return new_symbol(sym.LBRACE, yytext()); }
"}"			{ return new_symbol(sym.RBRACE, yytext()); }
"[" 		{ return new_symbol(sym.LANGLE, yytext()); }
"]"			{ return new_symbol(sym.RANGLE, yytext()); }
"+" 		{ return new_symbol(sym.PLUS, yytext()); }
"*" 		{ return new_symbol(sym.MUL, yytext()); }
"/" 		{ return new_symbol(sym.DIV, yytext()); }
"%" 		{ return new_symbol(sym.MOD, yytext()); }


<YYINITIAL> "//" 		     { yybegin(COMMENT); }
<COMMENT> .      { yybegin(COMMENT); }
<COMMENT> "\r\n" { yybegin(YYINITIAL); }
<COMMENT> "\n" { yybegin(YYINITIAL); }


"true"|"false" { return new_symbol(sym.BOOL, new Boolean (yytext().equals("true") ? true : false)); }

[0-9]+  { return new_symbol(sym.NUMBER, new Integer (yytext())); }
([a-z]|[A-Z])[a-z|A-Z|0-9|_]* 	{ return new_symbol(sym.IDENT, yytext()); }

"'"."'" { return new_symbol(sym.CHAR, new Character (yytext().charAt(1))); }


. { System.err.println("Leksicka greska ("+yytext()+") u liniji "+(yyline+1)); }