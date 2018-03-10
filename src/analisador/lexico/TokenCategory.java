package analisador.lexico;

public enum TokenCategory {
    
	main(1), id(2), intType(3), floatType(4), charType(5), boolType(6), stringType(7), 
    arrayType(8), funVoid(9), funDef(10), escBegin(11), escEnd(12), arrayBegin(13), arrayEnd(14), 
    arrayDesc(15), paramBegin(16), paramEnd(17), lineEnd(18), commaSep(19), loopSep(20), intCons(21), 
    floatCons(22), charCons(23), boolCons(24), stringCons(25), opAd(26), opMult(27), opExp(28), 
    opUnNeg(29), opEq(30), opConc(31), opRelEq(32), opRelLtGt(33), opLogAnd(34), opLogOr(35), opLogNot(36), 
    estIf(37), estElsif(38), estElse(39), estLoop(40), estDo(41), estWhile(42), unknown(43);
    
    private int value;

    private TokenCategory(int value) {
    	this.value = value;
    }
    
	public int getValue() {
		return value;
	}
    
}
