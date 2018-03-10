package analisador.lexico;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {
	
	public static void main(String[] args) {
		Analisador analisador = new Analisador();
		try {
			analisador.readFile(args[0]);
		} catch (FileNotFoundException e) {
			System.err.println("File not found");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("IO Exception");
			e.printStackTrace();
		}
		analisador.hasNextToken();		

	}

}
