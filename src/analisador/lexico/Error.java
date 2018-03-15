package analisador.lexico;

public class Error {
	
	private String description, tokenValue;
	private int line, column;
	
	public Error(String description, int line, int column, String tokenValue) {
		this.description = description;
		this.line = line;
		this.column = column;
		this.tokenValue = tokenValue;
	}

	@Override
	public String toString() {
		String errorFormat = "Error at [%03d, %03d] -> error: %s ~> token: {%s}";
		return String.format(errorFormat, line, column, description, tokenValue);
	}	

}
