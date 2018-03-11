package analisador.lexico;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LexemeTable {
	
	public static Map<String, TokenCategory> palavrasReservadas = new HashMap<String, TokenCategory>();
	public static Map<String, TokenCategory> separadores = new HashMap<String, TokenCategory>();
	public static Map<String, TokenCategory> operadores = new HashMap<String, TokenCategory>();
	public static List<Character> tokenEndings = new ArrayList<Character>();
	
	static {
		
		palavrasReservadas.put("main", TokenCategory.main);
		palavrasReservadas.put("int:", TokenCategory.intType);
		palavrasReservadas.put("float:", TokenCategory.floatType);
		palavrasReservadas.put("char:", TokenCategory.charType);
		palavrasReservadas.put("bool:", TokenCategory.boolType);
		palavrasReservadas.put("string:", TokenCategory.stringType);
		palavrasReservadas.put("array:", TokenCategory.arrayType);
		palavrasReservadas.put("void:", TokenCategory.funVoid);
		palavrasReservadas.put("if", TokenCategory.estIf);
		palavrasReservadas.put("elsif", TokenCategory.estElsif);
		palavrasReservadas.put("else", TokenCategory.estElse);
		palavrasReservadas.put("loop", TokenCategory.estLoop);
		palavrasReservadas.put("do", TokenCategory.estDo);
		palavrasReservadas.put("while", TokenCategory.estWhile);
		palavrasReservadas.put("fun", TokenCategory.funDef);
		palavrasReservadas.put("print", TokenCategory.instPrint);
		palavrasReservadas.put("read", TokenCategory.instRead);
		palavrasReservadas.put("return", TokenCategory.funReturn);
		
		separadores.put(",", TokenCategory.commaSep);
		separadores.put("!", TokenCategory.loopSep);
		separadores.put(";", TokenCategory.lineEnd);
		separadores.put("{", TokenCategory.escBegin);
		separadores.put("}", TokenCategory.escEnd);
		separadores.put("[", TokenCategory.arrayBegin);
		separadores.put("]", TokenCategory.arrayEnd);
		separadores.put("(", TokenCategory.paramBegin);
		separadores.put(")", TokenCategory.paramEnd);
		separadores.put("", TokenCategory.EOF);
		
		operadores.put("+", TokenCategory.opAd);
		operadores.put("-", TokenCategory.opAd);
		operadores.put("*", TokenCategory.opMult);
		operadores.put("/", TokenCategory.opMult);
		operadores.put("^", TokenCategory.opExp);
		operadores.put("=", TokenCategory.opEq);
		operadores.put("==", TokenCategory.opRelEq);
		operadores.put("#", TokenCategory.opRelEq);
		operadores.put("<", TokenCategory.opRelLtGt);
		operadores.put(">", TokenCategory.opRelLtGt);
		operadores.put("<=", TokenCategory.opRelLtGt);
		operadores.put(">=", TokenCategory.opRelLtGt);
		operadores.put("+=", TokenCategory.opConc);
		operadores.put("&&", TokenCategory.opLogAnd);
		operadores.put("||", TokenCategory.opLogOr);
		
		tokenEndings.add(' ');
		tokenEndings.add('\t');
		tokenEndings.add(',');
		tokenEndings.add(';');
		tokenEndings.add('+');
		tokenEndings.add('-');
		tokenEndings.add('*');
		tokenEndings.add('\\');
		tokenEndings.add('/');
		tokenEndings.add('#');
		tokenEndings.add('!');
		tokenEndings.add('<');
		tokenEndings.add('>');
		tokenEndings.add('=');
		tokenEndings.add('(');
		tokenEndings.add(')');
		tokenEndings.add('[');
		tokenEndings.add(']');
		tokenEndings.add('{');
		tokenEndings.add('}');
		tokenEndings.add('\'');
		tokenEndings.add('"');
		
	}

}
