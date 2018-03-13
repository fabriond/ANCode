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
	/**
	 * Checks if the code read still has a next token
	 * @return true if there is a next token, false otherwise
	 */
	public boolean hasNextToken() {
		
		if(!codeLines.isEmpty() && currentLine < codeLines.size()) {
			currentLineContent = codeLines.get(currentLine);
			
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
	
	/**
	 * Reads the next token in the code 
	 * @return next token in code
	 */
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
			if(current == '"'){ //checa se é constante string
				tokenValue += current;
				current = nextCharacter();
				while(current != '\n'){ //lê string até \n ou "
					tokenValue += current;
					current = nextCharacter();
					if(current == '\\') {//lê possíveis caracteres de escape
						current = nextCharacter();
						tokenValue += escapeSequences(current);
						current = nextCharacter();
					}
					if(current == '"'){
							tokenValue += current;
							currentColumn++;
							break;
					}
				}
			} else if(current == '\''){ //checa se é constante char
				tokenValue += current;
				current = nextCharacter();
				if(current == '\\') { //lê possível caracter de escape
					current = nextCharacter();
					tokenValue += escapeSequences(current);
				}
				else if(current != '\n'){ //lê caracter caso não seja caracter de escape
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
			} else if(current == ';'){ //daqui pra frente é feita a leitura de símbolos
				tokenValue += current;
				current = nextCharacter();
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
		tokenValue = tokenValue.trim(); //espaços depois do token são retirados
		previousToken = currentToken; //usado para diferenciar unário negativo de subtração
		token = new Token(tokenValue, tokenLine, tokenCol,analyzeTokenCategory(tokenValue));
		currentToken = token;
		return token;
	}

	/**
	 * Gets next character in line
	 * @return next character in line
	 * @return '\n' if at the end of the current line
	 */
	private Character nextCharacter(){
		currentColumn++;
		if(currentColumn < currentLineContent.length()) return currentLineContent.charAt(currentColumn);
		else return '\n';
	}

	/**
	 * Returns category of a token, based on its lexical value
	 * @param tokenValue
	 * @return Category of given token as TokenCategory
	 */
	private TokenCategory analyzeTokenCategory(String tokenValue) {
		if(tokenValue.equals("-") && isUnaryNegative())	return TokenCategory.opUnNeg;
		else if(LexemeTable.palavrasReservadas.containsKey(tokenValue)) return LexemeTable.palavrasReservadas.get(tokenValue);
		else if(LexemeTable.separadores.containsKey(tokenValue)) return LexemeTable.separadores.get(tokenValue);
		else if(LexemeTable.operadores.containsKey(tokenValue)) return LexemeTable.operadores.get(tokenValue);
		else return isConsOrId(tokenValue);
	}

	/**
	 * Checks if token is a unary negative
	 * @return true if currentToken is unary negative and false if not 
	 */
	private boolean isUnaryNegative(){
		if(previousToken != null) {
			int categoryValue = previousToken.getCategory().getValue();
	
			if(categoryValue >= 21 && categoryValue <= 25) return false;
			else if(categoryValue == 2) return false;
	
			return true;
		}
		else return true;
	}
	
	/**
	 * Gets category of literal constants or ids by their lexical value
	 * @param tokenValue
	 * @return token's category, if any constant type or an id
	 * @returns TokenCategory.unknown if type is not constant or id
	 */
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
	
	/**
	 * Deals with escape sequences in string and char constants  
	 * @param current, the current char being read
	 * @return char that has to be appended to the token's value
	 * @returns exits program and returns an empty char, if current is not valid
	 */
	private char escapeSequences(char current) {
		if(current == '"' || current == '\'' || current == '\\') return current;
		else if(current == 'b') return '\b';
		else if(current == 't') return '\t';
		else if(current == 'n') return '\n';
		else if(current == 'f') return '\f';
		else if(current == 'r') return '\r';
		else{
			errorMessage("Invalid escape sequence(valid ones are \\b \\t \\n \\f \\r \\\" \\' \\\\)", "\\"+current);
			return Character.MIN_VALUE;
		}
	}
	
	/**
	 * Prints an error message and exits program
	 * @param description of the error message
	 * @param value of the token that called error
	 */
	private void errorMessage(String description, String tokenValue) {
		System.err.println("Error at: "+(currentLine+1)+":"+(currentColumn)+" ~> "+tokenValue+", "+description);
		System.exit(1);
	}
}
