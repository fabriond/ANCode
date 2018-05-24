package analisador.sintatico;


import analisador.lexico.Lexic;
import analisador.lexico.Token;
import analisador.lexico.TokenCategory;

public class Syntactic {
	Lexic lexic;
	Token token;
	
	public Syntactic(Lexic lexic, Token token) {
		this.lexic = lexic;
		this.token = token;
	}
	
	public void S() {
		if(token.getCategory().equals(TokenCategory.intType)
			|| token.getCategory().equals(TokenCategory.floatType)
			|| token.getCategory().equals(TokenCategory.charType)
			|| token.getCategory().equals(TokenCategory.boolType)
			|| token.getCategory().equals(TokenCategory.stringType)
			|| token.getCategory().equals(TokenCategory.arrayType)){
			//System.out.println("S = Decl S");
			printProduction("S", "Decl S");
			Decl();
			S();
		} else if(token.getCategory().equals(TokenCategory.funDef)) {
			//System.out.println("S = 'funDef' FunName '(' Param ')' FunType Body S");
			printProduction("S", "'funDef' FunName '(' Param ')' FunType Body S");
			setNextToken();
			
			FunName();

			if(token.getCategory().equals(TokenCategory.paramBegin)) {
				setNextToken();
				
				Param();
				if(token.getCategory().equals(TokenCategory.paramEnd)){
					setNextToken();
					
					FunType();
					Body();
					S();
				} else unexpectedToken(")");//Erro();Erro();
			} else unexpectedToken("(");//Erro();Erro();
		} else printProduction("S", "epsilon"); // System.out.println("S = epsilon");
	}
	
	public void FunName() {
		if(token.getCategory().equals(TokenCategory.main)) {
			//System.out.println("FunName = 'main'");
			printProduction("FunName", "'main'");
			setNextToken();
			
		} else if(token.getCategory().equals(TokenCategory.id)) {
			//System.out.println("FunName = 'id'");
			printProduction("FunName", "'id");
			setNextToken();
			
		} else unexpectedToken("id or main");//Erro();Erro();
	}
	
	public void Param() {
		if(token.getCategory().equals(TokenCategory.intType)
				|| token.getCategory().equals(TokenCategory.floatType)
				|| token.getCategory().equals(TokenCategory.charType)
				|| token.getCategory().equals(TokenCategory.boolType)
				|| token.getCategory().equals(TokenCategory.stringType)
				|| token.getCategory().equals(TokenCategory.arrayType)) {
			//System.out.println("Param = Type 'id' ArrayOpt Paramr");
			printProduction("Param", "Type 'id' ArrayOpt Paramr");
			Type();
			if(token.getCategory().equals(TokenCategory.id)) {
				setNextToken();
				
				ArrayOpt();
				Paramr();
				
			}else unexpectedToken("id");//Erro();Erro();
		}else printProduction("Param", "epsilon"); // System.out.println("Param = epsilon");
	}
	
	public void Paramr() {
		if(token.getCategory().equals(TokenCategory.commaSep)) {
			//System.out.println("Paramr = ',' Type 'id' ArrayOpt Paramr");
			printProduction("Paramr", "',' Type 'id' ArrayOpt Paramr");
			setNextToken();
			
			Type();
			if(token.getCategory().equals(TokenCategory.id)) {
				if(lexic.hasNextToken()) token = lexic.nextToken();
				ArrayOpt();
				Paramr();
			} else unexpectedToken("id");//Erro();Erro();
		}else printProduction("Paramr", "epsilon"); // System.out.println("Paramr = epsilon");
	}
	
	public void LEc() {
		//System.out.println("LEc = Ec LEcr");
		printProduction("LEc", "Ec LEcr");
		Ec();
		LEcr();
	}
	
	public void LEcr() {
		if(token.getCategory().equals(TokenCategory.commaSep)) {
			//System.out.println("LEcr = ',' Ec LEcr");
			printProduction("LEcr", "',' Ec LEcr");
			setNextToken();
			
			Ec();
			LEcr();
		}else printProduction("LEcr","epsilon"); // System.out.println("LEcr = epsilon");
	}
	
