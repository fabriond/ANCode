package analisador.lexico;

import java.io.FileNotFoundException;
import java.io.IOException;

import analisador.sintatico.SyntacticAnalyzer;

public class Main {
	
	public static void main(String[] args) {
		try {
			Lexic lexic = new Lexic(args[0]);			
			//while(lexic.hasNextToken()) System.out.println(lexic.nextToken());
			if(lexic.hasNextToken()) {
				Token token = lexic.nextToken();
				SyntacticAnalyzer synAna = new SyntacticAnalyzer(lexic, token);
				synAna.S();
			}
		} catch (FileNotFoundException e) {
			System.err.println("File not found");
			//e.printStackTrace();
		} catch (Exception e) {
			System.err.println("IO Exception while reading file");
			//e.printStackTrace();
		}

	}

}
