package analisador.lexico;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Lexic {
	
	private Token previousToken;
	private Token currentToken;
	private String currentLineContent;
	private Error cachedError;//serve pra imprimir erros após a impressão do token que deu erro
	private int currentLine = 0, currentColumn = 0;
	private BufferedReader reader;
	
	/**
	 * Creates an instance of the Lexical analyzer for ANCode,  
	 * given the <code>filepath</code> of the file that will be compiled 
	 * @param filepath, a string that contains the path of the file to be read
	 * @throws FileNotFoundException if <code>filepath</code> is not valid
	 */
	public Lexic(String filepath) throws FileNotFoundException {
		this.reader = new BufferedReader(new FileReader(filepath));
	}
	
	/**
	 * Reads the next line from the file given in the constructor
	 * @return true if the next line has been read successfully, false otherwise
	 * @throws IOException
	 */
	private boolean readNextLine() {
		String codeLine = new String();
		try {
			codeLine = reader.readLine();	
		} catch(Exception e) {
			e.printStackTrace();
		}
		if(codeLine != null) {			
			currentLineContent = codeLine;
			return true;
		}
		else return false;
	}
	
	/**
	 * Checks if the code read still has a next token
	 * @return true if there is a next token, false otherwise
	 * @throws IOException
	 */
	public boolean hasNextToken() {
		sendError();
		System.out.println(currentToken);
		if(currentLine == 0 && currentColumn == 0) {//lê a primeira linha de código
			readNextLine();
			
			//da print na primeira linha
			System.out.println("Current Line: ");
			if(currentLineContent == null) {//caso o arquivo esteja vazio
				printCodeLine("");
				return false;//caso o arquivo esteja vazio não há mais tokens
			} else printCodeLine(currentLineContent);
			System.out.println();
			//fim do print da primeira linha
		}
		
		if(currentLineContent.substring(currentColumn).matches("\\s*")) {
			while(readNextLine()) {//itera enquanto não chegar no fim do arquivo
				currentLine++;
				currentColumn = 0;
				
				//da print na linha atual (exceto a primeira linha, pois ela já é tratada acima)
				System.out.println("\nCurrent Line: ");
				printCodeLine(currentLineContent);
				//print da linha atual termina aqui
				
				if(!currentLineContent.matches("\\s*")) return true;	
			}
			return false;//não há mais linhas a serem lidas(EOF)
		} else return true;//caso a linha atual não seja composta apenas de espaços em branco
		
	}
	
	private void printCodeLine(String content) {
		String format = "%4d  %s";
		System.out.println(String.format(format, currentLine+1, content+"\n"));
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
		int tokenCol = currentColumn;
		int tokenLine = currentLine;
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
		
		if(tokenValue.isEmpty()){
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
		//System.out.println(currentToken);
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
			TokenCategory categoryValue = previousToken.getCategory();

			if(categoryValue == TokenCategory.intCons || categoryValue == TokenCategory.floatCons) return false;
			else if(categoryValue == TokenCategory.id || categoryValue == TokenCategory.paramEnd) return false;
	
			return true;
		}
		else return true;
	}
	
	/**
	 * Gets category of literal constants or ids by their lexical value
	 * @param tokenValue
	 * @return token's category if it is any constant type or an id
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
	
	public Token getPreviousToken() {
		return previousToken;
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
	}
}
