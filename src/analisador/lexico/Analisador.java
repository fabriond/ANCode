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
	private int currentLine, currentColumn;
	
	public void readFile(String filepath) throws IOException, FileNotFoundException{
		
		BufferedReader reader = new BufferedReader(new FileReader(filepath));
		String codeLine = reader.readLine();

		while (codeLine != null) {
			System.out.println("-"+codeLine);
			
			codeLines.add(codeLine);
			codeLine = reader.readLine();
		}
		reader.close();
	
	}
	
	public boolean hasNextToken() {
		
		while(!codeLines.isEmpty()) {
			
		}
		
		return false;
		
	}

	
	
	
}
