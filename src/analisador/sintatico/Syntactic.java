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
			printProduction("S", "Decl S");
			Decl();
			S();
		} else if(token.getCategory().equals(TokenCategory.funDef)) {
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
				} else unexpectedToken(")");
			} else unexpectedToken("(");
		} else printProduction("S", "epsilon");
	}
	
	public void FunName() {
		if(token.getCategory().equals(TokenCategory.main)) {
			printProduction("FunName", "'main'");
			setNextToken();
			
		} else if(token.getCategory().equals(TokenCategory.id)) {
			printProduction("FunName", "'id'");
			setNextToken();
			
		} else unexpectedToken("id or main");
	}
	
	public void Param() {
		if(token.getCategory().equals(TokenCategory.intType)
				|| token.getCategory().equals(TokenCategory.floatType)
				|| token.getCategory().equals(TokenCategory.charType)
				|| token.getCategory().equals(TokenCategory.boolType)
				|| token.getCategory().equals(TokenCategory.stringType)
				|| token.getCategory().equals(TokenCategory.arrayType)) {
			printProduction("Param", "Type 'id' ArrayOpt Paramr");
			Type();
			if(token.getCategory().equals(TokenCategory.id)) {
				setNextToken();
				
				ArrayOpt();
				Paramr();
				
			}else unexpectedToken("id");
		}else printProduction("Param", "epsilon");
	}
	
	public void Paramr() {
		if(token.getCategory().equals(TokenCategory.commaSep)) {
			printProduction("Paramr", "',' Type 'id' ArrayOpt Paramr");
			setNextToken();
			
			Type();
			if(token.getCategory().equals(TokenCategory.id)) {
				if(lexic.hasNextToken()) token = lexic.nextToken();
				ArrayOpt();
				Paramr();
			} else unexpectedToken("id");
		}else printProduction("Paramr", "epsilon");
	}
	
	public void LEc() {
		printProduction("LEc", "Ec LEcr");
		Ec();
		LEcr();
	}
	
	public void LEcr() {
		if(token.getCategory().equals(TokenCategory.commaSep)) {
			printProduction("LEcr", "',' Ec LEcr");
			setNextToken();
			
			Ec();
			LEcr();
		}else printProduction("LEcr","epsilon");
	}
	
	public void FunType() {
		if(token.getCategory().equals(TokenCategory.funVoid)) {
			printProduction("FunType", "'funVoid'");
			setNextToken();
			
		} else {
			printProduction("FunType", "Type");
			Type();
		}
	}
	public void Body() {
		if(token.getCategory().equals(TokenCategory.escBegin)) {
			printProduction("Body", "'{' BodyPart '}'");
			setNextToken();
			
			BodyPart();
			if(token.getCategory().equals(TokenCategory.escEnd)) {
				if(lexic.hasNextToken()) token = lexic.nextToken();
			} else unexpectedToken("}");
		} else unexpectedToken("{");
	}
	
	public void BodyPart() {
		if(token.getCategory().equals(TokenCategory.intType)
				|| token.getCategory().equals(TokenCategory.floatType)
				|| token.getCategory().equals(TokenCategory.charType)
				|| token.getCategory().equals(TokenCategory.boolType)
				|| token.getCategory().equals(TokenCategory.stringType)
				|| token.getCategory().equals(TokenCategory.arrayType)) {
			printProduction("BodyPart", "Decl BodyPart");
			Decl();
			BodyPart();
		} else if(token.getCategory().equals(TokenCategory.id)) {
			printProduction("BodyPart", "Atr ';' BodyPart");
			Atr();
			if(token.getCategory().equals(TokenCategory.lineEnd)) {
				setNextToken();
				
			}else unexpectedToken(";");
			BodyPart();
		} else if(token.getCategory().equals(TokenCategory.estWhile)
				|| token.getCategory().equals(TokenCategory.estDo)
				|| token.getCategory().equals(TokenCategory.estLoop)
				|| token.getCategory().equals(TokenCategory.estIf)
				|| token.getCategory().equals(TokenCategory.instRead)
				|| token.getCategory().equals(TokenCategory.instPrint)){
			printProduction("BodyPart", "Command BodyPart");
			Command();
			BodyPart();
		} else if(token.getCategory().equals(TokenCategory.funReturn)) {
			printProduction("BodyPart", "Return ';'");
			Return();
			if(token.getCategory().equals(TokenCategory.lineEnd)) {
				setNextToken();
				
			}else unexpectedToken(";");
		} else printProduction("BodyPart", "epsilon");
	}
	
	public void Return() {
		if(token.getCategory().equals(TokenCategory.funReturn)) {
			printProduction("Return", "'funReturn' Ec");
			setNextToken();
			Ec();
		} else unexpectedToken("return");
	}
	
	public void Command() {
		if(token.getCategory().equals(TokenCategory.estWhile)) {
			printProduction("Command", "'estWhile' '(' Eb ')' Body");
			setNextToken();
			
			if(token.getCategory().equals(TokenCategory.paramBegin)) {
				setNextToken();
				
				Eb();
				if(token.getCategory().equals(TokenCategory.paramEnd)) {
					setNextToken();
					
					Body();
				}else unexpectedToken(")");
			}else unexpectedToken("(");
		} else if(token.getCategory().equals(TokenCategory.estDo)) {
			printProduction("Command", "'estDo' Body 'estWhile' '(' Eb ')' ';'");
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
							
						}else unexpectedToken(";");
					}else unexpectedToken(")");
				}unexpectedToken("(");
			}unexpectedToken("while");
		} else if(token.getCategory().equals(TokenCategory.estLoop)) {
			printProduction("Command", "'estLoop' '(' Atr 'loopSep' Eb 'loopSep' Atr ')' Body");
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
						}else unexpectedToken(")");
					}else unexpectedToken("!");
				}else unexpectedToken("!");
			}else unexpectedToken("(");
		} else if(token.getCategory().equals(TokenCategory.estIf)) {
			printProduction("Command", "'estIf' '(' Eb ')' Body IFr");
			setNextToken();
			
			if(token.getCategory().equals(TokenCategory.paramBegin)) {
				setNextToken();
				
				Eb();
				if(token.getCategory().equals(TokenCategory.paramEnd)) {
					Body();
					IFr();
				} else unexpectedToken(")");
			} else unexpectedToken("(");
		} else if(token.getCategory().equals(TokenCategory.instPrint)) {
			printProduction("Command", "'instPrint' '(' Ec ')' ';'");
			setNextToken();
			
			if(token.getCategory().equals(TokenCategory.paramBegin)) {
				setNextToken();
				
				Ec();
				if(token.getCategory().equals(TokenCategory.paramEnd)) {
					setNextToken();
					
					if(token.getCategory().equals(TokenCategory.lineEnd)) {
						setNextToken();
						
					}else unexpectedToken(";");
				} else unexpectedToken(")");
			}else unexpectedToken("(");
		} else if(token.getCategory().equals(TokenCategory.instRead)) {
			printProduction("Command", "'instRead' '(' IdL ')' ';'");
			setNextToken();
			
			if(token.getCategory().equals(TokenCategory.paramBegin)) {
				setNextToken();
				
				Id();
				if(token.getCategory().equals(TokenCategory.paramEnd)) {
					setNextToken();
					
					if(token.getCategory().equals(TokenCategory.lineEnd)) {
						setNextToken();
						
					}else unexpectedToken(";");
				} else unexpectedToken(")");
			}else unexpectedToken("(");
		}else unexpectedToken("command");
	}
	
	public void IdL() {
		if(token.getCategory().equals(TokenCategory.id)) {
			printProduction("IdL", "Id IdLr");
			setNextToken();
			
			IdLr();
		} else unexpectedToken("id");
	}
	
	public void IdLr() {
		if(token.getCategory().equals(TokenCategory.commaSep)) {
			printProduction("IdLr", "',' Id IdLr");
			setNextToken();
			
			if(token.getCategory().equals(TokenCategory.id)) {
				setNextToken();
				
				IdLr();
			} else unexpectedToken("id");
		} else printProduction("IdLr", "epsilon");
	}
	
	public void IFr() {
		if(token.getCategory().equals(TokenCategory.estElsif)) {
			printProduction("IFr", "'estElsif' '(' Eb ')' Body IFr");
			setNextToken();
			
			if(token.getCategory().equals(TokenCategory.paramBegin)) {
				setNextToken();
				
				Eb();
				if(token.getCategory().equals(TokenCategory.paramEnd)) {
					setNextToken();
					
					Body();
					IFr();
				} else unexpectedToken(")");
			} else unexpectedToken("(");
		} else if(token.getCategory().equals(TokenCategory.estElse)) {
			printProduction("IFr", "'estElse' Body");
			setNextToken();
			
			Body();
		} else printProduction("IFr", "epsilon");
	}
	
	public void Atr() {
		if(token.getCategory().equals(TokenCategory.id)) {
			printProduction("Atr", "'id' AtrR");
			setNextToken();
			
			AtrR();
		}else unexpectedToken("id");
	}
	
	public void AtrR() {
		if(token.getCategory().equals(TokenCategory.paramBegin)) {
			printProduction("AtrR", "FunCall");
			FunCall();
		} else {
			printProduction("AtrR", "ArrayAccess 'opEq' Ec");
			ArrayAccess();
			if(token.getCategory().equals(TokenCategory.opEq)) {
				setNextToken();
				
				Ec();
			}else unexpectedToken("=");	
		}
	}
	
	public void Decl() {
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
			printProduction("Type", "'" + token.getCategory() + "'");
			setNextToken();
			
		} else unexpectedToken("type");
	}
	
	public void LI() {
		if(token.getCategory().equals(TokenCategory.id)) {
			printProduction("LI", "'id' ArrayOpt Inst LIr");
			setNextToken();

			ArrayOpt();
			Inst();
			LIr();
		} else unexpectedToken("id");
	}
	
	public void ArrayOpt() {
		if(token.getCategory().equals(TokenCategory.paramBegin)) {
			printProduction("ArrayOpt", "'(' Type ',' 'intCons' ')'");
			setNextToken();
			Type();
			if(token.getCategory().equals(TokenCategory.commaSep)) {
				setNextToken();
				if(token.getCategory().equals(TokenCategory.intCons)) {
					setNextToken();
					if(token.getCategory().equals(TokenCategory.paramEnd)) {
						setNextToken();
					}else unexpectedToken(")");
				}else unexpectedToken("integer constant");
			}else unexpectedToken(",");
		} else printProduction("ArrayOpt", "epsilon");
	}
	
	public void Inst() {
		if(token.getCategory().equals(TokenCategory.opEq)) {
			printProduction("Inst", "'opEq' Inr");
			setNextToken();
			
			Inr();
		} else printProduction("Inst", "epsilon");
	}
	
	public void Inr() {
		if(token.getCategory().equals(TokenCategory.arrayBegin)) {
			printProduction("Inr", "ArrayCons");
			ArrayCons();
		} else {
			printProduction("Inr", "Ec");
			Ec();
		}
	}
	
	public void ArrayCons() {
		if(token.getCategory().equals(TokenCategory.arrayBegin)) {
			printProduction("ArrayCons", "'[' LEc ']'");
			setNextToken();
			
			LEc();
			if(token.getCategory().equals(TokenCategory.arrayEnd)) {
				setNextToken();
				
			}else unexpectedToken("]");
		}else unexpectedToken("[");
	}
	
	public void Ec() {
		printProduction("Ec", "Fc Ecr");
		Fc();
		Ecr();
	}
	
	public void Fc() {
		if(token.getCategory().equals(TokenCategory.stringCons)) {
			printProduction("Fc", "'stringCons'");
			setNextToken();
			
		} else if(token.getCategory().equals(TokenCategory.charCons)) {
			printProduction("Fc", "'charCons'");
			setNextToken();
			
		} else {
			printProduction("Fc", "Eb");
			Eb();
		}
	}
	
	public void Eb() {
		printProduction("Eb", "Tb Ebr");
		Tb();
		Ebr();
	}
	
	public void Tb() {
		printProduction("Tb", "Fb Tbr");
		Fb();
		Tbr();
	}
	
	public void Fb() {
		if(token.getCategory().equals(TokenCategory.opLogNot)) {
			printProduction("Fb", "'opLogNot' Fb");
			setNextToken();
			
			Fb();
		} else if(token.getCategory().equals(TokenCategory.boolCons)) {
			printProduction("Fb", "'boolCons'");
			setNextToken();
			
		} else {
			printProduction("Fb", "Ra Fbr");
			Ra();
			Fbr();
		}
	}
	
	public void Ra() {
		printProduction("Ra", "Ea Rar");
		Ea();
		Rar();
	}
	
	public void Ea() {
		printProduction("Ea", "Ta Ear");
		Ta();
		Ear();
	}
	
	public void Ta() {
		printProduction("Ta", "Pa Tar");
		Pa();
		Tar();
	}
	
	public void Pa() {
		printProduction("Pa", "Fa Par");
		Fa();
		Par();
	}
	
	public void Par() {
		if(token.getCategory().equals(TokenCategory.opExp)) {
			printProduction("Par", "'opExp' Pa");
			setNextToken();
			
			Pa();
		}else printProduction("Par", "epsilon");
	}

	public void Fa() {
		if(token.getCategory().equals(TokenCategory.paramBegin)) {
			printProduction("Fa", "'(' Eb ')'");
			setNextToken();
			
			Eb();
			if(token.getCategory().equals(TokenCategory.paramEnd)) {
				setNextToken();
				
			}else unexpectedToken(")");
		} else if(token.getCategory().equals(TokenCategory.opUnNeg)) {
			printProduction("Fa", "'opUnNeg' Fa");
			setNextToken();
			
			Fa();
		} else if(token.getCategory().equals(TokenCategory.id)) {
			printProduction("Fa", "Id");
			Id();
		} else if(token.getCategory().equals(TokenCategory.intCons)) {
			printProduction("Fa", "'intCons'");
			setNextToken();
			
		} else if(token.getCategory().equals(TokenCategory.floatCons)) {
			printProduction("Fa", "'floatCons'");
			setNextToken();
			
		} else unexpectedToken("constant, id or expression");
	}
	
	public void Id() {
		if(token.getCategory().equals(TokenCategory.id)) {
			printProduction("Id", "'id' Idr");
			setNextToken();
			
			Idr();
		}else unexpectedToken("id");
	}
	
	public void Idr() {
		if(token.getCategory().equals(TokenCategory.paramBegin)) {
			printProduction("Idr", "FunCall");
			FunCall();
		} else {
			printProduction("Idr", "ArrayAccess");
			ArrayAccess();
		}
	}
	
	public void FunCall() {
		if(token.getCategory().equals(TokenCategory.paramBegin)) {
			printProduction("FunCall", "'(' LEc ')'");
			setNextToken();
			
			LEc();
			if(token.getCategory().equals(TokenCategory.paramEnd)) {
				setNextToken();
				
			} else unexpectedToken(")");
		}else unexpectedToken("(");
	}
	
	public void ArrayAccess() {
		if(token.getCategory().equals(TokenCategory.arrayBegin)) {
			printProduction("ArrayAccess", "'[' Ea ']'");
			setNextToken();
			
			Ea();
			if(token.getCategory().equals(TokenCategory.arrayEnd)) {
				setNextToken();
				
			} else unexpectedToken("]");
		}else printProduction("ArrayAccess", "epsilon");
	}
	
	public void Tar() {
		if(token.getCategory().equals(TokenCategory.opMult)) {
			printProduction("Tar", "'opMult' Pa Tar" );
			setNextToken();
			
			Pa();
			Tar();
		}else printProduction("Tar", "epsilon");
	}
	
	public void Ear() {
		if(token.getCategory().equals(TokenCategory.opAd)) {
			printProduction("Ear", "'opAd' Ta Ear");
			setNextToken();
			
			Ta();
			Ear();
		}else printProduction("Ear", "epsilon");
	}
	
	public void Rar() {
		if(token.getCategory().equals(TokenCategory.opRelEq)) {
			printProduction("Rar", "'opRelEq' Ea Rar");
			setNextToken();
			
			Ea();
			Rar();
		}else printProduction("Rar", "epsilon");
	}
	
	public void Fbr() {
		if(token.getCategory().equals(TokenCategory.opRelLtGt)) {
			printProduction("Fbr", "'opRelLtGt' Ra Fbr");
			setNextToken();
			
			Ra();
			Fbr();
		}else printProduction("Fbr", "epsilon");
	}
	
	public void Tbr() {
		if(token.getCategory().equals(TokenCategory.opLogAnd)) {
			printProduction("Tbr", "'opLogAnd' Fb Tbr");
			setNextToken();
			
			Fb();
			Tbr();
		}else printProduction("Tbr", "epsilon");
	}
	
	public void Ebr() {
		if(token.getCategory().equals(TokenCategory.opLogOr)) {
			printProduction("Ebr", "'opLogOr' Tb Ebr");
			setNextToken();
			
			Tb();
			Ebr();
		}else printProduction("Ebr", "epsilon");
	}
	
	public void Ecr() {
		if(token.getCategory().equals(TokenCategory.opConc)) {
			printProduction("Ecr", "'opConc' ConcOpt Fc Ecr");
			setNextToken();
			
			ConcOpt();
			Fc();
			Ecr();
		}else printProduction("Ecr", "epsilon");
	}
	
	public void ConcOpt() {
		if(token.getCategory().equals(TokenCategory.arrayBegin)) {
			printProduction("ConcOpt", "'[' 'floatCons' ']'");
			setNextToken();
			
			if(token.getCategory().equals(TokenCategory.floatCons)) {
				setNextToken();
				
				if(token.getCategory().equals(TokenCategory.arrayEnd)) {
					setNextToken();
					
				}else unexpectedToken("]");
			}else unexpectedToken("float constant");
		}else printProduction("ConcOpt", "epsilon");
	}
	
	public void LIr() {
		if(token.getCategory().equals(TokenCategory.commaSep)) {
			printProduction("LIr", "',' 'id' ArrayOpt Inst LIr");
			setNextToken();
			
			if(token.getCategory().equals(TokenCategory.id)) {
				setNextToken();

				ArrayOpt();
				Inst();
				LIr();
			} else unexpectedToken("id");
		} else if(token.getCategory().equals(TokenCategory.lineEnd)) {
			printProduction("LIr", "';'");
			setNextToken();
		}
	}
	
	private void setNextToken() {
		if(lexic.hasNextToken()) token = lexic.nextToken();
		else sendError("Unexpected end of file");
	}
	
	private void unexpectedToken(String expected) {
		sendError("Expected " + expected+ " after "+lexic.getPreviousToken()+ " but got "+token);
	}
	
	public void printProduction(String left, String right) {
		String format = "%10s%s = %s";

		System.out.println(String.format(format, "", left, right));
	}
	
	public void sendError(String message) {
		System.err.println("Error: "+ message);
		System.exit(0);
	}
	
}
