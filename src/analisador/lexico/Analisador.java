package analisador.lexico;

import java.util.HashMap;
import java.util.Map;

public class Analisador {

	static {
		
		Map<String, TokenCategory> palavrasReservadas = new HashMap<String, TokenCategory>();
		palavrasReservadas.put("main", TokenCategory.main);
		palavrasReservadas.put("int", TokenCategory.intType);
		palavrasReservadas.put("float", TokenCategory.floatType);
		palavrasReservadas.put("char", TokenCategory.charType);
		palavrasReservadas.put("bool", TokenCategory.boolType);
		palavrasReservadas.put("string", TokenCategory.stringType);
		palavrasReservadas.put("array", TokenCategory.arrayType);
		palavrasReservadas.put("void", TokenCategory.funVoid);
		palavrasReservadas.put("if", TokenCategory.estIf);
		palavrasReservadas.put("elsif", TokenCategory.estElsif);
		palavrasReservadas.put("else", TokenCategory.estElse);
		palavrasReservadas.put("loop", TokenCategory.estLoop);
		palavrasReservadas.put("do", TokenCategory.estDo);
		palavrasReservadas.put("while", TokenCategory.estWhile);
		palavrasReservadas.put("fun", TokenCategory.funDef);
		
		Map<String, TokenCategory> separadores = new HashMap<String, TokenCategory>();
		separadores.put(",", TokenCategory.commaSep);
		separadores.put("!", TokenCategory.loopSep);
		separadores.put(";", TokenCategory.lineEnd);
		separadores.put("{", TokenCategory.escBegin);
		separadores.put("}", TokenCategory.escEnd);
		separadores.put("[", TokenCategory.arrayBegin);
		separadores.put("]", TokenCategory.arrayEnd);
		separadores.put("(", TokenCategory.paramBegin);
		separadores.put(")", TokenCategory.paramEnd);
		
		Map<String, TokenCategory> operadores = new HashMap<String, TokenCategory>();
		operadores.put("+", TokenCategory.opAd);
		operadores.put("-", TokenCategory.opAd);
		operadores.put("*", TokenCategory.opMult);
		operadores.put("/", TokenCategory.opMult);
		operadores.put("^", TokenCategory.opExp);
		operadores.put("==", TokenCategory.opRelEq);
		operadores.put("#", TokenCategory.opRelEq);
		operadores.put("<", TokenCategory.opRelLtGt);
		operadores.put(">", TokenCategory.opRelLtGt);
		operadores.put("<=", TokenCategory.opRelLtGt);
		operadores.put(">=", TokenCategory.opRelLtGt);
		operadores.put("+=", TokenCategory.opConc);
		
	}
	
	
	
}