	public void FunType() {
		if(token.getCategory().equals(TokenCategory.funVoid)) {
			//System.out.println("FunType = 'funVoid'");
			printProduction("FunType", "'funVoid'");
			setNextToken();
			
		} else {
			//System.out.println("FunType = Type");
			printProduction("FunType", "Type");
			Type();
		}
	}
	public void Body() {
		if(token.getCategory().equals(TokenCategory.escBegin)) {
			//System.out.println("Body = '{' BodyPart '}'");
			printProduction("Body", "'{' BodyPart '}'");
			setNextToken();
			
			BodyPart();
			if(token.getCategory().equals(TokenCategory.escEnd)) {
				if(lexic.hasNextToken()) token = lexic.nextToken();
			} else unexpectedToken("}");//Erro();
		} else unexpectedToken("{");//Erro();
	}
	
	public void BodyPart() {
		if(token.getCategory().equals(TokenCategory.intType)
				|| token.getCategory().equals(TokenCategory.floatType)
				|| token.getCategory().equals(TokenCategory.charType)
				|| token.getCategory().equals(TokenCategory.boolType)
				|| token.getCategory().equals(TokenCategory.stringType)
				|| token.getCategory().equals(TokenCategory.arrayType)) {
			//System.out.println("BodyPart = Decl BodyPart");
			printProduction("BodyPart", "Decl BodyPart");
			Decl();
			BodyPart();
		} else if(token.getCategory().equals(TokenCategory.id)) {
			//System.out.println("BodyPart = Atr ';' BodyPart");
			printProduction("BodyPart", "Atr ';' BodyPart");
			Atr();
			if(token.getCategory().equals(TokenCategory.lineEnd)) {
				setNextToken();
				
			}else unexpectedToken(";");//Erro();
			BodyPart();
		} else if(token.getCategory().equals(TokenCategory.estWhile)
				|| token.getCategory().equals(TokenCategory.estDo)
				|| token.getCategory().equals(TokenCategory.estLoop)
				|| token.getCategory().equals(TokenCategory.estIf)
				|| token.getCategory().equals(TokenCategory.instRead)
				|| token.getCategory().equals(TokenCategory.instPrint)){
			//System.out.println("BodyPart = Structure BodyPart");
			printProduction("BodyPart", "Structure BodyPart");
			Structure();
			BodyPart();
		} else if(token.getCategory().equals(TokenCategory.funReturn)) {
			//System.out.println("BodyPart = Return ';'");
			printProduction("BodyPart", "Return ';'");
			Return();
			if(token.getCategory().equals(TokenCategory.lineEnd)) {
				setNextToken();
				
			}else unexpectedToken(";");//Erro();
		} else printProduction("BodyPart", "epsilon"); // System.out.println("BodyPart = epsilon");
	}
	
	public void Return() {
		if(token.getCategory().equals(TokenCategory.funReturn)) {
			//System.out.println("Return = 'funReturn' Ec");
			printProduction("Return", "'funReturn' Ec");
			setNextToken();
			Ec();
		} else unexpectedToken("return");//Erro();
	}
	
