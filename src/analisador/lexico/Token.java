package analisador.lexico;

public class Token {
	
	private TokenCategory category;
	private int tokenLine;
	private int tokenColumn;
	private String value;
	
	public Token(TokenCategory category, int tokenLine, int tokenColumn, String value) {
		this.setCategory(category);
		this.setTokenLine(tokenLine);
		this.setTokenColumn(tokenColumn);
		this.setValue(value);
	}

	public TokenCategory getCategory() {
		return category;
	}

	public void setCategory(TokenCategory category) {
		this.category = category;
	}

	public int getTokenLine() {
		return tokenLine;
	}

	public void setTokenLine(int tokenLine) {
		this.tokenLine = tokenLine;
	}

	public int getTokenColumn() {
		return tokenColumn;
	}

	public void setTokenColumn(int tokenColumn) {
		this.tokenColumn = tokenColumn;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		String fmt = "[%03d, %03d] (%04d, %10s) {%s}";
		return String.format(fmt, tokenLine, tokenColumn, category.getValue(), category.toString(), value);
	}	

}
