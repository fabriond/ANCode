package analisador.lexico;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Analisador {
	
	private List<String> codeLines = new ArrayList<String>();
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
			System.out.println(currentLine + " < " + codeLines.size());
			return (currentLine < codeLines.size());
			
		}
		return false;
	}
	
}