	public void Structure() {
		if(token.getCategory().equals(TokenCategory.estWhile)) {
			//System.out.println("Structure = 'estWhile' '(' Eb ')' Body");
			printProduction("Structure", "'estWhile' '(' Eb ')' Body");
			setNextToken();
			
			
			if(token.getCategory().equals(TokenCategory.paramBegin)) {
				setNextToken();
				
				Eb();
				if(token.getCategory().equals(TokenCategory.paramEnd)) {
					setNextToken();
					
					Body();
				}else unexpectedToken(")");//Erro();
			}else unexpectedToken("(");//Erro();
		} else if(token.getCategory().equals(TokenCategory.estDo)) {
			//System.out.println("Structure = 'estDo' Body 'estWhile' '(' Eb ')' ';'");
			printProduction("Structure", "'estDo' Body 'estWhile' '(' Eb ')' ';'");
			setNextToken();
			
			Body();
			if(token.getCategory().equals(TokenCategory.estWhile)) {
				setNextToken();
				
				if(token.getCategory().equals(TokenCategory.paramBegin)) {
					setNextToken();
					
					Eb();
					if(token.getCategory().equals(TokenCategory.paramEnd)) {
						setNextToken();
						
						if(token.getCategory().equals(TokenCategory.lineEnd)) {
							setNextToken();
							
						}else unexpectedToken(";");//Erro();
					}else unexpectedToken(")");//Erro();
				}unexpectedToken("(");//Erro();
			}unexpectedToken("while");//Erro();
		} else if(token.getCategory().equals(TokenCategory.estLoop)) {
			//System.out.println("Structure = 'estLoop' '(' Atr 'loopSep' Eb 'loopSep' Atr ')' Body");
			printProduction("Structure", "'estLoop' '(' Atr 'loopSep' Eb 'loopSep' Atr ')' Body");
			setNextToken();
			
			if(token.getCategory().equals(TokenCategory.paramBegin)) {
				setNextToken();
				
				Atr();
				if(token.getCategory().equals(TokenCategory.loopSep)) {
					setNextToken();
					
					Eb();
					if(token.getCategory().equals(TokenCategory.loopSep)) {
						setNextToken();
						
						Atr();
						if(token.getCategory().equals(TokenCategory.paramEnd)) {
							setNextToken();
							
							Body();
						}else unexpectedToken(")");//Erro();
					}else unexpectedToken("!");//Erro();
				}else unexpectedToken("!");//Erro();
			}else unexpectedToken("(");//Erro();
		} else if(token.getCategory().equals(TokenCategory.estIf)) {
			//System.out.println("Structure = 'estIf' '(' Eb ')' Body IFr");
			printProduction("Structure", "'estIf' '(' Eb ')' Body IFr");
			setNextToken();
			
			if(token.getCategory().equals(TokenCategory.paramBegin)) {
				setNextToken();
				
				Eb();
				if(token.getCategory().equals(TokenCategory.paramEnd)) {
					Body();
					IFr();
				} else unexpectedToken(")");//Erro();
			} else unexpectedToken("(");//Erro();
		} else if(token.getCategory().equals(TokenCategory.instPrint)) {
			//System.out.println("Structure = 'instPrint' '(' Ec ')' ';'");
			printProduction("Structure", "'instPrint' '(' Ec ')' ';'");
			setNextToken();
			
			if(token.getCategory().equals(TokenCategory.paramBegin)) {
				setNextToken();
				
				Ec();
				if(token.getCategory().equals(TokenCategory.paramEnd)) {
					setNextToken();
					
					if(token.getCategory().equals(TokenCategory.lineEnd)) {
						setNextToken();
						
					}else unexpectedToken(";");//Erro();
				} else unexpectedToken(")");//Erro();
			}else unexpectedToken("(");//Erro();
		} else if(token.getCategory().equals(TokenCategory.instRead)) {
			//System.out.println("Structure = 'instRead' '(' IdL ')' ';'");
			printProduction("Structure", "'instRead' '(' IdL ')' ';'"); ///Trocar Structure por Command
			setNextToken();
			
			if(token.getCategory().equals(TokenCategory.paramBegin)) {
				setNextToken();
				
				Id();
				if(token.getCategory().equals(TokenCategory.paramEnd)) {
					setNextToken();
					
					if(token.getCategory().equals(TokenCategory.lineEnd)) {
						setNextToken();
						
					}else unexpectedToken(";");//Erro();
				} else unexpectedToken(")");//Erro();
			}else unexpectedToken("(");//Erro();
		}else unexpectedToken("structure");//Erro();
	}
	
	public void IdL() {
		if(token.getCategory().equals(TokenCategory.id)) {
			//System.out.println("IdL = Id IdLr");
			printProduction("IdL", "Id IdLr");
			setNextToken();
			
			IdLr();
		} else unexpectedToken("id");//Erro();
	}
	
