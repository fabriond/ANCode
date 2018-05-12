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
			Decl();
			S();
		} else if(token.getCategory().equals(TokenCategory.funDef)) {
			if(lexic.hasNextToken()) token = lexic.nextToken();
			else Erro();

			if(token.getCategory().equals(TokenCategory.id) || token.getCategory().equals(TokenCategory.main)) {
				if(lexic.hasNextToken()) token = lexic.nextToken();
				else Erro();

				if(token.getCategory().equals(TokenCategory.paramBegin)) {
					if(lexic.hasNextToken()) token = lexic.nextToken();
					else Erro();
					Param();
					if(token.getCategory().equals(TokenCategory.paramEnd)){
						if(lexic.hasNextToken()) token = lexic.nextToken();
						else Erro();
						FunType();
						FunBody();
						S();
					} else Erro();
				} else Erro();
			} else Erro();
		}
	}
	
	
	public void FunBody() {
		if(token.getCategory().equals(TokenCategory.escBegin)) {
			if(lexic.hasNextToken()) token = lexic.nextToken();
			else Erro();
			FunBP();
			if(token.getCategory().equals(TokenCategory.escEnd)) {
				if(lexic.hasNextToken()) token = lexic.nextToken();
			} else Erro();
		} else Erro();
	}
	
	public void FunBP() {
		BodyPart();
		FunBPr();
	}
	
	public void FunBPr() {
		if(token.getCategory().equals(TokenCategory.funReturn)) {
			if(lexic.hasNextToken()) token = lexic.nextToken();
			else Erro();
			Ec();
			if(token.getCategory().equals(TokenCategory.lineEnd)) {
				if(lexic.hasNextToken()) token = lexic.nextToken();
				else Erro();
			} else Erro();
		} 
	}
	
	public void Param() {
		Type();
		if(token.getCategory().equals(TokenCategory.id)) {
			if(lexic.hasNextToken()) token = lexic.nextToken();
			else Erro();
			ArrayOpt();
			Paramr();
			
		} else if(token.getCategory().equals(TokenCategory.paramEnd)) return ;
	}
	
	public void Paramr() {
		if(token.getCategory().equals(TokenCategory.commaSep)) {
			if(lexic.hasNextToken()) token = lexic.nextToken();
			else Erro();
			Type();
			if(token.getCategory().equals(TokenCategory.id)) {
				if(lexic.hasNextToken()) token = lexic.nextToken();
				ArrayOpt();
				Paramr();
			}
		}
	}
	
	public void LEc() {
		Ec();
		LEcr();
	}
	
	public void LEcr() {
		if(token.getCategory().equals(TokenCategory.commaSep)) {
			if(lexic.hasNextToken()) token = lexic.nextToken();
			else Erro();
			Ec();
			LEcr();
		}
	}
	
	public void FunType() {
		if(token.getCategory().equals(TokenCategory.funVoid)) {
			if(lexic.hasNextToken()) token = lexic.nextToken();
			else Erro();
		} else if(token.getCategory().equals(TokenCategory.intType)
				|| token.getCategory().equals(TokenCategory.floatType)
				|| token.getCategory().equals(TokenCategory.charType)
				|| token.getCategory().equals(TokenCategory.boolType)
				|| token.getCategory().equals(TokenCategory.stringType)
				|| token.getCategory().equals(TokenCategory.arrayType)) {
			Type();
		} else Erro();
	}
	
	public void Body() {
		if(token.getCategory().equals(TokenCategory.escBegin)) {
			if(lexic.hasNextToken()) token = lexic.nextToken();
			else Erro();
			BodyPart();
			if(token.getCategory().equals(TokenCategory.escEnd)) {
				if(lexic.hasNextToken()) token = lexic.nextToken();	
			} else Erro();
		} else Erro();
	}
	
	public void BodyPart() {
		if(token.getCategory().equals(TokenCategory.intType)
				|| token.getCategory().equals(TokenCategory.floatType)
				|| token.getCategory().equals(TokenCategory.charType)
				|| token.getCategory().equals(TokenCategory.boolType)
				|| token.getCategory().equals(TokenCategory.stringType)
				|| token.getCategory().equals(TokenCategory.arrayType)) {
			Decl();
			BodyPart();
		} else if(token.getCategory().equals(TokenCategory.id)) {
			Atr();
			BodyPart();
		} else if(token.getCategory().equals(TokenCategory.estWhile)
				|| token.getCategory().equals(TokenCategory.estDo)
				|| token.getCategory().equals(TokenCategory.estLoop)
				|| token.getCategory().equals(TokenCategory.estIf)
				|| token.getCategory().equals(TokenCategory.instRead)
				|| token.getCategory().equals(TokenCategory.instPrint)){
					Structure();
					BodyPart();
		}
	}
	
	public void Structure() {
		if(token.getCategory().equals(TokenCategory.estWhile)) {
			if(lexic.hasNextToken()) token = lexic.nextToken();
			else Erro();
			
			if(token.getCategory().equals(TokenCategory.paramBegin)) {
				if(lexic.hasNextToken()) token = lexic.nextToken();
				Eb();
				if(token.getCategory().equals(TokenCategory.paramEnd)) {
					if(lexic.hasNextToken()) token = lexic.nextToken();
					Body();
				}
			}
		} else if(token.getCategory().equals(TokenCategory.estDo)) {
			if(lexic.hasNextToken()) token = lexic.nextToken();
			else Erro();
			Body();
			if(token.getCategory().equals(TokenCategory.estWhile)) {
				if(lexic.hasNextToken()) token = lexic.nextToken();
				else Erro();
				if(token.getCategory().equals(TokenCategory.paramBegin)) {
					if(lexic.hasNextToken()) token = lexic.nextToken();
					else Erro();
					Eb();
					if(token.getCategory().equals(TokenCategory.paramEnd));
				}
			}
		} else if(token.getCategory().equals(TokenCategory.estLoop)) {
			if(lexic.hasNextToken()) token = lexic.nextToken();
			else Erro();
			if(token.getCategory().equals(TokenCategory.paramBegin)) {
				if(lexic.hasNextToken()) token = lexic.nextToken();
				else Erro();
				Atr();
				if(token.getCategory().equals(TokenCategory.loopSep)) {
					if(lexic.hasNextToken()) token = lexic.nextToken();
					else Erro();
					Eb();
					if(token.getCategory().equals(TokenCategory.loopSep)) {
						if(lexic.hasNextToken()) token = lexic.nextToken();
						else Erro();
						Atr();
						if(token.getCategory().equals(TokenCategory.paramEnd)) {
							if(lexic.hasNextToken()) token = lexic.nextToken();
							else Erro();
							Body();
						}
					}
				}
			}
		} else if(token.getCategory().equals(TokenCategory.estIf)) {
			if(lexic.hasNextToken()) token = lexic.nextToken();
			else Erro();
			if(token.getCategory().equals(TokenCategory.paramBegin)) {
				if(lexic.hasNextToken()) token = lexic.nextToken();
				else Erro();
				Eb();
				if(token.getCategory().equals(TokenCategory.paramEnd)) {
					Body();
					IFr();
				}
			}
		} else if(token.getCategory().equals(TokenCategory.instPrint)) {
			if(lexic.hasNextToken()) token = lexic.nextToken();
			else Erro();
			if(token.getCategory().equals(TokenCategory.paramBegin)) {
				if(lexic.hasNextToken()) token = lexic.nextToken();
				else Erro();
				Ec();
				if(token.getCategory().equals(TokenCategory.paramEnd)) {
					if(lexic.hasNextToken()) token = lexic.nextToken();
					else Erro();
					if(token.getCategory().equals(TokenCategory.lineEnd)) {
						if(lexic.hasNextToken()) token = lexic.nextToken();
						else Erro();
					}
				} else Erro();
			}
		} else if(token.getCategory().equals(TokenCategory.instRead)) {
			if(lexic.hasNextToken()) token = lexic.nextToken();
			else Erro();
			if(token.getCategory().equals(TokenCategory.paramBegin)) {
				if(lexic.hasNextToken()) token = lexic.nextToken();
				else Erro();
				Id();
				if(token.getCategory().equals(TokenCategory.paramEnd)) {
					if(lexic.hasNextToken()) token = lexic.nextToken();
					else Erro();
					if(token.getCategory().equals(TokenCategory.lineEnd)) {
						if(lexic.hasNextToken()) token = lexic.nextToken();
						else Erro();
					}
				} else Erro();
			}
		}
	}
	
	public void IdL() {
		if(token.getCategory().equals(TokenCategory.id)) {
			if(lexic.hasNextToken()) token = lexic.nextToken();
			else Erro();
			IdLr();
		}
	}
	
	public void IdLr() {
		if(token.getCategory().equals(TokenCategory.commaSep)) {
			if(lexic.hasNextToken()) token = lexic.nextToken();
			else Erro();
			if(token.getCategory().equals(TokenCategory.id)) {
				if(lexic.hasNextToken()) token = lexic.nextToken();
				else Erro();
				IdLr();
			} else Erro();
		}
	}
	
	public void IFr() {
		if(token.getCategory().equals(TokenCategory.estElsif)) {
			if(lexic.hasNextToken()) token = lexic.nextToken();
			else Erro();
			if(token.getCategory().equals(TokenCategory.paramBegin)) {
				if(lexic.hasNextToken()) token = lexic.nextToken();
				else Erro();
				Eb();
				if(token.getCategory().equals(TokenCategory.paramEnd)) {
					if(lexic.hasNextToken()) token = lexic.nextToken();
					else Erro();
					Body();
					IFr();
				}
			}
		} else if(token.getCategory().equals(TokenCategory.estElse)) {
			if(lexic.hasNextToken()) token = lexic.nextToken();
			else Erro();
			Body();
		}
	}
	
	public void Atr() {
		
		if(token.getCategory().equals(TokenCategory.id)) {
			if(lexic.hasNextToken()) token = lexic.nextToken();
			else Erro();
			ArrayAccess();
			if(token.getCategory().equals(TokenCategory.opEq)) {
				if(lexic.hasNextToken()) token = lexic.nextToken();
				else Erro();
				Ec();
				if(token.getCategory().equals(TokenCategory.lineEnd)){
					if(lexic.hasNextToken()) token = lexic.nextToken();
					else Erro();
				}
			}
		}	
	}
	
	public void Decl() {
		Type();
		LI();
	}
	
	public void Type() {
		if( token.getCategory().equals(TokenCategory.intType)
				|| token.getCategory().equals(TokenCategory.floatType)
				|| token.getCategory().equals(TokenCategory.charType)
				|| token.getCategory().equals(TokenCategory.boolType)
				|| token.getCategory().equals(TokenCategory.stringType)
				|| token.getCategory().equals(TokenCategory.arrayType)){
			if(lexic.hasNextToken()) token = lexic.nextToken();
			else Erro();
		} 
	}
	
	public void LI() {

		if(token.getCategory().equals(TokenCategory.id)) {
			if(lexic.hasNextToken()) token = lexic.nextToken();
			else return ;

			ArrayOpt();
			Inst();
			LIr();
		} 
	}
	
	public void ArrayOpt() {
		if(token.getCategory().equals(TokenCategory.paramBegin)) {
			if(lexic.hasNextToken()) token = lexic.nextToken();
			else Erro();
			Type();
			if(token.getCategory().equals(TokenCategory.commaSep)) {
				if(lexic.hasNextToken()) token = lexic.nextToken();
				else Erro();
				if(token.getCategory().equals(TokenCategory.intCons)) {
					if(lexic.hasNextToken()) token = lexic.nextToken();
					else Erro();
					if(token.getCategory().equals(TokenCategory.paramEnd)) {
						if(lexic.hasNextToken()) token = lexic.nextToken();
					}
				}
			}
		} 
	}
	
	public void Inst() {
		if(token.getCategory().equals(TokenCategory.opEq)) {
			if(lexic.hasNextToken()) token = lexic.nextToken();
			else Erro();
			Inr();
		} else if(token.getCategory().equals(TokenCategory.stringCons) 
				||token.getCategory().equals(TokenCategory.charCons)
				||token.getCategory().equals(TokenCategory.opLogNot)
				||token.getCategory().equals(TokenCategory.boolCons)
				||token.getCategory().equals(TokenCategory.paramBegin)
				||token.getCategory().equals(TokenCategory.opUnNeg)
				||token.getCategory().equals(TokenCategory.id)
				||token.getCategory().equals(TokenCategory.intCons)
				||token.getCategory().equals(TokenCategory.floatCons)) {
			Ec();
		} else if(token.getCategory().equals(TokenCategory.arrayBegin)){
			ArrayCons();
		}
	}
	
	public void Inr() {
		if(token.getCategory().equals(TokenCategory.arrayBegin)) {
			ArrayCons();
		}
		Ec();
	}
	
	public void ArrayCons() {
		if(token.getCategory().equals(TokenCategory.arrayBegin)) {
			if(lexic.hasNextToken()) token = lexic.nextToken();
			LEc();
			if(token.getCategory().equals(TokenCategory.arrayEnd)) {
				if(lexic.hasNextToken()) token = lexic.nextToken();
				else Erro();
			}
		}
	}
	
	public void Ec() {
		Fc();
		Ecr();
	}
	
	public void Fc() {
		if(token.getCategory().equals(TokenCategory.stringCons)) {
			if(lexic.hasNextToken()) token = lexic.nextToken();
			else Erro();
		} else if(token.getCategory().equals(TokenCategory.charCons)) {
			if(lexic.hasNextToken()) token = lexic.nextToken();
			else Erro();
		} else {
			Eb();
		}
	}
	
	public void Eb() {
		Tb();
		Ebr();
	}
	
	public void Tb() {
		Fb();
		Tbr();
	}
	
	public void Fb() {
		if(token.getCategory().equals(TokenCategory.opLogNot)) {
			if(lexic.hasNextToken()) token = lexic.nextToken();
			else Erro();
			Fb();
		} else if(token.getCategory().equals(TokenCategory.boolCons)) {
			if(lexic.hasNextToken()) token = lexic.nextToken();
			else Erro();
		} else {
			Ra();
			Fbr();
		}
	}
	
	public void Ra() {
		Ea();
		Rar();
	}
	
	public void Ea() {
		Ta();
		Ear();
	}
	
	public void Ta() {
		Pa();
		Tar();
	}
	
	public void Pa() {
		Fa();
		Par();
	}
	
	public void Par() {
		if(token.getCategory().equals(TokenCategory.opExp)) {
			if(lexic.hasNextToken()) token = lexic.nextToken();
			else Erro();
			Pa();
		}
	}
	
	public void Fa() {
		if(token.getCategory().equals(TokenCategory.paramBegin)) {
			if(lexic.hasNextToken()) token = lexic.nextToken();
			Eb();
			if(token.getCategory().equals(TokenCategory.paramEnd)) {
				if(lexic.hasNextToken()) token = lexic.nextToken();
			}
		} else if(token.getCategory().equals(TokenCategory.opUnNeg)) {
			if(lexic.hasNextToken()) token = lexic.nextToken();
			Fa();
		} else if(token.getCategory().equals(TokenCategory.id)) {
			Id();
		} else if(token.getCategory().equals(TokenCategory.intCons)) {
			if(lexic.hasNextToken()) token = lexic.nextToken();
		} else if(token.getCategory().equals(TokenCategory.floatCons)) {
			if(lexic.hasNextToken()) token = lexic.nextToken();
		} 
	}
	
	public void Id() {
		if(token.getCategory().equals(TokenCategory.id)) {
			if(lexic.hasNextToken()) token = lexic.nextToken();
			else Erro();
			Idr();
		}
	}
	
	public void Idr() {
		if(token.getCategory().equals(TokenCategory.arrayBegin)) {
			ArrayAccess();
		} else FunCall();
	}
	
	public void FunCall() {
		if(token.getCategory().equals(TokenCategory.paramBegin)) {
			if(lexic.hasNextToken()) token = lexic.nextToken();
			else Erro();
			LEc();
			if(token.getCategory().equals(TokenCategory.paramEnd)) {
				if(lexic.hasNextToken()) token = lexic.nextToken();
				else Erro();
			} else Erro();
		} else Erro();
	}
	
	public void ArrayAccess() {
		if(token.getCategory().equals(TokenCategory.arrayBegin)) {
			if(lexic.hasNextToken()) token = lexic.nextToken();
			else Erro();
			Ea();
			if(token.getCategory().equals(TokenCategory.arrayEnd)) {
				if(lexic.hasNextToken()) token = lexic.nextToken();
				else Erro();
			} else Erro();
		}
	}
	
	public void Tar() {
		if(token.getCategory().equals(TokenCategory.opMult)) {
			if(lexic.hasNextToken()) token = lexic.nextToken();
			else Erro();
			Pa();
			Tar();
		}
	}
	
	public void Ear() {
		if(token.getCategory().equals(TokenCategory.opAd)) {
			if(lexic.hasNextToken()) token = lexic.nextToken();
			else Erro();
			Ta();
			Ear();
		}
	}
	
	public void Rar() {
		if(token.getCategory().equals(TokenCategory.opRelEq)) {
			if(lexic.hasNextToken()) token = lexic.nextToken();
			else Erro();
			
			Ea();
			Rar();
		}
	}
	
	public void Fbr() {
		if(token.getCategory().equals(TokenCategory.opRelLtGt)) {
			if(lexic.hasNextToken()) token = lexic.nextToken();
			else Erro();
			
			Ra();
			Fbr();
		}
	}
	
	public void Tbr() {
		if(token.getCategory().equals(TokenCategory.opLogAnd)) {
			if(lexic.hasNextToken()) token = lexic.nextToken();
			else Erro();
			Fb();
			Tbr();
		}
	}
	
	public void Ebr() {
		if(token.getCategory().equals(TokenCategory.opLogOr)) {
			if(lexic.hasNextToken()) token = lexic.nextToken();
			else Erro();
			Tb();
			Ebr();
		}
	}
	
	public void Ecr() {
		if(token.getCategory().equals(TokenCategory.opConc)) {
			if(lexic.hasNextToken()) token = lexic.nextToken();
			else Erro();
			ConcOpt();
			Fc();
			Ecr();
		}
	}
	
	public void ConcOpt() {
		if(token.getCategory().equals(TokenCategory.arrayBegin)) {
			if(lexic.hasNextToken()) token = lexic.nextToken();
			else Erro();
			if(token.getCategory().equals(TokenCategory.floatCons)) {
				if(lexic.hasNextToken()) token = lexic.nextToken();
				else Erro();
				if(token.getCategory().equals(TokenCategory.arrayEnd)) {
					if(lexic.hasNextToken()) token = lexic.nextToken();
					else Erro();
				}
			}
		}
	}
	
	public void ArrayAux() {
		if(token.getCategory().equals(TokenCategory.commaSep)) {
			if(lexic.hasNextToken()) token = lexic.nextToken();
			else Erro();
			Ec();
			ArrayAux();
		}
	}
	
	public void LIr() {
		if(token.getCategory().equals(TokenCategory.commaSep)) {
			if(lexic.hasNextToken()) token = lexic.nextToken();
			else Erro();
			if(token.getCategory().equals(TokenCategory.id)) {
				if(lexic.hasNextToken()) token = lexic.nextToken();
				else Erro();

				ArrayOpt();
				Inst();
				LIr();
			} else Erro();
			
		} else if(token.getCategory().equals(TokenCategory.lineEnd)) {
			if(lexic.hasNextToken()) token = lexic.nextToken();
			else Erro();
		}
	}
	
	public void Erro() {
		System.out.println("Erro!");
		System.out.println(token);
		System.exit(0);
	}
	
}
