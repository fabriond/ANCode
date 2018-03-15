package analisador.lexico;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Lexic {
	
	private List<String> codeLines = new ArrayList<String>();
	private Token previousToken;
	private Token currentToken;
	private String currentLineContent;
	private Error cachedError;//serve pra imprimir erros após a impressão do token que deu erro
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
		sendError();
		if(!codeLines.isEmpty() && currentLine < codeLines.size()) {
			currentLineContent = codeLines.get(currentLine);
			
			if(currentColumn > currentLineContent.length()) {
				currentColumn = 0;
				currentLine++;
				currentLineContent = codeLines.get(currentLine);
			}
			
			// \\s significa whitespace
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
		} else {
			while(!LexemeTable.tokenEndings.contains(current)){
				tokenValue += current;
				current = nextCharacter();
			}
		}
		
		if(tokenValue == ""){
			if(current == '"'){ //checa se é constante string
				tokenValue += current;
				current = nextCharacter();
				if(current == '"'){ //checa se string é vazia
					tokenValue += current;
					currentColumn++;
				} else {
					while(current != '\n'){ //lê string até \n ou "
						if(current == '\\') current = nextCharacter();
						tokenValue += current;
						current = nextCharacter();
						if(current == '"'){
								tokenValue += current;
								currentColumn++;
								break;
						}
					}
				}
			} else if(current == '\''){ //checa se é constante char
				tokenValue += current;
				current = nextCharacter();
				if(current == '\\') { //lê possível caracter de escape
					current = nextCharacter();
					tokenValue += current;
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
			} else if(current == '.'){
				tokenValue += current;
				current = nextCharacter();
				while(Character.toString(current).matches("\\d")){
					tokenValue += current;
					current = nextCharacter();
				}
			}else if(current == '&'){
				tokenValue += current;
				current = nextCharacter();
				if(current == '&'){
					tokenValue += current;
					currentColumn++;
				} else errorMessage("Missing &", tokenValue);
			} else if(current == '|'){
				tokenValue += current;
				current = nextCharacter();
				if(current == '|'){
					tokenValue += current;
					currentColumn++;
				} else errorMessage("Missing |", tokenValue);
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
			else if(categoryValue == 2 || categoryValue == 17) return false;
	
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
			if(tokenValue.length() > 1 && tokenValue.endsWith("\"")) return TokenCategory.stringCons;
			else errorMessage("Missing terminating character \"", tokenValue);
		}
		//char constant
		else if(tokenValue.startsWith("\'")) {
			if(tokenValue.length() > 3) errorMessage("Invalid character constant", tokenValue);
			else if(tokenValue.length() > 1 && tokenValue.endsWith("\'")) return TokenCategory.charCons;
			else errorMessage("Missing terminating character \'", tokenValue);
		}
		//bool constant
		else if(tokenValue.equals("true") || tokenValue.equals("false")) return TokenCategory.boolCons;
		//variable id
		else if(tokenValue.matches("[a-z_A-Z](\\w)*")) return TokenCategory.id;
		//casos de erro de constantes numéricas e ids
		else if(tokenValue.matches("\\.\\d+")) errorMessage("Missing number before decimal point", tokenValue);
		else if(tokenValue.matches("\\d+\\.")) errorMessage("Missing number after decimal point", tokenValue);
		else if(tokenValue.matches("[a-z_A-Z](.)*")) errorMessage("Id contains invalid characters", tokenValue);
		else if(tokenValue.matches("[^a-z_A-Z&|](\\w)*")) errorMessage("Invalid id starting character", tokenValue);
		else if(tokenValue.matches("[^a-z_A-Z&|](.)*")) errorMessage("Invalid id", tokenValue);
		//unknown type
		return TokenCategory.unknown;
	}
	
	/**
	 * Prints cached error message, if any 
	 */
	private void sendError() {
		if(cachedError != null) System.err.println(cachedError);
		cachedError = null;
	}
	
	/**
	 * Creates an error message and caches it in cachedError
	 * @param description of the error message
	 * @param value of the token that called error
	 */
	private void errorMessage(String description, String tokenValue) {
		cachedError = new Error(description, currentLine+1, currentColumn, tokenValue);
		//String errorFormat = "Error at [%03d, %03d] -> error: %s ~> token: '%s'";
		//cachedError = (String.format(errorFormat, (currentLine+1), (currentColumn+1), description, tokenValue));
		//System.err.println("Error at ["+(currentLine+1)+":"+(currentColumn+1)+"] -> error: "+description+" ~> token: '"+tokenValue+"'");
	}
}