	public void IdLr() {
		if(token.getCategory().equals(TokenCategory.commaSep)) {
			//System.out.println("IdLr = ',' Id IdLr");
			printProduction("IdLr", "',' Id IdLr");
			setNextToken();
			
			if(token.getCategory().equals(TokenCategory.id)) {
				setNextToken();
				
				IdLr();
			} else unexpectedToken("id");//Erro();
		} else printProduction("IdLr", "epsilon"); // System.out.println("IdLr = epsilon");
	}
	
	public void IFr() {
		if(token.getCategory().equals(TokenCategory.estElsif)) {
			//System.out.println("IFr = 'estElsif' '(' Eb ')' Body IFr");
			printProduction("IFr", "'estElsif' '(' Eb ')' Body IFr");
			setNextToken();
			
			if(token.getCategory().equals(TokenCategory.paramBegin)) {
				setNextToken();
				
				Eb();
				if(token.getCategory().equals(TokenCategory.paramEnd)) {
					setNextToken();
					
					Body();
					IFr();
				} else unexpectedToken(")");//Erro();
			} else unexpectedToken("(");//Erro();
		} else if(token.getCategory().equals(TokenCategory.estElse)) {
			//System.out.println("IFr = 'estElse' Body");
			printProduction("IFr", "'estElse' Body");
			setNextToken();
			
			Body();
		} else printProduction("IFr", "epsilon"); // System.out.println("IFr = epsilon");
	}
	
	public void Atr() {
		if(token.getCategory().equals(TokenCategory.id)) {
			//System.out.println("Atr = 'id' AtrR");
			printProduction("Atr", "'id' AtrR");
			setNextToken();
			
			AtrR();
		}else unexpectedToken("id");//Erro();
	}
	
	public void AtrR() {
		if(token.getCategory().equals(TokenCategory.paramBegin)) {
			//System.out.println("AtrR = FunCall");
			printProduction("AtrR", "FunCall");
			FunCall();
		} else {
			//System.out.println("AtrR = ArrayAccess 'opEq' Ec");
			printProduction("AtrR", "ArrayAccess 'opEq' Ec");
			ArrayAccess();
			if(token.getCategory().equals(TokenCategory.opEq)) {
				setNextToken();
				
				Ec();
			}else unexpectedToken("=");//Erro();	
		}
	}
	
	public void Decl() {
		//System.out.println("Decl = Type LI");
		printProduction("Decl", "Type LI");
		Type();
		LI();
	}
	
	public void Type() {
		if(token.getCategory().equals(TokenCategory.intType)
		   || token.getCategory().equals(TokenCategory.floatType)
		   || token.getCategory().equals(TokenCategory.charType)
		   || token.getCategory().equals(TokenCategory.boolType)
		   || token.getCategory().equals(TokenCategory.stringType)
		   || token.getCategory().equals(TokenCategory.arrayType)) {
			//System.out.println("Type = '"+token.getCategory()+"'");
			printProduction("Type", "'" + token.getCategory() + "'");
			setNextToken();
			
		} else unexpectedToken("type");//Erro();
	}
	
	public void LI() {
		if(token.getCategory().equals(TokenCategory.id)) {
			//System.out.println("LI = 'id' ArrayOpt Inst LIr");
			printProduction("LI", "'id' ArrayOpt Inst LIr");
			setNextToken();

			ArrayOpt();
			Inst();
			LIr();
		} else unexpectedToken("id");//Erro();
	}
	
