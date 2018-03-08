package analisador.lexico;

public class Main {

	public static void main(String[] args) {
		//implementar teste de ler programa e imprimir token a token
		Token token = new Token(TokenCategory.paramBegin, 12, 144, "(");
		System.out.println(token);

	}

}
