package analisador.lexico;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {
	
	public static void main(String[] args) {
		Lexic lexic = new Lexic();
		try {
			lexic.readFile(args[0]);
		} catch (FileNotFoundException e) {
			System.err.println("File not found");
			//e.printStackTrace();
		} catch (IOException e) {
			System.err.println("IO Exception while reading file");
			//e.printStackTrace();
		}
		while(lexic.hasNextToken()) System.out.println(lexic.nextToken());

	}

}