	public void ArrayOpt() {
		if(token.getCategory().equals(TokenCategory.paramBegin)) {
			//System.out.println("ArrayOpt = '(' Type ',' 'intCons' ')'");
			printProduction("ArrayOpt", "'(' Type ',' 'intCons' ')'");
			setNextToken();
			Type();
			if(token.getCategory().equals(TokenCategory.commaSep)) {
				setNextToken();
				if(token.getCategory().equals(TokenCategory.intCons)) {
					setNextToken();
					if(token.getCategory().equals(TokenCategory.paramEnd)) {
						setNextToken();
					}else unexpectedToken(")");//Erro();
				}else unexpectedToken("integer constant");//Erro();
			}else unexpectedToken(",");//Erro();
		} else printProduction("ArrayOpt", "epsilon"); // System.out.println("ArrayOpt = epsilon");
	}
	
	public void Inst() {
		if(token.getCategory().equals(TokenCategory.opEq)) {
			//System.out.println("Inst = 'opEq' Inr");
			printProduction("Inst", "'opEq' Inr");
			setNextToken();
			
			Inr();
		} else printProduction("Inst", "epsilon"); // System.out.println("Inst = epsilon");
	}
	
	public void Inr() {
		if(token.getCategory().equals(TokenCategory.arrayBegin)) {
			//System.out.println("Inr = ArrayCons");
			printProduction("Inr", "ArrayCons");
			ArrayCons();
		} else {
			//System.out.println("Inr = Ec");
			printProduction("Inr", "Ec");
			Ec();
		}
	}
	
	public void ArrayCons() {
		if(token.getCategory().equals(TokenCategory.arrayBegin)) {
			//System.out.println("ArrayCons = '[' LEc ']'");
			printProduction("ArrayCons", "'[' LEc ']'");
			setNextToken();
			
			LEc();
			if(token.getCategory().equals(TokenCategory.arrayEnd)) {
				setNextToken();
				
			}else unexpectedToken("]");//Erro();
		}else unexpectedToken("[");
	}
	
	public void Ec() {
		//System.out.println("Ec = Fc Ecr");
		printProduction("Ec", "Fc Ecr");
		Fc();
		Ecr();
	}
	
	public void Fc() {
		if(token.getCategory().equals(TokenCategory.stringCons)) {
			//System.out.println("Fc = 'stringCons'");
			printProduction("Fc", "'stringCons'");
			setNextToken();
			
		} else if(token.getCategory().equals(TokenCategory.charCons)) {
			//System.out.println("Fc = 'charCons'");
			printProduction("Fc", "'charCons'");
			setNextToken();
			
		} else {
			//System.out.println("Fc = Eb");
			printProduction("Fc", "Eb");
			Eb();
		}
	}
	
	public void Eb() {
		//System.out.println("Eb = Tb Ebr");
		printProduction("Eb", "Tb Ebr");
		Tb();
		Ebr();
	}
	
	public void Tb() {
		//System.out.println("Tb = Fb Tbr");
		printProduction("Tb", "Fb Tbr");
		Fb();
		Tbr();
	}
	
	public void Fb() {
		if(token.getCategory().equals(TokenCategory.opLogNot)) {
			//System.out.println("Fb = 'opLogNot' Fb");
			printProduction("Fb", "'opLogNot' Fb");
			setNextToken();
			
			Fb();
		} else if(token.getCategory().equals(TokenCategory.boolCons)) {
			//System.out.println("Fb = 'boolCons'");
			printProduction("Fb", "'boolCons'");
			setNextToken();
			
		} else {
			//System.out.println("Fb = Ra Fbr");
			printProduction("Fb", "Ra Fbr");
			Ra();
			Fbr();
		}
	}
	
	public void Ra() {
		//System.out.println("Ra = Ea Rar");
		printProduction("Ra", "Ea Rar");
		Ea();
		Rar();
	}
	
	public void Ea() {
		//System.out.println("Ea = Ta Ear");
		printProduction("Ea", "Ta Ear");
		Ta();
		Ear();
	}
	
	public void Ta() {
		//System.out.println("Ta = Pa Tar");
		printProduction("Ta", "Pa Tar");
		Pa();
		Tar();
	}
	
	public void Pa() {
		//System.out.println("Pa = Fa Par");
		printProduction("Pa", "Fa Par");
		Fa();
		Par();
	}
	
