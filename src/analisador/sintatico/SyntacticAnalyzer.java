package analisador.sintatico;


import analisador.lexico.Lexic;
import analisador.lexico.Token;
import analisador.lexico.TokenCategory;

public class SyntacticAnalyzer {
	Lexic lexic;
	Token token;
	
	public SyntacticAnalyzer(Lexic lexic, Token token) {
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
			System.out.println("S = Decl S");
			Decl();
			S();
		} else if(token.getCategory().equals(TokenCategory.funDef)) {
			System.out.println("S = 'funDef' FunName '(' Param ')' FunType Body S");
			setNextToken();
			//if(lexic.hasNextToken()) token = lexic.nextToken();
			//else Erro();
			FunName();

			if(token.getCategory().equals(TokenCategory.paramBegin)) {
				setNextToken();
				//if(lexic.hasNextToken()) token = lexic.nextToken();
				//else Erro();
				Param();
				if(token.getCategory().equals(TokenCategory.paramEnd)){
					setNextToken();
					//if(lexic.hasNextToken()) token = lexic.nextToken();
					//else Erro();
					FunType();
					Body();
					S();
				} else unexpectedToken(")");//Erro();Erro();
			} else unexpectedToken("(");//Erro();Erro();
		} else System.out.println("S = epsilon");
	}
	
	public void FunName() {
		if(token.getCategory().equals(TokenCategory.main)) {
			System.out.println("FunName = 'main'");
			setNextToken();
			//if(lexic.hasNextToken()) token = lexic.nextToken();
			//else Erro();
		} else if(token.getCategory().equals(TokenCategory.id)) {
			System.out.println("FunName = 'id'");
			setNextToken();
			//if(lexic.hasNextToken()) token = lexic.nextToken();
			//else Erro();
		} else unexpectedToken("id or main");//Erro();Erro();
	}
	
	public void Param() {
		if(token.getCategory().equals(TokenCategory.intType)
				|| token.getCategory().equals(TokenCategory.floatType)
				|| token.getCategory().equals(TokenCategory.charType)
				|| token.getCategory().equals(TokenCategory.boolType)
				|| token.getCategory().equals(TokenCategory.stringType)
				|| token.getCategory().equals(TokenCategory.arrayType)) {
			System.out.println("Param = Type 'id' ArrayOpt Paramr");
			Type();
			if(token.getCategory().equals(TokenCategory.id)) {
				setNextToken();
				//if(lexic.hasNextToken()) token = lexic.nextToken();
				//else Erro();
				ArrayOpt();
				Paramr();
				
			}else unexpectedToken("id");//Erro();Erro();
		}else System.out.println("Param = epsilon");
	}
	
	public void Paramr() {
		if(token.getCategory().equals(TokenCategory.commaSep)) {
			System.out.println("Paramr = ',' Type 'id' ArrayOpt Paramr");
			setNextToken();
			//if(lexic.hasNextToken()) token = lexic.nextToken();
			//else Erro();
			Type();
			if(token.getCategory().equals(TokenCategory.id)) {
				if(lexic.hasNextToken()) token = lexic.nextToken();
				ArrayOpt();
				Paramr();
			} else unexpectedToken("id");//Erro();Erro();
		}else System.out.println("Paramr = epsilon");
	}
	
	public void LEc() {
		System.out.println("LEc = Ec LEcr");
		Ec();
		LEcr();
	}
	
	public void LEcr() {
		if(token.getCategory().equals(TokenCategory.commaSep)) {
			System.out.println("LEcr = ',' Ec LEcr");
			setNextToken();
			//if(lexic.hasNextToken()) token = lexic.nextToken();
			//else Erro();
			Ec();
			LEcr();
		}else System.out.println("LEcr = epsilon");
	}
	
	public void FunType() {
		if(token.getCategory().equals(TokenCategory.funVoid)) {
			System.out.println("FunType = 'funVoid'");
			setNextToken();
			//if(lexic.hasNextToken()) token = lexic.nextToken();
			//else Erro();
		} else /*if(token.getCategory().equals(TokenCategory.intType)
				|| token.getCategory().equals(TokenCategory.floatType)
				|| token.getCategory().equals(TokenCategory.charType)
				|| token.getCategory().equals(TokenCategory.boolType)
				|| token.getCategory().equals(TokenCategory.stringType)
				|| token.getCategory().equals(TokenCategory.arrayType)) */{
			System.out.println("FunType = Type");
			Type();
		}
	}
	
	public void Body() {
		if(token.getCategory().equals(TokenCategory.escBegin)) {
			System.out.println("Body = '{' BodyPart '}'");
			setNextToken();
			//if(lexic.hasNextToken()) token = lexic.nextToken();
			//else Erro();
			BodyPart();
			if(token.getCategory().equals(TokenCategory.escEnd)) {
				setNextToken();
				//if(lexic.hasNextToken()) token = lexic.nextToken();
				//else Erro();	
			} else unexpectedToken("}");//Erro();
		} else unexpectedToken("{");//Erro();Erro();
	}
	
	public void BodyPart() {
		if(token.getCategory().equals(TokenCategory.intType)
				|| token.getCategory().equals(TokenCategory.floatType)
				|| token.getCategory().equals(TokenCategory.charType)
				|| token.getCategory().equals(TokenCategory.boolType)
				|| token.getCategory().equals(TokenCategory.stringType)
				|| token.getCategory().equals(TokenCategory.arrayType)) {
			System.out.println("BodyPart = Decl BodyPart");
			Decl();
			BodyPart();
		} else if(token.getCategory().equals(TokenCategory.id)) {
			System.out.println("BodyPart = Atr ';' BodyPart");
			Atr();
			if(token.getCategory().equals(TokenCategory.lineEnd)) {
				setNextToken();
				//if(lexic.hasNextToken()) token = lexic.nextToken();
				//else Erro();
			}else unexpectedToken(";");//Erro();
			BodyPart();
		} else if(token.getCategory().equals(TokenCategory.estWhile)
				|| token.getCategory().equals(TokenCategory.estDo)
				|| token.getCategory().equals(TokenCategory.estLoop)
				|| token.getCategory().equals(TokenCategory.estIf)
				|| token.getCategory().equals(TokenCategory.instRead)
				|| token.getCategory().equals(TokenCategory.instPrint)){
			System.out.println("BodyPart = Structure BodyPart");
			Structure();
			BodyPart();
		} else if(token.getCategory().equals(TokenCategory.funReturn)) {
			System.out.println("BodyPart = Return ';'");
			Return();
			if(token.getCategory().equals(TokenCategory.lineEnd)) {
				setNextToken();
				//if(lexic.hasNextToken()) token = lexic.nextToken();
				//else Erro();
			}else unexpectedToken(";");//Erro();
		} else System.out.println("BodyPart = epsilon");
	}
	
	public void Return() {
		if(token.getCategory().equals(TokenCategory.funReturn)) {
			System.out.println("Return = 'funReturn' Ec");
			Ec();
		} else unexpectedToken("return");//Erro();
	}
	
	public void Structure() {
		if(token.getCategory().equals(TokenCategory.estWhile)) {
			System.out.println("Structure = 'estWhile' '(' Eb ')' Body");
			setNextToken();
			//if(lexic.hasNextToken()) token = lexic.nextToken();
			//else Erro();
			
			if(token.getCategory().equals(TokenCategory.paramBegin)) {
				setNextToken();
				//if(lexic.hasNextToken()) token = lexic.nextToken();
				//else Erro();
				Eb();
				if(token.getCategory().equals(TokenCategory.paramEnd)) {
					setNextToken();
					//if(lexic.hasNextToken()) token = lexic.nextToken();
					//else Erro();
					Body();
				}else unexpectedToken(")");//Erro();
			}else unexpectedToken("(");//Erro();
		} else if(token.getCategory().equals(TokenCategory.estDo)) {
			System.out.println("Structure = 'estDo' Body 'estWhile' '(' Eb ')' ';'");
			setNextToken();
			//if(lexic.hasNextToken()) token = lexic.nextToken();
			//else Erro();
			Body();
			if(token.getCategory().equals(TokenCategory.estWhile)) {
				setNextToken();
				//if(lexic.hasNextToken()) token = lexic.nextToken();
				//else Erro();
				if(token.getCategory().equals(TokenCategory.paramBegin)) {
					setNextToken();
					//if(lexic.hasNextToken()) token = lexic.nextToken();
					//else Erro();
					Eb();
					if(token.getCategory().equals(TokenCategory.paramEnd)) {
						setNextToken();
						//if(lexic.hasNextToken()) token = lexic.nextToken();
						//else Erro();
						if(token.getCategory().equals(TokenCategory.lineEnd)) {
							setNextToken();
							//if(lexic.hasNextToken()) token = lexic.nextToken();
							//else Erro();
						}else unexpectedToken(";");//Erro();
					}else unexpectedToken(")");//Erro();
				}unexpectedToken("(");//Erro();
			}unexpectedToken("while");//Erro();
		} else if(token.getCategory().equals(TokenCategory.estLoop)) {
			System.out.println("Structure = 'estLoop' '(' Atr 'loopSep' Eb 'loopSep' Atr ')' Body");
			setNextToken();
			//if(lexic.hasNextToken()) token = lexic.nextToken();
			//else Erro();
			if(token.getCategory().equals(TokenCategory.paramBegin)) {
				setNextToken();
				//if(lexic.hasNextToken()) token = lexic.nextToken();
				//else Erro();
				Atr();
				if(token.getCategory().equals(TokenCategory.loopSep)) {
					setNextToken();
					//if(lexic.hasNextToken()) token = lexic.nextToken();
					//else Erro();
					Eb();
					if(token.getCategory().equals(TokenCategory.loopSep)) {
						setNextToken();
						//if(lexic.hasNextToken()) token = lexic.nextToken();
						//else Erro();
						Atr();
						if(token.getCategory().equals(TokenCategory.paramEnd)) {
							setNextToken();
							//if(lexic.hasNextToken()) token = lexic.nextToken();
							//else Erro();
							Body();
						}else unexpectedToken(")");//Erro();
					}else unexpectedToken("!");//Erro();
				}else unexpectedToken("!");//Erro();
			}else unexpectedToken("(");//Erro();
		} else if(token.getCategory().equals(TokenCategory.estIf)) {
			System.out.println("Structure = 'estIf' '(' Eb ')' Body IFr");
			setNextToken();
			//if(lexic.hasNextToken()) token = lexic.nextToken();
			//else Erro();
			if(token.getCategory().equals(TokenCategory.paramBegin)) {
				setNextToken();
				//if(lexic.hasNextToken()) token = lexic.nextToken();
				//else Erro();
				Eb();
				if(token.getCategory().equals(TokenCategory.paramEnd)) {
					Body();
					IFr();
				} else unexpectedToken(")");//Erro();
			} else unexpectedToken("(");//Erro();
		} else if(token.getCategory().equals(TokenCategory.instPrint)) {
			System.out.println("Structure = 'instPrint' '(' Ec ')' ';'");
			setNextToken();
			//if(lexic.hasNextToken()) token = lexic.nextToken();
			//else Erro();
			if(token.getCategory().equals(TokenCategory.paramBegin)) {
				setNextToken();
				//if(lexic.hasNextToken()) token = lexic.nextToken();
				//else Erro();
				Ec();
				if(token.getCategory().equals(TokenCategory.paramEnd)) {
					setNextToken();
					//if(lexic.hasNextToken()) token = lexic.nextToken();
					//else Erro();
					if(token.getCategory().equals(TokenCategory.lineEnd)) {
						setNextToken();
						//if(lexic.hasNextToken()) token = lexic.nextToken();
						//else Erro();
					}else unexpectedToken(";");//Erro();
				} else unexpectedToken(")");//Erro();
			}else unexpectedToken("(");//Erro();
		} else if(token.getCategory().equals(TokenCategory.instRead)) {
			System.out.println("Structure = 'instRead' '(' IdL ')' ';'");
			setNextToken();
			//if(lexic.hasNextToken()) token = lexic.nextToken();
			//else Erro();
			if(token.getCategory().equals(TokenCategory.paramBegin)) {
				setNextToken();
				//if(lexic.hasNextToken()) token = lexic.nextToken();
				//else Erro();
				Id();
				if(token.getCategory().equals(TokenCategory.paramEnd)) {
					setNextToken();
					//if(lexic.hasNextToken()) token = lexic.nextToken();
					//else Erro();
					if(token.getCategory().equals(TokenCategory.lineEnd)) {
						setNextToken();
						//if(lexic.hasNextToken()) token = lexic.nextToken();
						//else Erro();
					}else unexpectedToken(";");//Erro();
				} else unexpectedToken(")");//Erro();
			}else unexpectedToken("(");//Erro();
		}else unexpectedToken("structure");//Erro();
	}
	
	public void IdL() {
		if(token.getCategory().equals(TokenCategory.id)) {
			System.out.println("IdL = Id IdLr");
			setNextToken();
			//if(lexic.hasNextToken()) token = lexic.nextToken();
			//else Erro();
			IdLr();
		} else unexpectedToken("id");//Erro();
	}
	
	public void IdLr() {
		if(token.getCategory().equals(TokenCategory.commaSep)) {
			System.out.println("IdLr = ',' Id IdLr");
			setNextToken();
			//if(lexic.hasNextToken()) token = lexic.nextToken();
			//else Erro();
			if(token.getCategory().equals(TokenCategory.id)) {
				setNextToken();
				//if(lexic.hasNextToken()) token = lexic.nextToken();
				//else Erro();
				IdLr();
			} else unexpectedToken("id");//Erro();
		} else System.out.println("IdLr = epsilon");
	}
	
	public void IFr() {
		if(token.getCategory().equals(TokenCategory.estElsif)) {
			System.out.println("IFr = 'estElsif' '(' Eb ')' Body IFr");
			setNextToken();
			//if(lexic.hasNextToken()) token = lexic.nextToken();
			//else Erro();
			if(token.getCategory().equals(TokenCategory.paramBegin)) {
				setNextToken();
				//if(lexic.hasNextToken()) token = lexic.nextToken();
				//else Erro();
				Eb();
				if(token.getCategory().equals(TokenCategory.paramEnd)) {
					setNextToken();
					//if(lexic.hasNextToken()) token = lexic.nextToken();
					//else Erro();
					Body();
					IFr();
				} else unexpectedToken(")");//Erro();
			} else unexpectedToken("(");//Erro();
		} else if(token.getCategory().equals(TokenCategory.estElse)) {
			System.out.println("IFr = 'estElse' Body");
			setNextToken();
			//if(lexic.hasNextToken()) token = lexic.nextToken();
			//else Erro();
			Body();
		} else System.out.println("IFr = epsilon");
	}
	
	public void Atr() {
		if(token.getCategory().equals(TokenCategory.id)) {
			System.out.println("Atr = 'id' AtrR");
			setNextToken();
			//if(lexic.hasNextToken()) token = lexic.nextToken();
			//else Erro();
			AtrR();
		}else unexpectedToken("id");//Erro();
	}
	
	public void AtrR() {
		if(token.getCategory().equals(TokenCategory.paramBegin)) {
			System.out.println("AtrR = FunCall");
			FunCall();
		} else /*if(token.getCategory().equals(TokenCategory.arrayBegin) 
				|| token.getCategory().equals(TokenCategory.opEq))*/ {
			System.out.println("AtrR = ArrayAccess 'opEq' Ec");
			ArrayAccess();
			if(token.getCategory().equals(TokenCategory.opEq)) {
				setNextToken();
				//if(lexic.hasNextToken()) token = lexic.nextToken();
				//else Erro();
				Ec();
			}else unexpectedToken("=");//Erro();	
		}
	}
	
	public void Decl() {
		System.out.println("Decl = Type LI");
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
			System.out.println("Type = '"+token.getCategory()+"'");
			setNextToken();
			//if(lexic.hasNextToken()) token = lexic.nextToken();
			//else Erro();
		} else unexpectedToken("type");//Erro();
	}
	
	public void LI() {
		if(token.getCategory().equals(TokenCategory.id)) {
			System.out.println("LI = 'id' ArrayOpt Inst LIr");
			setNextToken();
			//if(lexic.hasNextToken()) token = lexic.nextToken();
			//else Erro();

			ArrayOpt();
			Inst();
			LIr();
		} else unexpectedToken("id");//Erro();
	}
	
	public void ArrayOpt() {
		if(token.getCategory().equals(TokenCategory.paramBegin)) {
			System.out.println("ArrayOpt = '(' Type ',' 'intCons' ')'");
			setNextToken();
			//if(lexic.hasNextToken()) token = lexic.nextToken();
			//else Erro();
			Type();
			if(token.getCategory().equals(TokenCategory.commaSep)) {
				setNextToken();
				//if(lexic.hasNextToken()) token = lexic.nextToken();
				//else Erro();
				if(token.getCategory().equals(TokenCategory.intCons)) {
					setNextToken();
					//if(lexic.hasNextToken()) token = lexic.nextToken();
					//else Erro();
					if(token.getCategory().equals(TokenCategory.paramEnd)) {
						setNextToken();
						//if(lexic.hasNextToken()) token = lexic.nextToken();
						//else Erro();
					}else unexpectedToken(")");//Erro();
				}else unexpectedToken("integer constant");//Erro();
			}else unexpectedToken(",");//Erro();
		} else System.out.println("ArrayOpt = epsilon");
	}
	
	public void Inst() {
		if(token.getCategory().equals(TokenCategory.opEq)) {
			System.out.println("Inst = 'opEq' Inr");
			setNextToken();
			//if(lexic.hasNextToken()) token = lexic.nextToken();
			//else Erro();
			Inr();
		} else System.out.println("Inst = epsilon");
	}
	
	public void Inr() {
		if(token.getCategory().equals(TokenCategory.arrayBegin)) {
			System.out.println("Inr = ArrayCons");
			ArrayCons();
		} else /*if(token.getCategory().equals(TokenCategory.intCons)
				||token.getCategory().equals(TokenCategory.floatCons)
				||token.getCategory().equals(TokenCategory.boolCons)
				||token.getCategory().equals(TokenCategory.charCons)
				||token.getCategory().equals(TokenCategory.stringCons)
				||token.getCategory().equals(TokenCategory.opLogNot)
				||token.getCategory().equals(TokenCategory.opUnNeg)
				||token.getCategory().equals(TokenCategory.paramBegin)
				||token.getCategory().equals(TokenCategory.id))*/ {
			System.out.println("Inr = Ecr");
			Ec();
		}
	}
	
	public void ArrayCons() {
		if(token.getCategory().equals(TokenCategory.arrayBegin)) {
			System.out.println("ArrayCons = '[' LEc ']'");
			setNextToken();
			//if(lexic.hasNextToken()) token = lexic.nextToken();
			//else Erro();
			LEc();
			if(token.getCategory().equals(TokenCategory.arrayEnd)) {
				setNextToken();
				//if(lexic.hasNextToken()) token = lexic.nextToken();
				//else Erro();
			}else unexpectedToken("]");//Erro();
		}else unexpectedToken("[");
	}
	
	public void Ec() {
		System.out.println("Ec = Fc Ecr");
		Fc();
		Ecr();
	}
	
	public void Fc() {
		if(token.getCategory().equals(TokenCategory.stringCons)) {
			System.out.println("Fc = 'stringCons'");
			setNextToken();
			//if(lexic.hasNextToken()) token = lexic.nextToken();
			//else Erro();
		} else if(token.getCategory().equals(TokenCategory.charCons)) {
			System.out.println("Fc = 'charCons'");
			setNextToken();
			//if(lexic.hasNextToken()) token = lexic.nextToken();
			//else Erro();
		} else /*if(token.getCategory().equals(TokenCategory.intCons)
				||token.getCategory().equals(TokenCategory.floatCons)
				||token.getCategory().equals(TokenCategory.boolCons)
				||token.getCategory().equals(TokenCategory.opLogNot)
				||token.getCategory().equals(TokenCategory.opUnNeg)
				||token.getCategory().equals(TokenCategory.paramBegin)
				||token.getCategory().equals(TokenCategory.id))*/{
			System.out.println("Fc = Eb");
			Eb();
		}//else Erro();
	}
	
	public void Eb() {
		System.out.println("Eb = Tb Ebr");
		Tb();
		Ebr();
	}
	
	public void Tb() {
		System.out.println("Tb = Fb Tbr");
		Fb();
		Tbr();
	}
	
	public void Fb() {
		if(token.getCategory().equals(TokenCategory.opLogNot)) {
			System.out.println("Fb = 'opLogNot' Fb");
			setNextToken();
			//if(lexic.hasNextToken()) token = lexic.nextToken();
			//else Erro();
			Fb();
		} else if(token.getCategory().equals(TokenCategory.boolCons)) {
			System.out.println("Fb = 'boolCons'");
			setNextToken();
			//if(lexic.hasNextToken()) token = lexic.nextToken();
			//else Erro();
		} else /*if(token.getCategory().equals(TokenCategory.intCons)
				||token.getCategory().equals(TokenCategory.floatCons)
				||token.getCategory().equals(TokenCategory.opUnNeg)
				||token.getCategory().equals(TokenCategory.paramBegin)
				||token.getCategory().equals(TokenCategory.id))*/{
			System.out.println("Fb = Ra Fbr");
			Ra();
			Fbr();
		}
	}
	
	public void Ra() {
		System.out.println("Ra = Ea Rar");
		Ea();
		Rar();
	}
	
	public void Ea() {
		System.out.println("Ea = Ta Ear");
		Ta();
		Ear();
	}
	
	public void Ta() {
		System.out.println("Ta = Pa Tar");
		Pa();
		Tar();
	}
	
	public void Pa() {
		System.out.println("Pa = Fa Par");
		Fa();
		Par();
	}
	
	public void Par() {
		if(token.getCategory().equals(TokenCategory.opExp)) {
			System.out.println("Par = 'opExp' Pa");
			setNextToken();
			//if(lexic.hasNextToken()) token = lexic.nextToken();
			//else Erro();
			Pa();
		}else System.out.println("Par = epsilon");
	}

	public void Fa() {
		if(token.getCategory().equals(TokenCategory.paramBegin)) {
			System.out.println("Fa = '(' Eb ')'");
			setNextToken();
			//if(lexic.hasNextToken()) token = lexic.nextToken();
			//else Erro();
			Eb();
			if(token.getCategory().equals(TokenCategory.paramEnd)) {
				setNextToken();
				//if(lexic.hasNextToken()) token = lexic.nextToken();
				//else Erro();
			}else unexpectedToken(")");//Erro();
		} else if(token.getCategory().equals(TokenCategory.opUnNeg)) {
			System.out.println("Fa = 'opUnNeg' Fa");
			setNextToken();
			//if(lexic.hasNextToken()) token = lexic.nextToken();
			//else Erro();
			Fa();
		} else if(token.getCategory().equals(TokenCategory.id)) {
			System.out.println("Fa = Id");
			Id();
		} else if(token.getCategory().equals(TokenCategory.intCons)) {
			System.out.println("Fa = 'intCons'");
			setNextToken();
			//if(lexic.hasNextToken()) token = lexic.nextToken();
			//else Erro();
		} else if(token.getCategory().equals(TokenCategory.floatCons)) {
			System.out.println("Fa = 'floatCons'");
			setNextToken();
			//if(lexic.hasNextToken()) token = lexic.nextToken();
			//else Erro();
		} else unexpectedToken("constant, id or expression");//Erro();
	}
	
	public void Id() {
		if(token.getCategory().equals(TokenCategory.id)) {
			System.out.println("Id = 'id' Idr");
			setNextToken();
			//if(lexic.hasNextToken()) token = lexic.nextToken();
			//else Erro();
			Idr();
		}else unexpectedToken("id");//Erro();
	}
	
	public void Idr() {
		/*if(token.getCategory().equals(TokenCategory.arrayBegin)
				||token.getCategory().equals(TokenCategory.opEq)
				||token.getCategory().equals(TokenCategory.commaSep)
				||token.getCategory().equals(TokenCategory.opExp)
				||token.getCategory().equals(TokenCategory.opMult)
				||token.getCategory().equals(TokenCategory.opAd)
				||token.getCategory().equals(TokenCategory.arrayEnd)
				||token.getCategory().equals(TokenCategory.opRelEq)
				||token.getCategory().equals(TokenCategory.opRelLtGt)
				||token.getCategory().equals(TokenCategory.opLogAnd)
				||token.getCategory().equals(TokenCategory.opLogOr)
				||token.getCategory().equals(TokenCategory.opConc)
				||token.getCategory().equals(TokenCategory.lineEnd)) {
			System.out.println("Idr = ArrayAccess");
			ArrayAccess();
		} else */if(token.getCategory().equals(TokenCategory.paramBegin)) {
			System.out.println("Idr = FunCall");
			FunCall();
		} else {
			System.out.println("Idr = ArrayAccess");
			ArrayAccess();
		}
	}
	
	public void FunCall() {
		if(token.getCategory().equals(TokenCategory.paramBegin)) {
			System.out.println("FunCall = '(' LEc ')'");
			setNextToken();
			//if(lexic.hasNextToken()) token = lexic.nextToken();
			//else Erro();
			LEc();
			if(token.getCategory().equals(TokenCategory.paramEnd)) {
				setNextToken();
				//if(lexic.hasNextToken()) token = lexic.nextToken();
				//else Erro();
			} else unexpectedToken(")");//Erro();
		}else unexpectedToken("(");//Erro();
	}
	
	public void ArrayAccess() {
		if(token.getCategory().equals(TokenCategory.arrayBegin)) {
			System.out.println("ArrayAccess = '[' Ea ']'");
			setNextToken();
			//if(lexic.hasNextToken()) token = lexic.nextToken();
			//else Erro();
			Ea();
			if(token.getCategory().equals(TokenCategory.arrayEnd)) {
				setNextToken();
				//if(lexic.hasNextToken()) token = lexic.nextToken();
				//else Erro();
			} else unexpectedToken("]");//Erro();
		}else System.out.println("ArrayAccess = epsilon");
	}
	
	public void Tar() {
		if(token.getCategory().equals(TokenCategory.opMult)) {
			System.out.println("Tar = 'opMult' Pa Tar");
			setNextToken();
			//if(lexic.hasNextToken()) token = lexic.nextToken();
			//else Erro();
			Pa();
			Tar();
		}else System.out.println("Tar = epsilon");
	}
	
	public void Ear() {
		if(token.getCategory().equals(TokenCategory.opAd)) {
			System.out.println("Ear = 'opAd' Ta Ear");
			setNextToken();
			//if(lexic.hasNextToken()) token = lexic.nextToken();
			//else Erro();
			Ta();
			Ear();
		}else System.out.println("Ear = epsilon");
	}
	
	public void Rar() {
		if(token.getCategory().equals(TokenCategory.opRelEq)) {
			System.out.println("Rar = 'opRelEq' Ea Rar");
			setNextToken();
			//if(lexic.hasNextToken()) token = lexic.nextToken();
			//else Erro();
			Ea();
			Rar();
		}else System.out.println("Rar = epsilon");
	}
	
	public void Fbr() {
		if(token.getCategory().equals(TokenCategory.opRelLtGt)) {
			System.out.println("Fbr = 'opRelLtGt' Ra Fbr");
			setNextToken();
			//if(lexic.hasNextToken()) token = lexic.nextToken();
			//else Erro();
			Ra();
			Fbr();
		}else System.out.println("Fbr = epsilon");
	}
	
	public void Tbr() {
		if(token.getCategory().equals(TokenCategory.opLogAnd)) {
			System.out.println("Tbr = 'opLogAnd' Fb Tbr");
			setNextToken();
			//if(lexic.hasNextToken()) token = lexic.nextToken();
			//else Erro();
			Fb();
			Tbr();
		}else System.out.println("Tbr = epsilon");
	}
	
	public void Ebr() {
		if(token.getCategory().equals(TokenCategory.opLogOr)) {
			System.out.println("Ebr = 'opLogOr' Tb Ebr");
			setNextToken();
			//if(lexic.hasNextToken()) token = lexic.nextToken();
			//else Erro();
			Tb();
			Ebr();
		}else System.out.println("Ebr = epsilon");
	}
	
	public void Ecr() {
		if(token.getCategory().equals(TokenCategory.opConc)) {
			System.out.println("Ecr = 'opConc' ConcOpt Fc Ecr");
			setNextToken();
			//if(lexic.hasNextToken()) token = lexic.nextToken();
			//else Erro();
			ConcOpt();
			Fc();
			Ecr();
		}else System.out.println("Ecr = epsilon");
	}
	
	public void ConcOpt() {
		if(token.getCategory().equals(TokenCategory.arrayBegin)) {
			System.out.println("ConcOpt = '[' 'floatCons' ']'");
			setNextToken();
			//if(lexic.hasNextToken()) token = lexic.nextToken();
			//else Erro();
			if(token.getCategory().equals(TokenCategory.floatCons)) {
				setNextToken();
				//if(lexic.hasNextToken()) token = lexic.nextToken();
				//else Erro();
				if(token.getCategory().equals(TokenCategory.arrayEnd)) {
					setNextToken();
					//if(lexic.hasNextToken()) token = lexic.nextToken();
					//else Erro();
				}else unexpectedToken("]");//Erro("Expected ] after "+lexic.getPreviousToken());
			}else unexpectedToken("float constant");//Erro();
		}else System.out.println("ConcOpt = epsilon");
	}
	
	public void LIr() {
		if(token.getCategory().equals(TokenCategory.commaSep)) {
			System.out.println("LIr = ',' 'id' ArrayOpt Inst LIr");
			setNextToken();
			//if(lexic.hasNextToken()) token = lexic.nextToken();
			//else Erro();
			if(token.getCategory().equals(TokenCategory.id)) {
				setNextToken();
				//if(lexic.hasNextToken()) token = lexic.nextToken();
				//else Erro();

				ArrayOpt();
				Inst();
				LIr();
			} else unexpectedToken("id");//Erro("Expected id after "+lexic.getPreviousToken());
			
		} else if(token.getCategory().equals(TokenCategory.lineEnd)) {
			System.out.println("LIr = ';'");
			setNextToken();
			//if(lexic.hasNextToken()) token = lexic.nextToken();
			//else Erro();
		}
	}
	
	private void setNextToken() {
		if(lexic.hasNextToken()) token = lexic.nextToken();
		else sendError("Unnexpected end of file");
	}
	
	private void unexpectedToken(String expected) {
		sendError("Expected " + expected+ " after "+lexic.getPreviousToken());
	}
	
	public void sendError(String message) {
		System.err.println("Error: "+ message);
		//System.out.println(token);
		System.exit(0);
	}
	
}
