package analisador.lexico;

public enum TokenCategory {
    
	unknown, main, id, intType, floatType, charType, boolType, stringType, 
    arrayType, funVoid, funDef, escBegin, escEnd, arrayBegin, arrayEnd, 
    funReturn, paramBegin, paramEnd, lineEnd, commaSep, loopSep, intCons, 
    floatCons, charCons, boolCons, stringCons, opAd, opMult, opExp, 
    opUnNeg, opEq, opConc, opRelEq, opRelLtGt, opLogAnd, opLogOr, opLogNot, 
    estIf, estElsif, estElse, estLoop, estDo, estWhile, instRead, instPrint;
    
}