	public void Par() {
		if(token.getCategory().equals(TokenCategory.opExp)) {
			//System.out.println("Par = 'opExp' Pa");
			printProduction("Par", "'opExp' Pa");
			setNextToken();
			
			Pa();
		}else printProduction("Par", "epsilon"); // System.out.println("Par = epsilon");
	}

	public void Fa() {
		if(token.getCategory().equals(TokenCategory.paramBegin)) {
			//System.out.println("Fa = '(' Eb ')'");
			printProduction("Fa", "'(' Eb ')'");
			setNextToken();
			
			Eb();
			if(token.getCategory().equals(TokenCategory.paramEnd)) {
				setNextToken();
				
			}else unexpectedToken(")");//Erro();
		} else if(token.getCategory().equals(TokenCategory.opUnNeg)) {
			//System.out.println("Fa = 'opUnNeg' Fa");
			printProduction("Fa", "'opUnNeg' Fa");
			setNextToken();
			
			Fa();
		} else if(token.getCategory().equals(TokenCategory.id)) {
			//System.out.println("Fa = Id");
			printProduction("Fa", "Id");
			Id();
		} else if(token.getCategory().equals(TokenCategory.intCons)) {
			//System.out.println("Fa = 'intCons'");
			printProduction("Fa", "'intCons'");
			setNextToken();
			
		} else if(token.getCategory().equals(TokenCategory.floatCons)) {
			//System.out.println("Fa = 'floatCons'");
			printProduction("Fa", "'floatCons'");
			setNextToken();
			
		} else unexpectedToken("constant, id or expression");//Erro();
	}
	
	public void Id() {
		if(token.getCategory().equals(TokenCategory.id)) {
			//System.out.println("Id = 'id' Idr");
			printProduction("Id", "'id' Idr");
			setNextToken();
			
			Idr();
		}else unexpectedToken("id");//Erro();
	}
	
	public void Idr() {
		if(token.getCategory().equals(TokenCategory.paramBegin)) {
			//System.out.println("Idr = FunCall");
			printProduction("Idr", "FunCall");
			FunCall();
		} else {
			//System.out.println("Idr = ArrayAccess");
			printProduction("Idr", "ArrayAccess");
			ArrayAccess();
		}
	}
	
	public void FunCall() {
		if(token.getCategory().equals(TokenCategory.paramBegin)) {
			//System.out.println("FunCall = '(' LEc ')'");
			printProduction("FunCall", "'(' LEc ')'");
			setNextToken();
			
			LEc();
			if(token.getCategory().equals(TokenCategory.paramEnd)) {
				setNextToken();
				
			} else unexpectedToken(")");//Erro();
		}else unexpectedToken("(");//Erro();
	}
	
	public void ArrayAccess() {
		if(token.getCategory().equals(TokenCategory.arrayBegin)) {
			//System.out.println("ArrayAccess = '[' Ea ']'");
			printProduction("ArrayAccess", "'[' Ea ']'");
			setNextToken();
			
			Ea();
			if(token.getCategory().equals(TokenCategory.arrayEnd)) {
				setNextToken();
				
			} else unexpectedToken("]");//Erro();
		}else printProduction("ArrayAccess", "epsilon"); //  System.out.println("ArrayAccess = epsilon");
	}
	
	public void Tar() {
		if(token.getCategory().equals(TokenCategory.opMult)) {
			//System.out.println("Tar = 'opMult' Pa Tar");
			printProduction("Tar", "'opMult' Pa Tar" );
			setNextToken();
			
			Pa();
			Tar();
		}else printProduction("Tar", "epsilon"); // System.out.println("Tar = epsilon");
	}
	
	public void Ear() {
		if(token.getCategory().equals(TokenCategory.opAd)) {
			//System.out.println("Ear = 'opAd' Ta Ear");
			printProduction("Ear", "'opAd' Ta Ear");
			setNextToken();
			
			Ta();
			Ear();
		}else printProduction("Ear", "epsilon"); // System.out.println("Ear = epsilon");
	}
	
