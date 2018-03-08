package analisador.lexico;

public enum TokenCategory {
    
	main(1), id(2), intType(3), floatType(4), charType(5), boolType(6), stringType(7), 
    arrayType(8), funVoid(9), funDef(10), escBegin(11), escEnd(12), arrayBegin(13), arrayEnd(14), 
    arrayDesc(15), paramBegin(16), paramEnd(17), lineEnd(18), commaSep(19), loopSep(20), intCons(21), 
    floatCons(22), charCons(23), boolCons(24), stringCons(25), opAd(26), opMult(27), opExp(28), 
    opUnNeg(29), opConc(30), opRelEq(31), opRelLtGt(32), opLogAnd(33), opLogOr(34), opLogNot(35), 
    estIf(36), estElsif(37), estElse(38), estLoop(39), estDo(40), estWhile(41);
    
    private int value;

    private TokenCategory(int value) {
    	this.value = value;
    }
    
	public int getValue() {
		return value;
	}
    
}
