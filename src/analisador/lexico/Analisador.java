package analisador.lexico;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Analisador {
	
	private List<String> codeLines = new ArrayList<String>();
	private Token previousToken;
	private Token currentToken;
	private String currentLineContent;
	private int currentLine = 0, currentColumn = 0, tokenLine = 0, tokenCol = 0;
	
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
			
			//System.out.println(currentLineContent.length());
			//System.out.println(currentLine+" "+currentColumn);
			if(currentColumn > currentLineContent.length()) {
				currentLine++;
				currentColumn = 0;
			}

			while(currentLineContent.substring(currentColumn).matches("\\s*") && currentLine < codeLines.size()-1) {
				currentColumn = 0;
				currentLine++;
				currentLineContent = codeLines.get(currentLine);
				//System.out.print(currentLine+" - "+currentLineContent+"\n");
			}
			
			//System.out.println("(" + (currentLine) + " < " + (codeLines.size()) + ") ? ");
			return (!currentLineContent.substring(currentColumn).matches("\\s*") && currentLine < codeLines.size());
			
		}
		return false;
	}

	public Token nextToken(){
		Token token;
		char current;
		String tokenValue = "";
		current = currentLineContent.charAt(currentColumn);
		tokenCol = currentColumn;
		tokenLine = currentLine;
		while(current == ' ' || current == '\t'){
			current = nextCharacter();
			tokenCol++;
		}
		
		if(Character.toString(current).matches("\\d")){
			tokenValue += current;
			current = nextCharacter();
			while(Character.toString(current).matches("\\d")){
				tokenValue += current;
				current = nextCharacter();
			}
			if(current == '.'){
				tokenValue += current;
				current = nextCharacter();
				while(Character.toString(current).matches("\\d")){
					tokenValue += current;
					current = nextCharacter();
				}
			}
			if(current != ' '){
				while(!LexemeTable.tokenEndings.contains(current)){
					tokenValue += current;
					current = nextCharacter();
					if(current == '\n') break;
				}
			}
		} else {
			while(!LexemeTable.tokenEndings.contains(current)){
				tokenValue += current;
				current = nextCharacter();
				if(current == '\n') break;
			}
		}

		if(tokenValue == ""){
			if(current == '"'){
				tokenValue += current;
				current = nextCharacter();
				while(current != '\n'){
					tokenValue += current;
					current = nextCharacter();
					if(current == '"'){
						if(tokenValue.endsWith("\\")) {
							//checar se barra deve ser ocultado ou não
							tokenValue = tokenValue.replace("\\", "");
							tokenValue += current;
							current = nextCharacter();
						} else {
							tokenValue += current;
							currentColumn++;
							break;
						}
					}
				}
			} else if(current == ';'){
				tokenValue += current;
				current = nextCharacter();
			} else if(current == '\''){
				tokenValue += current;
				current = nextCharacter();
				if(current == '\\') {
					//checar se barra deve ser ocultado ou não
					tokenValue += current; 
					current = nextCharacter();
				}
				if(current != '\n'){
					tokenValue += current;
				}
				current = nextCharacter();
				if(current == '\''){
					tokenValue += current;
					currentColumn++;
				}
			} else if(current == '<' || current == '>' || current == '+' || current == '=' ){
				tokenValue += current;
				current = nextCharacter();
				if(current == '='){
					tokenValue += current;
					currentColumn++;
				}
			} else if(current == '#'){
				tokenValue += current;
				currentColumn++;
			} else if(current == ','){
				tokenValue += current;
				currentColumn++;
			} else if(current == '('){
				tokenValue += current;
				currentColumn++;
			} else if(current == ')'){
				tokenValue += current;
				currentColumn++;
			} else if(current == '['){
				tokenValue += current;
				currentColumn++;
			} else if(current == ']'){
				tokenValue += current;
				currentColumn++;
			} else if(current == '{'){
				tokenValue += current;
				currentColumn++;
			} else if(current == '}'){
				tokenValue += current;
				currentColumn++;
			} else if(current == '!'){
				tokenValue += current;
				currentColumn++;
			} else if(current == '&'){
				tokenValue += current;
				current = nextCharacter();
				if(current == '&'){
					tokenValue += current;
					currentColumn++;
				}
			} else if(current == '|'){
				tokenValue += current;
				current = nextCharacter();
				if(current == '|'){
					tokenValue += current;
					currentColumn++;
				}
			} else if(current == '^'){
				tokenValue += current;
				currentColumn++;
			} else {
				tokenValue += current;
				currentColumn++;
			}
		}
		tokenValue = tokenValue.trim();
		previousToken = currentToken;
		token = new Token(tokenValue, tokenLine, tokenCol,analizeTokenCategory(tokenValue));
		currentToken = token;
		return token;
	}

	private Character nextCharacter(){
		currentColumn++;
		if(currentColumn < currentLineContent.length()) return currentLineContent.charAt(currentColumn);
		else return '\n';
	}
	private TokenCategory analizeTokenCategory(String tokenValue) {
		if(tokenValue.equals("-") && isUnaryNegative())	return TokenCategory.opUnNeg;
		else if(LexemeTable.palavrasReservadas.containsKey(tokenValue)) return LexemeTable.palavrasReservadas.get(tokenValue);
		else if(LexemeTable.separadores.containsKey(tokenValue)) return LexemeTable.separadores.get(tokenValue);
		else if(LexemeTable.operadores.containsKey(tokenValue)) return LexemeTable.operadores.get(tokenValue);
		else return isConsOrId(tokenValue);
	}

	private boolean isUnaryNegative(){
		if(previousToken != null) {
			int categoryValue = previousToken.getCategory().getValue();
	
			if(categoryValue >= 21 && categoryValue <= 25) return false;
			else if(categoryValue == 2) return false;
	
			return true;
		}
		else return true;
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
			if(tokenValue.length() > 3 && !tokenValue.contains("\\")) errorMessage("Invalid character", tokenValue);
			else if(tokenValue.endsWith("\'")) return TokenCategory.charCons;
			else errorMessage("Missing terminating character \'", tokenValue);
		}
		//bool constant
		else if(tokenValue.equals("true") || tokenValue.equals("false")) return TokenCategory.boolCons;
		//variable id
		else if(tokenValue.matches("[a-z_A-Z](\\w)*")) return TokenCategory.id;
		//unknown type
		return TokenCategory.unknown;
	}
	
	private void errorMessage(String description, String tokenValue) {
		System.err.println("Error at: "+(currentLine+1)+":"+(currentColumn+1)+" ~> "+tokenValue+", "+description);
		System.exit(1);
	}
}