	public void Rar() {
		if(token.getCategory().equals(TokenCategory.opRelEq)) {
			//System.out.println("Rar = 'opRelEq' Ea Rar");
			printProduction("Rar", "'opRelEq' Ea Rar");
			setNextToken();
			
			Ea();
			Rar();
		}else printProduction("Rar", "epsilon"); // System.out.println("Rar = epsilon");
	}
	
	public void Fbr() {
		if(token.getCategory().equals(TokenCategory.opRelLtGt)) {
			//System.out.println("Fbr = 'opRelLtGt' Ra Fbr");
			printProduction("Fbr", "'opRelLtGt' Ra Fbr");
			setNextToken();
			
			Ra();
			Fbr();
		}else printProduction("Fbr", "epsilon"); // System.out.println("Fbr = epsilon");
	}
	
	public void Tbr() {
		if(token.getCategory().equals(TokenCategory.opLogAnd)) {
			//System.out.println("Tbr = 'opLogAnd' Fb Tbr");
			printProduction("Tbr", "'opLogAnd' Fb Tbr");
			setNextToken();
			
			Fb();
			Tbr();
		}else printProduction("Tbr", "epsilon"); // System.out.println("Tbr = epsilon");
	}
	
	public void Ebr() {
		if(token.getCategory().equals(TokenCategory.opLogOr)) {
			//System.out.println("Ebr = 'opLogOr' Tb Ebr");
			printProduction("Ebr", "'opLogOr' Tb Ebr");
			setNextToken();
			
			Tb();
			Ebr();
		}else printProduction("Ebr", "epsilon"); // System.out.println("Ebr = epsilon");
	}
	
	public void Ecr() {
		if(token.getCategory().equals(TokenCategory.opConc)) {
			//System.out.println("Ecr = 'opConc' ConcOpt Fc Ecr");
			printProduction("Ecr", "'opConc' ConcOpt Fc Ecr");
			setNextToken();
			
			ConcOpt();
			Fc();
			Ecr();
		}else printProduction("Ecr", "epsilon"); // System.out.println("Ecr = epsilon");
	}
	
	public void ConcOpt() {
		if(token.getCategory().equals(TokenCategory.arrayBegin)) {
			//System.out.println("ConcOpt = '[' 'floatCons' ']'");
			printProduction("ConcOpt", "'[' 'floatCons' ']'");
			setNextToken();
			
			if(token.getCategory().equals(TokenCategory.floatCons)) {
				setNextToken();
				
				if(token.getCategory().equals(TokenCategory.arrayEnd)) {
					setNextToken();
					
				}else unexpectedToken("]");//Erro();
			}else unexpectedToken("float constant");//Erro();
		}else System.out.println("ConcOpt = epsilon");
	}
	
	public void LIr() {
		if(token.getCategory().equals(TokenCategory.commaSep)) {
			//System.out.println("LIr = ',' 'id' ArrayOpt Inst LIr");
			printProduction("LIr", "',' 'id' ArrayOpt Inst LIr");
			setNextToken();
			
			if(token.getCategory().equals(TokenCategory.id)) {
				setNextToken();

				ArrayOpt();
				Inst();
				LIr();
			} else unexpectedToken("id");//Erro();
			
		} else if(token.getCategory().equals(TokenCategory.lineEnd)) {
			//System.out.println("LIr = ';'");
			printProduction("LIr", "';'");
			setNextToken();
			
		}
	}
	
	private void setNextToken() {
		if(lexic.hasNextToken()) token = lexic.nextToken();
		else sendError("Unexpected end of file");
	}
	
	private void unexpectedToken(String expected) {
		sendError("Expected " + expected+ " after "+lexic.getPreviousToken());
	}
	
	public void printProduction(String left, String right) {
		String format = "%10s %s = %s";

		System.out.println(String.format(format, "", left, right));
	}
	
	public void sendError(String message) {
		System.err.println("Error: "+ message);
		System.exit(0);
	}
	
}
