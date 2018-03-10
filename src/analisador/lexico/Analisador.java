package analisador.lexico;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Analisador {
	
	private List<String> codeLines = new ArrayList<String>();
	private Token previousToken;
	private Token nextToken;
	private String currentLineContent;
	private int currentLine = 0, currentColumn = 0;
	
	public void readFile(String filepath) throws IOException, FileNotFoundException{
		
		BufferedReader reader = new BufferedReader(new FileReader(filepath));
		String codeLine = reader.readLine();

		while (codeLine != null) {
			
			codeLines.add(codeLine);
			codeLine = reader.readLine();
			
		}
		System.out.println("Program has "+codeLines.size()+" lines of code\n");
		reader.close();
	
	}
	
	public boolean hasNextToken() {
		
		if(!codeLines.isEmpty() && currentLine < codeLines.size()) {
			currentLineContent = codeLines.get(currentLine);
			
			if(currentColumn > currentLineContent.length()) {
				currentLine++;
				currentColumn = 0;
			}
			while(currentLineContent.substring(currentColumn).matches("\\s*") && currentLine < codeLines.size()) {
				currentLineContent = codeLines.get(currentLine);
				currentColumn = 0;
				currentLine++;
			}
			System.out.println("(" + currentLine + " < " + codeLines.size() + ") ? ");
			return (currentLine < codeLines.size());
			
		}
		return false;
	}
	
	private TokenCategory analizeTokenCategory(String tokenValue) {
		if(LexemeTable.palavrasReservadas.containsKey(tokenValue)) return LexemeTable.palavrasReservadas.get(tokenValue);
		else if(LexemeTable.separadores.containsKey(tokenValue)) return LexemeTable.separadores.get(tokenValue);
		else if(LexemeTable.operadores.containsKey(tokenValue)) return LexemeTable.operadores.get(tokenValue);
		else return isConsOrId(tokenValue);
	}
	
	private TokenCategory isConsOrId(String tokenValue) {
		//int constant
		if(tokenValue.matches("\\d+")) return TokenCategory.intCons;
		//float constant
		else if(tokenValue.matches("(\\d)+\\.(\\d)+")) return TokenCategory.floatCons;
		//string constant
		else if(tokenValue.startsWith("\"")) {
			if(tokenValue.endsWith("\"")) return TokenCategory.stringCons;
			else errorMessage("Missing terminating character \"", tokenValue);
		}
		//char constant
		else if(tokenValue.startsWith("\'")) {
			if(tokenValue.length() > 3) errorMessage("Invalid character", tokenValue);
			else if(tokenValue.endsWith("\'")) return TokenCategory.charCons;
			else errorMessage("Missing terminating character \'", tokenValue);
		}
		//bool constant
		else if(tokenValue.equals("true") || tokenValue.equals("false")) return TokenCategory.boolCons;
		//variable id
		else if(tokenValue.matches("[a-zA-Z](\\w)*")) return TokenCategory.id;
		//unknown type
		return TokenCategory.unknown;
	}
	
	private void errorMessage(String description, String tokenValue) {
		System.err.println("Error at: "+currentLine+":"+currentColumn+" ~> "+tokenValue+", "+description);
		System.exit(1);
	}
}
